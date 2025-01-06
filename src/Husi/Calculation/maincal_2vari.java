package Husi.Calculation;


public class maincal_2vari extends maincal {
    @Override
    public void run() {
        listen.onProgressMax((int)Math.pow(((max_inch-min_inch)*(1/inc)),2));
        // 最小から最大までの範囲で1つ目の変数を探索
        for (double j = min_inch; j < max_inch; j += inc) {
            // 1つ目の変数を変更
            UpdateLinkLength(checkbox_data, 0, j);
            
            // さらに2つ目の変数を探索
            for (double i = min_inch; i < max_inch; i += inc) {
                // 2つ目の変数を変更
                UpdateLinkLength(checkbox_data, 1, i);

                if(isAMin(linkA, linkB, linkC, linkD)) {
                    if(!center) {
                        grashof(linkA, linkB, linkC, linkD, r, angleERR);
                    }else{
                        center_grashof(linkA, linkB, linkC, linkD, r, angleERR);
                    }
                }
                count();
            }
        }
    }
}

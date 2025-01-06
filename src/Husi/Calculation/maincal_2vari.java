package Husi.Calculation;


import Husi.mainsrc.MyFrame.variablelist;

public class maincal_2vari extends maincal {
    @Override
    public void run() {
        listen.onProgressMax((int)Math.pow(((max_inch-min_inch)*(1/inc)),2));
        // 最小から最大までの範囲で1つ目の変数を探索
        for (double j = min_inch; j < max_inch; j += inc) {
            // 1つ目の変数を変更
            searchvariable(variablelists, 0, j);
            
            // さらに2つ目の変数を探索
            for (double i = min_inch; i < max_inch; i += inc) {
                // 2つ目の変数を変更

                searchvariable(variablelists, 1, i);
                
                // 最小値を検索
                if(searcmin(A, B, C, D)) {
                    if(!center) {
                        grashof(A, B, C, D, r, angleERR);
                    }else{
                        center_grashof(A, B, C, D, r, angleERR);
                    }
                }
                count();
            }
        }
    }
}

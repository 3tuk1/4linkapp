package Husi.Calculation;


import Husi.mainsrc.MyFrame.variablelist;

public class maincal_3vari extends maincal {
    @Override
    public void run(){
        listen.onProgressMax((int)Math.pow(((max_inch-min_inch)*(1/inc)),3));
        // 最小から最大までの範囲で1つ目の変数を探索
        for (double j = min_inch; j < max_inch; j += inc) {
            // 1つ目の変数を変更
            searchvariable(variablelists, 0, j);
            
            // さらに2つ目の変数を探索
            for (double i = min_inch; i < max_inch; i += inc) {
                // 2つ目の変数を変更
                searchvariable(variablelists, 1, i);
                for(double k = min_inch;k<max_inch;k+=inc){
                    searchvariable(variablelists, 2, k);
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
}

package Husi.Calculation;

import Husi.mainsrc.Progresslistener;
import Husi.mainsrc.InputResult;
import Husi.mainsrc.MyFrame.checkbox_data;
//変数が一つの時の計算
public class MainCalc implements Runnable{
    InputResult inputresult = new InputResult();
    double linkA, linkB, linkC, linkD;
    double r,min_inch,max_inch,inc,angleERR;
    checkbox_data[] checkbox_data;
    String Filename;
    boolean center;
    Progresslistener listen;
    int prog_count = 0;


    public void Grashoftheorem(double a, double b, double c, double d, double r, checkbox_data[] checkbox_data, double min_inch, double max_inch, double inc, String Filename, double angleERR, boolean center){
        linkA =a;
        linkB =b;
        linkC =c;
        linkD =d;
        this.r = r;
        this.checkbox_data = checkbox_data;
        this.min_inch = min_inch;
        this.max_inch = max_inch;
        this.inc = inc;
        this.Filename = Filename;
        this.angleERR = angleERR;
        this.center = center;
        run();
    }

    public boolean isAMin (double A, double B, double C, double D){
        return (A<B)&&(A<C)&&(A<D);
    }

    // グラスホフの定理の適用
    public void grashof(double A,double B,double C,double D,double r,double angleERR){
        double amplitude=0;
        if(r>(A+D)){
            if (A + C <= B + D && A + D <= B + C && A + B <= C + D){
                amplitude= AmplitudeMethod.hurehaba(A, B, C, D);
                if(AngleError.solve(A, B, C, D,angleERR)){
                    if(r>(A+B)){
                        inputresult.inputResult(A, B, C, D, amplitude,Filename);
                    }
                }
            }
        }
    }

    // リンクを中心に配置したときのグラスホフの定理の適用
    public void center_grashof(double A,double B,double C,double D,double r,double angleERR){
        double amplitude=0;
        if(r==D){ // 中心に存在するか
            if (A + C <= B + D && A + D <= B + C && A + B <= C + D){
                amplitude = AmplitudeMethod.hurehaba(A,B,C,D);
                if(AngleError.solve(A, B, C, D,angleERR)) {
                    if (r > (A + B)) {
                        inputresult.inputResult(A, B, C, D, amplitude,Filename);
                    }
                }
            }
        }
    }

    public void UpdateLinkLength(checkbox_data[] checkbox_data, int number, double i){
        int number_current=0;
        if(checkbox_data[0].is_checked){
            if(number==number_current){
                linkA = i;
            }
            number_current++;
        }
        if (checkbox_data[1].is_checked) {
            if(number==number_current){
                linkB = i;
            }
            number_current++;
        }
        if (checkbox_data[2].is_checked) {
            if(number==number_current){
                linkC = i;
            }
            number_current++;
        }
        if(checkbox_data[3].is_checked){
            if(number==number_current){
                linkD = i;
            }
            number_current++;
        }
    }

    public void SetListen(Progresslistener listen){
        this.listen = listen;
    }

    public void count(){
        if(prog_count%10 == 0)
            listen.onProgressUpdate(prog_count);
        prog_count++;
    }

    @Override
    public void run() {
        listen.onProgressMax((int) ((max_inch-min_inch)*(1/inc)));
        for (double i = min_inch; i < max_inch; i+=inc) {
            UpdateLinkLength(checkbox_data, 0, i);
            if(isAMin(linkA, linkB, linkC, linkD)) {
                if(!center) {
                    grashof(linkA, linkB, linkC, linkD, r ,angleERR);
                }else{
                    center_grashof(linkA, linkB, linkC, linkD, r,angleERR);
                }
            }
            count();
        }
    }
}

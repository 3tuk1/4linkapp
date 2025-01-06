package Husi.Calculation;
import java.io.File;
import java.text.DecimalFormat;

import Husi.mainsrc.MyFrame;
import Husi.mainsrc.Progresslistener;
import Husi.mainsrc.inputresult;
import Husi.mainsrc.MyFrame.variablelist;
//変数が一つの時の計算
public class maincal implements Runnable{
    int count=0;
    inputresult inputresult = new inputresult();
    double A,B,C,D;
    double r,min_inch,max_inch,inc,angleERR;
    variablelist[] variablelists;
    String Filename;
    boolean center;
    DecimalFormat df = new DecimalFormat("#.00");
    Progresslistener listen;
    int prog_count = 0;
    public void Grashoftheorem(double a,double b,double c,double d,double r,variablelist[] variablelist,double min_inch,double max_inch,double inc,String Filename,double angleERR,boolean center){
        A=a;B=b;C=c;D=d;
        this.r = r;
        this.variablelists=variablelist;
        this.min_inch = min_inch;
        this.max_inch = max_inch;
        this.inc = inc;
        this.Filename = Filename;
        this.angleERR = angleERR;
        this.center = center;
        run();
    }
    public boolean searcmin(double A,double B,double C,double D){
        return (A<B)&&(A<C)&&(A<D);
    }
    public void searchvariable(variablelist[] variablelist,int number,double i){
        int number_current=0;
        if(variablelist[0].variables){
            if(number==number_current){
                A = i;
            }
            number_current++;
        } 
        if (variablelist[1].variables) {
            if(number==number_current){
                B = i;
            }
            number_current++;
        } 
        if (variablelist[2].variables) {
            if(number==number_current){
                C = i;
            }
            number_current++;
        } 
        if(variablelist[3].variables){
            if(number==number_current){
                D = i;
            }
            number_current++;
        }
    }
    public void grashof(double A,double B,double C,double D,double r,double angleERR){
        double amplitude=0;
        double leng=r;
            if(r>(A+D)){
                if (A + C <= B + D && A + D <= B + C && A + B <= C + D){
                    amplitude=amplitudemethod.hurehaba(A, B, C, D);
                    if(QuadraticEquation.solvekakudo(A, B, C, D,angleERR)){
                        //leng=QuadraticEquation.solveEquations(A, B, C, D, r);
                        if(leng>(A+B)){
                            inputresult.inputresult(A, B, C, D, amplitude,Filename);

                        }
                    }
                }
            }
        
    }

    public void center_grashof(double A,double B,double C,double D,double r,double angleERR){
        double leng , amplitude=0;
        if(r==D){//中心に存在するか
            if (A + C <= B + D && A + D <= B + C && A + B <= C + D){
                amplitude = amplitudemethod.hurehaba(A,B,C,D);
                if(QuadraticEquation.solvekakudo(A, B, C, D,angleERR)) {
                    leng = r;
                    if (leng > (A + B)) {
                        inputresult.inputresult(A, B, C, D, amplitude,Filename);

                    }
                }
            }
        }
    }

    public void setliten(Progresslistener listen){
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
            //変数を変えるための関数
            searchvariable(variablelists, 0, i);
            //Aが最短節であるかを確認する
            if(searcmin(A, B, C, D)) {
                if(!center) {
                    grashof(A, B, C, D, r ,angleERR);
                }else{
                    center_grashof(A, B, C, D, r,angleERR);
                }
            }
            count();
        }
    }
}

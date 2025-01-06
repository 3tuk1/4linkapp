package Husi.Calculation;


public class QuadraticEquation {
    /*public static double solveEquations(double a, double b, double d,double c, double r) {
        double s=(a+b+c+d)/2;
        double S=Math.sqrt(s*(s-a-b)*(s-c)*(s-d));//ヘロンの公式より三角形の面積を求める
        double slope=(2*S)/d;
        double leave=r-d;
        double A=1+Math.pow(slope,2);
        double B=2*leave;
        double C=Math.pow(leave,2)-Math.pow(r,2);
        double x1,x2,y1,y2;
        double leng;
        x1=(-B+Math.sqrt(Math.pow(B,2)-4*A*C))/(2*A);//二次方程式の解を求める
        x2=(-B-Math.sqrt(Math.pow(B,2)-4*A*C))/(2*A);
        y1=x1*slope;
        y2=x2*slope;
        if(y1>0){
            leng=Math.sqrt(Math.pow(x1, 2)+Math.pow(y1,2));
        }else if(y2>0){
            leng=Math.sqrt(Math.pow(x2, 2)+Math.pow(y2,2));
        }else{
            leng=-1;
        }
        return leng;
    }*/


    public static boolean solvekakudo(double a, double b, double c,double d,double angleERR){
        double cosValue1 = (amplitudemethod.getSquare(d)+amplitudemethod.getSquare(a+b)-amplitudemethod.getSquare(c))/(2*d*(a+b));
        double cosValue2 = (amplitudemethod.getSquare(d)+amplitudemethod.getSquare(b-a)-amplitudemethod.getSquare(c))/(2*d*(b-a));
        double ans = (Math.abs(Math.abs(Math.acos(cosValue1)) - Math.abs(Math.acos(cosValue2)))*180/Math.PI);
        return  ans<= angleERR;//誤差20度まで許容する
    }
}

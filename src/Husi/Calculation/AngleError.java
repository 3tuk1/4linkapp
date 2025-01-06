package Husi.Calculation;


public class AngleError {
    public static boolean solve(double a, double b, double c, double d, double angleERR){
        double cosValue1 = (amplitudeMethod.getSquare(d)+ amplitudeMethod.getSquare(a+b)- amplitudeMethod.getSquare(c))/(2*d*(a+b));
        double cosValue2 = (amplitudeMethod.getSquare(d)+ amplitudeMethod.getSquare(b-a)- amplitudeMethod.getSquare(c))/(2*d*(b-a));
        double ans = (Math.abs(Math.abs(Math.acos(cosValue1)) - Math.abs(Math.acos(cosValue2)))*180/Math.PI);
        return  ans<= angleERR;//誤差20度まで許容する
    }
}

package Husi.Calculation;


public class AngleError {
    public static boolean solve(double a, double b, double c, double d, double angleERR){
        double cosValue1 = (AmplitudeMethod.getSquare(d)+ AmplitudeMethod.getSquare(a+b)- AmplitudeMethod.getSquare(c))/(2*d*(a+b));
        double cosValue2 = (AmplitudeMethod.getSquare(d)+ AmplitudeMethod.getSquare(b-a)- AmplitudeMethod.getSquare(c))/(2*d*(b-a));
        double ans = (Math.abs(Math.abs(Math.acos(cosValue1)) - Math.abs(Math.acos(cosValue2)))*180/Math.PI);
        return  ans<= angleERR;//誤差20度まで許容する
    }
}

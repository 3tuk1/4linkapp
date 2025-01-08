package Husi.Calculation;


public class AmplitudeMethod {
    public static double hurehaba(double linkA, double linkB, double linkC, double linkD) {
        double ans, ans_abs;
        double cosValue1 = (getSquare(linkD) + getSquare(linkC) - getSquare(linkB-linkA)) / (2.0 * linkD * linkC);
        double cosValue2 = (getSquare(linkD) + getSquare(linkC) - getSquare(linkA+linkB)) / (2.0 * linkD * linkC);
        
        ans_abs = Math.abs(Math.acos(cosValue1)) - Math.abs(Math.acos(cosValue2));
        ans = Math.abs(ans_abs)*180/Math.PI;
        return ans;
    }
    public static double getSquare(double number) {
		return Math.pow(number,2);
	}
}

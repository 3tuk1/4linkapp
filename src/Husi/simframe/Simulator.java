package Husi.simframe;

public class Simulator {
    final public static int SHAPESCOPE = 10;
    coodinate coo = new coodinate();
    double thetaAD;
    double theta_variable = 0;
    
    public void calculate(double A, double B, double C, double D, double theta, double H){
        thetaAD += theta_variable;
        //リンク機構のそれぞれの角度を求める
        coo.Dx = 0;
        coo.Dy = 0;

        coo.Ax = coo.Dx + D * Math.cos(thetaAD);
        coo.Ay = coo.Dy + D * Math.sin(thetaAD);

        coo.Bx = A*Math.cos(theta+thetaAD-Math.PI)+coo.Ax;
        coo.By = A*Math.sin(theta+thetaAD-Math.PI)+coo.Ay;
        
        double l = Math.sqrt(pow(coo.Dx-coo.Bx)+pow(coo.Dy-coo.By));
        double rad = Math.acos((pow(l)+pow(B)-pow(C))/(2*B*l)) + Math.atan2(coo.Dy-coo.By, coo.Dx-coo.Bx);
        coo.Cx = coo.Bx + B*Math.cos(rad);
        coo.Cy = coo.By + B*Math.sin(rad);

        //ヒレの位置を求める
        double Htheta = Math.atan2((coo.Dy-coo.Cy),(coo.Dx-coo.Cx));

        coo.Hx = coo.Dx + H * Math.cos(Htheta);
        coo.Hy = coo.Dy + H * Math.sin(Htheta);

        //定数倍して図形を大きくする
        scope();
    }

    public void setThetaAD(double thetaAD) {
        this.thetaAD = thetaAD;
    }

    public void setTheta_variable(double value){this.theta_variable = value;}

    public static class coodinate {
        double Ax,Ay;
        double Bx,By;
        double Cx,Cy;
        double Dx,Dy;
        double Hx;
        public double Hy;
    }

    private double pow(double num){
        return Math.pow(num, 2);
    }

    private void scope(){
        coo.Ax *= SHAPESCOPE;
        coo.Ay *= SHAPESCOPE;
        coo.Bx *= SHAPESCOPE;
        coo.By *= SHAPESCOPE;
        coo.Cx *= SHAPESCOPE;
        coo.Cy *= SHAPESCOPE;
        coo.Dx *= SHAPESCOPE;
        coo.Dy *= SHAPESCOPE;
        coo.Hx *= SHAPESCOPE;
        coo.Hy *= SHAPESCOPE;
    }

    public void HtoCenter(double A, double B, double C, double D){
        //上下水平化の修正
        double acosValueMin = Math.acos((pow(D) + pow(C) - pow(B-A)) / (2.0 * D * C));
        double acosValuseMax = Math.acos((pow(D) + pow(C) - pow(A+B)) / (2.0 * D * C));

        thetaAD = (Math.abs(acosValuseMax)+Math.abs(acosValueMin))/2-Math.PI;
        
    }

    public coodinate getCoo(){
        return coo;
    }
}

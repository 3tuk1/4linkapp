package Husi.simframe;

import java.awt.Point;

public class cal_sim {
    final public static int shapescope = 10;
    coodinate coo = new coodinate();
    double thetaAD;
    double theta_variable = 0;

    Animation animation ;
    
    public coodinate cal_sim(double A,double B,double C,double D,double theta,double H){
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

        
        return coo;
    }

    public void setThetaAD(double thetaAD) {
        this.thetaAD = thetaAD;
    }

    public void setTheta_variable(double value){this.theta_variable = value;}
    public class coodinate{
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
        
        coo.Ax *= shapescope;
        coo.Ay *= shapescope;
        coo.Bx *= shapescope;
        coo.By *= shapescope;
        coo.Cx *= shapescope;
        coo.Cy *= shapescope;
        coo.Dx *= shapescope;
        coo.Dy *= shapescope;
        coo.Hx *= shapescope;
        coo.Hy *= shapescope;
    }
    public void setDxDy(Point point){
        double x= point.getX();
        double y= point.getY();
        coo.Ax += x;
        coo.Ay += y;
        coo.Bx += x;
        coo.By += y;
        coo.Cx += x;
        coo.Cy += y;
        coo.Dx += x;
        coo.Dy += y;
        coo.Hx += x;
        coo.Hy += y;
    }
    public void Htocenter(double A,double B,double C,double D){
        //上下水平化の修正
        double acosValuemin = Math.acos((getSquare(D) + getSquare(C) - getSquare(B-A)) / (2.0 * D * C));
        double acosValusemax = Math.acos((getSquare(D) + getSquare(C) - getSquare(A+B)) / (2.0 * D * C));

        thetaAD = (Math.abs(acosValusemax)+Math.abs(acosValuemin))/2-Math.PI;
        
    }
    public coodinate getcoo(){
        return coo;
    }

    private double getSquare(double num){
        return Math.pow(num,2);
    }
}

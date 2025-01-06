package Husi.simframe;

import Husi.simframe.cal_sim.coodinate;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.Font;


public class simFrame extends JFrame {
    private GridBagConstraints gbc = new GridBagConstraints();
    private coodinate coo;
    private int offsetx= 200;
    private double linkA_cal, linkB_cal, linkC_cal, linkD_cal, Finlengh_cal, radius;
    private JTextField A, B, C, D, H ,r;
    private JSlider offsetslider;
    private JSlider link_place_slider;
    private double thetaoffset=0;

    private double theta_angle_cal = 0;
    private  double linkA = 5;
    private  double linkB = 7;
    private double linkC = 10;
    private  double linkD = 9;
    private double Finlengh = 10;
    private double placeoffset=0;
    private JCheckBox checkboxsetAD,lenghoutput;
    private double abLength,bcLength,cdLength,daLength;
    private cal_sim cal = new cal_sim();

    private JTextField dis_DA_x,dis_DA_y,dis_DA_theta;
    DrawPanel DrawPanel = new DrawPanel();

    private XYSeries xySeries = new XYSeries("teko");

    public void simFrame(JPanel simPanel){
        JPanel InputPanel = createpanel();
        JPanel speedChartPanel = createChartpanel();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 5;
        gbc.weighty = 1;
        simPanel.add(DrawPanel,gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        simPanel.add(InputPanel,gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        simPanel.add(speedChartPanel,gbc);
    }
    private Timer timer;  // Timerを追加
    private double ani_theta = 0; // アニメーション用の角度


    ActionListener start = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            check=false;
            
            if (timer == null || !timer.isRunning()) {
                setxy(-1);//グラフのデータセットの初期化
                timer = new Timer(10, new ActionListener() { 
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateAnimation(); 
                    }
                });
                timer.start(); // タイマーを開始
            }
        }
    };
    public Point getCenterPoint(JPanel panel) {
        int centerX = panel.getWidth() / 2;
        int centerY = panel.getHeight() / 2;
        return new Point(centerX, centerY);
    }

    private void updateAnimation() {

        settheta(ani_theta);
        linkcal();
        lenght();

              
        DrawPanel.repaint(); 
        ani_theta += 1; // 角度を増加させてアニメーション
        if (ani_theta >= 361*2) {
            ani_theta = 0; // 角度をリセット
            timer.stop(); // 1サイクル後にアニメーションを停止

        }
    }

    class DrawPanel extends JPanel{

        @Override
        protected void paintComponent(Graphics g) {//四本線を描画する

            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            if(coo != null){
                // キャンバスの高さを取得
                int canvasHeight = g2d.getClipBounds().height;
                int canvasWidth = g2d.getClipBounds().width;

                BasicStroke stroke = new BasicStroke(5.0f);
                g2d.setStroke(stroke);

                //四節リンク機構の描画
                Line2D.Double lineAB = new Line2D.Double(offsetx+coo.Ax, canvasHeight - coo.Ay, offsetx+coo.Bx, canvasHeight - coo.By);
                g2d.draw(lineAB);

                Line2D.Double lineBC = new Line2D.Double(offsetx+coo.Bx, canvasHeight - coo.By, offsetx+coo.Cx, canvasHeight - coo.Cy);
                g2d.draw(lineBC);

                Line2D.Double lineCD = new Line2D.Double(offsetx+coo.Cx, canvasHeight - coo.Cy, offsetx+coo.Dx, canvasHeight - coo.Dy);
                g2d.draw(lineCD);

                float[] dashPattern = {5,5};//5pix line,5pix space
                BasicStroke dotted = new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10,dashPattern,0);
                g2d.setStroke(dotted);

                Line2D.Double lineAD_x = new Line2D.Double(offsetx+coo.Ax, canvasHeight - coo.Ay, offsetx+coo.Dx, canvasHeight - coo.Ay);
                g2d.draw(lineAD_x);

                Line2D.Double lineAD_y = new Line2D.Double(offsetx+coo.Dx, canvasHeight - coo.Ay, offsetx+coo.Dx, canvasHeight - coo.Dy);
                g2d.draw(lineAD_y);

                g2d.setStroke(stroke);
                Line2D.Double lineDH = new Line2D.Double(offsetx+coo.Hx, canvasHeight - coo.Hy, offsetx+coo.Dx, canvasHeight - coo.Dy);
                g2d.draw(lineDH);
                double diameter = 2*radius*cal_sim.shapescope;
                //カプセル内視鏡の描画
                Ellipse2D.Double capsule = new Ellipse2D.Double(offsetx+getCenterPoint(DrawPanel).getX()- diameter, canvasHeight - getCenterPoint(DrawPanel).getY()-diameter/2, diameter, diameter);
                g2d.draw(capsule);

                //反対側の四節リンク機構
                g2d.draw(new Line2D.Double(offsetx+canvasWidth-coo.Ax-diameter, canvasHeight - coo.Ay, offsetx+canvasWidth-coo.Bx-diameter, canvasHeight - coo.By));

                g2d.draw( new Line2D.Double(offsetx+canvasWidth-coo.Bx-diameter, canvasHeight - coo.By, offsetx+canvasWidth-coo.Cx-diameter, canvasHeight - coo.Cy));

                g2d.draw(new Line2D.Double(offsetx+canvasWidth-coo.Cx-diameter, canvasHeight - coo.Cy, offsetx+canvasWidth-coo.Dx-diameter, canvasHeight - coo.Dy));

                g2d.draw(new Line2D.Double(offsetx+canvasWidth-coo.Hx-diameter, canvasHeight - coo.Hy, offsetx+canvasWidth-coo.Dx-diameter, canvasHeight - coo.Dy));

                if(lenghoutput.isSelected()) {
                    // 線分ABの長さを表示
                    g2d.drawString(String.format("Aの長さ: %.2f", (abLength / cal_sim.shapescope)), (int) (offsetx+((coo.Ax + coo.Bx) / 2)), (int) ((canvasHeight - ((coo.Ay + coo.By) / 2)) + 20));


                    // 線分BCの長さを表示
                    g2d.drawString(String.format("Bの長さ: %.2f", bcLength / cal_sim.shapescope), (int) (offsetx+((coo.Cx + coo.Bx) / 2)), (int) (canvasHeight - ((coo.Cy + coo.By) / 2) + 20));


                    // 線分CDの長さを表示
                    g2d.drawString(String.format("Cの長さ: %.2f", cdLength / cal_sim.shapescope), (int) (offsetx+((coo.Cx + coo.Dx) / 2)), (int) (canvasHeight - ((coo.Dy + coo.Cy) / 2) + 20));


                    // 線分DAの長さを表示
                    g2d.drawString(String.format("Dの長さ: %.2f", daLength / cal_sim.shapescope), (int) (offsetx+((coo.Ax + coo.Dx) / 2)), (int) (canvasHeight - ((coo.Ay + coo.Dy) / 2) + 20));
                }
            }
        }

    }
    private void lenght(){
        // 線分ABの長さを計算
        abLength = Math.sqrt(Math.pow(coo.Bx - coo.Ax, 2) + Math.pow(coo.By - coo.Ay, 2));
        // 線分BCの長さを計算
        bcLength = Math.sqrt(Math.pow(coo.Cx - coo.Bx, 2) + Math.pow(coo.Cy - coo.By, 2));
        // 線分CDの長さを計算
        cdLength = Math.sqrt(Math.pow(coo.Dx - coo.Cx, 2) + Math.pow(coo.Dy - coo.Cy, 2));
        // 線分DAの長さを計算
        daLength = Math.sqrt(Math.pow(coo.Ax - coo.Dx, 2) + Math.pow(coo.Ay - coo.Dy, 2));
        double x,y;
        x = (Math.sqrt(Math.pow(coo.Ax - coo.Dx, 2)))/cal_sim.shapescope;
        y = (Math.sqrt(Math.pow(coo.Ay - coo.Dy, 2)))/cal_sim.shapescope;
        dis_DA_x.setText(String.valueOf(x));
        dis_DA_y.setText(String.valueOf(y));
        dis_DA_theta.setText(String.valueOf(Math.toDegrees(Math.atan2(y,x))));
    }


    private double theta=0;
        
    public void settheta(double value){
            value = Math.toRadians(value);
            theta = value;
    }
    private boolean check=false;    
    private void linkcal(){//四つの点を作成する
        linkA_cal=Double.parseDouble(A.getText());
        linkB_cal =Double.parseDouble(B.getText());
        linkC_cal =Double.parseDouble(C.getText());
        linkD_cal =Double.parseDouble(D.getText());
        Finlengh_cal =Double.parseDouble(H.getText());
        radius = Double.parseDouble(r.getText());

        if(!check&&checkboxsetAD.isSelected()){
            cal.Htocenter(linkA_cal, linkB_cal, linkC_cal, linkD_cal);
        }else{
            cal.setThetaAD(-Math.PI);
        }
        //このtheta オフセットによりリンクのヒレの水平化に成功
        thetaoffset = Math.acos((Math.pow(linkA_cal+ linkB_cal,2)+Math.pow(linkD_cal,2)-Math.pow(linkC_cal,2))/(2*(linkA_cal+ linkB_cal)* linkD_cal));
        theta = theta + thetaoffset;
        
        
        cal.cal_sim(linkA_cal, linkB_cal, linkC_cal, linkD_cal, theta, Finlengh_cal);
        //cal.setDxDy(getCenterPoint(DrawPanel));
        
        coo = cal.getcoo();
        setDxDy(getCenterPoint(DrawPanel));
        setxy(coo.Hy);
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
    public void setValue(double linkA, double linkB, double linkC, double linkD, double Fin_lengh){
        A.setText(String.valueOf(linkA));
        B.setText(String.valueOf(linkB));
        C.setText(String.valueOf(linkC));
        D.setText(String.valueOf(linkD));
        H.setText(String.valueOf(Fin_lengh));
    }


    private JPanel createpanel() {
        JPanel inputepanel = new JPanel(new GridLayout(6, 3, 10, 10));
        JPanel settingpanel = new JPanel(new GridLayout(6,2,10,10));
        JPanel multipanel = new JPanel(new GridBagLayout());
        JLabel A_label = new JLabel("節Aの長さ");
        A = new JTextField(String.valueOf(linkA),10);
        A.setEditable(true);
        A.setHorizontalAlignment(JTextField.RIGHT);

        JLabel B_label = new JLabel("節Bの長さ");
        B = new JTextField(String.valueOf(linkB),10);
        B.setHorizontalAlignment(JTextField.RIGHT);

        JLabel C_label = new JLabel("節Cの長さ");
        C = new JTextField(String.valueOf(linkC),10);
        C.setHorizontalAlignment(JTextField.RIGHT);

        JLabel D_label = new JLabel("節Dの長さ");
        D = new JTextField(String.valueOf(linkD),10);
        D.setHorizontalAlignment(JTextField.RIGHT);

        JLabel H_label = new JLabel("ヒレの長さ");
        H = new JTextField(String.valueOf(Finlengh),10);
        H.setHorizontalAlignment(JTextField.RIGHT);

        JLabel R_label = new JLabel("カプセル内視鏡の半径");
        r = new JTextField("18",10);
        r.setHorizontalAlignment(JTextField.RIGHT);


        JButton button = new JButton("start");
        button.addActionListener(start);
        button.setPreferredSize(new Dimension(100, 30));

        checkboxsetAD = new JCheckBox("上下の調整");
        lenghoutput = new JCheckBox("長さの表示");

        JLabel offset_label = new JLabel("offset");

        JLabel link_place_label = new JLabel("linkの位置調整");


        offsetslider = new JSlider();
        offsetslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int offsetvalue = source.getValue();
                setoffset(offsetvalue);
                settheta(ani_theta);
                linkcal();
                lenght();


                DrawPanel.repaint();
            }
        });

        link_place_slider = new JSlider(0,1000,0);
        link_place_slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int offsetvalue = source.getValue();
                theta_angle_cal = (offsetvalue  / 10 )  ;
                cal.setTheta_variable(Math.toRadians(theta_angle_cal));
                settheta(ani_theta);
                linkcal();
                lenght();


                DrawPanel.repaint();
            }
        });

        JLabel dis_x_label = new JLabel("ADのxの差");
        dis_DA_x = new JTextField("0",10);
        dis_DA_x.setHorizontalAlignment(JTextField.RIGHT);
        dis_DA_x.setEditable(false);

        JLabel dis_y_label = new JLabel("ADのyの差");
        dis_DA_y = new JTextField("0",10);
        dis_DA_y.setHorizontalAlignment(JTextField.RIGHT);
        dis_DA_y.setEditable(false);

        JLabel dis_theta_label = new JLabel("ADの角度");
        dis_DA_theta = new JTextField("0",10);
        dis_DA_theta.setHorizontalAlignment(JTextField.RIGHT);
        dis_DA_theta.setEditable(false);



        inputepanel.add(A_label);
        inputepanel.add(A);

        inputepanel.add(B_label);
        inputepanel.add(B);

        inputepanel.add(C_label);
        inputepanel.add(C);

        inputepanel.add(D_label);
        inputepanel.add(D);

        inputepanel.add(H_label);
        inputepanel.add(H);

        inputepanel.add(R_label);
        inputepanel.add(r);

        settingpanel.add(checkboxsetAD);
        settingpanel.add(lenghoutput);

        settingpanel.add(offset_label);
        settingpanel.add(offsetslider);

        settingpanel.add(link_place_label);
        settingpanel.add(link_place_slider);

        settingpanel.add(dis_x_label);
        settingpanel.add(dis_DA_x);

        settingpanel.add(dis_y_label);
        settingpanel.add(dis_DA_y);

        settingpanel.add(dis_theta_label);
        settingpanel.add(dis_DA_theta);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 4;
        multipanel.add(inputepanel,gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        multipanel.add(settingpanel,gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 2;
        multipanel.add(button,gbc);

        return multipanel;
    }

    private void setoffset(int offset){
        offsetx = (offset*10)-300;
    }

    private JPanel createChartpanel(){
        XYSeriesCollection xydata = new XYSeriesCollection();
        xydata.addSeries(xySeries);
        XYDataset speeddata = xydata;

        // 軸を生成
        ValueAxis xAxis = new NumberAxis("クランクの位相(° )");
        xAxis.setLabelFont(new Font("Meiryo", Font.BOLD, 14)); // ラベルフォントの設定
        xAxis.setTickLabelFont(new Font("Meiryo", Font.PLAIN,12));
        xAxis.setRange(0,720);
        ValueAxis yAxis = new NumberAxis("ヒレの変位(mm)");
        yAxis.setLabelFont(new Font("Meiryo", Font.BOLD, 14)); // ラベルフォントの設定
        yAxis.setTickLabelFont(new Font("Meiryo", Font.PLAIN,12));
        // レンダラ ⇒ これが全体のレンダラになる
        XYItemRenderer renderer = new StandardXYItemRenderer();
        // Plotを生成してチャートを表示する
        XYPlot xyPlot = new XYPlot(speeddata,xAxis,yAxis,renderer);




        // JFreeChartの作製
        JFreeChart jfreeChart = new JFreeChart("ヒレのy座標の変位", (Plot) xyPlot);
        jfreeChart.getTitle().setFont(new Font("Meiryo",Font.BOLD,20));

        jfreeChart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        ChartPanel chartPanel = new ChartPanel(jfreeChart);

        //chartPanel.setPreferredSize(new Dimension(700, 500));


        JPanel panel = new JPanel();

        panel.add(chartPanel);

        return panel;
    }
    private void setxy(double place) {
        if(placeoffset == 0){
            placeoffset = place /cal_sim.shapescope;
        }
        //thetaはradのため度に変換
        xySeries.add(Math.toDegrees(theta-thetaoffset),(place/cal_sim.shapescope)-placeoffset);
        if(place==-1){
            xySeries.clear();
            placeoffset = 0;
        }

    }
}

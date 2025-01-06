package Husi.mainsrc;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import Husi.Calculation.*;
import Husi.fileframe.*;
import Husi.simframe.simFrame;

public class MyFrame extends JFrame implements ActionListener {
    private JTextField A, B, C, D, R, min_vari, max_vari, angleERR;
    private JCheckBox checkbox1, checkbox2, checkbox3, checkbox4,checkbox5,checkbox6,checkbox7;
    private int max_inch=20;
    private int min_inch=1;
    private double inc;
    public JPanel graphPanel;
    private JLabel out_max;
    private JLabel out_min;
    inputresult inputresult = new inputresult();
    GridBagConstraints gbc = new GridBagConstraints();
    JPanel mainpanel = new JPanel(new GridBagLayout());
    JPanel filepanel = new JPanel(new GridBagLayout());
    JPanel simpanel = new JPanel(new GridBagLayout());
    private JProgressBar progbar = new JProgressBar();
    public static JTabbedPane tabbedpane;
    public static simFrame simFrame;



    MyFrame(String title) {
        setLAF();
        setTitle(title);
        setSize(1980,1080);
        setFullScreen();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon(getClass().getResource("/image/icon.png"));
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            setIconImage(icon.getImage());
        } else {
            System.out.println("アイコンの読み込みに失敗しました: /image/icon.png");
        }


        tabbedpane = new JTabbedPane();

        createimagePanel();

        JPanel panel = createpanel();
        JPanel input2Panel = create2panel();
        JPanel outputPanel = createOutPanel();
        JButton button = createButton();

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 2;
        mainpanel.add(panel,gbc);
         
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 2;
        mainpanel.add(input2Panel, gbc);
         
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        mainpanel.add(button, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        mainpanel.add(outputPanel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;

        fileFrame fileFrame = new fileFrame();
        fileFrame.fileFrame(filepanel);

        simFrame = new simFrame();
        simFrame.simFrame(simpanel);

        tabbedpane.addTab("計算", mainpanel);
        tabbedpane.addTab("ファイル", filepanel);
        tabbedpane.addTab("シミュレータ", simpanel);
        getContentPane().add(tabbedpane, BorderLayout.CENTER);
    }
    public class variablelist{
        public boolean variables;
        String variablename;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String Filename = inputresult.makefile();
        //変数用のリスト
        variablelist[] variablelist = new variablelist[4];
        //resultArea.setText("");
        for (int i = 0; i < 4; i++) {
            variablelist[i] = new variablelist(); // 各要素を初期化
        }
        variablelist[0].variables = checkbox1.isSelected(); // A
        variablelist[1].variables = checkbox2.isSelected(); // B
        variablelist[2].variables = checkbox3.isSelected(); // C
        variablelist[3].variables = checkbox4.isSelected(); // D
        int fixedValue = 0;
        //増加量用のboolean型の変数
        boolean scope10=checkbox5.isSelected();
        boolean scope100=checkbox6.isSelected();
        double a=1,b=1,c=1,d=1,r=18;
        if((scope10==true)&&(scope100==true)){
            JOptionPane.showMessageDialog(null,"倍率を両方選択しないでください。");
            return;
        }
        //増加量を変更する
        if(scope10==true){
            inc=0.1;
        }else if(scope100==true){
            inc=0.01;
        }else {
            inc=1;
        }

            if(checkbox1.isSelected()==true){
                A.setText("変数");
            }else{
                try {
                    a = Double.parseDouble(A.getText());
                    // 数値が正しく入力された場合の処理
                } catch (NumberFormatException ex) {
                    // 数値以外の文字列が入力された場合のエラーメッセージ表示
                    JOptionPane.showMessageDialog(null,"数字以外のものを入力したり空白にしないでください");
                    return;
                }
            }
            if(checkbox2.isSelected()==true){
                B.setText("変数");
            }else{
                try {
                    b = Double.parseDouble(B.getText());
                    // 数値が正しく入力された場合の処理
                } catch (NumberFormatException ex) {
                    // 数値以外の文字列が入力された場合のエラーメッセージ表示
                    JOptionPane.showMessageDialog(null,"数字以外のものを入力したり空白にしないでください");
                    return;
                }
            }
            if(checkbox3.isSelected()==true){
                C.setText("変数");
            }else{
                try {
                    c = Double.parseDouble(C.getText());
                    // 数値が正しく入力された場合の処理
                } catch (NumberFormatException ex) {
                    // 数値以外の文字列が入力された場合のエラーメッセージ表示
                    JOptionPane.showMessageDialog(null,"数字以外のものを入力したり空白にしないでください");
                    return;
                }
            }
            if(checkbox4.isSelected()==true){
                D.setText("変数");
            }else{
                try {
                    d = Double.parseDouble(D.getText());
                    // 数値が正しく入力された場合の処理
                } catch (NumberFormatException ex) {
                    // 数値以外の文字列が入力された場合のエラーメッセージ表示
                    JOptionPane.showMessageDialog(null,"数字以外のものを入力したり空白にしないでください");
                    return;
                }
            }
            r=Double.parseDouble(R.getText());
            try {
                min_inch = Integer.parseInt(min_vari.getText());
                max_inch = Integer.parseInt(max_vari.getText());
                // 数値が正しく入力された場合の処理
            } catch (NumberFormatException ex) {
                // 数値以外の文字列が入力された場合のエラーメッセージ表示
                JOptionPane.showMessageDialog(null,"数字以外のものを入力したり空白にしないでください");
                return;
            }
            
            
            
            if (variablelist[0].variables) {
                variablelist[0].variablename="A";
                fixedValue++;
            }else{
                variablelist[0].variablename=null;
            }
            if (variablelist[1].variables) {
                variablelist[1].variablename="B";
                fixedValue++;
            }else{
                variablelist[1].variablename=null;
            }
            if (variablelist[2].variables) {
                variablelist[2].variablename="C";
                fixedValue++;
            }else{
                variablelist[2].variablename=null;
            }
            if (variablelist[3].variables) {
                variablelist[3].variablename="D";
                fixedValue++;
            }else{
                variablelist[3].variablename=null;
            }

            ProgressReceiver recv = new ProgressReceiver();
            if (fixedValue == 0) {
                JOptionPane.showMessageDialog(null,"変数を選択してください。");
                return;
            }else if(fixedValue==3){

                maincal_3vari calnum = new maincal_3vari();
                calnum.setliten(recv);
                calnum.Grashoftheorem(a, b, c, d, r, variablelist, min_inch, max_inch, inc, Filename,Double.parseDouble(angleERR.getText()),checkbox7.isSelected());

            }else if(fixedValue==2) {
                maincal_2vari calnum= new maincal_2vari();
                calnum.setliten(recv);
                calnum.Grashoftheorem(a, b, c, d, r, variablelist, min_inch, max_inch, inc, Filename,Double.parseDouble(angleERR.getText()),checkbox7.isSelected());

            }else if(fixedValue==1){
                maincal  calnum =  new maincal();
                calnum.setliten(recv);
                calnum.Grashoftheorem(a, b, c, d, r, variablelist, min_inch, max_inch, inc, Filename,Double.parseDouble(angleERR.getText()),checkbox7.isSelected());

            }else if(fixedValue==4){
                maincal_4vari calnum =new maincal_4vari();
                calnum.setliten(recv);
                calnum.Grashoftheorem(a, b, c, d, r, variablelist, min_inch, max_inch, inc, Filename,Double.parseDouble(angleERR.getText()),checkbox7.isSelected());

            }
            String result ;
            maxandmin maxandmin = new maxandmin();
            maxandmin.maxString(Filename);
            out_max.setText(maxandmin.getMaxString());
            out_min.setText(maxandmin.getMinString());
            if(graphPanel != null){
                graphPanel.repaint(); 
            }
            
    }
    
    void setLAF() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			//com.sun.java.swing.plaf.gtk.GTKLookAndFeel
			//com.sun.java.swing.plaf.motif.MotifLookAndFeel
			//com.sun.java.swing.plaf.windows.WindowsLookAndFeel
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    private JPanel createpanel() {
        JPanel panel = new JPanel(new GridLayout(4, 3, 10, 10));
        JLabel A_label = new JLabel("節Aの長さ");
        A = new JTextField(10);
        A.setHorizontalAlignment(JTextField.RIGHT);

        JLabel B_label = new JLabel("節Bの長さ");
        B = new JTextField(10);
        B.setHorizontalAlignment(JTextField.RIGHT);

        JLabel C_label = new JLabel("節Cの長さ");
        C = new JTextField(10);
        C.setHorizontalAlignment(JTextField.RIGHT);

        JLabel D_label = new JLabel("節Dの長さ");
        D = new JTextField(10);
        D.setHorizontalAlignment(JTextField.RIGHT);

        checkbox1 = new JCheckBox("Aを変数");
        checkbox2 = new JCheckBox("Bを変数");
        checkbox3 = new JCheckBox("Cを変数");
        checkbox4 = new JCheckBox("Dを変数");

        panel.add(checkbox1);
        panel.add(A_label);
        panel.add(A);

        panel.add(checkbox2);
        panel.add(B_label);
        panel.add(B);

        panel.add(checkbox3);
        panel.add(C_label);
        panel.add(C);

        panel.add(checkbox4);
        panel.add(D_label);
        panel.add(D);
        return panel;
    }

    private void setFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        } else {
            System.err.println("フルスクリーンはサポートされていません");
            gd.setFullScreenWindow(this); 
        }
    }

    private JPanel create2panel() {

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        JPanel panel1 = new JPanel(new GridLayout(1, 3, 10, 10));
        JPanel panel2 = new JPanel(new GridLayout(1, 4, 10, 10));
        JPanel panel3 = new JPanel(new GridLayout(1, 3, 10, 10));
        JPanel panel4 = new JPanel(new GridLayout(1, 3, 10, 10));
        JPanel panel5 = new JPanel(new GridLayout(1, 3, 10, 10));

        JLabel min_vari_label = new JLabel("変数の最小値");
        min_vari = new JTextField("5",10);
        min_vari.setHorizontalAlignment(JTextField.RIGHT);

        JLabel max_vari_label = new JLabel("変数の最大値");
        max_vari = new JTextField("20",10);
        max_vari.setHorizontalAlignment(JTextField.RIGHT);

        JLabel R_label = new JLabel("カプセル内視鏡の半径");
        R = new JTextField("18",10);
        R.setHorizontalAlignment(JTextField.RIGHT);

        JLabel angleERR_JLabel = new JLabel("(Θ3+Θ4-π)の時の誤差(°)");
        angleERR = new JTextField("0",10);
        angleERR.setHorizontalAlignment(JTextField.RIGHT);

        
        checkbox5 = new JCheckBox("0.1単位");
        checkbox6 = new JCheckBox("0.01単位");
        checkbox7 = new JCheckBox("中心に置く");

        panel1.add(R_label);
        panel1.add(R);

        panel2.add(checkbox5);
        panel2.add(checkbox6);
        panel2.add(checkbox7);
        
        panel3.add(min_vari_label);
        panel3.add(min_vari);

        panel4.add(max_vari_label);
        panel4.add(max_vari);
        
        panel5.add(angleERR_JLabel);
        panel5.add(angleERR);

        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        panel.add(panel4);
        panel.add(panel5);

        return panel;
    }

    private JPanel createOutPanel(){
        JPanel panel = new JPanel(new GridBagLayout());

        out_max = new JLabel(" ");
        out_min = new JLabel(" ");
        JLabel out_maxtitle_label = new JLabel(" 最大値 :");
        out_maxtitle_label.setHorizontalAlignment(JLabel.LEFT);
        JLabel out_mintitle_label = new JLabel(" 最小値 :");
        out_mintitle_label.setHorizontalAlignment(JLabel.LEFT);

        progbar.setStringPainted(true);






        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        panel.add(out_maxtitle_label,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        panel.add(out_max,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        panel.add(out_mintitle_label,gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        panel.add(out_min,gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 2;
        gbc.weighty = 1;
        panel.add(progbar,gbc);

        gbc.fill = GridBagConstraints.NONE;
        return panel;
    }

    private void createimagePanel(){
        
        ImageIcon imagelinknomal = new ImageIcon(getClass().getResource("/image/四節リンク機構図(通常).png"));
        ImageIcon imagelinkmin = new ImageIcon(getClass().getResource("/image/四節リンク機構図(最短).png"));
        ImageIcon imagelinkmax = new ImageIcon(getClass().getResource("/image/四節リンク機構図(最長).png"));

        Image imgNormal = imagelinknomal.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        Image imgMin = imagelinkmin.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        Image imgMax = imagelinkmax.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);

        
        // リサイズした画像をImageIconに変換
        ImageIcon resizedNormal = new ImageIcon(imgNormal);
        ImageIcon resizedMin = new ImageIcon(imgMin);
        ImageIcon resizedMax = new ImageIcon(imgMax);

        // JLabelに設定
        JLabel inomal = new JLabel(resizedNormal);
        JLabel imin = new JLabel(resizedMin);
        JLabel imax = new JLabel(resizedMax);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0d;
        gbc.weighty = 1.0d;
        mainpanel.add(inomal, gbc);

        gbc.gridy = 1;
        mainpanel.add(imax, gbc);

        gbc.gridy = 2;
        mainpanel.add(imin,gbc);

    }

    class ProgressReceiver implements Progresslistener {

        @Override
        public void onProgressUpdate(int progress) {
            //Thread thread = new Thread(() -> {
                progbar.setValue(progress);
                repaint();
            //});
            //thread.start();
        }

        @Override
        public void onProgressMax(int max) {
            progbar.setMaximum(max);
        }
    }
    private JButton createButton(){
        JButton button = new JButton("計算");
        button.addActionListener(this);
        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }

}
    



package Husi.fileframe;

import Husi.mainsrc.MyFrame;
import Husi.mainsrc.Progresslistener;
import Husi.simframe.cal_sim;
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;



public class fileFrame extends JFrame {
    private JList<String> fileList;
    private cal_sim simulation = new cal_sim();
    private JComboBox rangecombo;
    private JComboBox sortcombo;

    private double  NumCycle = 1, sin_high = 1 ;
    private JTextField range1,range2,Fin_length;
    private DefaultListModel<String> listModel;
    private GridBagConstraints gbc = new GridBagConstraints();
    private JList<String> resultArea;
    private JCheckBox checkBoxup,checkBoxdown;

    private static final AtomicBoolean isRunning = new AtomicBoolean(false);
    private static String Filename;
    private static DefaultListModel<String> filelistmodel;
    private XYSeriesCollection xydata = new XYSeriesCollection();
    private XYSeries Fin_XY = new XYSeries("ヒレのｙ座標");
    private XYSeries sin_XY = new XYSeries("sin波形");
    private JPanel chartpanel = createChartpanel();
    private JPopupMenu resultpopup = new JPopupMenu();
    private JPopupMenu filepopup = new JPopupMenu();
    private double deviation = 0;
    private JTextField deviation_text;
    public  all_diffe ALL = new all_diffe();

    private JProgressBar bar = new JProgressBar();
    
    public void fileFrame(JPanel filepanel){
        listModel = new DefaultListModel<>();
        filelistmodel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        resultArea = new JList<>(filelistmodel);
        
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.addMouseListener(new FileMouse());
        resultArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultArea.addMouseListener(new resultMouse());
        addresultPopupMenuItem("シミュレーションに入力", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(resultArea!= null) {
                    String[] split = resultArea.getSelectedValue().split("[,:]");
                    MyFrame.simFrame.setValue(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Double.parseDouble(Fin_length.getText()));
                    MyFrame.tabbedpane.setSelectedIndex(2);
                }else{
                    JOptionPane.showMessageDialog(null,"値を選択してください!");
                }
            }
        });
        addresultPopupMenuItem("グラフ化", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedresult = resultArea.getSelectedValue();
                if(selectedresult != null) {
                    setXY(selectedresult);
                }else {
                    System.out.println("nullSelect");
                }
            }
        });

        addfilePopupMenuItem("差のグラフを作成", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectfile = fileList.getSelectedValue();
                System.out.println(selectfile);
                if(selectfile != null){
                    creategraph GR = new creategraph();
                    GR.setFilename(selectfile);
                        Fin_XY.clear();
                        sin_XY.clear();
                        xydata.addSeries(GR.creategraph());
                        chartpanel.repaint();

                }else {
                    System.out.println("nullselect");
                }
            }
        });

        JScrollPane filescrollPane = new JScrollPane(fileList);
        JScrollPane outscrollPane = new JScrollPane(resultArea); // スクロールペインに追加

        loadFiles("resultfile");
        JPanel sortpanel = createsortpanel();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        filepanel.add(filescrollPane,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        filepanel.add(outscrollPane,gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        filepanel.add(sortpanel,gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        filepanel.add(chartpanel,gbc);
    }

    private JPanel createChartpanel() {

        xydata.addSeries(Fin_XY);
        xydata.addSeries(sin_XY);
        XYDataset speeddata = xydata;

        // 軸を生成
        ValueAxis xAxis = new NumberAxis("クランクの位相(° )");
        xAxis.setLabelFont(new Font("Meiryo", Font.BOLD, 14)); // ラベルフォントの設定
        xAxis.setTickLabelFont(new Font("Meiryo", Font.PLAIN,12));
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



        JPanel panel = new JPanel();

        panel.add(chartPanel);

        return panel;
    }
    public void setXY(String result){
        double linkA,linkB,linkC,linkD,placeoffset=0;
        double cooHy_max = 0,cooHy_min = 0;

        //初期化
        Fin_XY.clear();
        sin_XY.clear();
        deviation = 0;

        String[] split = result.split("[,:]");
        linkA = Double.parseDouble(split[0]);
        linkB = Double.parseDouble(split[1]);
        linkC = Double.parseDouble(split[2]);
        linkD = Double.parseDouble(split[3]);
        simulation.Htocenter(linkA, linkB, linkC, linkD);
        double theta_offset = Math.acos((Math.pow(linkA+linkB,2)+Math.pow(linkD,2)-Math.pow(linkC,2))/(2*(linkA+linkB)*linkD));
        double theta;
        for(int i=0; i<361*NumCycle ; i++) {
            theta = Math.toRadians(i);
            //sin_XY.add(Math.toDegrees(theta),((sin_high*Math.sin(theta-(Math.PI/2)))+sin_high*Math.sin(Math.PI/2))*cal_sim.shapescope);
            simulation.cal_sim(linkA,linkB,linkC,linkD, theta+theta_offset, Double.parseDouble(Fin_length.getText()));
            cal_sim.coodinate coo = simulation.getcoo();
            //初期化
            if(theta == 0){
                cooHy_min = coo.Hy;
                cooHy_max = coo.Hy;
            }
            if( placeoffset == 0){
                placeoffset = coo.Hy /cal_sim.shapescope;
            }
            Fin_XY.add(Math.toDegrees(theta),(coo.Hy/cal_sim.shapescope)-placeoffset);
            if(cooHy_min>coo.Hy){
                cooHy_min = coo.Hy;
            }else if(cooHy_max < coo.Hy ){
                cooHy_max = coo.Hy;
            }
        }
        sin_high = cooHy_max/cal_sim.shapescope;
        for(int i=0; i<361*NumCycle ; i++) {
            theta = Math.toRadians(i);
            sin_XY.add(i,((sin_high*Math.sin(theta-(Math.PI/2)))+sin_high));
            deviation += Math.abs(sin_XY.getY(i).doubleValue()-Fin_XY.getY(i).doubleValue());
        }
        deviation = (deviation/360)/sin_high;
        deviation_text.setText(String.format("偏差 : %.5f",deviation));
        chartpanel.repaint();

    }

    ActionListener sortlistener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String rangeStr = (String) rangecombo.getSelectedItem();
            String sortStr = (String) sortcombo.getSelectedItem();
            double range_high = Double.parseDouble(range1.getText());
            double range_low = Double.parseDouble(range2.getText());
            boolean up = checkBoxup.isSelected();
            boolean down = checkBoxdown.isSelected();
            if(up&&down){
                JOptionPane.showMessageDialog(null,"チェックボックスの両方をONにしないでください");
                return;
            }
            //範囲を指定
            if(range_high<range_low){
                double temp;
                temp = range_high;
                range_high = range_low;
                range_low = temp; 
            }
            //範囲指定するところの値を取得する

            //ソート未実装
            if(up){
                quicksort.setUporDown(true);
                quicksort.quickSort(filelistmodel,0,filelistmodel.getSize(),select.getselectId(sortStr));
            }
            if(down){
                quicksort.setUporDown(false);
                quicksort.quickSort(filelistmodel,0,filelistmodel.getSize(),select.getselectId(sortStr));
            }
            for (int i = filelistmodel.getSize() - 1; i >= 0; i--) {
                String str = filelistmodel.getElementAt(i);
                if(str == null){
                    JOptionPane.showMessageDialog(null,"null");
                    break;
                }
                String[] split = str.split("[,:]");
                Double value = Double.parseDouble(split[select.getselectId(rangeStr)]);
                final int indexremove=i;

                if((range_low>value)||(range_high<value)){
                    SwingUtilities.invokeLater(() -> {
                        filelistmodel.removeElementAt(indexremove);//指定したインデックスを削除する！
                    });
                }
            }
        }
    };

    public static void setFileList(DefaultListModel<String> fileList) {
        SwingUtilities.invokeLater(() ->{
            filelistmodel=fileList;
        });

    }

    ActionListener reloadlitener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            listModel.clear();
            loadFiles("resultfile");
        }
    };

    ActionListener all_fill_cal = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedFile = fileList.getSelectedValue();
            if(selectedFile != null) {

                Thread thread = new Thread(() -> {
                    ProgressReceiver recv = new ProgressReceiver();
                    ALL.addProglistener(recv);
                    ALL.setfilename(selectedFile);
                    ALL.setFin_lengh(Double.parseDouble(Fin_length.getText()));
                    ALL.all_diff();
                });
                thread.start();
            }else{
                JOptionPane.showMessageDialog(null,"ファイルを選択してください");
            }
        }
    };

    private JPanel createsortpanel(){
        List<String> combodata = new ArrayList<String>();

        for(select sel : select.values()){
            combodata.add(select.getlabel(sel));
        }

        rangecombo = new JComboBox(new DefaultComboBoxModel(combodata.toArray()));
        rangecombo.setPreferredSize(new Dimension(80, 30));
        sortcombo = new JComboBox(new DefaultComboBoxModel(combodata.toArray()));
        sortcombo.setPreferredSize(new Dimension(80, 30));
        JPanel sortPanel = new JPanel(new GridBagLayout());

        JButton button = new JButton("ソート");
        button.addActionListener(sortlistener);
        button.setPreferredSize(new Dimension(100, 30));

        JButton reloadbutton = new JButton("フォルダ再読み込み");
        reloadbutton.addActionListener(reloadlitener);
        reloadbutton.setPreferredSize(new Dimension(100, 30));

        JLabel range1_Label = new JLabel("範囲");
        range1 = new JTextField("0",10);
        range1.setHorizontalAlignment(JTextField.RIGHT);

        JLabel range2_Label = new JLabel(" ~ ");
        range2 = new JTextField("20",10);
        range2.setHorizontalAlignment(JTextField.RIGHT);

        JLabel Fin_lenghLabel = new JLabel(" シミュレーションのヒレの長さ ");
        Fin_length = new JTextField("20",10);
        Fin_length.setHorizontalAlignment(JTextField.RIGHT);

        checkBoxup = new JCheckBox("大きい順");
        checkBoxdown = new JCheckBox("小さい順");

        deviation_text = new JTextField(String.valueOf(deviation),10);
        deviation_text.setEditable(false);
        deviation_text.setHorizontalAlignment(JTextField.RIGHT);

        JButton all_file_button = new JButton("フォルダ全部読み");
        all_file_button.addActionListener(all_fill_cal);
        all_file_button.setPreferredSize(new Dimension(100, 30));



        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        sortPanel.add(rangecombo,gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        sortPanel.add(range1_Label,gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1;
        sortPanel.add(range1,gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        sortPanel.add(range2_Label,gbc);

        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 1;
        sortPanel.add(range2,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        sortPanel.add(sortcombo,gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        sortPanel.add(checkBoxup,gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        sortPanel.add(checkBoxdown,gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        sortPanel.add(button,gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        sortPanel.add(reloadbutton,gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        sortPanel.add(Fin_lenghLabel,gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        sortPanel.add(Fin_length,gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        sortPanel.add(deviation_text,gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        sortPanel.add(all_file_button,gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        sortPanel.add(bar,gbc);


        return sortPanel;
    }
   
 private void loadFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    listModel.addElement(file.getName());
                } else if (file.isDirectory()) {
                    listModel.addElement(file.getName() + "/");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "The folder is empty or does not exist.");
        }
    }


    private JMenuItem addresultPopupMenuItem(String name, ActionListener al){
        //ポップアップの項目の追加
        JMenuItem item = new JMenuItem(name);
        //項目にアクションリスナーの設定
        item.addActionListener(al);
        //追加
        resultpopup.add(item);
        return item;
    }
    private JMenuItem addfilePopupMenuItem(String name, ActionListener al){
        //ポップアップの項目の追加
        JMenuItem item = new JMenuItem(name);
        //項目にアクションリスナーの設定
        item.addActionListener(al);
        //追加
        filepopup.add(item);
        return item;
    }
    private void openfile(){
        if (isRunning.compareAndSet(false, true)) { // スレッドが実行中でなければ
                filelistmodel.clear();
                Runnableoutfile runna = new Runnableoutfile();
                Thread thread = new Thread(() -> {
                    try {
                        runna.run();
                    } finally {
                        isRunning.set(false); // スレッド終了後、フラグをfalseに戻す
                    }
                });
                thread.start();
        }
    }

    public static DefaultListModel<String> getFilelistmodel() {
        return filelistmodel;
    }

    public static void setFilelistmodel(DefaultListModel<String> filelistmodel1) {
        filelistmodel = filelistmodel1;
    }

    public  static String getFilename() {
        return Filename;
    }

    class ProgressReceiver implements Progresslistener{
        @Override
        public void onProgressUpdate(int progress) {
            bar.setValue(progress);
            if(progress%10==0)
                repaint();
        }
        @Override
        public void onProgressMax(int max){
            bar.setMaximum(max);
        }

    }



    public class FileMouse extends MouseAdapter{
        public void mouseClicked(MouseEvent e){
            if (e.getClickCount() == 2) { // ダブルクリックされた場合
                String selectedFile = fileList.getSelectedValue();
                Filename  = "resultfile" + "\\"+selectedFile;
                openfile();
            }
            if(e.getButton() == MouseEvent.BUTTON3){
                filepopup.show(e.getComponent(),e.getX(),e.getY());
            }
        }
    }
    public class resultMouse extends MouseAdapter{

        public void mouseClicked(MouseEvent e){

            if(e.getButton() == MouseEvent.BUTTON3){
                resultpopup.show(e.getComponent(),e.getX(),e.getY());
            }
        }
    }
}
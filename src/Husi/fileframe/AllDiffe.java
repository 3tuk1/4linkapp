package Husi.fileframe;

import Husi.mainsrc.Progresslistener;
import Husi.mainsrc.InputResult;
import Husi.simframe.Simulator;

import javax.swing.*;
import java.io.*;




public class AllDiffe {
    private String filename;
    private String copyfilename;
    private double Fin_lengh;

    private Simulator simulation = new Simulator();
    private int[] prog = new int[2];

    private Progresslistener lisner ;
    public void all_diff(){
        int linevalue;
        String line_str;
        make_cope();
        linevalue = countLines(filename);
        lisner.onProgressMax(linevalue);
        double[] result= new double[linevalue];
        int line_count = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            while((line_str = reader.readLine()) != null){
                String[] splint_str = line_str.split("[,:]");
                double[] sim_result = sim_360(Double.parseDouble(splint_str[0]),Double.parseDouble(splint_str[1]),Double.parseDouble(splint_str[2]),Double.parseDouble(splint_str[3]),Fin_lengh);
                double high = sinhigh(sim_result);
                result[line_count] =  cal_diff(sim_result,high);
                line_count++;
                lisner.onProgressUpdate(line_count);
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"ファイルの読み込みに失敗しました");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"ファイルの読み込み中にエラーが発生しました");
            throw new RuntimeException(e);
        }
        outresult(result);
    }
    private void make_cope(){
        filename = "resultfile" + "\\" + filename;

        InputResult make =  new InputResult();
        copyfilename = make.makefile();
    }

    private int countLines(String filePath){
        int lineCount = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            while(reader.readLine() != null){
                lineCount++;
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"ファイルが存在しません");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"入出力エラー");
        }
        return lineCount;
    }
    public void setfilename(String filename){
        this.filename = filename;
    }
    public void setFin_lengh(double Fin){
        this.Fin_lengh = Fin;
    }
    private void setprog(int now,int max){
        this.prog[0] = now;
        this.prog[1] = max;
    }
    public void addProglistener(Progresslistener lisner){
        this.lisner = lisner;
    }
    private double[] sim_360(double linkA,double linkB,double linkC,double linkD,double Fin_length){
        double[] Calculation_results = new double[361];
        double placeoffset = 0;
        double theta_offset = Math.acos((Math.pow(linkA+linkB,2)+Math.pow(linkD,2)-Math.pow(linkC,2))/(2*(linkA+linkB)*linkD));
        for(int i = 0; i <= 360 ; i++){
            double theta = Math.toRadians(i);
            simulation.HtoCenter(linkA, linkB, linkC, linkD);
            simulation.calculate(linkA,linkB,linkC,linkD, theta+theta_offset, Fin_length);
            Simulator.coodinate coo = simulation.getCoo();
            if(i == 0){
                placeoffset = coo.Hy / Simulator.SHAPESCOPE;
            }
            Calculation_results[i] = (coo.Hy/ Simulator.SHAPESCOPE)-placeoffset;

        }
        return Calculation_results;
    }
    //theta = 0　の時に一番下の位置になるようoffsetでしているため最大値を求めるだけで高さがわかる.
    private double sinhigh(double[] Hy){
        double Hy_max = 0;
        for(int i = 0; i <=360 ; i++) {
            if (i == 0) {
                Hy_max = Hy[i];
            } else if (Hy_max < Hy[i]) {
                Hy_max = Hy[i];
            }
        }
        return (Hy_max/Math.sin(Math.PI/2));
    }
    private double cal_diff(double[] cal_result,double sinhigh){
        double diff_value = 0;
        sinhigh = sinhigh/2;
        for(int i = 0;i <= 360 ; i++){
            double theta = Math.toRadians(i);
            diff_value += Math.abs( cal_result[i] - ((sinhigh*Math.sin(theta-(Math.PI/2)))+sinhigh));
        }
        //サイン波形のずれの平均値
        return ((diff_value/360)/sinhigh);
    }
    private void outresult(double[] diff_value) {
        String line;
        int line_count = 0;
        //指定した行まで行く

        try (BufferedReader reader = new BufferedReader(new FileReader(filename));
            BufferedWriter writer = new BufferedWriter(new FileWriter(copyfilename))) {

            while ((line = reader.readLine()) != null) {
                String input_str = line + String.format(": %.5f", diff_value[line_count]);
                writer.write(input_str);
                writer.newLine();
                line_count++;
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "ファイルが存在しません");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ファイルの入出力がない");
        }
    }
    }

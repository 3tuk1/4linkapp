package Husi.fileframe;

import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CreateGraph {
    private XYSeries graph =new XYSeries("差のグラフ");;
    private String  Filename;

    public void setFilename(String Filename){
        this.Filename = "resultfile//"+Filename;
        String line;
    }
    public XYSeries creategraph(){
        int count_line =0;
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(Filename))) {
            while ((line = reader.readLine()) != null) {
                 graph.add(count_line,Double.parseDouble(line.split("[,:]")[5]));
                 count_line ++;
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "ファイルが存在しません");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ファイルの入出力がない");
        }
        return graph;
    }
}

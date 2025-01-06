package Husi.mainsrc;
import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

public class inputResult {

    public void inputResult(double A, double B, double C, double D, double amplitude, String Filename){
        DecimalFormat df = new DecimalFormat("#.00");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Filename,true))) {
            String inputString = df.format(A) + " , " + df.format(B) + " , " + df.format(C) + " , " + df.format(D) + " : " + df.format(amplitude) + "\n";

            // ファイルに書き込む
            bw.write(inputString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 結果出力用のファイルの作製
    public String makefile(){
        int count = 1;
        File newFile;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedNow = now.format(formatter);
        String filename = null;

        try {
            // ディレクトリが存在しない場合は作成
            File dir = new File("resultfile");
            if (!dir.exists()) {
                dir.mkdirs();
            }
        do {
            filename = "resultfile" + "\\" + count + "_" + formattedNow + ".txt";
            newFile = new File(filename);
            count++;
        }while (!newFile.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"ファイルを作成しました。\nファイル名は:"+filename);
        return filename;
    }

    // 計算結果の出力
    public  String outresult(String Filename,int lineNum){
        String result=null;
        int currentLine = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(Filename))) {
            while ((result = reader.readLine()) != null) {
                if(currentLine == lineNum){
                    return result;
                }
                currentLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

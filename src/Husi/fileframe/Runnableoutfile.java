package Husi.fileframe;
import javax.swing.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Runnableoutfile implements Runnable {
    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileFrame.getFilename()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String finalLine = line; // final変数に代入
                SwingUtilities.invokeLater(() -> {
                    DefaultListModel<String> filelistmodel = fileFrame.getFilelistmodel();
                    filelistmodel.addElement(finalLine);
                    fileFrame.setFilelistmodel(filelistmodel); 
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
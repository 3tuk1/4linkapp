package Husi.mainsrc;
import javax.swing.SwingUtilities;



public class Husi {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MyFrame frame = new MyFrame("節の長さを調べる");
            frame.setVisible(true);
        });
    }
    
}


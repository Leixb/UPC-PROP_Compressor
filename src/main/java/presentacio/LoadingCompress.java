package presentacio;

import javax.swing.*;
import java.awt.*;

public class LoadingCompress {
    private CtrlPresentacio cp = new CtrlPresentacio();

    private JPanel panelLC;
    private JLabel labelGIF;
    private JButton cancelLarCompressióButton;

    private static JFrame f;

    public LoadingCompress() {
        f = new JFrame("PIZ Compressor");

        ImageIcon icon = new ImageIcon("src/main/resources/loading.gif");
        labelGIF.setIcon(icon);
    }

    public void showLoadingCompress(int alg, String fileIn, String fileOut, short qualityJPEG) {
        f.setResizable(true);
        f.setContentPane(this.panelLC);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(490, 200));
        f.setPreferredSize(new Dimension(490,250));
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);

        //cp.compress(alg, fileIn, fileOut, qualityJPEG);
    }
}

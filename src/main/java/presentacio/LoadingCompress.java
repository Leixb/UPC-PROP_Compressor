package presentacio;

import javax.swing.*;
import java.awt.*;

public class LoadingCompress {
    private CtrlPresentacio cp = new CtrlPresentacio();

    private JPanel panelLC;
    private JLabel labelGIF;

    private static JFrame f;

    public LoadingCompress() {
        f = new JFrame("PIZ Compressor");

        ImageIcon icon = new ImageIcon("src/main/resources/loading.gif");
        labelGIF.setIcon(icon);
    }

    public void showLoadingCompress(int alg, String fileIn, String fileOut, short qualityJPEG) {
        f.setResizable(false);
        f.setContentPane(this.panelLC);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(490,250));
        f.setLocationRelativeTo(null);
        f.pack();

        f.setVisible(true);

        compress(alg, fileIn, fileOut, qualityJPEG);
    }

    private void compress(int alg, String fileIn, String fileOut, short qualityJPEG)
    {
        SwingWorker sw = new SwingWorker()
        {
            @Override
            protected Object doInBackground() throws Exception {
                cp.compress(alg, fileIn, fileOut, qualityJPEG);
                return null;
            }
            @Override
            protected void done() {
                f.setVisible(false);
                StatsCompress sc = new StatsCompress(cp);
                sc.showStatsCompress();
            }
        };

        sw.execute();
    }
}

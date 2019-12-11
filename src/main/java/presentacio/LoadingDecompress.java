package presentacio;

import javax.swing.*;
import java.awt.*;

public class LoadingDecompress {
    private CtrlPresentacio cp = new CtrlPresentacio();

    private JLabel labelGIF;
    private JPanel panelLD;

    private static JFrame f;

    public LoadingDecompress() {
        f = new JFrame("PIZ Compressor");

        ImageIcon icon = new ImageIcon("src/main/resources/loading.gif");
        labelGIF.setIcon(icon);
    }

    public void showLoadingDecompress(String fileIn, String fileOut) {
        f.setResizable(false);
        f.setContentPane(this.panelLD);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(510,250));
        setWindowLocation();
        f.pack();
        f.setVisible(true);

        decompress(fileIn, fileOut);
    }

    private void setWindowLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int windowX = Math.max(0, (screenSize.width  - 510) / 2);
        int windowY = Math.max(0, (screenSize.height - 250) / 2 - 100);

        f.setLocation(windowX, windowY);
    }

    private void decompress(String fileIn, String fileOut) {
        SwingWorker sw = new SwingWorker()
        {
            @Override
            protected Object doInBackground() throws Exception {
                cp.decompress(fileIn, fileOut);
                return null;
            }
            @Override
            protected void done() {
                f.setVisible(false);
                StatsDecompress sd = new StatsDecompress(cp);
                sd.showStatsDecompress();
            }
        };

        sw.execute();
    }
}

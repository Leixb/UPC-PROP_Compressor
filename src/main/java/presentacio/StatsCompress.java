package presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StatsCompress {
    private CtrlPresentacio cp;

    private JPanel panelStatsComp;
    private JButton buttonExit;
    private JButton buttonHome;
    private JLabel labelTime;
    private JLabel labelDeflate;
    private JLabel labelSpeed;
    private JButton buttonShowFile;

    private static JFrame f;


    StatsCompress(CtrlPresentacio ctrlPres) {
        f = new JFrame("PIZ Compressor | Estadístiques");
        cp = ctrlPres;

        buttonHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
                Presentacio.showPresentacio();
            }
        });

        buttonShowFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileOut = cp.getFileOut();
                fileOut = fileOut.substring(0, fileOut.lastIndexOf('/')+1);

                String OS = System.getProperty("os.name").toLowerCase();

                if (OS.contains("win")) {
                    try {
                        Runtime.getRuntime().exec("start " + fileOut);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(f, "Error al intentar obrir el fitxer.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if (OS.contains("mac")) {
                    try {
                        Runtime.getRuntime().exec("open " + fileOut);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(f, "Error al intentar obrir el fitxer.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
                    try {
                        Runtime.getRuntime().exec("xdg-open " + fileOut);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(f, "Error al intentar obrir el fitxer.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(f, "SO no suportat.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    void showStatsCompress() {
        String time = cp.getTime();
        String deflate = cp.getDeflated();
        String speed = cp.getSpeedCompress();

        labelTime.setText(time);
        labelDeflate.setText(deflate);
        labelSpeed.setText(speed);

        f.setResizable(false);
        f.setContentPane(this.panelStatsComp);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(510,250));
        setWindowLocation();
        f.pack();
        f.setVisible(true);
        JOptionPane.showMessageDialog(f, "Compressió exitosa!", "PIZ Compressor", JOptionPane.PLAIN_MESSAGE);
    }

    private void setWindowLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int windowX = Math.max(0, (screenSize.width  - 510) / 2);
        int windowY = Math.max(0, (screenSize.height - 250) / 2 - 100);

        f.setLocation(windowX, windowY);
    }
}

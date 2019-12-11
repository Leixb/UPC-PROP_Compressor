package presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;

public class StatsDecompress {
    private CtrlPresentacio cp;

    private JPanel panelStatsDecomp;
    private JButton buttonHome;
    private JButton buttonExit;
    private JLabel labelTime;
    private JLabel labelInflate;
    private JLabel labelSpeed;
    private JButton buttonOpenFile;

    private static JFrame f;

    StatsDecompress(CtrlPresentacio ctrlPres) {
        f = new JFrame("PIZ Compressor | Estadístiques");
        cp = ctrlPres;

        buttonHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
                Presentacio.showPresentacio();
            }
        });

        buttonOpenFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileOut = cp.getFileOut();
                String currentOS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

                if (currentOS.contains("win")) {
                    try {
                        Runtime.getRuntime().exec(fileOut);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(f, "Error al intentar obrir el fitxer.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if (currentOS.contains("mac")) {
                    try {
                        Runtime.getRuntime().exec("open " + fileOut);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(f, "Error al intentar obrir el fitxer.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if (currentOS.contains("nix") || currentOS.contains("nux") || currentOS.contains("aix")) {
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

    void showStatsDecompress() {
        String time = cp.getTime();
        String inflate = cp.getInflated();
        String speed = cp.getSpeedDecompress();

        labelTime.setText(time);
        labelInflate.setText(inflate);
        labelSpeed.setText(speed);


        f.setResizable(false);
        f.setContentPane(this.panelStatsDecomp);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(490,250));
        setWindowLocation();
        f.pack();
        f.setVisible(true);
        JOptionPane.showMessageDialog(f, "Descompressió exitosa!", "PIZ Compressor", JOptionPane.PLAIN_MESSAGE);
    }

    private void setWindowLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int windowX = Math.max(0, (screenSize.width  - 510) / 2);
        int windowY = Math.max(0, (screenSize.height - 250) / 2 - 100);

        f.setLocation(windowX, windowY);
    }
}

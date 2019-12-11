package presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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

    private String time;
    private String inflate;
    private String speed;

    public StatsDecompress(CtrlPresentacio ctrlPres) {
        f = new JFrame("PIZ Compressor | Estadístiques");
        cp = ctrlPres;

        buttonHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
                Presentacio p = new Presentacio();
                p.showPresentacio();
            }
        });

        buttonOpenFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileOut = cp.getFileOut();
                String OS = System.getProperty("os.name").toLowerCase();

                if (OS.indexOf("win") >= 0) {
                    try {
                        Runtime.getRuntime().exec(fileOut);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(f, "Error al intentar obrir el fitxer.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if (OS.indexOf("mac") >= 0) {
                    try {
                        Runtime.getRuntime().exec("open " + fileOut);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(f, "Error al intentar obrir el fitxer.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0) {
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

    public void showStatsDecompress() {
        time = cp.getTime();
        inflate = cp.getInflated();
        speed = cp.getSpeedDecompress();

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

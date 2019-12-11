package presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Presentacio {
    private JLabel labelName;
    private JPanel panelMain;
    private JButton buttonDecompress;
    private JButton buttonCompress;
    private JPanel panelName;
    private JPanel panelOptions;
    private JButton buttonExit;
    private JPanel panelSortir;
    private JLabel labelAsk;

    private static JFrame f = new JFrame("PIZ Compressor | Welcome");

    public Presentacio() {
        ImageIcon icon = new ImageIcon("src/main/resources/logo.png");
        labelName.setIcon(icon);

        buttonCompress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
                Compress c = new Compress();
                c.showCompress();
            }
        });

        buttonDecompress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
                Decompress d = new Decompress();
                d.showDecompress();
            }
        });
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void showPresentacio() {
        f.setResizable(false);
        f.setContentPane(new Presentacio().panelMain);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(510,250));
        setWindowLocation();
        f.pack();
        f.setVisible(true);
    }

    private static void setWindowLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int windowX = Math.max(0, (screenSize.width  - 510) / 2);
        int windowY = Math.max(0, (screenSize.height - 250) / 2 - 100);

        f.setLocation(windowX, windowY);
    }

    public static void main(String[] args) {
        showPresentacio();
    }
}

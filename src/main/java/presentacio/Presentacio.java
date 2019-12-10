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
        f.setResizable(true);
        f.setContentPane(new Presentacio().panelMain);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(490, 200));
        f.setPreferredSize(new Dimension(490,250));
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }

    public static void main(String[] args) {
        showPresentacio();
    }
}

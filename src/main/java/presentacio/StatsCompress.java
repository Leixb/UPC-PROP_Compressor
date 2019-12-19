package presentacio;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;

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
                Presentacio.showPresentacio(f.getLocation(), f.getSize());
            }
        });

        buttonShowFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileOut = cp.getFileOut();

                String currentOS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

                if (currentOS.contains("win")) {
                    try {
                        Runtime.getRuntime().exec("start " + fileOut.substring(0, fileOut.lastIndexOf('/') + 1));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(f, "Error al intentar mostrar el fitxer.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (currentOS.contains("mac")) {
                    try {
                        Runtime.getRuntime().exec("open -R " + fileOut);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(f, "Error al intentar mostrar el fitxer.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (currentOS.contains("nix") || currentOS.contains("nux") || currentOS.contains("aix")) {
                    try {
                        Runtime.getRuntime().exec("xdg-open " + fileOut.substring(0, fileOut.lastIndexOf('/') + 1));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(f, "Error al intentar mostrar el fitxer.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
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

    void showStatsCompress(Point p, Dimension d) {
        String time = cp.getTime();
        String deflate = cp.getDeflated();
        String speed = cp.getSpeedCompress();

        labelTime.setText(time);
        labelDeflate.setText(deflate);
        labelSpeed.setText(speed);

        f.setResizable(true);
        f.setContentPane(this.panelStatsComp);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setWindowLocationAndDimension(p, d);
        f.pack();
        f.setVisible(true);
        JOptionPane.showMessageDialog(f, "Compressió exitosa!", "PIZ Compressor", JOptionPane.PLAIN_MESSAGE);
    }

    private void setWindowLocationAndDimension(Point p, Dimension d) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        f.setLocation(p);
        f.setPreferredSize(d);
        f.setMinimumSize(new Dimension(screenSize.width / 3, screenSize.height / 4));
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelStatsComp = new JPanel();
        panelStatsComp.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        panelStatsComp.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 4, new Insets(0, 105, 10, 105), -1, -1));
        panelStatsComp.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 16, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Estadístiques de Compressió");
        label1.setVerticalAlignment(1);
        panel1.add(label1, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 30), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Temps:");
        panel1.add(label2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(70, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Desinflat:");
        panel1.add(label3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(70, -1), null, 0, false));
        labelDeflate = new JLabel();
        labelDeflate.setText("");
        panel1.add(labelDeflate, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Velocitat:");
        panel1.add(label4, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(70, -1), null, 0, false));
        labelSpeed = new JLabel();
        labelSpeed.setText("");
        panel1.add(labelSpeed, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelTime = new JLabel();
        labelTime.setText("");
        panel1.add(labelTime, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel1.add(spacer5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel1.add(spacer6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel1.add(spacer7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panelStatsComp.add(spacer8, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 5, 5, 5), -1, -1));
        panelStatsComp.add(panel2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonExit = new JButton();
        buttonExit.setText("Sortir");
        panel2.add(buttonExit, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel2.add(spacer9, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonHome = new JButton();
        buttonHome.setText("Tornar a l'inici");
        panel2.add(buttonHome, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonShowFile = new JButton();
        buttonShowFile.setText("Mostrar fitxer comprimit");
        panelStatsComp.add(buttonShowFile, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelStatsComp;
    }

}

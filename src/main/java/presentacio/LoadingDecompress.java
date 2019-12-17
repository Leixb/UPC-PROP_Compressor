package presentacio;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoadingDecompress {
    private CtrlPresentacio cp = new CtrlPresentacio();

    private JLabel labelGIF;
    private JPanel panelLD;
    private JButton buttonCancelDescomp;

    private static JFrame f;

    private SwingWorker sw;

    LoadingDecompress() {
        f = new JFrame("PIZ Compressor");

        ImageIcon icon = new ImageIcon("src/main/resources/loading.gif");
        labelGIF.setIcon(icon);

        buttonCancelDescomp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sw.cancel(true);
            }
        });
    }

    void showLoadingDecompress(String fileIn, String fileOut) {
        f.setResizable(false);
        f.setContentPane(this.panelLD);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(530, 250));
        setWindowLocation();
        f.pack();
        f.setVisible(true);

        decompress(fileIn, fileOut);
    }

    private void setWindowLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int windowX = Math.max(0, (screenSize.width - 530) / 2);
        int windowY = Math.max(0, (screenSize.height - 250) / 2 - 100);

        f.setLocation(windowX, windowY);
    }

    private void decompress(String fileIn, String fileOut) {
        sw = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                cp.decompress(fileIn, fileOut);
                return null;
            }

            @Override
            protected void done() {
                try {
                    if (this.isCancelled()) {
                        File file = new File(cp.getFileOut());
                        file.delete();
                        f.setVisible(false);
                        Presentacio.showPresentacio();
                    } else {
                        get();
                        f.setVisible(false);
                        StatsDecompress sd = new StatsDecompress(cp);
                        sd.showStatsDecompress();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(f, "Error al descomprimir: " + e.getCause().getMessage(), "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                    f.setVisible(false);
                    Presentacio.showPresentacio();
                }
            }
        };

        sw.execute();
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
        panelLD = new JPanel();
        panelLD.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelLD.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelGIF = new JLabel();
        labelGIF.setText("");
        panel1.add(labelGIF, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 15, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Descomprimint fitxer...");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panelLD.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panelLD.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 5, 0), -1, -1));
        panelLD.add(panel3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonCancelDescomp = new JButton();
        buttonCancelDescomp.setText("Cancel·lar descompressió");
        panel3.add(buttonCancelDescomp, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelLD;
    }

}

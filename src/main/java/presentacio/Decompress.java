package presentacio;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

public class Decompress {
    private JPanel panelDecompress;
    private JButton buttonBack;
    private JButton buttonDecompress;
    private JButton buttonSelectFileIn;
    private JTextField textfieldSelectFileOut;
    private JLabel labelFileIn;
    private JButton buttonSelectFileOut;
    private JLabel labelFileOut;

    private static JFrame f;

    private String fileIn;
    private String fileOut;


    Decompress() {
        f = new JFrame("PIZ Compressor | Descomprimir");

        labelFileIn.setVisible(false);
        labelFileOut.setVisible(false);
        buttonSelectFileOut.setEnabled(false);
        buttonDecompress.setEnabled(false);

        fileIn = fileOut = "";

        buttonDecompress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("".equals(fileIn)) {
                    JOptionPane.showMessageDialog(f, "Selecciona un fitxer a descomprimir.", "PIZ Compressor", JOptionPane.WARNING_MESSAGE);
                } else if ("".equals(fileOut)) {
                    JOptionPane.showMessageDialog(f, "Inserta un nom pel fitxer de destí.", "PIZ Compressor", JOptionPane.WARNING_MESSAGE);
                } else {
                    f.setVisible(false);
                    LoadingDecompress ld = new LoadingDecompress();
                    ld.showLoadingDecompress(fileIn, fileOut, f.getLocation(), f.getSize());
                }
            }
        });

        buttonSelectFileIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc;
                if ("".equals(fileIn)) fc = new JFileChooser("./data");
                else fc = new JFileChooser(fileIn);
                int result = fc.showOpenDialog(f);
                if (result == JFileChooser.APPROVE_OPTION) {
                    fileIn = fc.getSelectedFile().getAbsolutePath();
                    labelFileIn.setText(fittingLabel(fileIn, labelFileIn));
                    labelFileIn.setToolTipText(fileIn);
                    labelFileIn.setForeground(new Color(0, 190, 0));
                    labelFileIn.setVisible(true);
                    buttonSelectFileOut.setEnabled(true);
                } else if (result == JFileChooser.ERROR_OPTION) {
                    labelFileIn.setText("Error pujant fitxer.");
                    labelFileIn.setForeground(new Color(220, 0, 0));
                    labelFileIn.setVisible(true);
                }
            }
        });

        buttonSelectFileOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("".equals(fileIn)) {
                    JOptionPane.showMessageDialog(f, "Selecciona un fitxer a descomprimir.", "PIZ Compressor", JOptionPane.WARNING_MESSAGE);
                } else {
                    JFileChooser fc;

                    if ("".equals(fileOut)) fc = new JFileChooser(fileIn);
                    else {
                        fc = new JFileChooser(fileOut);
                        fc.setSelectedFile(new File(fileOut));
                    }

                    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    int result = fc.showSaveDialog(f);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        fileOut = fc.getSelectedFile().getAbsolutePath();
                        labelFileOut.setText(fittingLabel(fileOut, labelFileOut));
                        labelFileOut.setToolTipText(fileOut);
                        labelFileOut.setForeground(new Color(0, 190, 0));
                        labelFileOut.setVisible(true);
                        buttonDecompress.setEnabled(true);
                    } else if (result == JFileChooser.ERROR_OPTION) {
                        labelFileOut.setText("Error al seleccionar fitxer destí.");
                        labelFileOut.setForeground(new Color(220, 0, 0));
                        labelFileOut.setVisible(true);
                    }
                }
            }
        });

        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
                Presentacio.showPresentacio(f.getLocation(), f.getSize());
            }
        });
        panelDecompress.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                labelFileIn.setText(fittingLabel(fileIn, labelFileIn));
                labelFileOut.setText(fittingLabel(fileOut, labelFileOut));
            }
        });
    }

    private String fittingLabel(String file, JLabel label) {
        int maxWidth = f.getWidth() - 425;
        FontMetrics fm = label.getFontMetrics(label.getFont());

        String fittingText = "";
        for (int i = file.length() - 1; i >= 0; --i) {
            if (fm.stringWidth(fittingText + "...") < maxWidth) {
                fittingText = file.charAt(i) + fittingText;
            }
        }
        if (file.equals(fittingText)) return file;
        else return "..." + fittingText;
    }

    void showDecompress(Point p, Dimension d) {
        f.setResizable(true);
        f.setContentPane(this.panelDecompress);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setWindowLocationAndDimension(p, d);
        f.pack();
        f.setVisible(true);
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
        panelDecompress = new JPanel();
        panelDecompress.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        panelDecompress.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 5, new Insets(0, 20, 0, 20), -1, -1));
        panelDecompress.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Fitxer a descomprimir:");
        panel1.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(185, -1), null, null, 0, false));
        buttonSelectFileIn = new JButton();
        buttonSelectFileIn.setText("Seleccionar");
        panel1.add(buttonSelectFileIn, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(160, -1), null, null, 0, false));
        labelFileIn = new JLabel();
        Font labelFileInFont = this.$$$getFont$$$(null, Font.BOLD, -1, labelFileIn.getFont());
        if (labelFileInFont != null) labelFileIn.setFont(labelFileInFont);
        labelFileIn.setText("");
        panel1.add(labelFileIn, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 5, new Insets(0, 20, 0, 20), -1, -1));
        panelDecompress.add(panel2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Nom fitxer/directori destí:");
        panel2.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(185, -1), null, null, 0, false));
        labelFileOut = new JLabel();
        Font labelFileOutFont = this.$$$getFont$$$(null, Font.BOLD, -1, labelFileOut.getFont());
        if (labelFileOutFont != null) labelFileOut.setFont(labelFileOutFont);
        labelFileOut.setText("");
        panel2.add(labelFileOut, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSelectFileOut = new JButton();
        buttonSelectFileOut.setText("Seleccionar destí");
        panel2.add(buttonSelectFileOut, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(160, -1), null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel2.add(spacer4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel2.add(spacer5, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panelDecompress.add(spacer6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 5, 5, 5), -1, -1));
        panelDecompress.add(panel3, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonBack = new JButton();
        buttonBack.setText("Enrere");
        panel3.add(buttonBack, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel3.add(spacer7, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonDecompress = new JButton();
        buttonDecompress.setText("Descomprimir");
        panel3.add(buttonDecompress, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 20, 0), -1, -1));
        panelDecompress.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("DESCOMPRIMIR");
        panel4.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return panelDecompress;
    }

}

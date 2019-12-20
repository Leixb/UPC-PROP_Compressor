package presentacio;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class Compress {
    private JPanel panelCompress;
    private JComboBox menuAlgs;
    private JSpinner qualityJPEG;
    private JLabel labelQJPEG;
    private JButton buttonCompress;
    private JButton buttonBack;
    private JButton buttonSelectFileIn;
    private JLabel labelFileIn;
    private JButton buttonSelectFileOut;
    private JLabel labelFileOut;
    private JButton buttonViewFile;
    private JPanel panelAlgSelection;

    private JFrame f;

    private int algSelected;
    private int jpegQuality;
    private String fileIn;
    private String fileOut;

    Compress() {
        f = new JFrame("PIZ Compressor | Comprimir");

        labelQJPEG.setVisible(false);
        qualityJPEG.setVisible(false);
        labelFileIn.setVisible(false);
        labelFileOut.setVisible(false);
        panelAlgSelection.setVisible(false);
        buttonCompress.setEnabled(false);
        buttonSelectFileOut.setEnabled(false);
        buttonViewFile.setEnabled(false);

        qualityJPEG.setModel(new SpinnerNumberModel(90, 0, 99, 1));

        algSelected = 0;
        jpegQuality = 80;
        fileIn = fileOut = "";

        menuAlgs.addItem("Auto");
        menuAlgs.addItem("LZ78");
        menuAlgs.addItem("LZSS");
        menuAlgs.addItem("LZW");
        menuAlgs.addItem("JPEG");

        buttonCompress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("".equals(fileIn)) {
                    JOptionPane.showMessageDialog(f, "Selecciona un fitxer a comprimir.", "PIZ Compressor", JOptionPane.WARNING_MESSAGE);
                } else if ("".equals(fileOut)) {
                    JOptionPane.showMessageDialog(f, "Inserta un nom pel fitxer de destí.", "PIZ Compressor", JOptionPane.WARNING_MESSAGE);
                } else {
                    f.setVisible(false);
                    LoadingCompress lc = new LoadingCompress();
                    lc.showLoadingCompress(algSelected, fileIn, fileOut, (short) jpegQuality, f.getLocation(), f.getSize());
                }
            }
        });

        menuAlgs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                labelQJPEG.setVisible(false);
                qualityJPEG.setVisible(false);
                String alg = Objects.requireNonNull(menuAlgs.getSelectedItem()).toString();
                switch (alg) {
                    case "Auto":
                        algSelected = 0;
                        break;
                    case "LZ78":
                        algSelected = 1;
                        break;
                    case "LZSS":
                        algSelected = 2;
                        break;
                    case "LZW":
                        algSelected = 3;
                        break;
                    case "JPEG":
                        labelQJPEG.setVisible(true);
                        qualityJPEG.setVisible(true);
                        algSelected = 4;
                        break;
                }
            }
        });

        qualityJPEG.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                jpegQuality = (Integer) qualityJPEG.getValue();
            }
        });

        buttonSelectFileIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc;
                if ("".equals(fileIn)) fc = new JFileChooser("./data");
                else {
                    File fileInObj = new File(fileIn);
                    if (fileInObj.isFile()) fc = new JFileChooser(fileIn);
                    else fc = new JFileChooser(fileInObj.getParent());
                }
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                int result = fc.showOpenDialog(f);
                if (result == JFileChooser.APPROVE_OPTION) {
                    fileIn = fc.getSelectedFile().getAbsolutePath();
                    labelFileIn.setText(fittingLabel(fileIn, labelFileIn));
                    labelFileIn.setToolTipText(fileIn);
                    labelFileIn.setForeground(new Color(0, 190, 0));
                    labelFileIn.setVisible(true);
                    buttonSelectFileOut.setEnabled(true);
                    buttonViewFile.setEnabled(true);
                    if (new File(fileIn).isFile()) panelAlgSelection.setVisible(true);
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
                JFileChooser fc;
                String fileOutAux;
                if ("".equals(fileOut)) {
                    File fileInObj = new File(fileIn);
                    fileOutAux = fileInObj.getName();
                    if (fileInObj.isFile()) {
                        fc = new JFileChooser(fileIn);
                        fileOutAux = fileOutAux.substring(0, fileOutAux.lastIndexOf('.'));
                    } else {
                        fc = new JFileChooser(fileInObj.getParent());
                    }
                    fileOutAux += ".piz";
                } else {
                    File fileOutObj = new File(fileOut);
                    fc = new JFileChooser(fileOut);
                    fileOutAux = fileOutObj.getName();
                }

                fc.setSelectedFile(new File(fileOutAux));
                int result = fc.showSaveDialog(f);
                if (result == JFileChooser.APPROVE_OPTION) {
                    fileOut = fc.getSelectedFile().getAbsolutePath();
                    if (!fileOut.endsWith(".piz")) fileOut += ".piz";
                    labelFileOut.setText(fittingLabel(fileOut, labelFileOut));
                    labelFileOut.setToolTipText(fileOut);
                    labelFileOut.setForeground(new Color(0, 190, 0));
                    labelFileOut.setVisible(true);
                    buttonCompress.setEnabled(true);
                } else if (result == JFileChooser.ERROR_OPTION) {
                    labelFileOut.setText("Error al seleccionar fitxer destí.");
                    labelFileOut.setForeground(new Color(220, 0, 0));
                    labelFileOut.setVisible(true);
                }
            }
        });

        panelCompress.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                labelFileIn.setText(fittingLabel(fileIn, labelFileIn));
                labelFileOut.setText(fittingLabel(fileOut, labelFileOut));
            }
        });

        buttonViewFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("".equals(fileIn)) {
                    JOptionPane.showMessageDialog(f, "Selecciona un fitxer a comprimir.", "PIZ Compressor", JOptionPane.WARNING_MESSAGE);
                } else {
                    String currentOS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

                    if (currentOS.contains("win")) {
                        try {
                            Runtime.getRuntime().exec(fileIn);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(f, "Error al intentar obrir el fitxer.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (currentOS.contains("mac")) {
                        try {
                            Runtime.getRuntime().exec("open " + fileIn);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(f, "Error al intentar obrir el fitxer.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (currentOS.contains("nix") || currentOS.contains("nux") || currentOS.contains("aix")) {
                        try {
                            Runtime.getRuntime().exec("xdg-open " + fileIn);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(f, "Error al intentar obrir el fitxer.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(f, "SO no suportat.", "PIZ Compressor", JOptionPane.ERROR_MESSAGE);
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
    }

    private String fittingLabel(String file, JLabel label) {
        int maxWidth = f.getWidth() - 435;
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

    void showCompress(Point p, Dimension d) {
        f.setResizable(true);
        f.setContentPane(this.panelCompress);
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
        panelCompress = new JPanel();
        panelCompress.setLayout(new GridLayoutManager(8, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelCompress.setForeground(new Color(-14869219));
        final Spacer spacer1 = new Spacer();
        panelCompress.add(spacer1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panelCompress.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 5, 5, 5), -1, -1));
        panelCompress.add(panel1, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonCompress = new JButton();
        buttonCompress.setText("Comprimir");
        panel1.add(buttonCompress, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonBack = new JButton();
        buttonBack.setText("Enrere");
        panel1.add(buttonBack, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 5, new Insets(0, 20, 0, 20), -1, -1));
        panelCompress.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, -1, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Fitxer/directori a comprimir:");
        panel2.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(195, -1), null, null, 0, false));
        buttonSelectFileIn = new JButton();
        buttonSelectFileIn.setText("Seleccionar");
        panel2.add(buttonSelectFileIn, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(160, -1), null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel2.add(spacer4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel2.add(spacer5, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        labelFileIn = new JLabel();
        Font labelFileInFont = this.$$$getFont$$$(null, Font.BOLD, -1, labelFileIn.getFont());
        if (labelFileInFont != null) labelFileIn.setFont(labelFileInFont);
        labelFileIn.setForeground(new Color(-16777216));
        labelFileIn.setText("");
        panel2.add(labelFileIn, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 5, new Insets(5, 20, 0, 20), -1, -1));
        panelCompress.add(panel3, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, -1, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Nom fitxer destí (*.piz):");
        panel3.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(195, -1), null, null, 0, false));
        labelFileOut = new JLabel();
        Font labelFileOutFont = this.$$$getFont$$$(null, Font.BOLD, -1, labelFileOut.getFont());
        if (labelFileOutFont != null) labelFileOut.setFont(labelFileOutFont);
        labelFileOut.setText("");
        panel3.add(labelFileOut, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonSelectFileOut = new JButton();
        buttonSelectFileOut.setText("Selecciona destí");
        panel3.add(buttonSelectFileOut, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(160, -1), null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel3.add(spacer6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel3.add(spacer7, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 20, 0), -1, -1));
        panelCompress.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("COMPRIMIR");
        panel4.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelAlgSelection = new JPanel();
        panelAlgSelection.setLayout(new GridLayoutManager(1, 4, new Insets(10, 20, 0, 20), -1, -1));
        panelCompress.add(panelAlgSelection, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelAlgSelection.add(panel5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Selecciona Algoritme:");
        panel5.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        menuAlgs = new JComboBox();
        panel5.add(menuAlgs, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelAlgSelection.add(panel6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelQJPEG = new JLabel();
        labelQJPEG.setText("Qualitat JPEG (0-99): ");
        panel6.add(labelQJPEG, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        qualityJPEG = new JSpinner();
        panel6.add(qualityJPEG, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(0, 0), new Dimension(60, 27), null, 0, false));
        final Spacer spacer8 = new Spacer();
        panelAlgSelection.add(spacer8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panelAlgSelection.add(spacer9, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonViewFile = new JButton();
        buttonViewFile.setText("Visualitzar fitxer a comprimir");
        panelCompress.add(buttonViewFile, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return panelCompress;
    }

}

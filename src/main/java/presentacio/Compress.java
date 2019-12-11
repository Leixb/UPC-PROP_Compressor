package presentacio;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class Compress {
    private JPanel panelCompress;
    private JComboBox menuAlgs;
    private JSpinner qualityJPEG;
    private JLabel labelQJPEG;
    private JButton buttonCompress;
    private JButton buttonExit;
    private JButton buttonSelectFileIn;
    private JTextField textfieldSelectFileOut;
    private JLabel labelFileIn;

    private JFrame f;

    private int algSelected;
    private short jpegQuality;
    private String fileIn;
    private String fileOut;

    public Compress() {
        f = new JFrame("PIZ Compressor | Comprimir");

        labelQJPEG.setVisible(false);
        qualityJPEG.setVisible(false);
        labelFileIn.setVisible(false);

        qualityJPEG.setModel(new SpinnerNumberModel(50,0,99,1));

        algSelected = 0;
        jpegQuality = 50;
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
                }
                else if ("".equals(fileOut)) {
                    JOptionPane.showMessageDialog(f, "Inserta un nom pel fitxer de destí.", "PIZ Compressor", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    f.setVisible(false);
                    LoadingCompress lc = new LoadingCompress();
                    lc.showLoadingCompress(algSelected,fileIn,fileOut,jpegQuality);
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
                jpegQuality = (Short) qualityJPEG.getValue();
            }
        });

        buttonSelectFileIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc;
                if("".equals(fileIn)) fc = new JFileChooser();
                else fc = new JFileChooser(fileIn);

                int result = fc.showOpenDialog(f);
                if(result == JFileChooser.APPROVE_OPTION) {
                    fileIn = fc.getSelectedFile().getAbsolutePath();
                    if(fileIn.length() < 19) labelFileIn.setText(fileIn);
                    else labelFileIn.setText(fileIn.substring(0,19) + "...");
                    labelFileIn.setForeground(new Color(0,190,0));
                    labelFileIn.setVisible(true);
                }
                else if (result == JFileChooser.ERROR_OPTION) {
                    labelFileIn.setText("Error pujant fitxer.");
                    labelFileIn.setForeground(new Color(220,0,0));
                    labelFileIn.setVisible(true);
                }
            }
        });

        textfieldSelectFileOut.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                fileOut = textfieldSelectFileOut.getText();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                fileOut = textfieldSelectFileOut.getText();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                fileOut = textfieldSelectFileOut.getText();
            }
        });

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    void showCompress() {
        f.setResizable(false);
        f.setContentPane(this.panelCompress);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(510,250));
        setWindowLocation();
        f.pack();
        f.setVisible(true);
    }

    private void setWindowLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int windowX = Math.max(0, (screenSize.width  - 510) / 2);
        int windowY = Math.max(0, (screenSize.height - 250) / 2 - 100);

        f.setLocation(windowX, windowY);
    }
}

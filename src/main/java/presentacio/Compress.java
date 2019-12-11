package presentacio;

import com.sun.codemodel.internal.JOp;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import sun.lwawt.macosx.CSystemTray;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class Compress {
    private JPanel panelCompress;
    private JComboBox menuAlgs;
    private JPanel panelOpcions;
    private JSpinner qualityJPEG;
    private JLabel labelQJPEG;
    private JButton buttonCompress;
    private JButton buttonExit;
    private JPanel panelToCompress;
    private JPanel panelCompressed;
    private JPanel panelBottom;
    private JPanel panelAlgLeft;
    private JPanel panelAlgRight;
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

        algSelected = -1;
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
                if(algSelected == -1) {
                    JOptionPane.showMessageDialog(f, "Selecciona un algoritme.", "PIZ Compressor", JOptionPane.WARNING_MESSAGE);
                }
                else if (fileIn == "") {
                    JOptionPane.showMessageDialog(f, "Selecciona un fitxer a comprimir.", "PIZ Compressor", JOptionPane.WARNING_MESSAGE);
                }
                else if (fileOut == "") {
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
                String alg = menuAlgs.getSelectedItem().toString();
                if (alg == "Auto") {
                    algSelected = 0;
                }
                else if (alg == "LZ78") {
                    algSelected  = 1;
                }
                else if (alg == "LZSS") {
                    algSelected = 2;
                }
                else if (alg == "LZW") {
                    algSelected = 3;
                }
                else if (alg == "JPEG") {
                    labelQJPEG.setVisible(true);
                    qualityJPEG.setVisible(true);
                    algSelected = 4;
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
                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(f);
                if(result == JFileChooser.APPROVE_OPTION) {
                    fileIn = fc.getSelectedFile().getAbsolutePath();
                    if(fileIn.length() < 18) labelFileIn.setText(fileIn);
                    else labelFileIn.setText(fileIn.substring(0,18) + "...");
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

    public void showCompress() {
        f.setResizable(false);
        f.setContentPane(this.panelCompress);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(490,250));
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

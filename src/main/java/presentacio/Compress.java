package presentacio;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

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
    private JLabel labelName;
    private JPanel panelOpcions;
    private JSpinner qualityJPEG;
    private JLabel labelQJPEG;
    private JButton buttonCompress;
    private JButton buttonExit;
    private JLabel labelToCompressFile;
    private JLabel labelCompressedFile;
    private JPanel panelToCompress;
    private JPanel panelCompressed;
    private JPanel panelBottom;
    private JLabel labelAlgSelect;
    private JPanel panelAlgLeft;
    private JPanel panelAlgRight;
    private JButton buttonSelectFileIn;
    private JTextField textfieldSelectFileOut;
    private JLabel labelFileIn;

    private static JFrame f;

    private int algSelected;
    private int jpegQuality;
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

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
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
                JFileChooser fc = new JFileChooser();
                if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    fileIn = fc.getSelectedFile().getAbsolutePath();
                    labelFileIn.setText(fileIn.substring(0,15) + "...");
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
    }

    public void showCompress() {
        f.setResizable(true);
        f.setContentPane(this.panelCompress);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(450, 200));
        f.setPreferredSize(new Dimension(475,250));
        f.pack();
        f.setVisible(true);
    }
}

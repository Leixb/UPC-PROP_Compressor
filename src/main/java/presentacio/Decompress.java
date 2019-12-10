package presentacio;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Decompress {
    private JPanel panelDecompress;
    private JPanel panelToDecompress;
    private JPanel panelDecompressed;
    private JButton buttonExit;
    private JButton buttonDecompress;
    private JButton buttonSelectFileIn;
    private JTextField textfieldSelectFileOut;
    private JLabel labelFileIn;
    private JPanel pannelBottom;

    private static JFrame f;

    private String fileIn;
    private String fileOut;


    public Decompress() {
        f = new JFrame("PIZ Compressor | Descomprimir");

        labelFileIn.setVisible(false);

        fileIn = fileOut = "";

        buttonDecompress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileIn == "") {
                    JOptionPane.showMessageDialog(f, "Selecciona un fitxer a descomprimir.", "PIZ Compressor", JOptionPane.WARNING_MESSAGE);
                }
                else if (fileOut == "") {
                    JOptionPane.showMessageDialog(f, "Inserta un nom pel fitxer de destí.", "PIZ Compressor", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        buttonSelectFileIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(f);
                if(result == JFileChooser.APPROVE_OPTION) {
                    fileIn = fc.getSelectedFile().getAbsolutePath();
                    if(fileIn.length() < 15) labelFileIn.setText(fileIn);
                    else labelFileIn.setText(fileIn.substring(0,15) + "...");
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

    public void showDecompress() {
        f.setResizable(true);
        f.setContentPane(this.panelDecompress);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(490, 200));
        f.setPreferredSize(new Dimension(490,250));
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }
}

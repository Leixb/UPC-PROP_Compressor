package presentacio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatsCompress {
    private CtrlPresentacio cp;

    private JPanel panelStatsComp;
    private JButton buttonExit;
    private JButton buttonHome;
    private JLabel labelTime;
    private JLabel labelInflate;
    private JLabel labelSpeed;

    private static JFrame f;

    private String time;
    private String inflate;
    private String speed;

    public StatsCompress(CtrlPresentacio ctrlPres) {
        f = new JFrame("PIZ Compressor | Estadístiques");

        cp = ctrlPres;

        buttonHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.setVisible(false);
                Presentacio p = new Presentacio();
                p.showPresentacio();
            }
        });
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public void showStatsCompress() {
        time = cp.getTime();
        inflate = cp.getInflated();
        speed = cp.getSpeedCompress();

        labelTime.setText(time);
        labelInflate.setText(inflate);
        labelSpeed.setText(speed);

        f.setResizable(false);
        f.setContentPane(this.panelStatsComp);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(490,250));
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }
}

package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.rom.myfreetv.process.FreePlayJob;
import org.rom.myfreetv.process.JobManager;
import org.rom.myfreetv.streams.FileIn;
import org.rom.myfreetv.streams.Playable;
import org.rom.myfreetv.streams.TimeShiftFileIn;

public class FreePlayPanel extends JPanel implements ActionListener {

    private MyFreeTV owner;
    private JButton stop;
    private JLabel label;
    private LogoViewer logo;

    public FreePlayPanel(MyFreeTV owner) {
        super();
        this.owner = owner;
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(null, "Diffusion en cours"));

        logo = new LogoViewer(64, 40);
        add(logo, BorderLayout.WEST);

        label = new JLabel();
        add(label);

        JPanel buttons = new JPanel();
        stop = new JButton(ImageManager.getInstance().getImageIcon("stop"));
//        stop.setBorder(MyFreeTV.buttonBorder);
        stop.setToolTipText("Arrêter la diffusion.");
        stop.setActionCommand("stop");
        stop.addActionListener(this);
        // buttons.add(pause);
        buttons.add(stop);
        add(buttons, BorderLayout.EAST);
    }

    public void initButtons() {
        FreePlayJob pj = JobManager.getInstance().getFreePlay();
        if(pj != null) {
            Playable playable = pj.getPlayable();
            label.setIcon(ImageManager.getInstance().getImageIcon("play"));
            StringBuffer buf = new StringBuffer("<html><b>");
            if(playable instanceof FileIn) {
                if(playable instanceof TimeShiftFileIn) {
                    /* ce filein est alors utilisé pour le timeshift */
                    buf.append(playable.getChannel().getName());
                    buf.append("</b> <i>(");
                    buf.append(playable.getName());
                    buf.append(")</i>");
                } else {
                    /* ce filein est alors un fichier enregistré auparavant */
                    buf.append(playable.getName());
                    buf.append("</b>");
                }
            } else {
                buf.append(playable.getName());
                buf.append("</b>");
            }
            buf.append("</html>");
            label.setText(new String(buf));

            logo.setLogo(playable);

            // label.setText("<html><b>" + playable.getName() + "</b></html>");
            // pause.setEnabled(pj.canPause() && playable instanceof FileIn);
            // pause.setEnabled(pj.canPause());
            stop.setEnabled(true);
        } else {
            logo.setLogo(null);
            label.setIcon(ImageManager.getInstance().getImageIcon("stop"));
            label.setText("<html><i>Pas de diffusion en cours...</i></html>");
            // pause.setEnabled(false);
            stop.setEnabled(false);
            logo.setLogo(null);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if(s.equals("stop")) {
            owner.getActions().stopFreePlay();
        }
    }

    public void update() {
        initButtons();
        // if(JobsManager.getInstance().getPlay() != null) {
        // Playable playable =
        // JobsManager.getInstance().getPlay().getPlayable();
        // label.setIcon(ImageManager.getInstance().getImageIcon("play"));
        // label.setText("<html><b>" + channel.getName() + "</b></html>");
        // }
        // else {
        // label.setIcon(ImageManager.getInstance().getImageIcon("stop"));
        // label.setText("<html><i>Pas de lecture en cours...</i></html>");
        // }
    }

}

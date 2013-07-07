package org.rom.myfreetv.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.rom.myfreetv.guidetv.Emission;
import org.rom.myfreetv.guidetv.GuideTVManager;
import org.rom.myfreetv.process.JobManager;
import org.rom.myfreetv.process.PlayJob;
import org.rom.myfreetv.process.RecordJob;
import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.ChannelManager;
import org.rom.myfreetv.streams.FileIn;
import org.rom.myfreetv.streams.Playable;
import org.rom.myfreetv.streams.Recordable;
import org.rom.myfreetv.streams.TimeShiftFileIn;
import org.rom.myfreetv.view.EmissionDialog;

class PlayPanel extends JPanel implements ActionListener {

    private final static DateFormat formatter = new SimpleDateFormat("HH:mm");
    private MyFreeTV Lowner;
    private JButton stop, rec, prog;
    private JLabel label;
    private LogoViewer logo;

    public PlayPanel(MyFreeTV owner) {
        super();
        this.Lowner = owner;
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(null, "Lecture en cours"));

        logo = new LogoViewer(64, 40);
        add(logo, BorderLayout.WEST);

        label = new JLabel();
        add(label);

        label.addMouseListener(new MouseAdapter() {
        	@Override
             public void mouseClicked(MouseEvent e) {
                PlayJob pj = JobManager.getInstance().getPlay();
        		if(pj != null) {
	                Channel chan=Lowner.getSelectedChannel();
	
	                Emission curEmission = GuideTVManager.getInstance().getCurrent(chan);
	        		new EmissionDialog(Lowner,curEmission);
        		}
        	};
          });

        JPanel buttons = new JPanel();
        // pause = new
        // JButton(ImageManager.getInstance().getImageIcon("pause"));
        stop = new JButton(ImageManager.getInstance().getImageIcon("stop"));
        rec = new JButton(ImageManager.getInstance().getImageIcon("record"));
        prog = new JButton(ImageManager.getInstance().getImageIcon("prog"));
        // pause.setBorder(MyFreeTV.buttonBorder);
//        stop.setBorder(MyFreeTV.buttonBorder);
//        rec.setBorder(MyFreeTV.buttonBorder);
//        prog.setBorder(MyFreeTV.buttonBorder);
        // pause.setToolTipText("Mettre en pause / Reprendre la lecture.");
        stop.setToolTipText("Arrêter la lecture.");
        rec.setToolTipText("Enregistrer la chaîne en cours de lecture dans un fichier.");
        prog.setToolTipText("Programmer un enregistrement.");
        // pause.setActionCommand("pauseFromPlay");
        stop.setActionCommand("stop");
        rec.setActionCommand("rec");
        prog.setActionCommand("prog");
        // pause.addActionListener(owner);
        stop.addActionListener(this);
        rec.addActionListener(this);
        prog.addActionListener(this);
        // buttons.add(pause);
        buttons.add(stop);
        buttons.add(rec);
        buttons.add(prog);
        add(buttons, BorderLayout.EAST);
    }

    public void initButtons() {
        PlayJob pj = JobManager.getInstance().getPlay();
        if(pj != null) {
            Playable playable = pj.getPlayable();
            label.setIcon(ImageManager.getInstance().getImageIcon("play"));
            StringBuffer buf = new StringBuffer("<html><b>");
            if(playable instanceof FileIn) {
                if(playable instanceof TimeShiftFileIn) {
                    /* ce filein est alors utilis� pour le timeshift */
                    buf.append(playable.getChannel().getName());
                    buf.append("</b><br><i>(");
                    buf.append(playable.getName());
                    buf.append(")</i>");
                } else {
                    /* ce filein est alors un fichier enregistr� auparavant */
                    buf.append(playable.getName());
                    buf.append("</b>");
                }
            } else {
            	Emission emission = GuideTVManager.getInstance().getCurrent(playable.getChannel());
            	buf.append(playable.getName());
                buf.append("</b>");
                
               	if (emission!=null)
            	{
	        	    buf.append("<br>");
	        	    buf.append(formatter.format(emission.getStart().getTime()));
	        	    buf.append(" - ");
	        	    buf.append(formatter.format(emission.getEnd().getTime()));
	        	    buf.append(" : ");
	        	    buf.append(emission.getTitle());
	        	    if(emission.getSubtitle() != null)
	        	    {
	        		        buf.append(" (" + emission.getSubtitle() + ")");
	        	    }
            	}
            }
            buf.append("</html>");
            label.setText(new String(buf));

            logo.setLogo(playable);

            // label.setText("<html><b>" + playable.getName() + "</b></html>");
            // pause.setEnabled(pj.canPause() && playable instanceof FileIn);
            // pause.setEnabled(pj.canPause());
            stop.setEnabled(true);
            rec.setEnabled(playable instanceof Recordable && !JobManager.getInstance().isRecording((Recordable) playable));
            prog.setEnabled(playable instanceof TimeShiftFileIn || playable instanceof Channel);
        } else {
            logo.setLogo(null);
            label.setIcon(ImageManager.getInstance().getImageIcon("stop"));
            label.setText("<html><i>Pas de lecture en cours...</i></html>");
            // pause.setEnabled(false);
            stop.setEnabled(false);
            rec.setEnabled(false);
            prog.setEnabled(false);
            logo.setLogo(null);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if(s.equals("stop")) {
            Lowner.getActions().stopPlay();
        } else if(s.equals("rec")) {
            PlayJob job = JobManager.getInstance().getPlay();
            if(job != null) {
                Playable playable = job.getPlayable();
                if(playable != null && playable instanceof Recordable)
                    Lowner.getActions().record((Recordable) playable);
            }
        } else if(s.equals("prog")) {
            PlayJob job = JobManager.getInstance().getPlay();
            if(job != null) {
                Playable playable = job.getPlayable();
                System.out.println(playable != null);
                if(playable != null) {
                    System.out.println(123);
                    RecordJob rj = JobManager.getInstance().getRecordJob(playable.getChannel());
                    if(rj == null)
                        Lowner.getActions().prog(playable.getChannel());
                    else
                        Lowner.getActions().prog(rj);
                }
            }
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

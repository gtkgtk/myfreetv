package org.rom.myfreetv.view;

import javax.swing.JOptionPane;

import org.rom.myfreetv.config.Config;
import org.rom.myfreetv.files.FileUtils;
import org.rom.myfreetv.guidetv.Emission;
import org.rom.myfreetv.process.JobManager;
import org.rom.myfreetv.process.PlayJob;
import org.rom.myfreetv.process.RecordJob;
import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.Playable;
import org.rom.myfreetv.streams.RadioChannel;
import org.rom.myfreetv.streams.Recordable;

public class Actions {

    private MyFreeTV owner;

    Actions(MyFreeTV owner) {
        this.owner = owner;
    }

    public void play(Playable playable, float position) {
        try {
            int i = 0;
            do {
                /*
                 * on essaye de lancer la lecture, si ça n'a pas marché, on
                 * recommence 4 essais
                 */
                JobManager.getInstance().startPlay(playable, position);
                if(JobManager.getInstance().getPlay() == null) {
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException e) {}
                }
                i++;
            } while(JobManager.getInstance().getPlay() == null && i < 4);
//            if(JobManager.getInstance().getPlay().getPlayer() instanceof ActiveXVLC) {
//                JTabbedPane tab = owner.getTabbedPane();
//                tab.setSelectedIndex(tab.indexOfComponent(owner.getVLCPanel()));
//            }
        } catch(Exception e) {
            e.printStackTrace();
            echec();
        }
    }

    public void play(Playable playable) {
        play(playable, 0.0f);
    }

    public void freePlay(Playable playable) {
        try {
            JobManager.getInstance().startFreePlay(playable);
        } catch(Exception e) {
            e.printStackTrace();
            echec();
        }

    }

    public void record(Recordable recordable) {
        String filename = FileUtils.getDestination(owner, recordable);
        if(filename != null) {
        	if (recordable instanceof RadioChannel)
        	{
        		if (!FileUtils.isOgg(filename))
        			filename = filename+".ogg";
        	}
        	else if (recordable instanceof Channel)
        	{
        		if (!FileUtils.isMpeg(filename))
        			filename = filename+".mpg";
        	}
            try {
                JobManager.getInstance().startRec(recordable, filename);
            } catch(Exception e) {
                e.printStackTrace();
                echec();
            }
        }
    }
    
    public void pause() {
        PlayJob pj = JobManager.getInstance().getPlay();
        Playable playable = pj.getPlayable();
        boolean pause = pj.canPause();
        if(pause && !playable.canPause()) {
            pause = playable instanceof Recordable && JOptionPane.showConfirmDialog(owner, "Pour pouvoir mettre en pause, il faut activer le TimeShift.\nPour cela, vous devez enregistrer ce que vous regardez.\nVoulez-vous enregistrer?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
            if(pause) {
                record((Recordable) playable);
            }
        }
        if(pause) {
            try {
                JobManager.getInstance().pausePlay();
            } catch(Exception e) {
                e.printStackTrace();
                echec();
            }
        }
    }

    public void stopPlay() {
        try {
            JobManager.getInstance().stopPlay();
        } catch(Exception e) {
            e.printStackTrace();
            echec();
        }
    }

    public void stopFreePlay() {
        try {
            JobManager.getInstance().stopFreePlay();
        } catch(Exception e) {
            e.printStackTrace();
            echec();
        }
    }

    public void stopRecord(RecordJob job) {
        try {
            JobManager.getInstance().stopRec(job);
        } catch(Exception e) {
            e.printStackTrace();
            echec();
        }
    }

    public void prog(RecordJob job) {
        new ProgAddDialog(owner, job);
    }

    public void prog(Channel channel) {
        new ProgAddDialog(owner, channel);
    }

    public void prog(Emission emission) {
        new ProgAddDialog(owner, emission);
    }

    private void echec() {
        String s = "L'action n'a pas pu être effectuée.\nIl y a eu un problème de communication avec VLC.";
        if(Config.getInstance().isEmbedded())
            s += "\nConseil: Si vous avez toujours eu ce message d'erreur,\nréinstallez VLC, en activant à l'installation l'option \"ActiveX\".";
        JOptionPane.showMessageDialog(owner, s, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

}

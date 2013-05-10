package org.rom.myfreetv.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.rom.myfreetv.config.Config;
import org.rom.myfreetv.files.RecordFile;
import org.rom.myfreetv.files.RecordFileManager;
import org.rom.myfreetv.player.ExternVLC;
import org.rom.myfreetv.player.Player;
import org.rom.myfreetv.player.Recorder;
import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.Playable;
import org.rom.myfreetv.streams.Recordable;
import org.rom.myfreetv.streams.TimeShiftFileIn;
import org.rom.myfreetv.view.PlayerWaiter;

public class JobManager extends Observable implements Observer {

    private static JobManager instance;
    private final static int wait = 10000;

    private PlayJob play;
    private FreePlayJob freeplay;
    private List<RecordJob> records;

    private JobManager() {
        records = new ArrayList<RecordJob>();
    }

    public static JobManager getInstance() {
        if(instance == null)
            instance = new JobManager();
        return instance;
    }

    public Player createPlayer() {
        return new ExternVLC();
    }

    public Player createFreePlayer() {
        return new ExternVLC();
    }

    public Recorder createRecorder() {
        return new ExternVLC();
    }

    public void update(Observable o, Object arg) {
        if(o instanceof Job) {
            Job job = (Job) o;
            if(arg instanceof String) {
                if("stopped".equals(arg)) {
                    if(job == play) {
                        try {
                            stopPlay();
                        } catch(Exception e) {}
                    } else if(job == freeplay) {
                        try {
                            stopFreePlay();
                        } catch(Exception e) {}
                    } else if(job instanceof RecordJob) {
                        try {
                            stopRec((RecordJob) job);
                        } catch(Exception e) {}
                    }
                }
            }
        }
    }

    public void startPlay(Playable playable, boolean waitForTimeShift, float position) throws Exception {
        // try {
        // stopPlay();
        RecordJob job = null;
        if(playable instanceof Recordable)
            job = getRecordJob((Recordable) playable);
        Player player = createPlayer();
        if(player.haveToStopBeforePlay()) {
            stopPlay();
            // if(player instanceof ActiveXVLC) {
            // try { Thread.sleep(1000); } catch(InterruptedException e) {}
            // }
        }
        if(job != null) {
            TimeShiftFileIn input = new TimeShiftFileIn(job.getUrlOutput());
            if(job.getRecordable() instanceof Channel)
                input.setChannel((Channel) job.getRecordable());
            if(waitForTimeShift) {
                int time = (int) (System.currentTimeMillis() - job.getStartDate().getTimeInMillis());
                if(time < 0)
                    time = 0;
                if(wait > time)
                    new PlayerWaiter(wait - time);
            }
            playable = input;
            // play = new PlayJob(input,player);
        }
        // else {
        // play = new PlayJob(playable,player);
        // }
        play = new PlayJob(playable, player);
        player.setJob(play);
        play.addObserver(this);
        try {
            play.start(position);
        } catch(Exception e) {
            e.printStackTrace();
            play = null;
            throw e;
        } finally {
            setChanged();
            notifyObservers();
        }
        // } catch(Exception e) { e.printStackTrace(); throw e; }
    }

    public void startPlay(Playable playable, float position) throws Exception {
        startPlay(playable, true, position);
    }

    public void startPlay(Playable playable) throws Exception {
        startPlay(playable, 0.0f);
    }

    public void startFreePlay(Playable playable) throws Exception {
        Player player = createFreePlayer();
        if(player.haveToStopBeforePlay())
            stopFreePlay();
        freeplay = new FreePlayJob(playable, player);
        player.setJob(freeplay);
        freeplay.addObserver(this);
        try {
            freeplay.start();
        } catch(Exception e) {
            e.printStackTrace();
            freeplay = null;
            throw e;
        } finally {
            setChanged();
            notifyObservers();
        }

    }

    public void stopPlay(boolean andKill) throws Exception {
        // try {
        if(play != null) {
            try {
                play.stop(andKill);
                // } catch(Exception e) { e.printStackTrace(); throw e; }
            } finally {
                play.deleteObserver(this);
                play = null;
                setChanged();
                notifyObservers();
            }
        }
        // } catch(Exception e) { e.printStackTrace(); throw e; }
    }

    public void stopFreePlay() throws Exception {
        if(freeplay != null) {
            try {
                freeplay.stop();
            } finally {
                freeplay.deleteObserver(this);
                freeplay = null;
                setChanged();
                notifyObservers();
            }
        }
    }

    public void stopPlay() throws Exception {
        stopPlay(true);
    }

    public void pausePlay() throws Exception {
        if(play != null && play.canPause()) {
            play.pause();
        }
    }

    public RecordJob startRec(final Recordable recordable, String output) throws Exception {
        RecordJob rj = null;
        if(!isRecording(recordable)) {
            boolean alreadyPlaying = (recordable instanceof Playable) && isPlaying((Playable) recordable);
            if(alreadyPlaying)
                stopPlay();
            // System.out.println("lecture stopp�e pour timeshift");
            Recorder recorder = createRecorder();
            rj = new RecordJob(recordable, output, recorder);
            recorder.setJob(rj);
            rj.addObserver(this);
            try {
                rj.start();
                records.add(rj);
                RecordFile rf = new RecordFile(rj.getUrlOutput(), rj.getStartDate(), rj.getRecordable().getChannel());
                rf.setJob(rj);
                RecordFileManager.getInstance().add(rf);
            } finally {
                if(alreadyPlaying) {
                    // new Thread() {
                    // public void run() {
                    // new PlayerWaiter(wait);
                    // // try {
                    // // Thread.sleep(4500); /* attend pr laisser un peu le
                    // temps d'enregistrer qqs secondes */
                    // // } catch(InterruptedException e) {}
                    // try {
                    // startPlay((Playable)recordable,false);
                    // // setChanged();
                    // // notifyObservers();
                    // } catch(Exception e) { }
                    // }
                    // }.start();
                    startPlay((Playable) recordable);
                    // System.out.println("red�marrage de la lecture en
                    // timeshift");
                }
                setChanged();
                notifyObservers();
            }
        }
        return rj;
    }

    public void stopRec(RecordJob job) throws Exception {
        boolean isPlaying = (job.getStream() instanceof Playable) && isPlaying((Playable) job.getStream());
        if(isPlaying)
            stopPlay();
        job.stop();
        job.deleteObserver(this);
        records.remove(job);
        try {
            if(isPlaying)
                startPlay((Playable) job.getStream());
            // } catch(Exception e) { play = null; throw e; }
        } finally {
            setChanged();
            notifyObservers();
        }
    }

    public PlayJob getPlay() {
        return play;
    }

    public FreePlayJob getFreePlay() {
        return freeplay;
    }

    public List<RecordJob> getRecords() {
        return records;
    }

    public boolean isPlaying(Playable playable) {
        return play != null && play.getPlayable().equals(playable);
    }

    public RecordJob getRecordJob(Recordable recordable) {
        RecordJob job = null;
        int i = 0;
        while(i < records.size() && job == null) {
            if(records.get(i).getRecordable().equals(recordable))
                job = records.get(i);
            i++;
        }
        return job;
    }

    public boolean isRecording(Recordable recordable) {
        return getRecordJob(recordable) != null;
    }

    public boolean isUsed(File file) {
        boolean found = false;
        int i = 0;
        while(i < records.size() && !found) {
            RecordJob j = records.get(i);
            if(j.getUrlOutput().equals(file.getAbsolutePath()))
                found = true;
            i++;
        }
        return found;
    }

}

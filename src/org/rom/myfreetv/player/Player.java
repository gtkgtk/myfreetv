package org.rom.myfreetv.player;

import org.rom.myfreetv.process.Job;
import org.rom.myfreetv.streams.Playable;

public interface Player {

    public void play(Playable playable, float position) throws Exception;
    public void play(Playable playable) throws Exception;

    public void playToTV(Playable playable, float position) throws Exception;
    public void playToTV(Playable playable) throws Exception;

    public void stop(boolean andKill) throws Exception;
    public void stop() throws Exception;

    public boolean canPause();

    public void pause() throws Exception;

    public boolean haveToStopBeforePlay();

    public void setJob(Job job);

    public boolean isRunning();

}

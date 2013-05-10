package org.rom.myfreetv.player;

import org.rom.myfreetv.process.Job;
import org.rom.myfreetv.streams.Recordable;

public interface Recorder {

    public void record(Recordable recordable, String output) throws Exception;

    public void stop() throws Exception;

    public void setJob(Job job);

    public boolean isRunning();

}
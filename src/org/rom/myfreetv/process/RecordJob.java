package org.rom.myfreetv.process;

import org.rom.myfreetv.player.Recorder;
import org.rom.myfreetv.streams.Recordable;

public class RecordJob extends Job {

    // private Playable playable;
    protected String output;
    protected Recorder recorder;

    public RecordJob(Recordable recordable, String output, Recorder recorder) {
        super(recordable);
        // this.playable = playable;
        this.output = output;
        this.recorder = recorder;
    }

    public void start() throws Exception {
        super.start();
        recorder.record((Recordable) stream, output);
    }

    public void stop() throws Exception {
        super.stop();
        recorder.stop();
    }

    public Recordable getRecordable() {
        return (Recordable) stream;
    }

    public String getUrlInput() {
        return stream.getUrl();
    }

    public String getUrlOutput() {
        return output;
    }

    public boolean isRunning() {
        return recorder.isRunning();
    }

    public String toString() {
        return stream + " " + output;
    }

}

package org.rom.myfreetv.process;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import org.rom.myfreetv.streams.Stream;

public abstract class Job extends Observable implements Comparable<Job>, Observer {

    protected Calendar startDate;
    protected Stream stream;

    public Job(Stream stream) {
        this.stream = stream;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void start(float position) throws Exception {
        startDate = Calendar.getInstance();
    }

    public void start() throws Exception {
        start(0.0f);
    }

    public void stop() throws Exception {
        startDate = null;
    }

    public Stream getStream() {
        return stream;
    }

    public abstract boolean isRunning();

    public int compareTo(Job other) {
        return startDate.compareTo(other.startDate);
    }

    public void update(Observable o, Object arg) {
        setChanged();
        // String s = this instanceof RecordJob ? "record_" : "play_";
        notifyObservers(arg);
    }

}

package org.rom.myfreetv.files;

import java.io.File;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import org.rom.myfreetv.process.RecordJob;
import org.rom.myfreetv.streams.Channel;

public class RecordFile extends Observable implements Comparable<RecordFile>, Observer {

    private File file;
    private Calendar startDate;
    private Channel channel;
    private RecordJob job;

    private int mo;

    public RecordFile(String filename, Calendar startDate, Channel channel) {
        file = new File(filename);
        this.startDate = startDate;
        this.channel = channel;
        initMo();
    }

    public RecordJob getJob() {
        return job;
    }

    public void setJob(RecordJob job) {
        this.job = job;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public Channel getChannel() {
        return channel;
    }

    public int compareTo(RecordFile other) {
        if(file.equals(other.file))
            return 0;
        else
            return startDate.compareTo(other.startDate);
    }

    public void initMo() {
        mo = file.exists() ? ((int) (file.length() / 1048576)) : -1;
    }

    public int getMo() {
        return mo;
    }

    // public boolean rename(String dest) {
    // boolean ok;
    // try {
    // ok = file.renameTo(new File(dest));
    // } catch(Exception e) {
    // ok = false;
    // }
    // if(ok) {
    // setChanged();
    // notifyObservers();
    // }
    // return ok;
    // }

    public void update(Observable o, Object args) {
        // if(o instanceof RecordJob) {
        // RecordJob rj = (RecordJob)o;
        job = null;
        // }
    }

    public String toString() {
        return channel.toString() + " " + file.getName();
    }

}

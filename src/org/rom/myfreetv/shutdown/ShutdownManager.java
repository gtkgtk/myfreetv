package org.rom.myfreetv.shutdown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;

import org.rom.myfreetv.config.Config;

public class ShutdownManager extends Observable {

    private static ShutdownManager instance;

    private Calendar date;
    private boolean stop;

    private ShutdownManager() {
        new Thread() {

            public void run() {
                while(true) {
                    if(!stop) {
                        if(date != null && Calendar.getInstance().compareTo(date) >= 0) {
                            stop = true;
                            setChanged();
                            notifyObservers("shutdown");
                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch(InterruptedException e) {}
                }
            }
        }.start();
        init();
    }

    public static ShutdownManager getInstance() {
        if(instance == null)
            instance = new ShutdownManager();
        return instance;
    }

    public void init() {
        stop = false;
        date = null;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void shutdown() {
        List<String> list = new ArrayList<String>();
        list.add("shutdown");
        if(Config.getInstance().isWindowsOS()) {
            list.add("-s");
            list.add("-t");
            list.add("0");
        } else {
            list.add("-h");
            list.add("-q");
            list.add("now");
        }
        String[] args = new String[list.size()];
        list.toArray(args);
        try {
            Runtime.getRuntime().exec(args);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}

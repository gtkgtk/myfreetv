package org.rom.myfreetv.files;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.rom.myfreetv.config.Config;
import org.rom.myfreetv.process.JobManager;
import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.ChannelManager;

public class RecordFileManager extends Observable implements Observer {

    //private final static String recordFilesSaves = "records.dat";
    private final static String recordFilesSaves = Config.RECORD_FILENAME;
    private static RecordFileManager instance;

    private List<RecordFile> files;

    private RecordFileManager() {
        files = new ArrayList<RecordFile>();
        JobManager.getInstance().addObserver(this);
    }

    public static RecordFileManager getInstance() {
        if(instance == null)
            instance = new RecordFileManager();
        return instance;
    }

    public void save() {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(new File(recordFilesSaves)));
            for(RecordFile r : files) {
                dos.writeUTF(r.getFile().getAbsolutePath());
                dos.writeLong(r.getStartDate().getTimeInMillis());
                dos.writeInt(r.getChannel().getNum());
            }
        } catch(IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if(dos != null) {
                try {
                    dos.close();
                } catch(IOException e) {}
            }
        }
    }

    public void load() {
        files.clear();
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(new File(recordFilesSaves)));
            while(true) {
                String filename = dis.readUTF();
                long date = dis.readLong();
                int chan = dis.readInt();

                Calendar cal = new GregorianCalendar();
                cal.setTimeInMillis(date);
                Channel channel = ChannelManager.getInstance().getChannel(chan);
                if(channel == null)
                    channel = new Channel(chan, null, null);
                RecordFile r = new RecordFile(filename, cal, channel);
                r.addObserver(this);
                files.add(r);
            }
        } catch(IOException e) {} finally {
            try {
                if(dis != null) {
                    dis.close();
                }
            } catch(IOException e) {}
            Collections.sort(files);
            setChanged();
            notifyObservers();
        }
    }

    public void add(RecordFile recordFile) {
        File file = recordFile.getFile();
        int i = 0;
        while(i < files.size()) {
            if(files.get(i).getFile().equals(file)) {
                files.remove(i);
            } else {
                i++;
            }
        }
        recordFile.addObserver(this);
        files.add(recordFile);
        Collections.sort(files);
        save();
        setChanged();
        notifyObservers();
    }

    public void remove(RecordFile recordFile) {
        recordFile.deleteObserver(this);
        files.remove(recordFile);
        save();
        setChanged();
        notifyObservers();
    }

    public boolean rename(RecordFile rf, File dest) {
        if(rf.getFile().renameTo(dest)) {
            rf.setFile(dest);
            save();
            setChanged();
            notifyObservers();
            return true;
        } else
            return false;
    }

    public void clean() {
        int i = 0;
        while(i < files.size()) {
            RecordFile rf = files.get(i);
            boolean fileExists = rf.getFile().exists();
            boolean hasNoJob = rf.getJob() == null;
            if(!fileExists && hasNoJob)
                files.remove(i);
            else
                i++;
        }
        save();
        setChanged();
        notifyObservers();
    }

    public List<RecordFile> getRecordFiles() {
        return files;
    }

    public void update(Observable o, Object arg) {
        for(RecordFile rf : files) {
            if(rf.getJob() != null && !JobManager.getInstance().getRecords().contains(rf.getJob())) {
                rf.setJob(null);
                setChanged();
            }
        }
        if(hasChanged())
            save();
        notifyObservers();
    }

}

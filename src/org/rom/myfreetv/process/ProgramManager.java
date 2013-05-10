package org.rom.myfreetv.process;

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
import org.rom.myfreetv.files.FileUtils;
import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.ChannelManager;
import org.rom.myfreetv.streams.Recordable;

public class ProgramManager extends Observable implements Observer, Runnable {

    private final static String progSaves = Config.PROGRAMMATION_FILENAME;
    private static ProgramManager instance;

    private List<Program> programs;
    private Thread runner;

    private ProgramManager() {
        programs = new ArrayList<Program>();
        JobManager.getInstance().addObserver(this);
        // new Thread(this).start();
    }

    public static ProgramManager getInstance() {
        if(instance == null)
            instance = new ProgramManager();
        return instance;
    }

    public void runThread() {
        if(runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    public void save() {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(new File(progSaves)));
            for(Program p : programs) {
                if(p.getRecordable() instanceof Channel) {
                    if(p.getProgramRules() instanceof OnceProgramRules) {
                        dos.writeChar('o');
                        dos.writeInt(((Channel) p.getRecordable()).getNum());
                        dos.writeLong(p.getStart().getTimeInMillis());
                        dos.writeLong(p.getStop().getTimeInMillis());
                        dos.writeUTF(p.getFilename());
                    } else if(p.getProgramRules() instanceof HebdoProgramRules) {
                        HebdoProgramRules hp = (HebdoProgramRules) p.getProgramRules();
                        dos.writeChar('h');
                        dos.writeInt(((Channel) p.getRecordable()).getNum());
                        dos.writeByte(hp.getDays());
                        dos.writeShort(hp.getStartMin());
                        dos.writeShort(hp.getStopMin());
                        dos.writeUTF(hp.getPath());
                    }
                }
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
        programs.clear();
        // new Exception("abc").printStackTrace();
        // System.out.println("--------------------------------------");
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(new File(progSaves)));
            while(true) {
                char c = dis.readChar();
                int chan = dis.readInt();
                Channel channel = ChannelManager.getInstance().getChannel(chan);
                if(channel == null)
                    channel = new Channel(chan, null, null);
                switch(c) {
                    case 'o':
                        long start = dis.readLong();
                        long stop = dis.readLong();
                        String filename = dis.readUTF();
                        // if(channel != null) {
                        Calendar startCal = new GregorianCalendar();
                        startCal.setTimeInMillis(start);
                        Calendar stopCal = new GregorianCalendar();
                        stopCal.setTimeInMillis(stop);

                        // try {
                        programs.add(new Program(channel, new OnceProgramRules(startCal, stopCal, filename)));
                        // } catch(ProgramAddException e) {}
                        // programs.add(new Program(channel,new
                        // OnceProgramRules(startCal,stopCal,new
                        // File(filename))));
                        // }
                        break;
                    case 'h':
                        int days = dis.readByte();
                        short startTime = dis.readShort();
                        short stopTime = dis.readShort();
                        String pathname = dis.readUTF();

                        // if(channel != null) {
                        // try {
                        programs.add(new Program(channel, new HebdoProgramRules(channel, days, startTime, stopTime, pathname)));
                        // } catch(ProgramAddException e) {}
                        // }
                        break;
                }
            }
        } catch(IOException e) {} finally {
            try {
                if(dis != null) {
                    dis.close();
                }
            } catch(IOException e) {}
            Collections.sort(programs);
            setChanged();
            notifyObservers();
        }
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public boolean isProgrammed(RecordJob job) {
        boolean found = false;
        int i = 0;
        while(i < programs.size() && !found) {
            Program p = programs.get(i);
            if(p.getJob() == job)
                found = true;
            i++;
        }
        return found;
    }

    // public Program getPrograms(int index) { return programs.get(index); }

    public void add(Program program) throws ProgramAddException {
        if(program.getStart().compareTo(program.getStop()) >= 0)
            throw new ProgramAddException("L'arrêt doit être postérieur au lancement.");
        if(program.getStop().compareTo(Calendar.getInstance()) <= 0)
            throw new ProgramAddException("La programmation doit être future.");
        for(Program p : programs) {
            if(intersect(p, program))
                throw new ProgramAddException("La période d'enregistrement d'une chaîne ne doit pas\nintersecter une autre programmation de la même cha�ne.");
        }
        programs.add(program);
        Collections.sort(programs);
        save();
        setChanged();
        notifyObservers();
    }

    public void remove(Program program) {
        programs.remove(program);
        save();
        setChanged();
        notifyObservers();
    }

    public void modif(Program oldProg, Program newProg) {
        oldProg.setValues(newProg);
        save();
        setChanged();
        notifyObservers("program_hour");
    }

    public void update(Observable o, Object arg) {
        // List<Program> aSuppr = new ArrayList<Program>();
        int i = 0;
        while(i < programs.size()) {
            Program program = programs.get(i);
            if(program.getJob() != null && program.isStarted() && !JobManager.getInstance().getRecords().contains(program.getJob())) {
                program.setJob(null);
                if(!program.init(true)) {
                    programs.remove(i);
                    setChanged();
                } else
                    i++;
            } else
                i++;
        }
        if(hasChanged())
            save();
        notifyObservers();
        // for(Program program : programs) {
        // if(program.getJob() != null && program.isStarted() &&
        // !JobManager.getInstance().getRecords().contains(program.getJob())) {
        // aSuppr.add(program);
        // setChanged();
        // }
        // }
        // for(Program p : aSuppr) {
        // p.setJob(null);
        // if(!p.init(true)) {
        // programs.remove(p);
        // setChanged();
        // save();
        // }
        // }
        // notifyObservers();
    }

    public boolean isUsed(File file) {
        boolean found = false;
        int i = 0;
        while(i < programs.size() && !found) {
            Program p = programs.get(i);
            if(p.getFilename().equals(file.getAbsolutePath()))
                found = true;
            i++;
        }
        return found;
    }

    public void run() {
        while(true) {
            List<Program> toStart = new ArrayList<Program>();
            List<Program> toStop = new ArrayList<Program>();
            Calendar cal = Calendar.getInstance();
            toStart.clear();
            toStop.clear();
            for(Program p : programs) {
                boolean before = cal.compareTo(p.getStart()) < 0;
                boolean after = cal.compareTo(p.getStop()) >= 0;
                boolean between = !before && !after;
                Recordable recordable = p.getRecordable();
                boolean ok = false; /* true si cha�ne connue */
                if(recordable instanceof Channel) {
                    ok = recordable.getUrl() != null;
                }
                if(before && p.isStarted() || (after)) {
                    toStop.add(p);
                } else if(between && !p.isStarted() && ok) {
                    toStart.add(p);
                }
                // else if(after && (!ok || p.isStarted())) {
                // toStop.add(p);
                // }
            }
            for(Program p : toStart) {
                try {
                    // System.out.println(FileUtils.generateInexistantFilename(p.getFilename()));
                    RecordJob rj = JobManager.getInstance().startRec(p.getRecordable(), FileUtils.generateInexistantFilename(p.getFilename()));
                    if(rj != null)
                        p.setJob(rj);
                    else
                        toStop.add(p);
                    setChanged();
                } catch(Throwable e) {}
            }
            for(Program p : toStop) {
                RecordJob rj = p.getJob();
                if(rj != null) {
                    try {
                        JobManager.getInstance().stopRec(rj);
                    } catch(Throwable e) {}
                    p.setJob(null);
                    setChanged();
                }
                if(!p.init(true)) {
                    programs.remove(p);
                    save();
                    setChanged();
                }
            }
            notifyObservers();
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {}
        }
    }

    public static boolean intersect(Program p, Program q) {
        boolean intersect = false;
        if(p.getRecordable().equals(q.getRecordable())) {
            if(p.getProgramRules() instanceof OnceProgramRules && q.getProgramRules() instanceof OnceProgramRules) {
                Calendar start1 = p.getStart();
                Calendar start2 = q.getStart();
                Calendar stop1 = p.getStop();
                Calendar stop2 = q.getStop();
                if(stop1.compareTo(start2) > 0 && stop2.compareTo(start1) > 0)
                    intersect = true;
            } else if(p.getProgramRules() instanceof HebdoProgramRules && q.getProgramRules() instanceof HebdoProgramRules) {
                HebdoProgramRules hp = (HebdoProgramRules) p.getProgramRules();
                HebdoProgramRules hq = (HebdoProgramRules) q.getProgramRules();
                int start1 = hp.getStartMin();
                int stop1 = hp.getStopMin();
                int start2 = hq.getStartMin();
                int stop2 = hq.getStopMin();
                if(stop1 < start1)
                    stop1 += 24 * 60;
                if(stop2 < start2)
                    stop2 += 24 * 60;
                if(stop1 > start2 && stop2 > start1) {
                    int days[] = { Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY };
                    int i = 0;
                    while(i < days.length && !intersect) {
                        if(hp.isActiveOnDay(days[i]) && hq.isActiveOnDay(days[i]))
                            intersect = true;
                        i++;
                    }
                }
            } else {
                if(p.getProgramRules() instanceof HebdoProgramRules) {
                    Program tmp = p;
                    p = q;
                    q = tmp;
                }
                /*
                 * p.programRules instanceof OnceProgramRules && q.programRules
                 * instanceof HebdoProgramRules
                 */
                Calendar start = p.getProgramRules().getStart();
                Calendar stop = p.getProgramRules().getStop();
                Calendar stopDay = new GregorianCalendar(stop.get(Calendar.YEAR), stop.get(Calendar.MONTH), stop.get(Calendar.DAY_OF_MONTH) + 1);
                HebdoProgramRules hq = (HebdoProgramRules) q.getProgramRules();
                int startTime = hq.getStartMin();
                int stopTime = hq.getStopMin();
                if(stopTime < startTime)
                    stopTime += 24 * 60;
                Calendar cal = new GregorianCalendar();
                cal.setTimeInMillis(start.getTimeInMillis());
                int i = 0;
                while(cal.compareTo(stopDay) < 0 && i < 7 && !intersect) {
                    if(hq.isActiveOnDay(cal.get(Calendar.DAY_OF_WEEK))) {
                        Calendar hqStart = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), startTime / 60, startTime % 60);
                        Calendar hqStop = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), stopTime / 60, stopTime % 60);
                        if(stop.compareTo(hqStart) > 0 && hqStop.compareTo(start) > 0)
                            intersect = true;
                    }
                    i++;
                }
            }
        }
        return intersect;
    }

}

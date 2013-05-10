package org.rom.myfreetv.process;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.rom.myfreetv.streams.Recordable;

public class Program implements Comparable<Program> {

    private static DateFormat formatter = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm");
    private static DateFormat formatterTime = new SimpleDateFormat("HH:mm");
    private static NumberFormat formatter00 = new DecimalFormat("00");

    private Recordable recordable;
    // private Calendar start;
    // private Calendar stop;
    // private File file;
    private RecordJob job;
    // private boolean complete;
    private ProgramRules programRules;
    private String programHTML;

    public Program(Recordable recordable, ProgramRules programRules) {
        this.recordable = recordable;
        this.programRules = programRules;
        // this.start = start;
        // this.stop = stop;
        // this.file = file;
        initHTML();
    }

    public Recordable getRecordable() {
        return recordable;
    }

    public Calendar getStart() {
        return programRules.getStart();
    }

    public Calendar getStop() {
        return programRules.getStop();
    }

    public String getFilename() {
        return programRules.getFilename();
    }

    public RecordJob getJob() {
        return job;
    }

    public ProgramRules getProgramRules() {
        return programRules;
    }

    public void setJob(RecordJob job) {
        this.job = job;
    }

    // public void setComplete(boolean complete) { this.complete = complete; }

    public boolean isStarted() {
        return job != null;
    }

    // public boolean isComplete() { return complete; }

    public boolean hasNext() {
        return programRules.hasNext();
    }

    public boolean init(boolean next) {
        return programRules.init(next);
    }

    public boolean init() {
        return init(false);
    }

    public void setValues(Program other) {
        recordable = other.recordable;
        job = other.job;
        programRules = other.programRules;
        initHTML();
    }

    public int compareTo(Program other) {
        return getStart().compareTo(other.getStart());
    }

    public String toString() {
        return recordable.toString() + " " + programRules.toString();
    }

    private void initHTML() {
        StringBuffer period = new StringBuffer("<html><font size=\"2\">");
        if(getProgramRules() instanceof OnceProgramRules) {
            Calendar start = getStart();
            Calendar stop = getStop();
            boolean sameDay = start.get(Calendar.YEAR) == stop.get(Calendar.YEAR) && start.get(Calendar.MONTH) == stop.get(Calendar.MONTH) && start.get(Calendar.DAY_OF_MONTH) == stop.get(Calendar.DAY_OF_MONTH);
            period.append(formatter.format(new Date(start.getTimeInMillis())));
            period.append(" - ");
            if(!sameDay)
                period.append(formatter.format(new Date(stop.getTimeInMillis())));
            else
                period.append(formatterTime.format(new Date(stop.getTimeInMillis())));
//            period.append("<br><i>");
//            period.append(getFilename());
//            period.append("</i>");
        } else if(getProgramRules() instanceof HebdoProgramRules) {
            HebdoProgramRules hp = (HebdoProgramRules) getProgramRules();
            int nb = 0;
            if(hp.isActiveOnDay(Calendar.MONDAY)) {
                period.append("Lundi");
                nb++;
            }
            if(hp.isActiveOnDay(Calendar.TUESDAY)) {
                if(nb > 0)
                    period.append(" ");
                period.append("Mardi");
                nb++;
            }
            if(hp.isActiveOnDay(Calendar.WEDNESDAY)) {
                if(nb > 0)
                    period.append(" ");
                period.append("Mercredi");
                nb++;
            }
            if(hp.isActiveOnDay(Calendar.THURSDAY)) {
                if(nb > 0)
                    period.append(" ");
                period.append("Jeudi");
                nb++;
            }
            if(hp.isActiveOnDay(Calendar.FRIDAY)) {
                if(nb > 0)
                    period.append(" ");
                period.append("Vendredi");
                nb++;
            }
            if(hp.isActiveOnDay(Calendar.SATURDAY)) {
                if(nb > 0)
                    period.append(" ");
                period.append("Samedi");
                nb++;
            }
            if(hp.isActiveOnDay(Calendar.SUNDAY)) {
                if(nb > 0)
                    period.append(" ");
                period.append("Dimanche");
                nb++;
            }
            period.append(", de ");
            period.append(formatter00.format(hp.getStartMin() / 60));
            period.append(':');
            period.append(formatter00.format(hp.getStartMin() % 60));
            period.append(" à ");
            period.append(formatter00.format(hp.getStopMin() / 60));
            period.append(':');
            period.append(formatter00.format(hp.getStopMin() % 60));
            // period.append("<br><i>");
            // period.append(hp.getPath());
            // period.append("</i>");
        }
        period.append("</font></html>");
        programHTML = new String(period);
    }

    public String getHTML() {
        return programHTML;
    }

}

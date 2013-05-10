package org.rom.myfreetv.process;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.rom.myfreetv.files.FileUtils;
import org.rom.myfreetv.streams.Channel;

public class HebdoProgramRules extends ProgramRules {

    public static int LUNDI = 1;
    public static int MARDI = 1 << 1;
    public static int MERCREDI = 1 << 2;
    public static int JEUDI = 1 << 3;
    public static int VENDREDI = 1 << 4;
    public static int SAMEDI = 1 << 5;
    public static int DIMANCHE = 1 << 6;

    private Channel channel;
    private int days;
    private String path;
    private int startMin; /* in minutes of day */
    private int stopMin;

    private Calendar start;
    private Calendar stop;

    public HebdoProgramRules(Channel channel, int days, int startMin, int stopMin, String path) {
        this.channel = channel;
        this.days = days;
        this.startMin = startMin;
        this.stopMin = stopMin;
        this.path = path;
        init();
    }

    public HebdoProgramRules(Channel channel, int days, int startHour, int startMinute, int stopHour, int stopMinute, String path) {
        this(channel, days, startHour * 60 + startMinute, stopHour * 60 + stopMinute, path);
    }

    public int getDays() {
        return days;
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getStop() {
        return stop;
    }

    public String getFilename() {
        return path + File.separatorChar + FileUtils.generateAutoFilename(getStart(), channel);
    }

    public String getPath() {
        return path;
    }

    public int getStartMin() {
        return startMin;
    }

    public int getStopMin() {
        return stopMin;
    }

    public boolean hasNext() {
        return days > 0;
    }

    public boolean init(boolean next) {
        boolean hasNext = hasNext();
        if(hasNext) {
            Calendar cal = Calendar.getInstance();

            if(next && stop != null) {
                cal.setTimeInMillis(stop.getTimeInMillis());
            }
            int time = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
            int startTime = startMin;
            int stopTime = stopMin;
            if(stopTime <= startTime) {
                if(time < stopTime) {
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                }
            } else {
                if(time >= stopTime) {
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
            if(stopTime <= startMin)
                stopTime += 24 * 60;

            int i = 0;
            while(!isActiveOnDay(cal.get(Calendar.DAY_OF_WEEK)) && i < 7) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
                i++;
            }
            if(i == 7)
                hasNext = false;
            else {
                start = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), startMin / 60, startMin % 60);
                stop = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), stopTime / 60, stopTime % 60);
            }
        }
        return hasNext;
    }

    public boolean isActiveOnDay(int field) {
        boolean active = false;
        switch(field) {
            case Calendar.MONDAY:
                active = days % 2 == 1;
                break;
            case Calendar.TUESDAY:
                active = (days >> 1) % 2 == 1;
                break;
            case Calendar.WEDNESDAY:
                active = (days >> 2) % 2 == 1;
                break;
            case Calendar.THURSDAY:
                active = (days >> 3) % 2 == 1;
                break;
            case Calendar.FRIDAY:
                active = (days >> 4) % 2 == 1;
                break;
            case Calendar.SATURDAY:
                active = (days >> 5) % 2 == 1;
                break;
            case Calendar.SUNDAY:
                active = (days >> 6) % 2 == 1;
                break;
        }
        return active;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if(isActiveOnDay(Calendar.MONDAY))
            buf.append("Lundi ");
        if(isActiveOnDay(Calendar.TUESDAY))
            buf.append("Mardi ");
        if(isActiveOnDay(Calendar.WEDNESDAY))
            buf.append("Mercredi ");
        if(isActiveOnDay(Calendar.THURSDAY))
            buf.append("Jeudi ");
        if(isActiveOnDay(Calendar.FRIDAY))
            buf.append("Vendredi ");
        if(isActiveOnDay(Calendar.SATURDAY))
            buf.append("Samedi ");
        if(isActiveOnDay(Calendar.SUNDAY))
            buf.append("Dimanche ");
        buf.append(startMin / 60);
        buf.append(":");
        buf.append(startMin % 60);
        buf.append(" - ");
        buf.append(stopMin / 60);
        buf.append(":");
        buf.append(stopMin % 60);
        buf.append(" ");
        buf.append(path);
        return new String(buf);
    }

}

package org.rom.myfreetv.process;

import java.util.Calendar;

public class OnceProgramRules extends ProgramRules {

    private Calendar start;
    private Calendar stop;
    private String filename;

    public OnceProgramRules(Calendar start, Calendar stop, String filename) {
        this.start = start;
        this.stop = stop;
        this.filename = filename;
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getStop() {
        return stop;
    }

    public String getFilename() {
        return filename;
    }

    public boolean hasNext() {
        return true;
    }

    public boolean init(boolean next) {
        return !next && Calendar.getInstance().compareTo(stop) <= 0;
    }

    public String toString() {
        return start + " - " + stop + " " + filename;
    }

}

package org.rom.myfreetv.process;

import java.util.Calendar;

public abstract class ProgramRules {

    public abstract Calendar getStart();

    public abstract Calendar getStop();

    public abstract String getFilename();

    public abstract boolean hasNext();

    public abstract boolean init(boolean next);

    public boolean init() {
        return init(false);
    }

}

package org.rom.myfreetv.config;

public class MuxMode {

    public final static MuxMode TS = new MuxMode("ts");
    public final static MuxMode PS = new MuxMode("ps");

    private String name;

    public MuxMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MuxMode getMuxMode(String name) {
        MuxMode mode = getDefault();
        if(name != null) {
            if(name.equals(TS.getName()))
                mode = TS;
            else if(name.equals(PS.getName()))
                mode = PS;
        }
        return mode;
    }

    public static MuxMode getDefault() {
        return TS;
    }

}
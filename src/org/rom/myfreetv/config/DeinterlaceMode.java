package org.rom.myfreetv.config;

public class DeinterlaceMode {

    public final static DeinterlaceMode NONE = new DeinterlaceMode("none");
    public final static DeinterlaceMode BLEND = new DeinterlaceMode("blend");
    public final static DeinterlaceMode BOB = new DeinterlaceMode("bob");
    public final static DeinterlaceMode DISCARD = new DeinterlaceMode("discard");
    public final static DeinterlaceMode LINEAR = new DeinterlaceMode("linear");
    public final static DeinterlaceMode MEAN = new DeinterlaceMode("mean");
    public final static DeinterlaceMode X = new DeinterlaceMode("x");

    private String name;

    private DeinterlaceMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static DeinterlaceMode getDeinterlaceMode(String name) {
        DeinterlaceMode mode = getDefault();
        if(name != null) {
            if(name.equals(BLEND.getName()))
                mode = BLEND;
            else if(name.equals(BOB.getName()))
                mode = BOB;
            else if(name.equals(DISCARD.getName()))
                mode = DISCARD;
            else if(name.equals(LINEAR.getName()))
                mode = LINEAR;
            else if(name.equals(MEAN.getName()))
                mode = MEAN;
            else if(name.equals(X.getName()))
                mode = X;
        }
        return mode;
    }

    public static DeinterlaceMode getDefault() {
        return LINEAR;
    }

}

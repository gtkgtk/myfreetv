package org.rom.myfreetv.streams;

import java.io.File;

public class TimeShiftFileIn extends FileIn {

    public TimeShiftFileIn(String url) {
        super(url);
    }

    public TimeShiftFileIn(File file) {
        super(file.getAbsolutePath());
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean equals(Object other) {
        String urlOther = null;
        if(other instanceof Channel) {
            urlOther = ((Channel) other).getUrl();
            return channel.getUrl().equals(urlOther);
        } else if(other instanceof FileIn) {
            urlOther = ((FileIn) other).getChannel().getUrl();
            return getUrl().equals(urlOther);
        } else
            return false;
    }

}

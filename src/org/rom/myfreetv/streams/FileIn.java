package org.rom.myfreetv.streams;

import java.io.File;

public class FileIn extends File implements Playable {

    protected Channel channel;

    public FileIn(String url) {
        super(url);
    }

    public FileIn(File file) {
        super(file.getAbsolutePath());
    }

    public String getUrl() {
        return getAbsolutePath();
    }

    public Channel getChannel() {
        return channel;
    }

    // public void setChannel(Channel channel) {
    // this.channel = channel;
    // }

    public String getName() {
        return super.getName();
        // if(channel != null)
        // return channel.getName();
        // else
        // return super.getName();
    }

    public boolean equals(Object other) {
        boolean eq = false;
        if(other instanceof FileIn) {
            eq = getUrl().equals(((FileIn) other).getUrl());
        }
        return eq;
    }

    public boolean canPause() {
        return true;
    }

}

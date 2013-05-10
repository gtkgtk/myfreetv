package org.rom.myfreetv.process;

import org.rom.myfreetv.player.Player;
import org.rom.myfreetv.streams.Playable;

public class FreePlayJob extends Job {

    private Player player;

    public FreePlayJob(Playable playable, Player player) {
        super(playable);
        this.player = player;
    }

    public void start(float position) throws Exception {
        super.start(position);
        player.playToTV((Playable) stream, position);
    }

    public void stop(boolean andKill) throws Exception {
        super.stop();
        player.stop(andKill);
    }

    public void stop() throws Exception {
        stop(true);
    }

    public boolean canPause() {
        return player.canPause();
    }

    public void pause() throws Exception {
        player.pause();
    }

    public Playable getPlayable() {
        return (Playable) stream;
    }

    public Player getPlayer() {
        return player;
    }

    public String getUrlInput() {
        return stream.getUrl();
    }

    public boolean isRunning() {
        return player.isRunning();
    }

}

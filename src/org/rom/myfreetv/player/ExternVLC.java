package org.rom.myfreetv.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.rom.myfreetv.config.Config;
import org.rom.myfreetv.config.DeinterlaceMode;
import org.rom.myfreetv.files.FileUtils;
import org.rom.myfreetv.process.Job;
import org.rom.myfreetv.streams.Playable;
import org.rom.myfreetv.streams.RadioChannel;
import org.rom.myfreetv.streams.Recordable;

public class ExternVLC extends Observable implements Player, Recorder, Runnable {

    private Job job;
    private Process process;

    public void setJob (Job job) {
        if(this.job != null)
            deleteObserver (this.job);
        this.job = job;
        if(job != null)
            addObserver (job);
    }

    public void play (Playable playable) throws Exception {
        launch (getPlayCommand (playable));
    }

    public void play (Playable playable, float position) throws Exception {
        play (playable);
    }

    public void playToTV (Playable playable, float position) throws Exception {
        playToTV (playable);
    }

    public void playToTV (Playable playable) throws Exception {
        launch (getFreePlayCommand (playable));
    }

    public void record (Recordable recordable, String output) throws Exception {
        launch (getRecordCommand (recordable, output));
    }

    public void stop (boolean andKill) throws Exception {
        process.destroy ();
        // PrintWriter pw = new PrintWriter(process.getOutputStream());
        // pw.println("quit");
    }

    public void stop () throws Exception {
        stop (true);
    }

    public boolean canPause () {
        return false;
    }

    public void pause () {}

    public boolean haveToStopBeforePlay () {
        return true;
    }

    public boolean isRunning () {
        boolean isOk = true;
        try {
            process.exitValue ();
            isOk = false;
        } catch(IllegalThreadStateException e) {}
        return isOk;
    }

    public static void reset (String url) throws IOException {
        String cmd = url + " --intf=dummy";
        if(Config.getInstance ().isWindowsOS ())
            cmd += " --dummy-quiet";
        cmd += " --reset-config --reset-plugins-cache --wx-embed --save-config vlc:quit";
        Runtime.getRuntime ().exec (cmd);
    }

    private void launch (String[] cmd) throws IOException {
        // System.err.println("-- commande exécutée --\n" + cmd);
        process = Runtime.getRuntime ().exec (cmd);
        process.getInputStream ().close ();
        process.getOutputStream ().close ();
        new Thread (this).start ();
    }

    private String[] getPlayCommand (Playable playable) {
        List<String> args = new ArrayList<String>();
        for(String s : Config.getInstance ().getVlcPath ().split (";"))
            args.add (s);

        for(String s : playable.getUrl ().split (";"))
            args.add (s);
        //args.add(playable.getUrl().replaceAll(" ", "%20"));

        if( playable instanceof RadioChannel) {
            System.out.println ("--radio--");
            //args.add ("--realrtsp-caching=600");
            //args.add ("--audio-filter=goom");
        } else if(Config.getInstance ().getDeinterlaceMode () != DeinterlaceMode.NONE) {
            args.add ("--vout-filter=deinterlace");
            args.add ("--deinterlace-mode=" + Config.getInstance ().getDeinterlaceMode ().getName ());
        }
        //System.out.println ("Commande exécutée (tokens) ----\n" + args);

//
//      args.add("--dshow-vdev");
//      args.add("\"Pinnacle WDM PCTV Video Capture\"");
//      args.add("--dshow-adev");
//      args.add("\"\"");
//      args.add("--dshow-size");
//      args.add("\"640X480\"");
//
//       /*
//      args.add("--intf");
//      args.add("wx");
//      args.add("--extraintf");
//      args.add("telnet");
//      args.add("--vlm-conf");
//      args.add("mosa.conf");
//      */

        String[] argv = new String[args.size ()];
        return args.toArray (argv);
    }
    private String[] getCmd () {
        String[] s = {
            "del all",
            "new bg broadcast enabled",
            "setup bg input \"rtsp://mafreebox.freebox.fr/freeboxtv/203\"",
            "setup bg output #bridge-in{offset=100}:display",
            "setup bg option sub-filter=mosaic",
            "setup bg option mosaic-alpha=255",
            "setup bg option mosaic-height=100",
            "setup bg option mosaic-width=200",
            "setup bg option mosaic-align=5",
            "setup bg option mosaic-xoffset=10",
            "setup bg option mosaic-yoffset=10",
            "setup bg option mosaic-vborder=5",
            "setup bg option mosaic-hborder=5",
            "setup bg option mosaic-position=1",
            "setup bg option mosaic-rows=1",
            "setup bg option mosaic-cols=1",
            "setup bg option mosaic-order=_,_",
            "setup bg option mosaic-delay=0",
            "setup bg option mosaic-keep-picture",
            "new channel1 broadcast enabled",
            "setup channel1 input \"rtsp://mafreebox.freebox.fr/freeboxtv/202\"",
            "setup channel1 output #duplicate{dst=mosaic-bridge{id=1,height=144,width=180},select=video}",
            "control bg play",
            "control channel1 play"};
        return s;
    }
    private String[] getFreePlayCommand (Playable playable) {
        boolean transcode = !FileUtils.isMpeg (playable.getUrl ());
        List<String> args = new ArrayList<String>();
        for(String s : Config.getInstance ().getVlcPath ().split (";"))
            args.add (s);
        args.add (playable.getUrl ().replaceAll (" ", "%20"));
        args.add ("--extraintf=http");
        args.add ("--http-host=:8080");
        if(transcode)
            args.add ("--sout=#transcode:std");
        else
            args.add ("--sout=\"#std\"");
        args.add ("--sout-standard-access=udp");
        args.add ("--sout-standard-mux=ts");
        args.add ("--sout-standard-url=212.27.38.253:1234");
        args.add ("--sout-ts-pid-video=68");
        args.add ("--sout-ts-pid-audio=69");
        args.add ("--sout-ts-pid-spu=70");
        args.add ("--sout-ts-pcr=80");
        args.add ("--sout-ts-dts-delay=400");
        if(transcode) {
            args.add ("--sout-transcode-vcodec=mp2v");
            args.add ("--sout-transcode-vb=6000");
            args.add ("--sout-transcode-fps=25.0");
            args.add ("--sout-ffmpeg-keyint=24");
            args.add ("--sout-ffmpeg-interlace");
            args.add ("--no-sout-ffmpeg-interlace-me");
            args.add ("--sout-transcode-acodec=mpga");
            args.add ("--sout-transcode-ab=256");
            args.add ("--sout-transcode-channels=2");
            args.add ("--file-caching=1000");
            args.add ("--sout-udp-caching=300");
            args.add ("--sout-transcode-maxwidth=720");
            args.add ("--sout-transcode-maxheight=576");
        }
        args.add ("--http-src=./http-fbx");
        //System.out.println ("Commande exécutée (tokens) ----\n" + args);
        String[] argv = new String[args.size ()];
        return args.toArray (argv);
    }

    private String[] getRecordCommand (Recordable recordable, String output) {
        List<String> args = new ArrayList<String>();
        for(String s : Config.getInstance ().getVlcPath ().split (";"))
            args.add (s);
        for(String s : recordable.getUrl ().split (";"))
            args.add (s);
        args.add ("--intf=dummy");
        if(Config.getInstance ().isWindowsOS ())
            args.add ("--dummy-quiet");

        String s="";
        if( recordable instanceof RadioChannel)
        	s =  ":sout=#transcode{vcodec=none,acodec=vorbis,ab=128,channels=2}:duplicate{dst=std{access=file,mux=ogg,dst=\"" + output +"\"}}";
        else
            s = ":sout=#duplicate{dst=std{access=file,mux=" + Config.getInstance ().getMuxMode ().getName () + ",dst=\"" + output + "\" }}";

        if(Config.getInstance ().isWindowsOS ()) {
            for(String e : s.split (" "))
                args.add (e);
        } else {
            args.add (s);
        }
        if( !(recordable instanceof RadioChannel))
            args.add (":sout-all");
//        System.out.println ("Commande exécutée (tokens) ----\n" + args);
        String[] argv = new String[args.size ()];
        return args.toArray (argv);
    }

    public void run () {
        if(process != null) {
            /* attente pour corriger un bug pour certains utilisateurs linux */
            try {
                Thread.sleep (1500);
            } catch(InterruptedException e) {}
            try {
                process.getErrorStream ().close ();
            } catch(IOException e) {}
            try {
                process.waitFor ();
            } catch(InterruptedException e) {}
        }
        setChanged ();
        notifyObservers ("stopped");
    }

}

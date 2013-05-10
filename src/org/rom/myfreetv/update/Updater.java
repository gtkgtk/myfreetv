package org.rom.myfreetv.update;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Updater {

    private final static String filename = "http://myfreetv.sourceforge.net/last_version.php";
    private static Updater instance;

    public static Updater getInstance() {
        if(instance == null)
            instance = new Updater();
        return instance;
    }

    public String getLastVersion() {
        String version = null;
        Scanner sc = null;
        try {
            URL fileUrl = new URL(filename);
            URLConnection conn = fileUrl.openConnection();
            conn.setUseCaches(false);
            conn.connect();

            sc = new Scanner(conn.getInputStream());
            if(sc.hasNextLine())
                version = sc.nextLine();
        } catch(IOException e) {} finally {
            if(sc != null)
                sc.close();
        }
        return version;
    }

}

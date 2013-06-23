package org.rom.myfreetv.guidetv;

//import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import org.rom.myfreetv.streams.Channel;

class ChannelGuide extends Observable {

    private Channel channel;
    private Map<Calendar, List<Emission>> emissions;

    ChannelGuide(Channel channel) {
        this.channel = channel;
        emissions = new HashMap<Calendar, List<Emission>>();
    }

    // private Calendar getStartOnDB() {
    // return startOnDB;
    // }
    //    
    // private Calendar getStopOnDB() {
    // return stopOnDB;
    // }

    public List<Emission> getEmissions(final Calendar day) {
        List<Emission> list = emissions.get(day);
        if(list == null && GuideTVManager.db != null) {
            new Thread() {

                public void run() {
                    emissions.put(day, new ArrayList<Emission>()); /*
                                                                     * pour
                                                                     * éviter
                                                                     * plusieurs
                                                                     * demandes
                                                                     * à la db
                                                                     */
                    List<Emission> list = null;
                    //try {
                        list = GuideTVManager.db.getEmissions(channel, day);
                    //} catch(SQLException e) {
                    //    e.printStackTrace();
                    //}
                    if(list != null) {
                        emissions.put(day, list);
                        setChanged();
                        notifyObservers();
                    } else {
                        emissions.remove(day);
                    }
                }
            }.start();
        }
        return list;
    }

    public Emission getCurrent() {
        Calendar cur = Calendar.getInstance();
        Calendar day = new GregorianCalendar(cur.get(Calendar.YEAR), cur.get(Calendar.MONTH), cur.get(Calendar.DAY_OF_MONTH));
        List<Emission> list = getEmissions(day);
        Emission emission = null;
        if(list != null) {
            int i = 0;
            while(i < list.size() && emission == null) {
                Emission e = list.get(i);
                if(isCurrent(cur, e.getStart(), e.getEnd()))
                    emission = e;
                i++;
            }
        }
        return emission;
    }

    private static boolean isCurrent(Calendar current, Calendar start, Calendar stop) {
        return current.compareTo(start) >= 0 && current.compareTo(stop) < 0;
    }

}

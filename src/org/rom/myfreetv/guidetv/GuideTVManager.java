package org.rom.myfreetv.guidetv;

//import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.rom.myfreetv.streams.Channel;

public class GuideTVManager extends Observable implements Observer {

    private static GuideTVManager instance;

    static Database db;
    private Map<Integer, ChannelGuide> channelGuides;
    private Calendar update;
    //private boolean refreshed;

    private GuideTVManager() {
        channelGuides = new HashMap<Integer, ChannelGuide>();
        new Thread() {

            public void run() {
                do {
                    //try {
                        db = new Database();
                        update = db.getLastUpdate();
                    //} catch(SQLException e) {
                    //    e.printStackTrace();
                    //}
                    try {
                        Thread.sleep(10000);
                    } catch(InterruptedException e) {}
                } while(db == null);
                setChanged();
                notifyObservers();
//                while(true) {
//                    refreshed = false;
//                    initCache();
//                    try {
//                        Thread.sleep(3600000);
//                    } catch(InterruptedException e) {}
//                }
            }
        }.start();
    }

    public static GuideTVManager getInstance() {
        if(instance == null)
            instance = new GuideTVManager();
        return instance;
    }

    // public void closeDB() {
    // if(db != null) {
    // try {
    // db.close();
    // } catch(SQLException e) {}
    // }
    // }

    public void initCache() {
//        if(!refreshed && MyFreeTV.getInstance() != null && MyFreeTV.getInstance().isGuideTVVisible()) {
            //try {
                if(db != null) {
                    Calendar dbUpdate = db.getLastUpdate();
                    boolean ok = update != null && dbUpdate.compareTo(update) <= 0;
                    if(!ok) {
                        channelGuides.clear();
                        update = dbUpdate;
                    }
                    //refreshed = true;
                }
            //} catch(SQLException e) {}
//        }
    }

    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers("guidetv");
        //notifyObservers();
    }

    public List<Emission> getEmissions(Channel channel, Calendar day) {
        // initCache();
        day = new GregorianCalendar(day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH));
        List<Emission> list = null;
        ChannelGuide cg = channelGuides.get(channel.getNum());
        if(cg == null) {
//            initCache();
            cg = new ChannelGuide(channel);
            cg.addObserver(this);
            channelGuides.put(channel.getNum(), cg);
        }
        list = cg.getEmissions(day);
        /*
         * null si pas encore chargé de la db, lance un processus qui le charge
         * et qui notifiera qd il aura fini
         */
        return list;
    }

    public Emission getCurrent(Channel channel) {
        // initCache();
        Emission emission = null;
        ChannelGuide cg = channelGuides.get(channel.getNum());
        if(cg == null) {
//            initCache();
            cg = new ChannelGuide(channel);
        }
        emission = cg.getCurrent();
        return emission;
    }

    public Emission getNext(Channel channel) {
        // initCache();
        Emission emission = null;
        ChannelGuide cg = channelGuides.get(channel.getNum());
        if(cg == null) {
//            initCache();
            cg = new ChannelGuide(channel);
        }
        emission = cg.getNext();
        return emission;
    }

    // private void save() {
    // try {
    // ObjectOutputStream oos = new ObjectOutputStream(new
    // FileOutputStream(filename));
    // oos.writeLong(update.getTimeInMillis());
    // for(java.util.Map.Entry<Integer,List<Emission>> me :
    // emissions.entrySet())
    // for(Emission emission : me.getValue())
    // oos.writeObject(emission);
    // } catch(IOException ioe) {}
    // }

    // public List<Emission> getEmissions(Channel channel, Calendar day) {
    // List<Emission> result = new ArrayList<Emission>();
    // Calendar startDay = new
    // GregorianCalendar(day.get(Calendar.YEAR),day.get(Calendar.MONTH),day.get(Calendar.DAY_OF_MONTH));
    // Calendar stopDay = new GregorianCalendar();
    // stopDay.setTimeInMillis(startDay.getTimeInMillis());
    // stopDay.add(Calendar.DAY_OF_MONTH,1);
    // int num = channel.getNum();
    // if(!emissions.containsKey(num) && db != null) {
    // try {
    // emissions.put(num,db.getEmissions(channel));
    // } catch(SQLException e) {}
    // }
    // for(Emission emission : emissions.get(num)) {
    // Calendar emDate = emission.getStart();
    // if(emDate.compareTo(startDay) >= 0 && emDate.compareTo(stopDay) < 0)
    // result.add(emission);
    // }
    // Collections.sort(result);
    // return result;
    // }

}

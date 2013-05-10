package org.rom.myfreetv.audience;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.ChannelManager;

public class XMLChannel implements Comparable<XMLChannel> {

    private final static NumberFormat formatter = new DecimalFormat("0.00");
    private final static NumberFormat formatter00 = new DecimalFormat("00");
    private Channel channel;
    private float marketShare;
    private Calendar start;
    private Calendar stop;
    private String title;

    public XMLChannel(String channelName, String market, String startTime, String stopTime, String title) throws XMLParseException {
        if(channelName == null || market == null)
            throw new XMLParseException("Données manquantes dans le fichier XML pour parser l'audience.");
        channel = ChannelManager.getInstance().getChannel(channelName);// getChannelFromName(channelName);
        if(channel == null)
            channel = new Channel(0, channelName, null);
        try {
            marketShare = Float.parseFloat(market);
        } catch(NumberFormatException e) {
            throw new XMLParseException("Impossible de parser la part de marchée de " + channelName + ".");
        }

        if(startTime != null && stopTime != null) {
            if(startTime.length() != 5 || startTime.charAt(2) != ':' || stopTime.length() != 5 || stopTime.charAt(2) != ':')
                throw new XMLParseException("Impossible de parser les horaires de " + channelName + ".");
            int startHour, startMinute, stopHour, stopMinute;
            try {
                startHour = Integer.parseInt(startTime.substring(0, 2));
                startMinute = Integer.parseInt(startTime.substring(3));
                stopHour = Integer.parseInt(stopTime.substring(0, 2));
                stopMinute = Integer.parseInt(stopTime.substring(3));
            } catch(NumberFormatException e) {
                throw new XMLParseException("Impossible de parser les horaires de " + channelName + " (" + startTime + "-" + stopTime + " ).");
            }
            if(startHour < 0 || startHour >= 24 || startMinute < 0 || startMinute >= 60 || stopHour < 0 || stopHour >= 24 || stopMinute < 0 || stopMinute >= 60)
                throw new XMLParseException("Horaires invalides pour " + channelName + ".");
            int startMin = startHour * 60 + startMinute;
            int stopMin = stopHour * 60 + stopMinute;
            if(stopMin < startMin)
                stopMin += 1440;
            Calendar current = Calendar.getInstance();
            int currentMin = current.get(Calendar.HOUR) * 60 + current.get(Calendar.MINUTE);
            // if(startMin <= currentMin && currentMin < stopMin) {
            start = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH), startMin / 60, startMin % 60);
            stop = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH), stopMin / 60, stopMin % 60);
            // }
            if(startMin > currentMin || currentMin >= stopMin) {
                int distanceFin = currentMin - stopMin;
                if(distanceFin < 0)
                    distanceFin += 1440;
                int distanceDebut = startMin - currentMin;
                if(distanceDebut < 0)
                    distanceDebut += 1440;
                if(distanceDebut < distanceFin && currentMin >= stopMin) {
                    start.add(Calendar.DAY_OF_MONTH, 1);
                    stop.add(Calendar.DAY_OF_MONTH, 1);
                } else if(distanceDebut >= distanceFin && currentMin < startMin) {
                    start.add(Calendar.DAY_OF_MONTH, 1);
                    stop.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
        }
        this.title = title;
    }

    // private Channel getChannelFromName(String name) {
    // List<Channel> channels = ChannelsManager.getInstance().getChannels();
    // Channel ch = null;
    // int i = 0;
    // while(i < channels.size() && ch == null) {
    // if(channels.get(i).getName().equals(name))
    // ch = channels.get(i);
    // i++;
    // }
    // if(ch == null) {
    // i = 0;
    // while(i < channels.size() && ch == null) {
    // if(channels.get(i).getName().startsWith(name))
    // ch = channels.get(i);
    // i++;
    // }
    // }
    // return ch;
    // }

    public Channel getChannel() {
        return channel;
    }

    public float getMarketShare() {
        return marketShare;
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getStop() {
        return stop;
    }

    public String getTitle() {
        return title;
    }

    public int compareTo(XMLChannel other) {
        return new Float(marketShare).compareTo(new Float(other.marketShare));
    }

    public String toString() {
        int startHour = start.get(Calendar.HOUR_OF_DAY);
        int startMin = start.get(Calendar.MINUTE);
        int stopHour = stop.get(Calendar.HOUR_OF_DAY);
        int stopMin = stop.get(Calendar.MINUTE);
        return channel + " (" + formatter.format(marketShare) + ") " + formatter00.format(startHour) + ":" + formatter00.format(startMin) + " - " + formatter00.format(stopHour) + " : " + formatter00.format(stopMin) + " " + title;
    }

}

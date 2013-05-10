package org.rom.myfreetv.audience;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class AudienceXML extends DefaultHandler {

    private final static String xmlUrl = "http://audience.free.fr/audience15.xml";

    private static enum Key {
        CHANNEL, MARKET_SHARE, START_TIME, STOP_TIME, TITLE
    }

    private static AudienceXML instance;
    private XMLReader xr;
    private List<XMLChannel> list;
    private List<XMLChannel> temp;

    private String channelName, marketShare, startTime, stopTime, title;
    private boolean isInsideChannel;
    private Key current;

    private AudienceXML() throws SAXException {
        xr = XMLReaderFactory.createXMLReader();
        xr.setContentHandler(this);
        xr.setErrorHandler(this);
        list = new ArrayList<XMLChannel>(15);
    }

    public static AudienceXML getInstance() {
        if(instance == null) {
            try {
                instance = new AudienceXML();
            } catch(SAXException e) {}
        }
        return instance;
    }

    public List<XMLChannel> getList() {
        return list;
    }

    public void init() throws IOException, SAXException {
        // try {
        // System.out.println(temp.size() + " " + list.size());
        // } catch(Exception e) {}
        URL fileUrl = new URL(xmlUrl);
        URLConnection conn = fileUrl.openConnection();
        conn.setUseCaches(false);
        conn.connect();
        temp = new ArrayList<XMLChannel>(15);
        xr.parse(new InputSource(new BufferedReader(new InputStreamReader((conn.getInputStream())))));
        // list.clear();
        list = temp;
        // Collections.sort(list);
    }

    private void clearCache() {
        current = null;
        channelName = null;
        marketShare = null;
        startTime = null;
        stopTime = null;
        title = null;
    }

    public void startElement(String uri, String name, String qName, Attributes atts) {
        if(name.equals("channel")) {
            clearCache();
            isInsideChannel = true;
        } else if(isInsideChannel) {
            if(name.equals("name"))
                current = Key.CHANNEL;
            else if(name.equals("market_share"))
                current = Key.MARKET_SHARE;
            else if(name.equals("epg_start"))
                current = Key.START_TIME;
            else if(name.equals("epg_end"))
                current = Key.STOP_TIME;
            else if(name.equals("epg_title"))
                current = Key.TITLE;
            else
                current = null;
        }
    }

    public void endElement(String uri, String name, String qName) {
        if(name.equals("channel")) {
            isInsideChannel = false;
            try {
                temp.add(new XMLChannel(channelName, marketShare, startTime, stopTime, title));
            } catch(XMLParseException e) {
                System.err.println(e);
            }
        }
        current = null;
    }

    public void characters(char[] ch, int start, int length) {
        if(isInsideChannel && current != null) {
            String s = new String(ch, start, length).trim();
            if(current == Key.CHANNEL)
                channelName = s;
            else if(current == Key.MARKET_SHARE)
                marketShare = s;
            else if(current == Key.START_TIME)
                startTime = s;
            else if(current == Key.STOP_TIME)
                stopTime = s;
            else if(current == Key.TITLE)
                title = s;
        }
    }

}

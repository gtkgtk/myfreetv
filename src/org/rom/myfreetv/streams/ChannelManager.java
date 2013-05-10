package org.rom.myfreetv.streams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import org.rom.myfreetv.config.Config;
import org.rom.myfreetv.files.RecordFileManager;
import org.rom.myfreetv.process.ProgramManager;

public class ChannelManager {

   private final static String filename = "http://mafreebox.freebox.fr/freeboxtv/playlist.m3u";
   private final static String localFilename = "playlist.m3u";
   private static ChannelManager instance;
   // public final static NumberFormat formatter = new DecimalFormat("000");

   private List<Channel> channels;
   private List<Channel> channelsFavoris;
   private List<Channel> channelsRadios;

   public enum ChannelType { TV, FAVORIS, RADIO};
   public static ChannelType selectedType = ChannelType.TV;
   // private List<String> channelsHTML;

   private ChannelManager() {
      channels = new ArrayList<Channel>(250);
      channelsFavoris = new ArrayList<Channel>(250);
      channelsRadios = new ArrayList<Channel>(250);
   }

   public static ChannelManager getInstance() {
      if(instance == null)
         instance = new ChannelManager();
      return instance;
   }

   public static void initialize() throws ChannelsLoadException {
      // getInstance().channelsHTML.clear();
      getInstance().load();
      getInstance().loadFavoris();
      //getInstance().loadRadios();

      // for(Channel channel : getInstance().channels) {
      // StringBuffer sb = new StringBuffer("<html><font size=\"2\"><font
      // color=\"#3f3f3f\">");
      // sb.append(formatter.format(channel.getNum()));
      // sb.append("</font></font> <b>");
      // sb.append(channel.getName());
      // sb.append("</b></html>");
      // getInstance().channelsHTML.add(new String(sb));
      // }
   }

   public void addFavoris(Channel ch) {
      if(!channelsFavoris.contains(ch))
         channelsFavoris.add(ch);
   }
   public void removeFavoris(Channel ch) {
      if(channelsFavoris.contains(ch))
         channelsFavoris.remove(ch);
   }

   public void saveFavoris() {
       File f = Config.FAVORIS_FILE;

      if(f.exists()) {
         f.delete();
      }

      try {
         RandomAccessFile raf = new RandomAccessFile(f, "rw");
         //le num de la chaine peut changer,
         //on ne conserve que le nom...à voir
         raf.writeBytes("#EXTM3U\n");
         ListIterator<Channel> ite = channelsFavoris.listIterator();
         while(ite.hasNext()){
            Channel tmp = ite.next();
            raf.writeBytes(tmp.getSaveString()+"\n");//tmp.getName()+"\n");
         }
         raf.close();
      } catch(Exception e) {
         System.err.println("Probleme d'écriture du fichier favoris");
      }
   }
/*
   private void loadRadios() {
      File f = new File("radios.m3u");
      Scanner sc = null;

      if(!f.exists()) {
         System.err.println("Radios introuvables");
         return;
      }
      try {
         sc = new Scanner(f, "UTF-8");
      } catch (FileNotFoundException ex) {
         System.err.println("Probleme de lecture du fichier radios");
         return;
      }
      if(sc.hasNextLine()) {
         while(sc.hasNextLine()) {
            String s = sc.nextLine();
            if(s.startsWith("#EXTINF:")) {
               int num;
               String name;
               String url;
               int i = s.indexOf(",");
               int j = s.indexOf("-", i);
               if(i >= 0 && j >= 0) {
                  try {
                     num = Integer.parseInt(s.substring(i + 1, j - 1));
                     name = s.substring(j + 2);
                     url = sc.nextLine();
                     channelsRadios.add(new RadioChannel(num, name, url));
                  } catch(Throwable e) {}
               }
            }
         }
      }
   }
 */
   private void loadFavoris() {
      File f = Config.FAVORIS_FILE;//new File("favoris.m3u");
      Scanner sc = null;

      if(!f.exists()) {
//         System.err.println("Favoris introuvables");
         return;
      }
      try {
         sc = new Scanner(f, "UTF-8");
      } catch (FileNotFoundException ex) {
         System.err.println("Probleme de lecture du fichier favoris");
         return;
      }
      if(sc.hasNextLine()) {
         Channel tmp;
         while(sc.hasNextLine()) {
            String s = sc.nextLine();
            if(s.startsWith("#EXTINF:")) {
               int num;
               String name;
               String url;
               int i = s.indexOf(",");
               int j = s.indexOf("-", i);
               if(i >= 0 && j >= 0) {
                  try {
                     num = Integer.parseInt(s.substring(i + 1, j - 1));
                     name = s.substring(j + 2);
                     url = sc.nextLine();
                     if(num < 10000) {
                        //on verifie si la chaine favorite est tjs dispo
                        ListIterator<Channel> ite = channels.listIterator();
                        while(ite.hasNext()){
                           tmp = ite.next();
                           if(tmp.getName().compareTo(name)==0) {
                              channelsFavoris.add(tmp);
                              break;
                           }
                        }
                     } else //c'est une radio :)
                        channelsFavoris.add(new RadioChannel(num, name, url));
                  } catch(Throwable e) {}
               }
            }
         }
      }
   }
   private void load() throws ChannelsLoadException {
      /*
       String t =  "dshow://";
        //:dshow-vdev=\"Pinnacle%20WDM%20PCTV%20Video%20Capture\" :dshow-adev=\"\" :dshow-size=\"640X480\""; 
      channels.add(new Channel(666, "FreeTV", t));
      */
      Scanner sc = null;
      try {
         File file = new File(localFilename);
         if(file.exists())
            sc = new Scanner(file, "UTF-8");
         else {
            URL fileUrl = new URL(filename);
            URLConnection conn = fileUrl.openConnection();
            conn.setUseCaches(false);
            conn.connect();
            sc = new Scanner(conn.getInputStream(), "UTF-8");
         }
         if(sc.hasNextLine()) {
            while(sc.hasNextLine()) {
               String s = sc.nextLine();
               if(s.startsWith("#EXTINF:")) {
                  int num;
                  String name;
                  String url;
                  int i = s.indexOf(",");
                  int j = s.indexOf("-", i);
                  if(i >= 0 && j >= 0) {
                     try {
                        num = Integer.parseInt(s.substring(i + 1, j - 1));
                        name = s.substring(j + 2);
                        String opt = "";
                        url = sc.nextLine ();
                        while(url.startsWith ("#EXTVLCOPT:"))
                        {
                            opt = opt.concat (url.replaceAll ("#EXTVLCOPT:",";--") );
                            url = sc.nextLine();
                        }
                        url = url.replaceAll(" ", "%20");
                        url = url + opt;
                        if(num>10000)
                        {
                            //System.out.println ("Radio: "+num+" "+name+" "+url);
                            channelsRadios.add(new RadioChannel(num, name, url));
                        }
                        else
                            channels.add(new Channel(num, name, url));
                     } catch(Throwable e) {}
                  }
               }
            }
         }
      } catch(IOException e) {
         throw new ChannelsLoadException("Impossible de récupérer la liste des chaînes.");
      } finally {
         if(sc != null)
            sc.close();
         new Thread() {

            public void run() {
               ProgramManager.getInstance().load();
               RecordFileManager.getInstance().load();
            }
         }.start();
      }
   }

   // TV = liste par defaut
   public List<Channel> getChannels() {
      return getChannels(ChannelManager.selectedType);
   }
   public List<Channel> getChannels(ChannelType type) {
      switch(type) {
         case TV :
            return channels;
         case FAVORIS :
            return channelsFavoris;
         case RADIO :
            return channelsRadios;
         default:
            return channels;
      }
   }
   public void changeChannel(ChannelType type) {
      ChannelManager.selectedType = type;
   }
   // public List<String> getChannelsHTML() {
   // return channelsHTML;
   // }

   public Channel getChannel(int num) {
      List<Channel> tmpChannels = getChannels();

      Channel ch = null;
      int i = 0;
      while(i < tmpChannels.size() && ch == null) {
         if(tmpChannels.get(i).getNum() == num)
            ch = tmpChannels.get(i);
         i++;
      }
      return ch;
   }

   public Channel getChannel(String name) {
      List<Channel> tmpChannels = getChannels();

      Channel ch = null;
      int i = 0;
      while(i < tmpChannels.size() && ch == null) {
         if(tmpChannels.get(i).getName().equals(name))
            ch = tmpChannels.get(i);
         i++;
      }
      if(ch == null) {
         i = 0;
         while(i < tmpChannels.size() && ch == null) {
            if(tmpChannels.get(i).getName().startsWith(name))
               ch = tmpChannels.get(i);
            i++;
         }
      }
      return ch;
   }

}

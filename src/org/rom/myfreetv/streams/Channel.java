package org.rom.myfreetv.streams;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Channel implements Comparable<Channel>, Playable, Recordable {

   public final static NumberFormat formatter = new DecimalFormat("000");

   private int num;
   private String name;
   private String url;
   private String html;

   public Channel(int num, String name, String url) {
      this.num = num;
      this.name = name;
      this.url = url;
      StringBuffer sb = new StringBuffer("<html><font size=\"2\"><font color=\"#3f3f3f\">");
      sb.append(formatter.format(num));
      sb.append("</font></font> <b>");
      sb.append(name);
      sb.append("</b></html>");
      // StringBuffer sb = new StringBuffer("<html><font size=\"3\"><font
      // color=\"#3f3f3f\"><b>");
      // sb.append(formatter.format(num));
      // sb.append("</b></font></font> ");
      // sb.append(name);
      // sb.append("</html>");
      html = new String(sb);
   }

   public int getNum() {
      return num;
   }

   public String getName() {
      return name;
   }

   public String getUrl() {
      return url;
   }

   public String getHTML() {
      return html;
   }

   public String getStringNum() {
      return formatter.format(num);
   }

   public int compareTo(Channel other) {
      int diff = num - other.num;
      if(diff == 0)
         diff = name.compareTo(other.name);
      return diff;
   }

   public boolean equals(Object other) {
      String urlOther = null;
      if(other instanceof TimeShiftFileIn)
         urlOther = ((FileIn) other).getChannel().getUrl();
      else if(other instanceof Channel || other instanceof FileIn)
         urlOther = ((Channel) other).getUrl();
      return url.equals(urlOther);
   }

   public Channel getChannel() {
      return this;
   }

   public boolean canPause() {
      return false;
   }

   public String toString() {
      return getStringNum() + " " + name;
   }

   public String getSaveString() {
     return "#EXTINF:0,"+num+" "+"-"+" "+name+"\n"+url;
   }

}

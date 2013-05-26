package org.rom.myfreetv.guidetv;

import java.awt.Image;
import java.io.*;
import java.net.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.filter.*;
import org.rom.myfreetv.config.Config;
import org.rom.myfreetv.guidetv.Emission;
import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.ChannelManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.GregorianCalendar;
import java.util.Calendar;

import javax.swing.ImageIcon;

public class Database {

	   static org.jdom.Document document;
	   static org.jdom.Document ChannelDoc;
	   static Element racine;
	   static Element racineChannels;
	   private boolean FetchedXML=false;
	   private Calendar DbDate;
	   

	   public Database()
	   {
		  SAXBuilder sxbChannels = new SAXBuilder();
		  try
		  {
		     //On crée un nouveau document JDOM avec en argument le fichier XML
		     //Le parsing est terminé ;)
			 ChannelDoc = sxbChannels.build(new File("dist/ListeChaines.xml"));
		  }catch(Exception e){
			  try
			  {
				 ChannelDoc = sxbChannels.build(new File("./ListeChaines.xml"));
			  }catch(Exception e1){};
			  
		  };
	      racineChannels = ChannelDoc.getRootElement();
		  
	      DbDate = getLastUpdate();
	      //Temporaire : affiche les channels de tvguide.xml
	      //afficheALL(1,"20130207000000", "20130208235959");
	   }
	   
	   public Calendar getLastUpdate() {
		   // verify if database is new enough
   
		   if (FetchedXML==false)
		   {
		      try
		      {
			      Calendar currentTime = new GregorianCalendar();
			      Calendar dbdate = new GregorianCalendar();
			      long currentdateinms=currentTime.getTimeInMillis();

			      dbdate.setTimeInMillis(GetDbDate());
		    	  // 259200000L = 1000ms*60sec*60min*24hours*3days
		    	  if (currentdateinms > GetDbDate()+259200000L)
		    	  {
		    		  System.out.println("current time exceeds from more than 3 days the database date ; current="+currentTime.getTime()+", dbdate="+dbdate.getTime());
				      document = GetdbFile();
				      //On initialise un nouvel élément racine avec l'élément racine du document.
				      racine = document.getRootElement();

				      try
				      {
				    	  DbDate = StoreDbDate();
				      }
				      catch(Exception e){
				    	  DbDate=currentTime;
				      }
		    	  }
		    	  else
		    	  {
					  SAXBuilder sxbTVGuide = new SAXBuilder();
		    		  System.out.println("database date is still under 3 days of peremption ; current="+currentTime.getTime()+", dbdate="+dbdate.getTime());
				      try
				      {
				    	  document = sxbTVGuide.build(new File(Config.TVGUIDE_FILENAME));
				      }
					  catch(Exception e){
			    		  System.out.println("no Database in Path, try to retrieve from server");
				    	  document = GetdbFile();
					  }
				      //On initialise un nouvel élément racine avec l'élément racine du document.
				      racine = document.getRootElement();

		    	  }
		      }
		      catch(Exception e){
	       		    System.out.println("Bad URL ("+Config.getInstance().getKazerPath().getUrl()+") for tvguide database :\nEnter a valid kazer url in Config panel shall be http://www.kazer.org/tvguide.xml?u=xxxxxxx");

		      }
	
		      FetchedXML=true;
		      
		   }
		   return DbDate;
	    }
	   
	   private org.jdom.Document GetdbFile () throws Exception
	   {
			SAXBuilder sxbTVGuide = new SAXBuilder();
		    try {
			   URL adresse = new URL(Config.getInstance().getKazerPath().getUrl());
			   InputStream stream = adresse.openStream();
		       document = sxbTVGuide.build(stream);
		       // store the new file in the filesystem
	            XMLOutputter out = new XMLOutputter();
		        try
		        {
		        	java.io.FileWriter writer = new java.io.FileWriter(Config.TVGUIDE_FILENAME);
		            out.output(document, writer);
		            writer.flush();
		            writer.close();
		        }
		        catch (Exception e)
		        {
		        	System.out.println("TVGUIDE creation file error");
		        }
			} 
			catch (MalformedURLException e) {
       		    System.out.println("Bad URL ("+Config.getInstance().getKazerPath().getUrl()+") for tvguide database :\nEnter a valid kazer url in Config panel shall be http://www.kazer.org/tvguide.xml?u=xxxxxxx");
       		}
		    return document;
	   }

	   private Calendar StoreDbDate() throws Exception
	   {
	      //On crée une List contenant tous les noeuds "etudiant" de l'Element racine
	      List listChannels = racine.getChildren("programme");
	      Calendar cal = new GregorianCalendar();

	      //On crée un Iterator sur notre liste
	      Iterator i = listChannels.iterator();
	      if(i.hasNext())
	      {
	         //On recrée l'Element courant à chaque tour de boucle afin de
	         //pouvoir utiliser les méthodes propres aux Element comme :
	         //sélectionner un nœud fils, modifier du texte, etc...
	         Element courant = (Element)i.next();
		     cal=ParseDateString(courant.getAttributeValue("start"));
		     
		     // sauvegarder la date dans la config utilisateur
		     Config.getInstance().setKazerFileDate(String.valueOf(cal.getTimeInMillis()));
		     Config.getInstance().saveProperties();
		     
		     // ajouter une balise update dans le document avec la date du fichier guide
/*	         if(racineChannels.getChild("update")!=null)
	         {
	        	 // la balise existe déjà
	        	 Element upd=racineChannels.getChild("update");
	        	 upd.setText(String.valueOf(cal.getTimeInMillis()));
	         }
	         else
	         {
	        	 // sinon en créer une
	     		 Element upd = new Element("update");
	        	 upd.setText(String.valueOf(cal.getTimeInMillis()));
	    		racineChannels.addContent(upd);
	         }
	         // sauver le fichier modifié
	         XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
	         try
	         {
	        	 sortie.output(ChannelDoc, new FileOutputStream("dist/ListeChaines.xml"));
	         }
	         catch (Exception e)
	         {
	        	 sortie.output(ChannelDoc, new FileOutputStream("./ListeChaines.xml"));
	         } */
	      }
	      return cal;
	   }

	   private long GetDbDate() throws Exception
	   {
	      long dbdateinms;
	      String storedDate=Config.getInstance().getKazerFileDate();
	      dbdateinms=Long.valueOf(storedDate);
/*	      
	      if(racineChannels.getChild("update")!=null)
	      {
	       	 // la balise existe déjà
	       	 Element upd=racineChannels.getChild("update");
	       	 String storedDate=upd.getText();
	       	 dbdateinms=Long.valueOf(storedDate);
	      }
	      else
	      {
	    	  dbdateinms=0L;
	      } */
    	  //System.out.println("dbdateinms="+dbdateinms);
		  return dbdateinms;
	   }

	   //Ajouter cette méthodes à la classe JDOM2
	   static void afficheALL(int channel, String StartDate, String EndDate)
	   {
		  String KazerChannel = GetKazerChannel(channel);
		  
	      //On crée une List contenant tous les noeuds "etudiant" de l'Element racine
	      List listChannels = racine.getChildren("programme");

	      //On crée un Iterator sur notre liste
	      Iterator i = listChannels.iterator();
	      while(i.hasNext())
	      {
	         //On recrée l'Element courant à chaque tour de boucle afin de
	         //pouvoir utiliser les méthodes propres aux Element comme :
	         //sélectionner un nœud fils, modifier du texte, etc...
	         Element courant = (Element)i.next();
	         if (courant.getAttributeValue("channel").equals(KazerChannel))
	         {
	        	 String subtitle = new String("No Subtitle");
	        	 ContentFilter filtre = new ContentFilter(false);
	        	 filtre.setCommentVisible(true);
	        	 String showview=new String("No code");
		         //On affiche le nom de l’élément courant
	        	 List children = courant.getContent(filtre);
	        	 Iterator iterator = children.iterator();
	        	 if(iterator.hasNext())
	        	 {
	        	     Comment comment = (Comment) iterator.next();
			         showview=comment.getText().substring(7);
	        	 }
	        	 //if (courant.getAttributeValue("sub-title"))
	        		 subtitle=courant.getChildTextNormalize("sub-title");
	        	 if (	(courant.getAttributeValue("start").compareTo(StartDate)>=0)&&
	        			(courant.getAttributeValue("start").compareTo(EndDate)<=0))
	        	 {
	        		 System.out.println("Titre :\""+courant.getChildTextNormalize("title")+
     		 						"\"\nsous-titre \""+subtitle+
		        		 			"\"\nDescription:\n"+courant.getChildText("desc")+
     		 						"\n====>commence à "+courant.getAttributeValue("start")+
		        		 			" et finit à "+courant.getAttributeValue("stop")+
		        		 			"\nCode showview = "+showview+"\n");
	        		 //Calendar cal=ParseDateString(courant.getAttributeValue("start"));
	        		 //System.out.println("kazername of channel 5 is "+GetKazerChannel(5));
	        		 //System.out.println("kazername of channel 1 is "+GetKazerChannel(1));
	        	 }
	         }
	      }
	   }

	   
	   //Ajouter cette méthodes à la classe JDOM2
	   static String GetKazerChannel(int iFreeChannel)
	   {
		   int entier;
	      //On crée une List contenant tous les noeuds "etudiant" de l'Element racine
	      List listChannels = racineChannels.getChildren("channel");

	      //On crée un Iterator sur notre liste
	      Iterator i = listChannels.iterator();
	      while(i.hasNext())
	      {
	         //On recrée l'Element courant à chaque tour de boucle afin de
	         //pouvoir utiliser les méthodes propres aux Element comme :
	         //sélectionner un nœud fils, modifier du texte, etc...
	         Element courant = (Element)i.next();
	         //On affiche le nom de l’élément courant
			 Integer monnombre=new Integer(courant.getAttributeValue("free-id"));
	         entier=monnombre.intValue();
	         if (entier==iFreeChannel)
	        	 return courant.getChild("kazer").getAttributeValue("id");
	         //System.out.println ("la chaine "+courant.getChild("display-name").getText()+" a pour numéro "+entier+" et nom "+courant.getChild("kazer").getAttributeValue("id"));
	      }
	      return "Not Found";
	   }
	   
	   static Calendar ParseDateString(String iString)
	   {
		   Calendar calendar = new GregorianCalendar();
		   Integer monnombre=new Integer(iString.substring(0, 4));
	       int entier=monnombre.intValue();

		   //System.out.println ("Year="+iString.substring(0, 4));
		   calendar.set(Calendar.YEAR, entier);
		   
		   monnombre=new Integer(iString.substring(4, 6));
		   entier=monnombre.intValue();
		   //System.out.println ("Month="+iString.substring(4, 6));
		   calendar.set(Calendar.MONTH, entier-1);
		   
		   monnombre=new Integer(iString.substring(6, 8));
		   entier=monnombre.intValue();
		   //System.out.println ("Day="+iString.substring(6, 8));
		   calendar.set(Calendar.DAY_OF_MONTH, entier);

		   monnombre=new Integer(iString.substring(8, 10));
		   entier=monnombre.intValue();
		   if (entier>=12)
			   calendar.set(Calendar.HOUR, entier-12);
		   else
			   calendar.set(Calendar.HOUR, entier);
		   calendar.set(Calendar.HOUR_OF_DAY, entier);
		   //System.out.println ("Hour="+iString.substring(8, 10));

		   monnombre=new Integer(iString.substring(10, 12));
		   entier=monnombre.intValue();
		   calendar.set(Calendar.MINUTE, entier);
		   //System.out.println ("Min="+iString.substring(10, 12));

		   monnombre=new Integer(iString.substring(12, 14));
		   entier=monnombre.intValue();
		   calendar.set(Calendar.SECOND, entier);
		   //System.out.println ("Sec="+iString.substring(12, 14));
		   return calendar;
	   }
	   
	   
	  
	    public List<Emission> getEmissions(Channel channel, Calendar startInterval, Calendar stopInterval)  {
	    	// First get the kazer channelname out of channel
	    	String kazerChannel= GetKazerChannel(channel.getNum());
	    	String StartDate=new String("20130201");
	    	String EndDate=new String("29990101");
	    	// set the start and end dates for comparison
            List<Emission> list = new ArrayList<Emission>();
            if(startInterval != null || stopInterval != null) {
                if(startInterval != null) {
                	String filler1=new String(), filler2 = new String() ;
                   	if ((startInterval.get(Calendar.MONTH)+1)<10) filler1="0";
                   	if ((startInterval.get(Calendar.DAY_OF_MONTH))<10) filler2="0";
                    StartDate=String.valueOf(startInterval.get(Calendar.YEAR))+filler1+
                    		String.valueOf(startInterval.get(Calendar.MONTH)+1)+filler2+
                    		String.valueOf(startInterval.get(Calendar.DAY_OF_MONTH))+
                    		"000000";
                }
                if(stopInterval != null) {
                	String filler1=new String(), filler2 = new String() ;
                  	if ((stopInterval.get(Calendar.MONTH)+1)<10) filler1="0";
                   	if ((stopInterval.get(Calendar.DAY_OF_MONTH))<10) filler2="0";
                    EndDate=String.valueOf(stopInterval.get(Calendar.YEAR))+filler1+
                    		String.valueOf(stopInterval.get(Calendar.MONTH)+1)+filler2+
                    		String.valueOf(stopInterval.get(Calendar.DAY_OF_MONTH))+"000000";
                }
            }
            // finally compute the list
  	      //On crée une List contenant tous les noeuds "programme" de l'Element racine
  	      List listChannels = racine.getChildren("programme");
  	      //System.out.println("start at "+StartDate+" : end at "+EndDate);

  	      //On crée un Iterator sur notre liste
  	      Iterator i = listChannels.iterator();
  	      while(i.hasNext())
  	      {
  	         //On recrée l'Element courant à chaque tour de boucle afin de
  	         //pouvoir utiliser les méthodes propres aux Element comme :
  	         //sélectionner un nœud fils, modifier du texte, etc...
  	         Element courant = (Element)i.next();
  	         if (courant.getAttributeValue("channel").equals(kazerChannel))
  	         {
  	        	 String subtitle = new String("No Subtitle");
  	        	 ContentFilter filtre = new ContentFilter(false);
  	        	 filtre.setCommentVisible(true);
  	        	 String showview=new String("No code");
  		         //On affiche le nom de l’élément courant
  	        	 List children = courant.getContent(filtre);
  	        	 Iterator iterator = children.iterator();
  	        	 if(iterator.hasNext())
  	        	 {
  	        	     Comment comment = (Comment) iterator.next();
  			         showview=comment.getText().substring(7);
  	        	 }
  	        	 //if (courant.getAttributeValue("sub-title"))
  	        		 subtitle=courant.getChildTextNormalize("sub-title");
  	        	 if (	(courant.getAttributeValue("start").compareTo(StartDate)>=0)&&
  	        			(courant.getAttributeValue("start").compareTo(EndDate)<=0))
  	        	 {
  	        		//Calendar cal=ParseDateString(courant.getAttributeValue("start"));

  	                //ImageIcon icon = null;
  	                Image image = null;

  	                channel = ChannelManager.getInstance().getChannel(channel.getNum());
  	                if(channel == null)
  	                    channel = new Channel(0, null, null);
  	                Calendar startDate = ParseDateString(courant.getAttributeValue("start"));
  	                Calendar endDate = ParseDateString(courant.getAttributeValue("stop"));
  	                list.add(new Emission(channel.getNum(), channel, startDate, endDate, courant.getChildTextNormalize("title"), subtitle, courant.getChildTextNormalize("category"), courant.getChildText("desc"), showview, image));
  	        	 }
  	         }
  	      }

	      Collections.sort(list);
	      return list;
	  }

	  public List<Emission> getEmissions(Channel channel, Calendar day) {
	        Calendar startDay = new GregorianCalendar(day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH));
	        Calendar stopDay = new GregorianCalendar();
	        stopDay.setTimeInMillis(startDay.getTimeInMillis());
	        stopDay.add(Calendar.DAY_OF_MONTH, 1);
	        return getEmissions(channel, startDay, stopDay);
	  }

}

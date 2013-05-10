package org.rom.myfreetv.files;

import java.awt.Component;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.rom.myfreetv.config.Config;
import org.rom.myfreetv.process.JobManager;
import org.rom.myfreetv.process.ProgramManager;
import org.rom.myfreetv.streams.Channel;
import org.rom.myfreetv.streams.RadioChannel;
import org.rom.myfreetv.streams.Recordable;
import org.rom.myfreetv.view.MyFreeTV;

public class FileUtils {

   public static enum Type {
      FILE, DIRECTORY
   }

   private static NumberFormat formatter = new DecimalFormat("00");

   public static boolean isMpeg(String filename) {
      filename = filename.toLowerCase();
      return filename.endsWith(".mpg") || filename.endsWith(".mpeg");
   }
   public static boolean isMp3(String filename) {
      filename = filename.toLowerCase();
      return filename.endsWith(".mp3");
   }

   public static String getDestination(Component parent, Recordable recordable) {
      if(Config.getInstance().getAutoPath().isEnabled())
         return Config.getInstance().getAutoPath().getUrl() + File.separatorChar + generateAutoFilename(Calendar.getInstance(), recordable);
      else
         return chooseDestination(parent, Config.getInstance().getCurrentPath(), Type.FILE, recordable);
   }

   public static String chooseOpen(Component parent, String currentPath, String title, Type type) {
      String filename = null;
      File file = null;
      JFileChooser chooser = new JFileChooser();
//        if(MyFreeTV.getInstance().getDecoration()) {
//            chooser.setDefaultLookAndFeelDecorated(false);
//            chooser.getRootPane().setWindowDecorationStyle(javax.swing.JRootPane.FILE_CHOOSER_DIALOG);
//            chooser.setUndecorated(true);
//        }
      if(currentPath != null && currentPath.length() > 0)
         chooser.setCurrentDirectory(new File(currentPath));
      else {
         String cur = Config.getInstance().getCurrentPath();
         if(cur != null && cur.length() > 0)
            chooser.setCurrentDirectory(new File(cur));
      }
      if(title != null)
         chooser.setDialogTitle(title);
      chooser.setFileSelectionMode(type == Type.DIRECTORY ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_AND_DIRECTORIES);
      if(chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
         file = chooser.getSelectedFile();
         if(file.isDirectory())
            Config.getInstance().setCurrentPath(file.getAbsolutePath());
         else
            Config.getInstance().setCurrentPath(file.getParentFile().getAbsolutePath());
         filename = file.getAbsolutePath();
      }
      return filename;
   }

   public static String chooseDestination(Component parent, String currentPath, Type type, boolean autoOverwrite,  Recordable recordable) {
      boolean redo;
      File file = null;
      if(type == Type.FILE) {
         do {
            file = null;
            JFileChooser chooser = new JFileChooser();
            if(currentPath != null)
               chooser.setCurrentDirectory(new File(currentPath));
            MyFileFilter filter;
            if(recordable instanceof RadioChannel)
               filter = new MyFileFilter(new String[] { "mp3" }, "Fichiers MP3");
            else
               filter = new MyFileFilter(new String[] { "mpg", "mpeg" }, "Fichiers MPEG");
            chooser.addChoosableFileFilter(filter);
            String filename = null;
            if(chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
               file = chooser.getSelectedFile();
               filename = file.getAbsolutePath();
               if(recordable instanceof RadioChannel)
                  if(!FileUtils.isMp3(filename)) {
                  filename += ".mp3";
                  file = new File(filename);
                  } else {
                  if(!FileUtils.isMpeg(filename)) {
                     filename += ".mpg";
                     file = new File(filename);
                  }}
               String newPath = file.getParent();
               if(newPath != null)
                  Config.getInstance().setCurrentPath(newPath);
               if(JobManager.getInstance().isUsed(file) || ProgramManager.getInstance().isUsed(file)) {
                  JOptionPane.showMessageDialog(parent, "Ce fichier est déjà utilisé pour un autre enregistrement.", "Erreur", JOptionPane.ERROR_MESSAGE);
                  redo = true;
               } else if(!autoOverwrite && file != null && file.exists() && JOptionPane.showConfirmDialog(parent, "Voulez-vous vraiment écraser ce fichier?", "Confirmation", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
                  redo = true;
               else
                  redo = false;
            } else
               redo = false;
         } while(redo);
      } else if(type == Type.DIRECTORY) {
         JFileChooser chooser = new JFileChooser();
         chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
         if(currentPath != null)
            chooser.setCurrentDirectory(new File(currentPath));
         if(chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
            file = chooser.getSelectedFile();
      }
      String filename = null;
      if(file != null)
         filename = file.getAbsolutePath();
      return filename;
   }

   public static String chooseDestination(Component parent, String currentPath, Type type,  Recordable recordable) {
      return chooseDestination(parent, currentPath, type, false, recordable);
   }

   public static String generateInexistantFilename(String filename) {
      int lastIndex = filename.lastIndexOf('.');
      String ext = "";
      if(lastIndex >= 0) {
         ext = filename.substring(lastIndex);
         filename = filename.substring(0, lastIndex);
      }
      String name = filename + ext;
      File file = new File(name);
      int i = 1;
      while(file.exists()) {
         name = filename + "-" + i + ext;
         file = new File(name);
         i++;
      }
      return name;
   }

   public static String generateAutoFilename(Calendar cal, Recordable recordable) {
      if(cal == null)
         cal = Calendar.getInstance();
      StringBuffer buf = new StringBuffer(/*
       * Config.getInstance().getAutoPath() +
       * File.separatorChar
       */);
      buf.append(MyFreeTV.name);
      buf.append("-");
      buf.append(cal.get(Calendar.YEAR));
      buf.append(formatter.format(cal.get(Calendar.MONTH) + 1));
      buf.append(formatter.format(cal.get(Calendar.DAY_OF_MONTH)));
      buf.append("-");
      buf.append(formatter.format(cal.get(Calendar.HOUR_OF_DAY)));
      buf.append(formatter.format(cal.get(Calendar.MINUTE)));
      buf.append(formatter.format(cal.get(Calendar.SECOND)));
      if(recordable instanceof Channel) {
         buf.append("_");
         buf.append(Channel.formatter.format(((Channel) recordable).getNum()));
      }
      if(recordable instanceof RadioChannel)
         buf.append(".mp3");
      else
         buf.append(".mpg");
      // String filename = generateInexistantFilename(new String(buf));
      return new String(buf);
   }

}

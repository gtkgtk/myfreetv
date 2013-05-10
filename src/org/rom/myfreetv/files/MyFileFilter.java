package org.rom.myfreetv.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

public class MyFileFilter extends FileFilter {

    private List<String> extensions;
    private String description;
    private String fullDescription;

    public MyFileFilter(String[] exts, String description) {
        extensions = new ArrayList<String>(exts.length);
        for(String s : exts)
            extensions.add(s);
        this.description = description;
    }

    public boolean accept(File f) {
        boolean accept = false;
        if(f != null) {
            if(f.isDirectory())
                accept = true;
            else {
                String ext = getExtension(f);
                if(ext != null && extensions.contains(ext))
                    accept = true;
            }
        }
        return accept;
    }

    public static String getExtension(File f) {
        String ext = null;
        if(f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if(i > 0 && i < filename.length() - 1)
                ext = filename.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static String getPathOnly(File f) {
        String path = f.getAbsolutePath();
        int index = path.lastIndexOf('\\');
        if(index < 0)
            return null;
        else if(index == path.length() - 1)
            return path;
        else
            return path.substring(0, index + 1);
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public String getDescription() {
        if(fullDescription == null) {
            StringBuffer descr = new StringBuffer();
            int i = 0;
            for(String s : extensions) {
                descr.append("*.").append(s);
                if(i++ < extensions.size() - 1)
                    descr.append(", ");
            }
            descr.append(" (").append(description).append(")");
            fullDescription = new String(descr);
        }
        return fullDescription;
    }

}

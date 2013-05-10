package org.rom.myfreetv.view;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.rom.myfreetv.streams.Stream;

public class LogoViewer extends JLabel {

    private ImageIcon image;
    // private VolatileImage vi;
    // private Graphics2D offScreen;
    private int width, height;

    LogoViewer(int width, int height) {
        super();
        setHorizontalAlignment(CENTER);
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
    }

    // public void setImage(ImageIcon image) {
    // if(image != null) {
    // int w = width;
    // int h = height;
    // // int w = (getWidth() == 0) ? width : getWidth();
    // // int h = (getHeight() == 0) ? height : getHeight();
    // int dimX = image.getIconWidth();
    // int dimY = image.getIconHeight();
    // double rapportX = (double) dimX / w;
    // double rapportY = (double) dimY / h;
    // if(rapportX > 1 || rapportY > 1) {
    // double maxRapport = Math.max(rapportX, rapportY);
    // dimX /= maxRapport;
    // dimY /= maxRapport;
    // image = new ImageIcon(image.getImage().getScaledInstance(dimX, dimY,
    // Image.SCALE_AREA_AVERAGING));
    // }
    // }
    // setScaledImage(image);
    // this.image = image;
    // repaint();
    // }
    // public void setScaledImage(ImageIcon image) {
    // setIcon(image);
    // }

    public void setLogo(Stream stream) {
        if(stream == null)
            setIcon(null);
        else
            setIcon(ImageManager.getInstance().getScaledLogoImageIcon(stream, (getWidth() == 0) ? width : getWidth(), (getHeight() == 0) ? height : getHeight()));
    }

    // public void paintComponent(Graphics g) {
    // super.paintComponent(g);
    // Graphics2D g2D = (Graphics2D)g;
    // /* création d'une VolatileImage avec son Graphics offScreen. */
    // if(vi == null || vi.validate(getGraphicsConfiguration()) ==
    // VolatileImage.IMAGE_INCOMPATIBLE) {
    // /*VolatileImage*/ vi = createVolatileImage(getWidth(),getHeight());
    // /*Graphics2D*/ offScreen = (Graphics2D)vi.createGraphics();
    // /** Désactivation de l'anti-aliasing */
    // offScreen.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    // RenderingHints.VALUE_ANTIALIAS_OFF);
    // offScreen.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
    // RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    // /** Demande de rendu rapide */
    // offScreen.setRenderingHint(RenderingHints.KEY_RENDERING,
    // RenderingHints.VALUE_RENDER_SPEED);
    // offScreen.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
    // RenderingHints.VALUE_COLOR_RENDER_SPEED);
    // offScreen.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
    // RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
    // offScreen.setRenderingHint(RenderingHints.KEY_DITHERING,
    // RenderingHints.VALUE_DITHER_DISABLE);
    // }
    //        
    // int compHeight = getHeight();
    // int compWidth = getWidth();
    // offScreen.setBackground(getBackground());
    // offScreen.clearRect(0,0,compWidth,compHeight);
    //        
    // if(image != null) {
    // int iconHeight = image.getIconHeight();
    // int iconWidth = image.getIconWidth();
    // int posX = (compWidth - iconWidth) / 2;
    // int posY = (compHeight - iconHeight) / 2;
    // offScreen.drawImage(image.getImage(),posX,posY,null);
    // }
    //        
    // g.drawImage(vi,0,0,null);
    // }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aroueterra.EquipmentFramework.UI.custom;

import aroueterra.EquipmentFramework.UI.inventory.Inventory;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 * @author aroue
 */
public class ImagePanel extends JPanel {

    private Image image;
    private String resource;

    public ImagePanel() {
        this.image = null;
        this.resource = "";
        setPreferredSize(new Dimension(32, 32));
        setMinimumSize(new Dimension(32, 32));
    }

    public ImagePanel(Image image) {
        //this.image = scaleImage(image);
        this.image = image;

    }

    public ImagePanel(Image image, String resource) {
        this.image = image;
        this.resource = resource;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //g2.drawImage(image, 0, 0, this);
        g.drawImage(getImage(), 0, 0, getWidth(), getHeight(), this);
        //g.drawImage(getImage().getScaledInstance(getWidth(), -1, Image.SCALE_SMOOTH), getWidth(), getHeight(), this);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String path) {
        BufferedImage newImage = null;
        try {
            newImage = ImageIO.read(getClass().getResource(path));
        } catch (IOException ex) {
            Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.image = newImage;
        this.setOpaque(false);
        this.revalidate();
        this.repaint();
    }

    private Image scaleImage(Image rawImage) {
        Image scaledImage = null;
        System.out.println("Scaling");
        try {
            int rawImageWidth = rawImage.getWidth(this);
            int rawImageHeight = rawImage.getHeight(this);
            int paneWidth = (int) getWidth();
            int paneHeight = (int) getHeight();
            System.out.println("Image W = " + rawImageWidth
                    + ", H = " + rawImageHeight
                    + "; Pane W = " + paneWidth
                    + ", H = " + paneHeight);
            // preserve the original ratio
            float widthRatio = (float) rawImageWidth / (float) paneWidth;
            float heightRatio = (float) rawImageHeight / (float) paneHeight;
            int widthFactor = -1;
            int heightFactor = -1;
            if ((widthRatio > heightRatio) && (widthRatio > 1.0)) {
                widthFactor = paneWidth;
            } else if ((heightRatio > widthRatio) && (heightRatio > 1.0)) {
                heightFactor = paneHeight;
            }
            System.out.println("widthRatio = "
                    + String.format("%.3f", widthRatio)
                    + ", heightRatio = "
                    + String.format("%.3f", heightRatio));
            System.out.println("widthFactor = " + widthFactor
                    + ", heightFactor = " + heightFactor);
            if ((widthFactor < 0) && (heightFactor < 0)) {
                scaledImage = rawImage;
            } else {
                scaledImage = rawImage.getScaledInstance(widthFactor, heightFactor,
                        Image.SCALE_SMOOTH);
                // load the new image, 'getScaledInstance' loads asynchronously
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(scaledImage, 0);
                tracker.waitForID(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (scaledImage);
    }
}

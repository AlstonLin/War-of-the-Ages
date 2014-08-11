import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * A popup that will deliver a message.
 * 
 * @author Alston Lin
 * @version Beta 2.1
 */
public class Popup extends Actor
{
    /**
     * Creates a new popup message to the user.
     * 
     * @param xSize     The size of the x side of the popup
     * @param ySize     The size of the y side of the popup
     * @param message   The message to display
     */
    public Popup(int xSize, int ySize, String message) {
        GreenfootImage image = new GreenfootImage(xSize, ySize);
        image.setColor(Color.BLACK);
        image.fillRect(0, 0, xSize, ySize);
        image.setColor(Color.WHITE);
        image.drawString(message, 10, 25);
        setImage(image);
    }

    /**
     * Destroys the popup. 
     */
    public void destroy() {
        getWorld().removeObject(this);
    }    
}

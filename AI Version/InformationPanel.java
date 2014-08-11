import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Font;

/**
 * A panel that will display the information given.
 * 
 * @author Alston Lin
 * @version Beta 2.1
 */
public class InformationPanel extends Actor
{
    //Create objects
    private static final GreenfootImage ORIGINAL;
    private static final Font FONT;

    static {
        ORIGINAL = new GreenfootImage("InfoPanel.png");
        FONT = new Font("Serif", Font.PLAIN, 18);
    }

    /**
     * Creates a new InformationPanel, sets the image
     */
    public InformationPanel() {
        setImage(ORIGINAL);
    }

    /**
     * Displays the given Strings onto the informationPanel. 
     * 
     * @param toDisplay     The text that will be displayed
     */
    public void display(String toDisplay) {
        GreenfootImage image = new GreenfootImage(ORIGINAL); //Clones the original
        setImage(image); //Sets the image as the clone
        image.setFont(FONT);
        image.drawString(toDisplay, 20, 25);
    }
}

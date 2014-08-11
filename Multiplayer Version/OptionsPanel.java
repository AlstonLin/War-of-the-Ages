import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The panel where the player can choose to save image or go back to the menu.
 * 
 * @author Alstion Lin
 * @version 1.0
 */
public class OptionsPanel extends Actor
{
    //Image of the Options Panel
    GreenfootImage image = new GreenfootImage("GameOptionsPanel.png");

    /**
     * Sets the image of this panel
     */
    public OptionsPanel() {
        setImage(image);
    }
}

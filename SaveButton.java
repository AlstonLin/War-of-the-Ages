import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Saves the current game using Serialization when clicked.
 * 
 * @author Alston Lin, Koko Deng
 * @version Beta 2.1
 */
public class SaveButton extends Button
{   
    /**
     * Sets the image for ZoomOutButton object
     */
    public SaveButton() {
        super(new GreenfootImage("SaveButton.png"));
    }

    /**
     * Sets the world
     */
    public void click() {
        ((Game)getWorld()).save();
    }   

    /**
     * Returns the information of this button
     * 
     * @return      The information of this button
     */
    public String getHoverInfo() {
        return "Save Game";
    }
}

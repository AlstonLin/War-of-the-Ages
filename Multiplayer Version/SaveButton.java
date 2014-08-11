import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import javax.swing.JOptionPane;

/**
 * Saves the current game using Serialization when clicked.
 * 
 * @author Alston Lin, Koko Deng
 * @version 1.0
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
        JOptionPane pane = new JOptionPane();
        pane.showMessageDialog(null, "Game Saved");
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

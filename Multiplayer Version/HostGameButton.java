import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Host game button, when clicked hosts a new game
 * 
 * @author Alston Lin
 * @version 1.0
 */
public class HostGameButton extends Button
{    
    /**
     * Sets the image for this object
     */
    public HostGameButton() {
        super(new GreenfootImage("MultiplayerHostButton.png"));
    }

    /**
     * Hosts a new room
     */
    public void click() {
        Greenfoot.setWorld(new Room(true, null));
    }

    /**
     * Returns the information of this button
     * 
     * @return      The information of this button
     */
    public String getHoverInfo() {
        return "Host a game";
    }
}

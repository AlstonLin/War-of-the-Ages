import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Creates a new Game
 * 
 * @author Alston Lin
 * @version 1.0
 */
public class NewGameButton extends Button
{   
    /**
     * Initializes button through setting its image
     */
    public NewGameButton() {
        super(new GreenfootImage("NewGameButton.png"));

    }

    /**
     * Sets the World when clicked
     */
    public void click() {
        Greenfoot.setWorld(new Game());
    }

    /**
     * Returns information of this object
     * 
     * @return      Information to display
     */
    public String getHoverInfo() {
        return "Create a new game";
    }
}

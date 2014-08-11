import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Button when clicked, goes back to the Menu
 * 
 * @author Jesmin Hondell, Alston Lin, Koko Deng
 * @version 1.0
 */
public class MenuButton extends Button
{   
    /**
     * Initializes button through setting its image
     */
    public MenuButton() {
        super(new GreenfootImage("MenuButton.png"));
    }

    /**
     * Sets the world back to the title screen
     */
    public void click() {
        Greenfoot.setWorld(new TitleScreen());
    }

    /**
     * Returns information of this object
     * 
     * @return      Information to display
     */
    public String getHoverInfo() {
        return "Main Menu";
    }
}

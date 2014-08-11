 import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The title screen of the game
 * 
 * @author Jesmin Hondell
 * @version Beta 2.1
 */
public class TitleScreen extends World
{
    /**
     * Constructor for objects of class TitleScreen.
     */
    public TitleScreen() {    
        super(1280, 720, 1); 
        setBackground(new GreenfootImage("TitlePage.png"));
        setup();
    }

    /**
     * Adds the buttons onto the Title screen
     */
    private void setup() {
        addObject(new NewGameButton(), 270, 512);
        addObject(new LoadGameButton(), 520, 512);
        addObject(new HostGameButton(), 770, 512);
        addObject(new JoinGameButton(), 1020, 512);
    }
}

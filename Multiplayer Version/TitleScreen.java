import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The title screen of the game.
 * 
 * The music is called "Kingdom Dance.mp3" from the movie Tangled.
 * 
 * @author Jesmin Hondell
 * @version 1.0
 */
public class TitleScreen extends World
{
    //Opening sounds
    GreenfootSound openingMusic = new GreenfootSound("Kingdom Dance.mp3");
    
    /**
     * Constructor for objects of class TitleScreen.
     */
    public TitleScreen() {    
        super(1280, 720, 1); 
        setBackground(new GreenfootImage("TitlePage.png"));
        setup();

        openingMusic.play();
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

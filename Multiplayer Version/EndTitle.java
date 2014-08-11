import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * When there is a victor displays "Victory" or "
 * Defeat" sign accordingly.
 * 
 * Music is called "The Pirate That Should Not Be.mp3" from the Pirates of the Caribbean
 * 
 * @author Koko Deng, Jesmin Hondell
 * @version 1.0
 */
public class EndTitle extends Actor
{
    //Timer variable
    private int counter;

    //Sound
    GreenfootSound endingMusic = new GreenfootSound("endingMusic.mp3");

    /**
     * sets the image according to the victor
     */
    public EndTitle(boolean victory) {
        if (victory == true) {
            setImage(new GreenfootImage("VictorySign.png"));
        }
        if (victory == false) {
            setImage(new GreenfootImage("DefeatSign.png"));
        }

        counter = 150;
        endingMusic.play();
    }

    /**
     * Counts down the timer in which the message will remain on screen
     */
    public void act() {
        timer();
    }    

    /**
     * The timer in which the message will remain on screen.
     * Once removed, will display the menu screen again.	
     */
    public void timer() {
        if (counter == 0) {
            getWorld().removeObject(this);
            Greenfoot.setWorld(new TitleScreen());
            endingMusic.stop();
        } else {
            counter--;
        }
    }
}

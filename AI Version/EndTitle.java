import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * Write a description of class EndTitle here.
 * 
 * @author Koko 
 * @version (a version number or a date)
 */
public class EndTitle extends Actor
{
    private int counter;
    
    public EndTitle(boolean victory) {
        if (victory == true) {
            setImage(new GreenfootImage("VictorySign.png"));
        }
        if (victory == false) {
            setImage(new GreenfootImage("DefeatSign.png"));
        }

        counter = 100;
    }

    /**
     * Act - do whatever the EndTitle wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        timer();
    }    

    public void timer() {
        if (counter == 0) {
            getWorld().removeObject(this);
            Greenfoot.setWorld(new TitleScreen());
        } else {
            counter--;
        }
    }
}

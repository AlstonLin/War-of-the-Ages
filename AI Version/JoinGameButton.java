import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Scanner;

/**
 * Write a description of class JoinGameButton here.
 * 
 * @author Alston Lin
 * @version Beta 2.1
 */
public class JoinGameButton extends Button
{   
    public static String HOSTNAME = "Alston";
    
    /**
     * Sets the image for this object
     */
    public JoinGameButton() {
        super(new GreenfootImage("MultiplayerJoinButton.png"));
    }

    /**
     * Sets the world
     */
    public void click() {
        Scanner scanner = new Scanner(System.in);
        //System.out.println("Enter the hostname of the server");
        //String hostname = scanner.nextLine();
        Greenfoot.setWorld(new Room(false, HOSTNAME));
    }

    /**
     * Returns the information of this button
     * 
     * @return      The information of this button
     */
    public String getHoverInfo() {
        return "Join a game";
    }
}

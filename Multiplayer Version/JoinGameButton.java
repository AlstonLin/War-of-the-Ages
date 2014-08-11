import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * When this button is clicked, joins a currently hosted game
 * 
 * @author Alston Lin
 * @version 1.0
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
        JOptionPane pane = new JOptionPane("Join Room");
        String hostname = pane.showInputDialog("Please enter the hostname of the server to join:");
        Greenfoot.setWorld(new Room(false, hostname));
        pane.setVisible(false);
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

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.*;

/**
 * Loads a previously saved Game
 * 
 * @author Alston Lin, Koko Deng
 * @version 1.0
 */
public class LoadGameButton extends Button
{
    /**
     * Sets the image for this object
     */
    public LoadGameButton() {
        super(new GreenfootImage("LoadGameButton.png"));
    }

    /**
     * When clicked, loads the game from file
     */
    public void click() {
        load();
    }

    /**
     * Returns the information of this button
     * 
     * @return      The information of this button
     */
    public String getHoverInfo() {
        return "Load a game";
    } 

    /**
     * Loads the game from file
     */
    private void load() {
        try{
            FileInputStream fileIn = new FileInputStream(Game.SAVE_FILE_NAME);
            ObjectInputStream stream = new ObjectInputStream(fileIn);
            Greenfoot.setWorld((Game)stream.readObject());
            fileIn.close();
            stream.close();
        } catch (IOException e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Could not find the save file");
            e.printStackTrace();
        }
    }
}

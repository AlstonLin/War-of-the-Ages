import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * A naturally occuring source of gold. 
 * 
 * @author Alston Lin
 * @version 1.0
 */
public class Mine extends Nature implements Gatherable
{   
    //Constants
    public static final int STARTING_GOLD = 3000;
    public static final GreenfootImage ORIGINAL = new GreenfootImage("GoldMine.png");
    
    //Instance variables
    private int resources;

    /**
     * Sets the Mine's image and location
     * 
     * @param x         The x index of the cell on the map
     * @param y         The y index of the cell on the map
     * @param cellSize  The size of each side
     */
    public Mine(int x, int y, int cellSize) {
        super(x, y, cellSize);
        traversable = false;
        resources = STARTING_GOLD;
    }

    /**
     * Returns the original image
     * 
     * @return      The original image
     */
    public GreenfootImage getOriginalImage() {
        return ORIGINAL;    
    }

    /**
     * Retuns the number of resources
     * 
     * @return      The amount of resources the mine has left
     */
    public int getResource() {
        return resources;
    }

    /**
     * Removes the specified gold from the Mine. If the amount of resources
     * reached 0, this will destroy itself.
     * 
     * @param resource      The amount of gold to remove
     */
    public void removeResource(int resource) {
        resources -= resource;
        if (resources <= 0) {
            super.destroy();
        }
    }

    /**
     * Returns information of this object
     * 
     * @return      Information to display
     */
    public String getInformation()
    {
        String temp = "{Mine} Gold: " + resources;
        return (temp);
    }

    /**
     * Returns a LinkedList of this objects's buttons
     * 
     * @return      LinkedList of this objects's buttons
     */
    public LinkedList<Button> getButtons() {
        return new LinkedList(); //No buttons
    }
}

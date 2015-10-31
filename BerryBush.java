import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * A naturally occuring source of food found in the wild.
 * 
 * @author Alston Lin, Koko Deng
 * @version Alpha 2.1
 */
public class BerryBush extends Nature implements Gatherable
{   
    //Constants    
    public static final int STARTING_FOOD = 1000;
    public static final GreenfootImage ORIGINAL = new GreenfootImage("BerryBush.png");
    //Instance variables
    private int resources;

    /**
     * Creates a new BerryBush at the given map coordinates.
     * 
     * @param x         The x index of the cell on the map
     * @param y         The y index of the cell on the map
     * @param cellSize  The size of each side 
     */
    public BerryBush(int x, int y, int cellSize) {
        super(x, y, cellSize);
        traversable = false;
        resources = STARTING_FOOD;
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
     * Retunrs the number of resources
     * 
     * @return      The amount of resources the berry bush has left
     */
    public int getResources() {
        return resources;
    }

    /**
     * Removes the specified food from the bush. If the amount of resources
     * reached 0, this will destroy itself.
     * 
     * @param resource   The amount of gold to remove
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
        String temp = "{Berry Bush} Food: " + resources;
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

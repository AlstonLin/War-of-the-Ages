import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * A naturally occuring source of wood
 * 
 * @author Alston Lin, Koko Deng
 * @version 1.0
 */
public class Tree extends Nature implements Gatherable
{   
    //Constants
    public static final int STARTING_WOOD = 500;
    public static final GreenfootImage ORIGINAL = new GreenfootImage("Tree.png");
    
    //Instance variables
    private int resources;

    /**
     * Creates a new Tree at the given map coordinates.
     * 
     * @param x         The x index of the cell on the map
     * @param y         The y index of the cell on the map
     * @param cellSize The size of each side
     */
    public Tree(int x, int y, int cellSize) {
        super(x, y, cellSize);
        traversable = false;
        resources = STARTING_WOOD;
    }
    
    /**
     * Removes the specified wood in the Tree. If the amount of resources
     * reached 0, this will destroy itself.
     * 
     * @param resource The amount of gold to remove
     */
    public void removeResource(int resource) {
        resources -= resource;
        if (resources <= 0) {
            super.destroy();
        }
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
     * Returns the number of resources
     * 
     * @return      The amount of resources the tree has left
     */
    public int getResource(int resources) {
        return resources;
    }

    /**
     * Returns information of this object
     * 
     * @return      Information to display
     */
    public String getInformation()
    {
        String temp = "{Tree} Wood: " + resources;
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

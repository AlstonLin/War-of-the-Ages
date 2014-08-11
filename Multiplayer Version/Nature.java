import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Nature encompasses any stationary objects that provide resources.
 * 
 * @author Alston Lin
 * @version 1.0
 */
public abstract class Nature extends Cell
{
    //Instance variables
    protected String stats;

    /**
     * Constructor for the Nature object. Sets the size, and location
     * 
     * @param x         The x index of the cell on the map
     * @param y         The y index of the cell on the map
     * @param cellSize  The size of each side 
     */
    public Nature (int x, int y, int cellSize) {
        super(x, y, cellSize);
        resize(cellSize);
    }
}

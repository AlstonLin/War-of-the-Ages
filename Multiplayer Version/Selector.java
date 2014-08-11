import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Allows the user to select multiple Units on the User-Interface.
 * 
 * @author Alston Lin
 * @version 1.0
 */
public class Selector extends Actor
{   
    /**
     * Constructor for Selector; sets the initial size as 1x1 and
     * transparency to 50.
     */
    public Selector() {
        getImage().setTransparency(50); 
        getImage().scale(1, 1); 
    }

    /**
     * Updates the selector to the given coordinates to create 
     * a rectangle
     * 
     * @param x1    The x position of the first coordinate
     * @param y1    The y position of the first coordinate
     * @param x2    The x position of the second coordinate
     * @param y2    The y position of the second coordinate
     */
    public void resize(int x1, int y1, int x2, int y2) {
        int xSize = Math.abs(x1 - x2);
        int ySize = Math.abs(y1 - y2);
        getImage().scale(xSize == 0 ? xSize + 1 : xSize, ySize == 0 ? ySize + 1 : ySize); 
        setLocation((x1 + x2) / 2, (y1 + y2) / 2);
    }

    /**
     * Allows public access to the superclass' method
     * 
     * @return      An List of Objects of the given class that intersects with the Selector
     */
    public List getIntersectingObjects(Class cls) {
        return super.getIntersectingObjects(cls);
    }

    /**
     * Allows public access to the superclass' method
     * 
     * @return      An Object of the given class that intersects with the Selector
     */
    public Actor getOneIntersectingObject(Class cls) {
        return super.getOneIntersectingObject(cls);
    }
}

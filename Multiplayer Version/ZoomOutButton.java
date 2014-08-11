import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Button when clicked, pans out the map
 * 
 * @author Koko Deng
 * @version 1.0
 */
public class ZoomOutButton extends Button
{   
    /**
     * Sets the image for ZoomOutButton object
     */
    public ZoomOutButton() {
        super(new GreenfootImage("NegArrow.png"));
    }

    /**
     * Changes the cell size so it appears as if the map was zoomed out
     */
    public void click() {
        ((Game)getWorld()).getMap().changeCellSize(-4);
    }  

    /**
     * Returns the information of this button
     * 
     * @return      The information of this button
     */
    public String getHoverInfo() {
        return "Zoom Out";
    } 
}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Button when clicked, pans into the map
 * 
 * @author Alston Lin
 * @version Beta 2.1
 */
public class ZoomInButton extends Button
{   
    /**
     * Sets the image for ZoomInButton object
     */
    public ZoomInButton() {
        super(new GreenfootImage("PosArrow.png"));
    }

    /**
     * Changes the cell size so it appears as if the map was zoomed in
     */
    public void click() {
        ((Game)getWorld()).getMap().changeCellSize(4);
    }

    /**
     * Returns the information of this button
     * 
     * @return      The information of this button
     */
    public String getHoverInfo() {
        return "Zoom In";
    }
}

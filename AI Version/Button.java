import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A generic button that will call the abstract method click() when clicked upon.
 * The image for this Object should be set by the subclass.
 * 
 * @author Alston Lin
 * @version Beta 2.1
 */
public abstract class Button extends Actor
{   
    //Objects
    private Popup popup;

    /**
     * Creates a new Button with the given image.
     * 
     * @param image     The appearence of the Button
     */
    public Button(GreenfootImage image) {
        setImage(image);
    }

    /**
     * Checks if this Button has been clicked. 
     * Called before every frame is rendered; 
     */
    public void act() {   
        MouseInfo info = Greenfoot.getMouseInfo(); 
        if (info != null) { //Prevents a Greenfoot bug
            int width = getImage().getWidth();
            int height = getImage().getHeight();
            if (Greenfoot.mouseClicked(this)) { //This has been clicked
                if (info.getButton() == 1) { //Clicked on
                    click();
                }
            }else if (info.getX() > getX() - width && info.getX() < getX() + width && info.getY() < getY() + height && info.getY() > getY() - height) { //Mouse is over this
                if (popup == null) { //No popup made yet
                    if (getHoverInfo().length() > 0) { //Something to display
                        popup = new Popup(10 * getHoverInfo().length(), 50, getHoverInfo());
                        getWorld().addObject(popup, getX() + 100, getY() - 50);
                    }
                }
            }else{
                if (popup != null) { //No longer hovering
                    getWorld().removeObject(popup);
                    popup = null;
                }
            }
        }
    }    

    /**
     * Removes the popUp
     */
    public void forceRemovePopup() {
        if (popup != null) {
            getWorld().removeObject(popup);
            popup = null;
        }
    }

    /**
     * Actions corresponding to the clicked button 
     */
    public abstract void click();

    /**
     * Gets the info that will be displayed when the mouse
     * hovers over the Button.
     */
    public abstract String getHoverInfo();   

    /**
     * Returns the Pop Up
     * 
     * @return      The pop up
     */
    public Popup getPopup() {
        return popup;
    }
}

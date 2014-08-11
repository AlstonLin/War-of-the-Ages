import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * The panel where buttons will be displayed.
 * 
 * @author Alston Lin, Koko Deng
 * @version 1.0
 */
public class ActionsPanel extends Actor
{   
    //Constants
    private static final int WIDTH = 718, HEIGHT = 180;
    
    //Instance variables
    private LinkedList<Button> buttons; 

    /**
     * Constructor for ActionsPanel, sets the size
     */
    public ActionsPanel() {
        this.setImage("ButtonPanel.png");
        getImage().scale(WIDTH, HEIGHT);

        buttons = new LinkedList();
    }

    /**
     * Sets the Buttons to appear on the display
     */
    public void setButtons(LinkedList<Button> buttons) {
        //Removes the current buttons
        for (Button button : this.buttons) {
            button.forceRemovePopup();
            getWorld().removeObject(button);
        }
        this.buttons = buttons;

        //Adds the new Buttons
        int size;
        if (buttons.size() > 0) {
            size = buttons.size();
        } else {
            return;
        }
        int xIncrement = WIDTH / (size + 1);
        for (int i = 0; i < size; i++) {
            Button button = buttons.get(i);
            int x = xIncrement * (i + 1);
            int y = getY();
            getWorld().addObject(buttons.get(i), x, y);
        }
    }
}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * The Object of which this will be implemented upon would
 * be able to be selected by the user and display Buttons on the
 * ActionsPanel and information on the InformationPanel
 * 
 * @author Alston Lin
 * @version 1.0
 */
public interface Selectable
{   
    /**
     * Will return Buttons, if any, that will be put on
     * the ActionsPanel to allow the user to click on once 
     * the Object us selected.
     * 
     * @return      A List of Buttons that the user can click
     */
    public LinkedList<Button> getButtons();
    
    /**
     * Returns the text to display on the InformationPanel
     * 
     * @return      the text to display
     */
    public String getInformation();
}

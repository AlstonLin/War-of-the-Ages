import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;
/**
 * Write a description of class Artillery here.
 * 
 * @author (your name) 
 * @version Beta 2.1
 */
public abstract class MeleeArtillery extends Unit
{

    /**
     * Constructor for Dragon, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public MeleeArtillery(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        type = Unit.SIEGE;
    }  

    /**
     * What happens when the Unit arrives at the destination Cell
     */
    public void arrive() {
    }

    /**
     * Returns Buttons when unit is selected, also determines what happens once buttons are pressed
     * 
     * @return      The buttons when unit is selected
     */
    public LinkedList<Button> getButtons() {
        LinkedList list = new LinkedList();
        //Adds a Button allowing the unit to Commit suicide
        GreenfootImage image = new GreenfootImage("DestroyButton.png");
        image.scale(64, 64);
        list.add(new Button(image){
                public void click() {
                    takeDamage(9001.0);
                }

                public String getHoverInfo() {
                    return "Kill Yo'self";
                }
            });
        return list;
    }

    /**
     * Returns basic Information to be used in the information Panel
     */
    public String getInformation() {
        String lines = ("Type of Unit: " + type + " | Health: " + currentHealth + " | Attack Damage: " + attack);
        InformationPanel informationPanel = new InformationPanel();
        informationPanel.display(lines);
        //System.out.println("SELECTED AND CALLED");
        return lines;
    }
}

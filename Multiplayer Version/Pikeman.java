import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Pikeman
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class Pikeman extends MeleeInfantry
{
    //CONSTANTS
    public static final int FOOD_COST = 60, WOOD_COST = 60, GOLD_COST = 0, SPAWN_TIME = 90;
    private static final GreenfootImage FRONT = new GreenfootImage("PikemanLeft.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("PikemanBackLeft.png");
    private static final GreenfootImage LEFT = new GreenfootImage("PikemanRight.png");
    private static final GreenfootImage BACK = new GreenfootImage("PikemanBackLeft.png");

    /**
     * Constructor for Pikeman, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public Pikeman(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Pikeman";

        //Combat Statistics
        speed = 3;
        range = 1;
        attackSpeed = 1;
        health = 150.0;
        currentHealth = health;
        attack = 15.0;
        //Resistance multipliers
        meleeArmour = 0.25;
        rangedArmour = 0.25;
        siegeArmour = 0.0;
        //Attack multipliers
        meleeInfantry = 1.0;
        rangedInfantry = 1.0;
        meleeCavalry = 2.0;
        rangedCavalry = 2.0;
        siege = 1.5;
        building = 3.0;
    }

    /**
     * What happens when the Unit arrives at the selected Unit
     *
     * @param unit      The Unit it is interacting with
     */
    public void interactWith(Unit unit) {
    }

    /**
     * What happens when the Unit arrives at the destination Cell
     *
     * @param cell      The Cell that the Unit will enter
     */
    public void enterCell(Cell cell) {
    }

    /**
     * Returns the image when object is facing left
     * 
     * @return      The image when object is facing left
     */
    public GreenfootImage getLeftImage() {
        return LEFT;
    }

    /**
     * Returns the image when object is facing right
     * 
     * @return      The image when object is facing right
     */
    public GreenfootImage getRightImage() {
        return RIGHT;
    }

    /**
     * Returns the image when object is facing the front
     * 
     * @return      The image when object is facing the front
     */
    public GreenfootImage getFrontImage() {
        return FRONT;
    }

    /**
     * Returns the image when object is facing back (away)
     * 
     * @return      The image when object is facing back (away)
     */
    public GreenfootImage getBackImage() {
        return BACK;
    }
}

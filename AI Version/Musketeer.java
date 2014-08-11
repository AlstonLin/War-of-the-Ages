import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Musketeer
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class Musketeer extends RangedInfantry
{
    //CONSTANTS
    public static final int  FOOD_COST = 100, WOOD_COST = 10, GOLD_COST = 25, SPAWN_TIME = 90;
    private static final GreenfootImage FRONT = new GreenfootImage("MusketeerLeft.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("MusketeerBackRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("MusketeerRight.png");
    private static final GreenfootImage BACK = new GreenfootImage("MusketeerBack.png");

    /**
     * Constructor for Musketeer, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public Musketeer(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Musketeer";

        //Combat Statistics
        speed = 3;
        range = 3;
        attackSpeed = 1;
        health = 200.0;
        currentHealth = health;
        attack = 18.0;
        //Resistance multipliers
        meleeArmour = 0.1;
        rangedArmour = 0.0;
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

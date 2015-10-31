import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * RPG
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class RPG extends RangedInfantry
{
    //CONSTANTS
    public static final int FOOD_COST = 80, WOOD_COST = 80, GOLD_COST = 100, SPAWN_TIME = 100;
    private static final GreenfootImage FRONT = new GreenfootImage("RPGFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("RPGRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("RPGLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("RPGBack.png");

    /**
     * Constructor for Helicopter, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public RPG(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "RPG";

        //Combat Statistics
        speed = 4;
        range = 5;
        attackSpeed = 2;
        health = 400.0;
        currentHealth = health;
        attack = 50.0;
        //Resistance multipliers
        meleeArmour = 0.20;
        rangedArmour = 0.30;
        siegeArmour = 0.20;
        //Attack multipliers
        meleeInfantry = 2.0;
        rangedInfantry = 1.0;
        meleeCavalry = 3.0;
        rangedCavalry = 3.0;
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

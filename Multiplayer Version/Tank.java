import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Tank
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung 
 * @version Beta 2.1
 */
public class Tank extends RangedCavalry
{
    //CONSTANTS
    public static final int FOOD_COST = 40, WOOD_COST = 200, GOLD_COST = 80, SPAWN_TIME = 160;
    private static final GreenfootImage FRONT = new GreenfootImage("TankFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("TankRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("TankLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("TankBack.png");

    /**
     * Constructor for Tank, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public Tank(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Tank";

        //Combat Statistics
        speed = 8;
        range = 4;
        attackSpeed = 2;
        health = 400.0;
        currentHealth = health;
        attack = 30.0;
        //Resistance multipliers
        meleeArmour = 0.70;
        rangedArmour = 0.70;
        siegeArmour = 0.20;
        //Attack multipliers
        meleeInfantry = 3.0;
        rangedInfantry = 2.0;
        meleeCavalry = 3.0;
        rangedCavalry = 1.5;
        siege = 1.0;
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

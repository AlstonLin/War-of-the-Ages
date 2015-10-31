import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Rifleman
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class Rifleman extends RangedInfantry
{
    //CONSTANTS
    public static final int FOOD_COST = 80, WOOD_COST = 80, GOLD_COST = 30, SPAWN_TIME = 80;
    private static final GreenfootImage FRONT = new GreenfootImage("RiflemanRight.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("RiflemanBackRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("RiflemanLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("RiflemanBack.png");

    /**
     * Constructor for Rifleman, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public Rifleman(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Rifleman";

        //Combat Statistics
        speed = 4;
        range = 4;
        attackSpeed = 1;
        health = 200.0;
        currentHealth = health;
        attack = 15.0;
        //Resistance multipliers
        meleeArmour = 0.30;
        rangedArmour = 0.30;
        siegeArmour = 0.20;
        //Attack multipliers
        meleeInfantry = 2.0;
        rangedInfantry = 1.0;
        meleeCavalry = 2.0;
        rangedCavalry = 1.0;
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

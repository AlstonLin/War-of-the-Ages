import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Catapult
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Alpha 2.1
 */
public class Catapult extends RangedArtillery
{
    //CONSTANTS
    public static final int FOOD_COST = 10, WOOD_COST = 120, GOLD_COST = 40, SPAWN_TIME = 300;
    private static final GreenfootImage FRONT = new GreenfootImage("CatapultFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("CatapultRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("CatapultLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("CatapultBack.png");

    /**
     * Constructor for Catapult, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public Catapult(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Catapult";

        //Combat Statistics
        speed = 2;
        range = 5;
        attackSpeed = 2;
        health = 320.0;
        currentHealth = health;
        attack = 20.0;
        //Resistance multipliers
        meleeArmour = 0.0;
        rangedArmour = 0.30;
        siegeArmour = 0.20;
        //Attack multipliers
        meleeInfantry = 2.0;
        rangedInfantry = 2.0;
        meleeCavalry = 1.0;
        rangedCavalry = 2.0;
        siege = 1.0;
        building = 2.0;
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

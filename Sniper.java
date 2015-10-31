import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Sniper
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class Sniper extends RangedInfantry
{
    //CONSTANTS
    public static final int  FOOD_COST = 80, WOOD_COST = 80, GOLD_COST = 80, SPAWN_TIME = 80;
    private static final GreenfootImage FRONT = new GreenfootImage("SniperFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("SniperRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("SniperLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("SniperBack.png");

    /**
     * Constructor for Sniper, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public Sniper(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Sniper";

        //Combat Statistics
        speed = 4;
        range = 8;
        attackSpeed = 2;
        health = 200.00;
        currentHealth = health;
        attack = 25.0;
        //Resistance multipliers
        meleeArmour = 0.3;
        rangedArmour = 0.3;
        siegeArmour = 0.0;
        //Attack multipliers
        meleeInfantry = 2.0;
        rangedInfantry = 2.0;
        meleeCavalry = 1.0;
        rangedCavalry = 1.0;
        siege = 1.5;
        building = 3.0;
    }

    /**
     * Setss Sniper's image
     */
    public void act() {
        setImage("sniper.png");
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

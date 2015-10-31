import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Battering Ram
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class BatteringRam extends MeleeArtillery
{
    //CONSTANTS
    public static final int FOOD_COST = 10, WOOD_COST = 1200, GOLD_COST = 40, SPAWN_TIME = 300;
    private static final GreenfootImage FRONT = new GreenfootImage("BatteringRamFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("BatteringRamRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("BatteringRamLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("BatteringRamBack.png");

    /**
     * Constructor for Dragon, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public BatteringRam(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Battering Ram";

        //Combat Statistics
        speed = 2;
        range = 1;
        attackSpeed = 2;
        health = 400.0;
        currentHealth = health;
        attack = 20.0;
        //Resistance multipliers
        meleeArmour = 0.2;
        rangedArmour = 0.50;
        siegeArmour = 0.20;
        //Attack multipliers
        meleeInfantry = 0.0;
        rangedInfantry = 0.0;
        meleeCavalry = 0.0;
        rangedCavalry = 0.0;
        siege = 0.0;
        building = 5.0;
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

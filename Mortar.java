import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Mortar
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class Mortar extends RangedArtillery
{
    //CONSTANTS
    public static final int FOOD_COST = 10, WOOD_COST = 120, GOLD_COST = 40, SPAWN_TIME = 240;
    private static final GreenfootImage FRONT = new GreenfootImage("MortarFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("MortarRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("MortarLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("MortarBack.png");

    /**
     * Constructor for Mortar, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public Mortar(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Spearman";

        //Combat Statistics
        speed = 3;
        range = 8;
        attackSpeed = 2;
        health = 1000.0;
        currentHealth = health;
        attack = 50.0;
        //Resistance multipliers
        meleeArmour = 0.70;
        rangedArmour = 0.70;
        siegeArmour = 0.70;
        //Attack multipliers
        meleeInfantry = 1.0;
        rangedInfantry = 1.0;
        meleeCavalry = 1.0;
        rangedCavalry = 1.0;
        siege = 1.5;
        building = 4.0;
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

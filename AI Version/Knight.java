import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Knight
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung 
 * @version Beta 2.1
 */
public class Knight extends MeleeCavalry
{
    //CONSTANTS
    public static final int FOOD_COST = 120, WOOD_COST = 15, GOLD_COST = 50, SPAWN_TIME = 200;
    private static final GreenfootImage FRONT = new GreenfootImage("KnightBack.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("KnightRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("KnightLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("KnightFront.png");

    /**
     * Constructor for Knight, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public Knight(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Unit name
        unit = "Knight";

        //Combat Statistics
        speed = 9;
        range = 1;
        attackSpeed = 1;
        health = 320.00;
        currentHealth = health;
        attack = 20.00;

        //Resistance multipliers
        meleeArmour = 0.20;
        rangedArmour = 0.30;
        siegeArmour = 0.00;

        //Attack multipliers
        meleeInfantry = 1.0;
        rangedInfantry = 2.0;
        meleeCavalry = 1.0;
        rangedCavalry = 1.0;
        siege = 2.0;
        building = 1.0;
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

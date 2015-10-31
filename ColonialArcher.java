import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Colonial Archer
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class ColonialArcher extends RangedInfantry
{
    //CONSTANTS
    public static final int FOOD_COST = 60, WOOD_COST = 60, GOLD_COST = 0, SPAWN_TIME = 90;
    private static final GreenfootImage FRONT = new GreenfootImage("ColonialArcherFrontLeft.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("ColonialArcherBackRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("ColonialArcherFrontRight.png");
    private static final GreenfootImage BACK = new GreenfootImage("ColonialArcherBackLeft.png");

    /**
     * Constructor for ColonialArcher, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public ColonialArcher(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Colonial Archer";

        //Combat Statistics
        speed = 3;
        range = 4;
        attackSpeed = 1;
        health = 100.0;
        currentHealth = health;
        attack = 12.0;
        //Resistance multipliers
        meleeArmour = 0.25;
        rangedArmour = 0.25;
        siegeArmour = 0.0;
        //Attack multipliers
        meleeInfantry = 2.0;
        rangedInfantry = 2.0;
        meleeCavalry = 1.0;
        rangedCavalry = 2.0;
        siege = 1.0;
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

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Mounted Archer
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class MountedArcher extends RangedCavalry
{
    //CONSTANTS
    public static final int FOOD_COST = 110, WOOD_COST = 40, GOLD_COST = 50, SPAWN_TIME = 200;
    private static final GreenfootImage FRONT = new GreenfootImage("MountedArcherFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("MountedArcherRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("MountedArcherLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("MountedArcherBack.png");
    
    /**
     * Constructor for MountedArcher, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public MountedArcher(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Mounted Archer";

        //Combat Statistics
        speed = 9;
        range = 4;
        attackSpeed = 1;
        health = 160.0;
        currentHealth = health;
        attack = 12.0;
        //Resistance multipliers
        meleeArmour = 0.15;
        rangedArmour = 0.15;
        siegeArmour = 0.0;
        //Attack multipliers
        meleeInfantry = 1.0;
        rangedInfantry = 1.0;
        meleeCavalry = 3.0;
        rangedCavalry = 1.0;
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

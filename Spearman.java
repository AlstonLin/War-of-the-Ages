import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Spearmen cost resources to spawn and deal attack damage
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class Spearman extends MeleeInfantry
{   
    //Constants
    public static final int FOOD_COST = 40, WOOD_COST = 40, GOLD_COST = 0, SPAWN_TIME = 100;
    private static final GreenfootImage FRONT = new GreenfootImage("SpearmanFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("SpearmanRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("SpearmanLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("SpearmanBack.png");

    /**
     * Constructor for Spearman, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public Spearman(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Spearman";

        //Combat Statistics
        speed = 3;
        range = 1;
        attackSpeed = 1;
        health = 80.0;
        currentHealth = health;
        attack = 10.0;

        //Resistance multipliers
        meleeArmour = 0.0;
        rangedArmour = 0.0;
        siegeArmour = 0.0;

        //Attack multipliers
        meleeInfantry = 1.0;
        rangedInfantry = 1.0;
        meleeCavalry = 2.0;
        rangedCavalry = 2.0;
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

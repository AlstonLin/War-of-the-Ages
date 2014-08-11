import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Organ Gun
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class OrganGun extends RangedArtillery
{
    //Constants
    public static final int FOOD_COST = 10, WOOD_COST = 120, GOLD_COST = 40, SPAWN_TIME = 270;
    private static final GreenfootImage FRONT = new GreenfootImage("OrganGunFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("OrganGunRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("OrganGunLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("OrganGunBack.png");

    /**
     * Constructor for OrganGun, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public OrganGun(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Organ Gun";

        //Combat Statistics
        speed = 3;
        range = 30;
        attackSpeed = 2;
        health = 400.0;
        currentHealth = health;
        attack = 21.0;
        //Resistance multipliers
        meleeArmour = 0.0;
        rangedArmour = 0.20;
        siegeArmour = 0.20;
        //Attack multipliers
        meleeInfantry = 2.0;
        rangedInfantry = 2.0;
        meleeCavalry = 1.0;
        rangedCavalry = 1.0;
        siege = 1.0;
        building = 1.50;
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

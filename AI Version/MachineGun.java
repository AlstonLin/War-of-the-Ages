import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Machine Gun
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class MachineGun extends RangedInfantry
{
    //CONSTANTS
    public static final int FOOD_COST = 80, WOOD_COST = 80, GOLD_COST = 100, SPAWN_TIME = 80;
    private static final GreenfootImage FRONT = new GreenfootImage("MachineGunFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("MachineGunRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("MachineGunLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("MachineGunBack.png");

    /**
     * Constructor for MachineGun, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public MachineGun(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Machine Gun";

        //Combat Statistics
        speed = 4;
        range = 2;
        attackSpeed = 1;
        health = 400.0;
        currentHealth = health;
        attack = 30.0;
        //Resistance multipliers
        meleeArmour = 0.20;
        rangedArmour = 0.20;
        siegeArmour = 0.00;
        //Attack multipliers
        meleeInfantry = 2.0;
        rangedInfantry = 2.0;
        meleeCavalry = 1.5;
        rangedCavalry = 1.5;
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

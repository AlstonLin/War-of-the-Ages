import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * LAV
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class LAV extends RangedCavalry
{
    //Constants
    public static final int FOOD_COST = 40, WOOD_COST = 200, GOLD_COST = 80, SPAWN_TIME = 160;
    private static final GreenfootImage FRONT = new GreenfootImage("LAVFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("LAVRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("LAVLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("LAVBack.png");

    public LAV(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        
        //Combat Statistics
        speed = 12;
        range = 4;
        attackSpeed = 1;
        health = 400.0;
        currentHealth = health;
        attack = 15.0;
        //Resistance multipliers
        meleeArmour = 0.65;
        rangedArmour = 0.60;
        siegeArmour = 0.20;
        //Attack multipliers
        meleeInfantry = 3.0;
        rangedInfantry = 1.5;
        meleeCavalry = 3.0;
        rangedCavalry = 1.5;
        siege = 1.0;
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

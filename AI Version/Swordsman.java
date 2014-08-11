import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Swordsman
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class Swordsman extends MeleeInfantry
{
    //CONSTANTS
    public static final int FOOD_COST = 75, WOOD_COST = 15, GOLD_COST = 10, SPAWN_TIME = 150;
    private static final GreenfootImage FRONT = new GreenfootImage("SwordsmanFrontLeft.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("SwordsmanBackRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("SwordsmanFrontRight.png");
    private static final GreenfootImage BACK = new GreenfootImage("SwordsmanBackLeft.png");

    /**
     * Constructor for Swordsman, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public Swordsman(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Swordsman";

        //Combat Statistics
        speed = 3;
        range = 1;
        attackSpeed = 1;
        health = 160.0;
        currentHealth = health;
        attack = 14.0;
        //Resistance multipliers
        meleeArmour = 0.25;
        rangedArmour = 0.25;
        siegeArmour = 0.25;
        //Attack multipliers
        meleeInfantry = 1.0;
        rangedInfantry = 1.0;
        meleeCavalry = 1.0;
        rangedCavalry = 1.0;
        siege = 1.5;
        building = 3.0;
    }

    /**
     * What happens when the Unit arrives at the selected Unit
     *
     * @param unit      The Unit it is interacting with
     */
    public void interactWith(Unit unit) {
    }

    /**
     * What happens when the Unit arrives at the destination Cell
     *
     * @param cell      The Cell that the Unit will enter
     */
    public void enterCell(Cell cell) {
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

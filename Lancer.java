import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Lancer
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class Lancer extends MeleeCavalry
{
    //CONSTANTS
    public static final int FOOD_COST = 100, WOOD_COST = 40, GOLD_COST = 50, SPAWN_TIME = 200;
    private static final GreenfootImage FRONT = new GreenfootImage("LancerFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("LancerRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("LancerLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("LancerBack.png");

    /**
     * Constructor for Lancer, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public Lancer(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Lancer";

        //Combat Statistics
        speed = 9;
        range = 1;
        attackSpeed = 1;
        health = 240.00;
        currentHealth = health;
        attack = 12.0;
        //Resistance multipliers
        meleeArmour = 0.15;
        rangedArmour = 0.20;
        siegeArmour = 0.0;
        //Attack multipliers
        meleeInfantry = 1.0;
        rangedInfantry = 2.0;
        meleeCavalry = 2.0;
        rangedCavalry = 2.0;
        siege = 2.0;
        building = 2.0;
    } 

    public GreenfootImage getLeftImage() {
        return LEFT;
    }

    public GreenfootImage getRightImage() {
        return RIGHT;
    }

    public GreenfootImage getFrontImage() {
        return FRONT;
    }

    public GreenfootImage getBackImage() {
        return BACK;
    }
}

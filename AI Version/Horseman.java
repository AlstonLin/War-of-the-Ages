import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Horseman
 * 
 * @author James Ly, Jesmin Hondell, Daniel Chung
 * @version Beta 2.1
 */
public class Horseman extends MeleeCavalry
{
    //CONSTANTS
    public static final int FOOD_COST = 120, WOOD_COST = 12, GOLD_COST = 50, SPAWN_TIME = 180;
    private static final GreenfootImage FRONT = new GreenfootImage("HorsemanFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("HorsemanRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("HorsemanLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("HorsemanBack.png");

    /**
     * Constructor for Horseman, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public Horseman(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        //Description
        unit = "Horseman";

        //Combat Statistics
        speed = 9;
        range = 1;
        attackSpeed = 1;
        health = 400.0;
        currentHealth = health;
        attack = 21.0;
        //Resistance multipliers
        meleeArmour = 0.20;
        rangedArmour = 0.30;
        siegeArmour = 0.20;
        //Attack multipliers
        meleeInfantry = 1.0;
        rangedInfantry = 2.0;
        meleeCavalry = 1.0;
        rangedCavalry = 1.0;
        siege = 2.0;
        building = 1.5;
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

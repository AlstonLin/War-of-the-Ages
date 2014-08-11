import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.Serializable;
import java.util.LinkedList;

/**
 * The class used to hold certain values, such as number of resources, which should 
 * be specific to one player and not in a shared pool.
 * 
 * @author Alston Lin, James Ly
 * @version 1.0
 */
public class Player implements Serializable
{
    //Constants
    public static final int POP_CAP = 100;
    public static final int WOOD = 0, FOOD = 1, GOLD = 2, AGE = 3, MAX_POPULATION = 4;
    private static int IDCounter;    

    //Instance variables
    protected LinkedList<Building> buildings;
    protected LinkedList<Unit> units;
    protected Center center;
    protected int ID; 
    protected int wood, food, gold; //Resources
    protected int age; //1 - MEDIEVAL, 2 - COLONIAL, 3 - MODERN
    protected int maxPopulation;
    protected boolean agingUp;

    /**
     * The constructor for a player to keep track of wood, food, gold number of Units and Buildings.
     * This constructor will give a default value of 200 of everything.
     */
    public Player() {
        this.wood = 4000;
        this.food = 4000;
        this.gold = 4000;

        maxPopulation = 10;
        ID = IDCounter++;
        age = 1;
        buildings = new LinkedList();
        units = new LinkedList();
        agingUp = false;
    }

    /**
     * Updates all the Clients of a field of this Player
     * in multiplayerMode.
     * 
     * @param game The multplayer game
     * @param field The field to update as defined by the constants of this class
     */
    public void updateClients(Game game, int field) {
        int[] data = new int[3];
        //Which player to update
        data[0] = ID;
        //Which field to update
        data[1] = field;
        //What the new field value should be
        switch(field){
            case WOOD:
            data[2] = wood;
            break;
            case FOOD:
            data[2] = food;
            break;
            case GOLD:
            data[2] = gold;
            break;
            case MAX_POPULATION:
            data[2] = maxPopulation;
            break;
        }
        game.sendUpdate(new Update(Update.PLAYER_UPDATE, data));
    }
    
    /**
     * Sets the given field as the given value.
     * 
     * @param field The integer representation of which field to change (see constants)
     * @param value The value of the field to change to
     */
    public void updateField(int field, int value){
        switch(field){
            case WOOD:
            wood = value;
            break;
            case FOOD:
            food = value;
            break;
            case GOLD:
            gold = value;
            break;
            case MAX_POPULATION:
            maxPopulation = value;
            break;
        }
    }

    /**
     * Changes the value of wood by the given amount.
     * The value of wood should be checked beforehand to
     * ensure that the value will not go below 0.
     * 
     * @param game          The game to update
     * @param change        The change in wood
     */
    public void changeWood(Game game, int change) {
        wood += change;
        if (game.isMultiplayer() && game.isHosting()){
            updateClients(game, WOOD);
        }
    }

    /**
     * Changes the value of food by the given amount.
     * The value of food should be checked beforehand to
     * ensure that the value will not go below 0. 
     * 
     * @param game          The game to update
     * @param change        The change in food
     */
    public void changeFood(Game game, int change) {
        food += change;
        if (game.isMultiplayer() && game.isHosting()){
            updateClients(game, FOOD);
        }
    }

    /**
     * Changes the value of gold by the given amount.
     * The value of gold should be checked beforehand to
     * ensure that the value will not go below 0. 
     * 
     * @param game      The game to update
     * @param change    The change in gold
     */
    public void changeGold(Game game, int change) {
        gold += change;
        if (game.isMultiplayer() && game.isHosting()){
            updateClients(game, GOLD);
        }
    }

    /**
     * Returns the amount of wood this Player has
     * 
     * @return      The amount of wood this Player has
     */
    public int getWood() {
        return wood;
    }

    /**
     * Returns the amount of food this Player has
     * 
     * @return      The amount of food this Player has
     */
    public int getFood() {
        return food;
    }

    /**
     * Returns the amount of gold this Player has
     * 
     * @return      The amount of gold this Player has
     */
    public int getGold() {
        return gold;
    }

    /**
     * Returns the current technological age of this Player
     * 
     * @return      The current technological age of this Player
     */
    public int getAge() {
        return age;
    }

    /**
     * Returns the Buildings this player owns
     * 
     * @return      The Buildings this player owns
     */
    public LinkedList getBuildings() {
        return buildings;
    }

    /**
     * Returns the Units this player owns
     * 
     * @return      The Units this player owns
     */
    public LinkedList getUnits() {
        return units;
    }

    /**
     * Sets the age of this Player (1, 2, or 3 ONLY)
     * 
     */
    public void setAge(Game game, int age) {
        this.age = age;
        //Sets the images of the buildings
        for (Building building : buildings) {
            building.setAge(age);
        }

        if (game.isMultiplayer() && game.isHosting()){
            updateClients(game, AGE);
        }
    }

    /**
     * Returns the ID number of the Player for multiplayer
     * 
     * @return      The ID number of the Player for multiplayer
     */
    public int getID() {
        return ID;
    }

    /**
     * Returns the current Center for this Player
     * 
     * @return      The current Center for this Player
     */
    public Center getCenter() {
        return center;
    }

    /**
     * Returns the new Center for this Player
     * 
     * @param       The new Center for this Player
     */
    public void setCenter(Center center) {
        this.center = center;
    }

    /**
     * Returns the maxiumum population of Units that the Game can hold
     * 
     * @return      The maxiumum population of Units that the Game can hold
     */
    public int getMaxPopulation() {
        return maxPopulation;
    }

    /**
     * Changes the maximum population, to a minimum of 10
     * and maximum 0. 
     * 
     * @param game          The game to update
     * @param change        The change in population
     */
    public void changeMaxPopulation(Game game, int change) {
        maxPopulation += change;

        if (maxPopulation < 10) {
            maxPopulation = 10;
        } else if (maxPopulation > POP_CAP) {
            maxPopulation = POP_CAP;
        }

        if (game.isMultiplayer() && game.isHosting()){
            updateClients(game, MAX_POPULATION);
        }
    }

    /**
     * Returns the population.
     * 
     * @return population   Returns the number of Units currently in the world
     */
    public int getPopulation() {
        return getUnits().size();
    }

    /**
     * Returns a boolean that is true if this player is the user
     * 
     * @param game      The Game to compare to 
     * @return          True if this is the user
     */
    public boolean isUser(Game game) {
        return ID == game.getUser().getID();
    }

    /**
     * Sets the age
     * 
     * @param aging     The new Age
     */
    public void setAgingUp(boolean aging) {
        this.agingUp = aging;
    }

    /**
     * Returns if the world is currently aging up
     * 
     * @return      If the world is currently aging up
     */
    public boolean isAgingUp() {
        return agingUp;
    }
}

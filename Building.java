import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * Buildings are utilized by the Units as they provide multiple assets.
 * For example, a defense or etc.
 * 
 * @author Alston Lin
 * @version Beta 2.0
 */
public abstract class Building extends Cell
{   
    //Instance variables
    protected Player owner;
    protected int health;
    protected int counter;
    protected SpawnPoint spawnPoint;
    protected String structureName;
    protected transient InformationPanel informationPanel;

    /**
     * Sets the Building's image and location
     * 
     * @param owner     The Player that owns this Building
     * @param x         The x index of the cell on the map
     * @param y         The y index of the cell on the map
     * @param cellSize  The size of each side 
     */
    public Building(Player owner, int x, int y, int cellSize) {
        super(x, y, cellSize);
        traversable = false;
        this.owner = owner;
        owner.getBuildings().add(this);
        setAge(owner.getAge());
        resize(cellSize);
    }

    /**
     * Returns a Linked List of all the buttons utilized by this Building   
     * 
     * @return        Linked List of all the buttons utilized by this Building
     */
    public LinkedList<Button> getButtons() {
        LinkedList list = getSpecificButtons();

        //Adds a Button allowing it to destroy itself
        if (owner.getID() == ((Game)getWorld()).getUser().getID()) {
            list.add(new Button(new GreenfootImage("DestroyButton.png")) {
                    public void click() {
                        destroy();
                        ((Game)getWorld()).getActionsPanel().setButtons(new LinkedList());
                    }

                    public String getHoverInfo() {
                        return "Destory Building";
                    }
                });
        }
        return list;
    }

    /**
     * Changes the Cell back to grass.
     */
    public void destroy() {
        if (this != null) {
            Map map = ((Game)getWorld()).getMap();
            map.setCell(getMapX(), getMapY(), new Grass(getMapX(), getMapY(), map.getCellSize()));
            owner.getBuildings().remove(this);
        }
    }

    /**
     * Returns a Linked List of all the specific buttons utilized by this Building  
     * 
     * @return        Linked List of all the buttons utilized by this Building
     */
    protected abstract LinkedList<Button> getSpecificButtons();

    /**
     * Checks if the player has sufficient resources
     * 
     * @param unit      Checks if a unit has enough resources to be built
     */
    public boolean checkResources(Unit unit) {
        if (owner.getGold() >= unit.getGoldCost() && owner.getWood() >= unit.getWoodCost() && owner.getFood() >= unit.getFoodCost()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds the given unit to the clients if it's hosting in multiplayer mode
     * 
     * @param unit      The unit that will be added
     */
    public void addUnitToMultiplayer(Unit unit) {
        //Updates if it's in multiplayer mode as a server
        Game game = (Game)getWorld();
        if (game.isHosting()) {
            game.sendUpdate(new Update(Update.UNIT_CREATION, unit));
        } else if (game.isMultiplayer()) {
            if (game.isMultiplayer() && !game.isHosting()) { //A client
                game.sendRequest(new Request(Request.CREATE_UNIT, unit));
            }    
        }
    }

    /**
     * Returns the health of this building
     * 
     * @return      The current health of this Building
     */
    public int getHealth() {
        return health;
    }   

    /**
     * Sets the health of this Building by the given
     * amount, and destroys it if health reaches below zero.
     * 
     * @param change    The change in health
     */
    public void changeHealth(int change) {
        health += change;
        if (health <= 0) {
            destroy();
        }
    }

    /**
     * Returns the SpawnPoint of this Building
     * 
     * @return      The SpawnPoint of this Building, if any
     */
    public SpawnPoint getSpawnPoint() {
        return spawnPoint;
    }

    /**
     * Returns the owner of this Building
     * 
     * @return      The owner of this Building
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the image of the building according to the age
     * 
     * @param age The new age of the Building
     */
    public abstract void setAge(int age);
}

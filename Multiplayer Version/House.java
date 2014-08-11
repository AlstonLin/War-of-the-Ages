import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * House is able to hold a maximum of 10 units
 * 
 * @author Alston Lin, Koko Deng
 * @version 1.0
 */
public class House extends Building
{   
    //Constants
    public static final int WOOD_COST = 100, GOLD_COST = 0, BUILD_TIME = 200;

    /**
     * Sets the House's image and location
     * 
     * @param owner     The Player that owns this
     * @param x         The x index of the cell on the map
     * @param y         The y index of the cell on the map
     * @param cellSize  The size of each side 
     */
    public House(Player owner, int x, int y, int cellSize) {
        super(owner, x, y, cellSize);

        traversable = false;
        this.spawnPoint = spawnPoint;

        //Information about this building
        structureName = "House";         
        health = 800;
    
    }
    
    /**
     * Returns information of the Barrack
     * 
     * @return      Information to display
     */
    public String getInformation() {
        stats = "{"+ structureName + "} Health: " + health;
        informationPanel = new InformationPanel();
        informationPanel.display(stats);
        return stats;
    }

    /**
     * Returns a LinkedList of this objects's buttons
     * 
     * @return      LinkedList of this objects's buttons
     */
    public LinkedList<Button> getSpecificButtons() {
        return new LinkedList();
    }

    /**
     * Destroys this Building and the SpawnPoint.
     */
    public void destroy() {
        super.destroy();
        spawnPoint.destroy();
        owner.changeMaxPopulation((Game)getWorld(), -10);
    }

    /**
     * Sets the image of the building according to the age
     * 
     * @param age    The new age of the Building
     */
    public void setAge(int age) {
        if (age == 1) { //Medieval Age
            setImage("House.png");
        } else if (age == 2) { //Colonial Age
            setImage("ColonialHouse.png");
        } else if (age == 3) { //Modern age
            setImage("ModernHouse.png");
        }

        if (getWorld() != null) {
            resize(((Game)getWorld()).getMap().getCellSize());
        }
    }

    /**
     * Returns the image of this building
     * 
     * @return          A maximum resolution image of this Building according
     * to the current age
     */
    public GreenfootImage getOriginalImage(){
        return getImage(owner);
    }

    /**
     * Returns the image that an instance of this class would have
     * if it were to spawn. 
     * 
     * @param owner     The Player that would be the owner of this
     * @return          The desired image
     */
    public static GreenfootImage getImage(Player owner) {
        if (owner.getAge() == 1) {
            return new GreenfootImage("House.png");
        } else if (owner.getAge() == 2) {
            return new GreenfootImage("ColonialHouse.png");
        } else {
            return new GreenfootImage("ModernHouse.png");
        }
    }
} 

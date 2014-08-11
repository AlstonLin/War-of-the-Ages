import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * A Wall acts as a defense barrier, cannot be traversed
 * 
 * @author Alston Lin, Koko Deng
 * @version 1.0
 */
public class Wall extends Building
{   
    //Constants 
    public static final int WOOD_COST = 5, GOLD_COST = 5, BUILD_TIME = 50;
    public static final GreenfootImage ORIGINAL = new GreenfootImage("Wall.png");

    /**
     * Sets the Wall's image and location
     * 
     * @param owner     The Player that owns this
     * @param x         The x index of the cell on the map
     * @param y         The y index of the cell on the map
     * @param cellSize  The size of each side 
     */
    public Wall(Player owner, int x, int y, int cellSize) {
        super(owner, x, y, cellSize);

        this.spawnPoint = spawnPoint;

        //Information about this object      
        structureName = "Wall";
        health = 1000;
    } 

    /**
     * Returns information of the Barrack
     * 
     * @return      Information to display
     */
    public String getInformation() {
        stats = "{" + structureName + "} Health: " + health;
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
    }

    /**
     * Sets the image of the building according to the age
     * 
     * @param age   The new age of the Building
     */
    public void setAge(int age) {
        if (getWorld() != null){
            resize(((Game)getWorld()).getMap().getCellSize());
        }
    }
    
        /**
     * Returns the image that an instance of this class would have
     * if it were to spawn. 
     * 
     * @param owner     The Player that would be the owner of this
     * @return          The desired image
     */
    public static GreenfootImage getImage(Player owner) {
                //Set image according to the age
        if (owner.getAge() == 1) {
            return new GreenfootImage("WoodWall.png");
        } else if (owner.getAge() == 2) {
            return new GreenfootImage("Wall.png");
        } else {
            return new GreenfootImage("BrickWall.png");
        }
    }

    /**
     * @return A maximum resolution image of this Building according
     * to the current age
     */
    public GreenfootImage getOriginalImage(){
        return getImage(owner);
    }
}
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * Represents a Building that is currently in progress in being built.
 * 
 * @author Alston Lin, Koko Deng
 * @version Beta 2.1
 */
public class ConstructionSite extends Building
{   
    //Instance variables
    private int timeLeft;
    private Building builtTo; 

    /**
     * Creates a new ConstructionSite that will start building the specified Building.'
     * 
     * @param owner         The Player that owns this
     * @param x             The x coordinate on the Map
     * @param y             The y coordinate on the Map
     * @param buildTime     The number of acts it will take to build the Building
     * @param builtTo       The Building that this will build into when done
     * @param spawnPoint    The SpawnPoint of where units will spawn into, or null if there wont be a spawnPoint
     * @param cellSize      The size of each side 
     */
    public ConstructionSite(Player owner, int x, int y, int buildTime, Building builtTo, SpawnPoint spawnpoint, int cellSize) {
        super(owner, x, y, cellSize);

        //Initiate variables
        timeLeft = buildTime;
        this.builtTo = builtTo;
        this.spawnPoint = spawnPoint;

        //Information about this building
        structureName = "Construction Site";
        health = 1;
    }

    /**
     * Act method, the Construction Site remains there until time runs out
     */
    public void act() {
        Game game = (Game)getWorld();
        //Lets the server build
        if (game.isMultiplayer() && !game.isHosting()){
            return;
        }
        timeLeft--;
        if (timeLeft == 0) { //Done building
            ((Game)getWorld()).getMap().setCell(getMapX(), getMapY(), builtTo);
        }
    }    

    /**
     * @return A maximum resolution image of this Building according
     * to the current age
     */
    public GreenfootImage getOriginalImage(){
        return getImage(owner);
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
     * Returns information of this Workshop
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
     * Destroys this Building and the SpawnPoint.
     */
    public void destroy() {
        super.destroy();
        if (spawnPoint != null) {
            spawnPoint.destroy();
        }
    }

    /**
     * Sets the image of the building according to the age
     * 
     * @param age   The new age of the Building
     */
    public void setAge(int age) {
        //Changes image according to the age
        if (age == 1) { //Medieval Age
            setImage("MedievalConstructionSite.png");
        } else if (age == 2) { //Colonial Age
            setImage("ColonialConstructionSite.png");
        } else if (age == 3) { //Modern age
            setImage("ModernConstructionSite.png");
        }

        //Resize image to scale
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
        if (owner.getAge() == 1) {
            return new GreenfootImage("MedievalConstructionSite.png");
        } else if (owner.getAge() == 2) {
            return new GreenfootImage("ColonialConstructionSite.png");
        } else {
            return new GreenfootImage("ModernConstructionSite.png");
        }
    }

    /**
     * Returns the amount of time it would take to build this
     * 
     * @return      The time it would take to build this.
     */
    public int getBuildTime() {
        return timeLeft;
    }

    /**
     * Returns the Building this will become
     * 
     * @return      The Building this will become
     */
    public Building getBuildTo() {
        return builtTo;
    }

    /**
     * Returns the Spawnpoint of this object
     * 
     * @return      The Spawnpoint of this ContructionSite, if there is one
     */
    public SpawnPoint getSpawnPoint() {
        return spawnPoint;
    }
}

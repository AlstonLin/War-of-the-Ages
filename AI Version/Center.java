import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * Center is able to spawn civilians and increase the in-game age
 * 
 * @author Alston Lin, Koko Deng
 * @version 1.0
 */
public class Center extends Building
{   
    //Constants
    public static final int WOOD_COST = 600, GOLD_COST = 100, BUILD_TIME = 600;
    public static final int AGE_UP_FOOD_COST = 800, AGE_UP_WOOD_COST = 1000, AGE_UP_GOLD_COST = 800, AGE_UP_TIME = 300;
    public static final GreenfootImage ORIGINAL = new GreenfootImage("Center.png");

    //Button pictures
    private static GreenfootImage civilianButton = new GreenfootImage("CivilianFront.png");
    private static GreenfootImage ageUpButton = new GreenfootImage("AgeUpButton.png");

    //Spawning Instance variables
    private LinkedList<Unit> queue; //Spawn queue
    private Unit currentSpawn;//The current Unit to spawn

    //Instance variables
    private double attack;
    private int range;
    private int attackSpeed;

    //Timer variables
    private int counter;
    private int ageUpCounter;

    /**
     * Sets the Center's image and location
     * 
     * @param owner         The Player that owns this
     * @param x             The x index of the cell on the map
     * @param y             The y index of the cell on the map
     * @param spawnPoint    This object's spawn point
     * @param cellSize      The size of each side 
     */
    public Center(Player owner, int x, int y, SpawnPoint spawnPoint, int cellSize) {
        super(owner, x, y, cellSize);

        traversable = false;
        this.spawnPoint = spawnPoint;
        queue = new LinkedList();

        //Information about this building  
        structureName = "Center";
        health = 2000;

        attack = 10.00;
        range = 30;
        attackSpeed = 2;

        ageUpCounter = AGE_UP_TIME;
    }

    /**
     * Act method, spawns the Unit(s)
     */
    public void act() {
        ageUp();
        spawnUnits();
    }

    /**
     * Returns information of this object.
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
     * Returns a LinkedList of this objects's buttons.
     * 
     * @return      LinkedList of this objects's buttons
     */
    public LinkedList<Button> getSpecificButtons() {
        LinkedList list = new LinkedList();

        if (owner.getID() == ((Game)getWorld()).getUser().getID()) {
            updateImage();

            //Adds buttons onto Actions Panel
            list.add(new Button(civilianButton){ //Spawn Civilian
                    public void click() {
                        spawnCivilian();
                        updateImage();
                    }

                    public String getHoverInfo() {
                        return "<<Spawn Civilian>> Food Cost: " + Civilian.FOOD_COST ;
                    }
                });
            if (owner.getAge() != 3) { //Only displays the age up option if not in modern age
                updateImage();
                list.add(new Button(ageUpButton){ //Age Up
                        public void click() {
                            if (!owner.isAgingUp()) {
                                Game game = (Game)getWorld();
                                if (game.isMultiplayer() && !game.isHosting()){ //Client
                                    game.sendRequest(new Request(Request.AGE_UP, owner)); 
                                } else{
                                    if (owner.getFood() >= AGE_UP_FOOD_COST && owner.getWood() >= AGE_UP_WOOD_COST && owner.getGold() >= AGE_UP_GOLD_COST) {
                                        owner.setAgingUp(true);
                                        updateImage();
                                        owner.changeFood((Game)getWorld(), -AGE_UP_FOOD_COST);
                                        owner.changeWood((Game)getWorld(), -AGE_UP_WOOD_COST);
                                        owner.changeGold((Game)getWorld(), -AGE_UP_GOLD_COST);
                                        if (game.isHosting()){ //Server
                                            game.sendUpdate(new Update(Update.AGE_UP, owner)); 
                                        } 
                                    }
                                }
                            }
                        }

                        public String getHoverInfo() {
                            return "<<Age Up>> Food Cost: " + AGE_UP_FOOD_COST + " Wood Cost: " + AGE_UP_WOOD_COST + 
                            " Gold Cost: " + AGE_UP_GOLD_COST + " Time: " + AGE_UP_TIME;
                        }
                    });
            } 
        }

        return list;
    }

    /**
     * Spawns a Civilian
     */
    public void spawnCivilian() {
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= Civilian.FOOD_COST 
        && owner.getWood() >= Civilian.WOOD_COST && owner.getGold() >= Civilian.GOLD_COST) {
            owner.changeFood((Game)getWorld(), -Civilian.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Civilian.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Civilian.GOLD_COST);
            Civilian civilian = new Civilian(owner, spawnPoint, ((Game) getWorld()).getMap());                
            queue.add(civilian);

            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Spawns a Unit, each Unit has a spawn time, a queue is used to ensure only one Unit spawns at a time
     */
    public void spawnUnits() {
        if (currentSpawn != null) { //Currently spawning something
            if (counter == 0) { //Ready to spawn
                Game game = (Game)getWorld();
                if (!(game.isMultiplayer() && !game.isHosting())) { //Not Client
                    getWorld().addObject(currentSpawn, spawnPoint.getX(), spawnPoint.getY());
                    currentSpawn.setSpawned(true);
                }
                addUnitToMultiplayer(currentSpawn);
                currentSpawn = null;
            } else {
                counter--;
            }
        } else if (!queue.isEmpty()) { //Not spawning anything, but there are things to spawn
            currentSpawn = queue.removeFirst();
            counter = Civilian.SPAWN_TIME;
        }
    }

    /**
     * Ages up the owner to the next technological stage.
     */
    public void ageUp() {
        if (owner.isAgingUp()) { 
            if (ageUpCounter == 0) {
                owner.setAge((Game)getWorld(), owner.getAge() + 1);
                owner.setAgingUp(false);
                setAge(owner.getAge());
                updateImage();
                ageUpCounter = AGE_UP_TIME;
            } else {
                ageUpCounter--;
            }
        }
    }
    
    /**
     * Ages up the server in multiplayer mode.
     */
    public void serverAgeUp(){
        Game game = (Game)getWorld();
        if (owner.getFood() >= AGE_UP_FOOD_COST && owner.getWood() >= AGE_UP_WOOD_COST && owner.getGold() >= AGE_UP_GOLD_COST) {
            owner.setAgingUp(true);
            updateImage();
            owner.changeFood((Game)getWorld(), -AGE_UP_FOOD_COST);
            owner.changeWood((Game)getWorld(), -AGE_UP_WOOD_COST);
            owner.changeGold((Game)getWorld(), -AGE_UP_GOLD_COST);
            if (game.isHosting()){ //Server
                game.sendUpdate(new Update(Update.AGE_UP, owner)); 
            } 
        }
    }

    /**
     * Ages up a client in multiplayer mode.
     */
    public void clientAgeUp(){
        owner.setAgingUp(true);
        updateImage();
        owner.changeFood((Game)getWorld(), -AGE_UP_FOOD_COST);
        owner.changeWood((Game)getWorld(), -AGE_UP_WOOD_COST);
        owner.changeGold((Game)getWorld(), -AGE_UP_GOLD_COST);
    }

    /**
     * Destroys this Building and the SpawnPoint.
     */
    public void destroy() {
        if (owner.getID() == ((Game)getWorld()).getUser().getID()) {
            getWorld().addObject(new EndTitle(false), 554, 269);
        } else {
            getWorld().addObject(new EndTitle(true), 554, 269);
        }

        super.destroy();
        spawnPoint.destroy();
    }

    /**
     * Updates the various button images. 
     * Button will appear transluscent if there are not enough resources to spawn the Unit.
     */
    private void updateImage() {
        //For Civilian
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < Civilian.FOOD_COST 
        || owner.getWood() < Civilian.WOOD_COST || owner.getGold() < Civilian.GOLD_COST) {
            civilianButton.setTransparency(100);
        } else {
            civilianButton.setTransparency(255);
        }

        //For Age Up
        if (owner.isAgingUp()) {
            ageUpButton.setTransparency(100);
        } else {
            ageUpButton.setTransparency(255);
        }
    }

    /**
     * Sets the image of the building according to the age
     * 
     * @param age       The new age of the Building
     */
    public void setAge(int age) {
        //Set image according to the age
        if (age == 1) { //Medieval Age
            setImage("Center.png");
        } else if (age == 2) { //Colonial Age
            setImage("ColonialCenter.png");
        } else if (age == 3) { //Modern age
            setImage("ModernCenter.png");
        }

        if (getWorld() != null) {
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
            return new GreenfootImage("Center.png");
        } else if (owner.getAge() == 2) {
            return new GreenfootImage("ColonialCenter.png");
        } else {
            return new GreenfootImage("ModernCenter.png");
        }
    }

    /**
     * Returns the image of this building.
     * 
     * @return A maximum resolution image of this Building according
     * to the current age
     */
    public GreenfootImage getOriginalImage(){
        return getImage(owner);
    }
}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * Stable is where the Player can spawn various Cavalry Units
 * 
 * @author Koko Deng, Alston Lin
 * @version 1.0
 */
public class Stable extends Building
{
    //Constants
    public static final int WOOD_COST = 200, GOLD_COST = 0, BUILD_TIME = 300;

    //Button images
    private static GreenfootImage knightButton = new GreenfootImage("KnightFront.png");
    private static GreenfootImage lancerButton = new GreenfootImage("LancerRight.png");
    private static GreenfootImage mountedArcherButton = new GreenfootImage("MountedArcherRight.png");
    private static GreenfootImage horsemanButton = new GreenfootImage("HorsemanFront.png");
    private static GreenfootImage dragoonButton = new GreenfootImage("DragoonRight.png");
    private static GreenfootImage tankButton = new GreenfootImage("TankRight.png");
    private static GreenfootImage lavButton = new GreenfootImage("LAVRight.png");

    //Instance variables
    private LinkedList<Unit> queue; //Spawn queue
    private Unit currentSpawn;//The current Unit to spawn
    private int counter;//Timer for object spawning

    /**
     * Sets the Stable's image and location
     * 
     * @param owner     The Player that owns this
     * @param x         The x index of the cell on the map
     * @param y         The y index of the cell on the map
     * @param cellSize  The size of each side 
     */
    public Stable(Player owner, int x, int y, SpawnPoint spawnPoint, int cellSize) {
        super(owner, x, y, cellSize);

        this.spawnPoint = spawnPoint;
        queue = new LinkedList();

        //Information about this building     
        structureName = "Stable";
        health = 1200;
    } 

    /**
     * Act method, spawns the Unit(s)
     */
    public void act() {
        spawnUnits();
        setAge(owner.getAge());
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
        LinkedList list = new LinkedList();

        //Adds buttons onto Actions Panel
        if (owner.getAge() == 1 && owner.getID() == ((Game)getWorld()).getUser().getID()) { //Medieval 
            updateButtonImage();
            list.add(new Button(lancerButton){ //Spawn Lancer
                    public void click() {
                        spawnLancer();
                        updateButtonImage();
                    }

                    public String getHoverInfo() {
                        return "Food: " + Lancer.FOOD_COST + " | Wood:" + Lancer.WOOD_COST + " | Gold:" + Lancer.GOLD_COST + " | Training time: " + Lancer.SPAWN_TIME;
                    }
                });
        } else if (owner.getAge() == 2 && owner.getID() == ((Game)getWorld()).getUser().getID()) { //Colonial Age
            list.add(new Button(horsemanButton){ //Spawn Horseman
                    public void click() {
                        spawnHorseman();
                        updateButtonImage();
                    }

                    public String getHoverInfo() {
                        return "Food: " + Horseman.FOOD_COST + " | Wood:" + Horseman.WOOD_COST + " | Gold:" + Horseman.GOLD_COST + " | Training time: " + Horseman.SPAWN_TIME;
                    }
                });
            list.add(new Button(dragoonButton){ //Spawn Dragoon
                    public void click() {
                        spawnDragoon();
                        updateButtonImage();
                    }

                    public String getHoverInfo() {
                        return "Food: " + Dragoon.FOOD_COST + " | Wood:" + Dragoon.WOOD_COST + " | Gold:" + Dragoon.GOLD_COST + " | Training time: " + Dragoon.SPAWN_TIME;
                    }
                });
        } else if (owner.getAge() == 3 && owner.getID() == ((Game)getWorld()).getUser().getID()) { //Modern
            list.add(new Button(tankButton){ //Spawn Tank
                    public void click() {
                        spawnTank();
                        updateButtonImage();
                    }

                    public String getHoverInfo() {
                        return "Food: " + Tank.FOOD_COST + " | Wood:" + Tank.WOOD_COST + " | Gold:" + Tank.GOLD_COST + " | Build time: " + Tank.SPAWN_TIME;
                    }
                });
            list.add(new Button(lavButton){ //Spawn LAV
                    public void click() {
                        spawnLAV();
                        updateButtonImage();
                    }

                    public String getHoverInfo() {
                        return "Food: " + LAV.FOOD_COST + " | Wood:" + LAV.WOOD_COST + " | Gold:" + Tank.GOLD_COST + " | Build time: " + LAV.SPAWN_TIME;
                    }
                }); 
        } else { //This should never be called
            throw new IllegalStateException();
        }

        return list;
    }

    /**
     * Spawns a Unit, each Unit has a spawn time, a queue is used to ensure only one Unit spawns at a time
     */
    public void spawnUnits() {
        if (currentSpawn != null){ //Currently spawning something
            if (counter == 0){ //Ready to spawn
                Game game = (Game)getWorld();
                getWorld().addObject(currentSpawn, spawnPoint.getX(), spawnPoint.getY());
                if (!(game.isMultiplayer() && !game.isHosting())){ //Not Client
                    currentSpawn.setSpawned(true);
                    addUnitToMultiplayer(currentSpawn);
                }
                currentSpawn = null;
            } else {
                counter--;
            }
        } else if (!queue.isEmpty()){ //Not spawning anything, but there are things to spawn
            currentSpawn = queue.removeFirst();
            if (owner.getAge() == 1) {if (currentSpawn instanceof Lancer){
                    counter = Lancer.SPAWN_TIME;
                }
            } else if (owner.getAge() == 2) {
                if (currentSpawn instanceof Horseman){
                    counter = Horseman.SPAWN_TIME;
                } else if (currentSpawn instanceof Dragoon){
                    counter = Dragoon.SPAWN_TIME;
                }
            } else if (owner.getAge() == 3) {
                if (currentSpawn instanceof Tank){
                    counter = Tank.SPAWN_TIME;
                }
            }   
        }
    }

    /**
     * Spawns a Lancer
     */
    public void spawnLancer() {
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= Lancer.FOOD_COST 
        && owner.getWood() >= Lancer.WOOD_COST && owner.getGold() >= Lancer.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -Lancer.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Lancer.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Lancer.GOLD_COST);

            queue.add(new Lancer(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Spawns a Horseman
     */
    public void spawnHorseman() {        
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= Horseman.FOOD_COST 
        && owner.getWood() >= Horseman.WOOD_COST && owner.getGold() >= Horseman.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -Horseman.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Horseman.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Horseman.GOLD_COST);

            queue.add(new Horseman(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Spawns a Dragoon
     */
    public void spawnDragoon() {        
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= Dragoon.FOOD_COST 
        && owner.getWood() >= Dragoon.WOOD_COST && owner.getGold() >= Dragoon.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -Dragoon.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Dragoon.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Dragoon.GOLD_COST);

            queue.add(new Dragoon(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Spawns a Tank
     */
    public void spawnTank() {        
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= Tank.FOOD_COST 
        && owner.getWood() >= Tank.WOOD_COST && owner.getGold() >= Tank.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -Tank.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Tank.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Tank.GOLD_COST);

            queue.add(new Tank(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Spawns a LAV
     */
    public void spawnLAV() {        
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= LAV.FOOD_COST 
        && owner.getWood() >= LAV.WOOD_COST && owner.getGold() >= LAV.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -LAV.FOOD_COST);
            owner.changeWood((Game)getWorld(), -LAV.WOOD_COST);
            owner.changeGold((Game)getWorld(), -LAV.GOLD_COST);

            queue.add(new LAV(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }

    }

    /**
     * Destroys this Building and the SpawnPoint.
     */
    public void destroy() {
        super.destroy();
        spawnPoint.destroy();
    }

    /**
     * Updates the various button images. 
     * Button will appear transluscent if there are not enough resources to spawn the Unit.
     */
    private void updateButtonImage() {
        //For Lancer
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < Lancer.FOOD_COST || owner.getWood() < Lancer.WOOD_COST 
        || owner.getGold() < Lancer.GOLD_COST) {
            lancerButton.setTransparency(100);
        } else {
            lancerButton.setTransparency(255);
        }

        //For Horseman
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < Horseman.FOOD_COST || owner.getWood() < Horseman.WOOD_COST 
        || owner.getGold() < Horseman.GOLD_COST) {
            horsemanButton.setTransparency(100);
        } else {
            horsemanButton.setTransparency(255);
        }

        //For Dragoon
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < Dragoon.FOOD_COST || owner.getWood() < Dragoon.WOOD_COST 
        || owner.getGold() < Dragoon.GOLD_COST) {
            dragoonButton.setTransparency(100);
        } else {
            dragoonButton.setTransparency(255);
        }

        //For Tank
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < Tank.FOOD_COST || owner.getWood() < Tank.WOOD_COST 
        || owner.getGold() < Tank.GOLD_COST) {
            tankButton.setTransparency(100);
        } else {
            tankButton.setTransparency(255);
        }

        //For LAV
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < LAV.FOOD_COST || owner.getWood() < LAV.WOOD_COST 
        || owner.getGold() < LAV.GOLD_COST) {
            lavButton.setTransparency(100);
        } else {
            lavButton.setTransparency(255);
        }
    }

    /**
     * Sets the image of the building according to the age
     * 
     * @param age   The new age of the Building
     */
    public void setAge(int age) {
        if (age == 1) { //Medieval Age
            setImage("Stable.png");
        } else if (age == 2) { //Colonial Age
            setImage("ColonialStable.png");
        } else if (age == 3) { //Modern age
            setImage("ModernStable.png");
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
            return new GreenfootImage("Stable.png");
        } else if (owner.getAge() == 2) {
            return new GreenfootImage("ColonialStable.png");
        } else {
            return new GreenfootImage("ModernStable.png");
        }
    }

    /**
     * Returns the image of this building
     * 
     * @return A maximum resolution image of this Building according
     * to the current age
     */
    public GreenfootImage getOriginalImage(){
        return getImage(owner);
    }
}

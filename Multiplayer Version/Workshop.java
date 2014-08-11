import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * Write a description of class Workshop here.
 * 
 * @author Koko Deng, Alston Lin
 * @version 1.0
 */
public class Workshop extends Building
{   
    //Constants
    public static final int WOOD_COST = 250, GOLD_COST = 0, BUILD_TIME = 300;

    //Button pictures
    private static GreenfootImage batteringRamButton = new GreenfootImage("BatteringRamRight.png");
    private static GreenfootImage catapultButton = new GreenfootImage("CatapultFront.png");
    private static GreenfootImage cannonButton = new GreenfootImage("CannonRight.png");
    private static GreenfootImage organGunButton = new GreenfootImage("OrganGunLeft.png");
    private static GreenfootImage mortarButton = new GreenfootImage("MortarRight.png");

    //Spawning Instance variables
    private LinkedList<Unit> queue; //Spawn queue
    private Unit currentSpawn;//The current Unit to spawn
    private int counter;//Timer for object spawning

    /**
     * Sets the Workshop's image and location
     * 
     * @param owner     The Player that owns this
     * @param x         The x index of the cell on the map
     * @param y         The y index of the cell on the map
     * @param cellSize  The size of each side 
     */
    public Workshop(Player owner, int x, int y, SpawnPoint spawnPoint, int cellSize) {
        super(owner, x, y, cellSize);

        this.spawnPoint = spawnPoint;
        queue = new LinkedList();

        //Information about this building
        structureName = "Workshop";
        health = 1200;
    } 

    /**
     * Act method, spawns the Unit(s)
     */
    public void act() {
        spawnUnits();
    }

    /**
     * Returns information of the Workshop
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
        if (owner.getAge() == 1 && owner.getID() == ((Game)getWorld()).getUser().getID()) { //Medieval Age
            updateButtonImage();

            //Adds buttons onto Actions Panel
            list.add(new Button(batteringRamButton){ //Spawn Battering Ram
                    public void click() {
                        spawnBatteringRam();
                        updateButtonImage();
                    }

                    public String getHoverInfo() {
                        return "Food: " + BatteringRam.FOOD_COST + " | Wood:" + BatteringRam.FOOD_COST + " | Gold:" + BatteringRam.GOLD_COST + " | Training time: " + BatteringRam.SPAWN_TIME;
                    }
                });
            list.add(new Button(catapultButton){ //Spawn Catapult
                    public void click() {
                        spawnCatapult();
                        updateButtonImage();
                    }

                    public String getHoverInfo() {
                        return "Food: " + Catapult.FOOD_COST + " | Wood:" + Catapult.FOOD_COST + " | Gold:" + Catapult.GOLD_COST + " | Training time: " + Catapult.SPAWN_TIME;
                    }
                });
        } else if (owner.getAge() == 2 && owner.getID() == ((Game)getWorld()).getUser().getID()) { //Colonial Age
            list.add(new Button(cannonButton){ //Spawn Cannon
                    public void click() {
                        spawnCannon();
                        updateButtonImage();
                    }

                    public String getHoverInfo() {
                        return "Food: " + Cannon.FOOD_COST + " | Wood:" + Cannon.WOOD_COST + " | Gold:" + Cannon.GOLD_COST + " | Training time: " + Cannon.SPAWN_TIME;
                    }
                });
        } else if (owner.getAge() == 3 && owner.getID() == ((Game)getWorld()).getUser().getID()) { //Modern Age
            list.add(new Button(mortarButton){ //Spawn Mortar
                    public void click() {
                        spawnMortar();
                        updateButtonImage();
                    }

                    public String getHoverInfo() {
                        return "Food: " + Mortar.FOOD_COST + " | Wood:" + Mortar.WOOD_COST + " | Gold:" + Mortar.GOLD_COST + " | Training time: " + Mortar.SPAWN_TIME;
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
                if (!(game.isMultiplayer() && !game.isHosting())){ //Not Client
                    getWorld().addObject(currentSpawn, spawnPoint.getX(), spawnPoint.getY());
                    currentSpawn.setSpawned(true);
                }
                addUnitToMultiplayer(currentSpawn);
                currentSpawn = null;
            } else {
                counter--;
            }
        } else if (!queue.isEmpty()){ //Not spawning anything, but there are things to spawn
            currentSpawn = queue.removeFirst();
            if (owner.getAge() == 1) {
                if (currentSpawn instanceof BatteringRam){
                    counter = BatteringRam.SPAWN_TIME;
                } else if (currentSpawn instanceof Catapult){
                    counter = Catapult.SPAWN_TIME;
                }
            } else if (owner.getAge() == 2) {
                if (currentSpawn instanceof Cannon){
                    counter = Cannon.SPAWN_TIME;
                }            } else if (owner.getAge() == 3) {
                if (currentSpawn instanceof Mortar){
                    counter = Mortar.SPAWN_TIME;
                }
            }   
        }
    }

    /**
     * Spawns a BatteringRam
     */
    public void spawnBatteringRam() {        
        if (owner.getFood() >= BatteringRam.FOOD_COST &&  owner.getWood() >= BatteringRam.WOOD_COST && owner.getGold() >= BatteringRam.GOLD_COST) {
            owner.changeFood((Game)getWorld(), -BatteringRam.FOOD_COST);
            owner.changeWood((Game)getWorld(), -BatteringRam.WOOD_COST);
            owner.changeGold((Game)getWorld(), -BatteringRam.GOLD_COST);

            //Add spawn into queue
            queue.add(new BatteringRam(owner, spawnPoint, ((Game) getWorld()).getMap()));
        }
    }

    /**
     * Spawns a Catapult
     */
    public void spawnCatapult() {        
        if (owner.getFood() >= Catapult.FOOD_COST &&  owner.getWood() >= Catapult.WOOD_COST && owner.getGold() >= Catapult.GOLD_COST) {
            owner.changeFood((Game)getWorld(), -Catapult.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Catapult.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Catapult.GOLD_COST);        

            //Add spawn into queue
            queue.add(new Catapult(owner, spawnPoint, ((Game) getWorld()).getMap()));
        }
    }

    /**
     * Spawns a Cannon
     */
    public void spawnCannon() {        
        if (owner.getFood() >= Cannon.FOOD_COST &&  owner.getWood() >= Cannon.WOOD_COST && owner.getGold() >= Cannon.GOLD_COST) {
            owner.changeFood((Game)getWorld(), -Cannon.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Cannon.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Cannon.GOLD_COST);            

            Cannon cannon = new Cannon(owner, spawnPoint, ((Game) getWorld()).getMap());
            queue.add(cannon);
        }
    }

    /**
     * Spawns a Mortar
     */
    public void spawnMortar() {        
        if (owner.getFood() >= Mortar.FOOD_COST &&  owner.getWood() >= Mortar.WOOD_COST && owner.getGold() >= Mortar.GOLD_COST) {
            owner.changeFood((Game)getWorld(), -Mortar.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Mortar.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Mortar.GOLD_COST);      

            Mortar mortar = new Mortar(owner, spawnPoint, ((Game) getWorld()).getMap()); queue.add(mortar);
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Destroys this Building and the SpawnPoint
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
        //For Battering Ram
        if (owner.getFood() < BatteringRam.FOOD_COST || owner.getWood() < BatteringRam.WOOD_COST 
        || owner.getGold() < BatteringRam.GOLD_COST) {
            batteringRamButton.setTransparency(100);
        } else {
            batteringRamButton.setTransparency(255);
        }

        //For Catapult
        if (owner.getFood() < Catapult.FOOD_COST || owner.getWood() < Catapult.WOOD_COST 
        || owner.getGold() < Catapult.GOLD_COST) {
            catapultButton.setTransparency(100);
        } else {
            catapultButton.setTransparency(255);
        }

        //For Cannon
        if (owner.getFood() < Cannon.FOOD_COST || owner.getWood() < Cannon.WOOD_COST 
        || owner.getGold() < Cannon.GOLD_COST) {
            cannonButton.setTransparency(100);
        } else {
            cannonButton.setTransparency(255);
        }

        //For Mortar
        if (owner.getFood() < Mortar.FOOD_COST || owner.getWood() < Mortar.WOOD_COST 
        || owner.getGold() < Mortar.GOLD_COST) {
            mortarButton.setTransparency(100);
        } else {
            mortarButton.setTransparency(255);
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
            setImage("Workshop.png");
        } else if (age == 2) { //Colonial Age
            setImage("ColonialWorkshop.png");
        } else if (age == 3) { //Modern age
            setImage("ModernWorkshop.png");
        }

        if (getWorld() != null) {
            resize(((Game)getWorld()).getMap().getCellSize());
        }
    }

    /**
     * Returns the image of this building
     * 
     * @return      A maximum resolution image of this Building according
     * to the current age
     */
    public GreenfootImage getOriginalImage() {
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
            return new GreenfootImage("Workshop.png");
        } else if (owner.getAge() == 2) {
            return new GreenfootImage("ColonialWorkshop.png");
        } else {
            return new GreenfootImage("ModernWorkshop.png");
        }
    }
}

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * Barracks is where the Spearman, Swordsman, and Archer spawn.
 * 
 * @author Koko Deng, Alston Lin
 * @version 1.0
 */
public class Barracks extends Building
{   
    //Constants
    public static final int WOOD_COST = 200, GOLD_COST = 0, BUILD_TIME = 300;

    //Button pictures
    private static GreenfootImage spearmanButton = new GreenfootImage("SpearmanFront.png");
    private static GreenfootImage swordsmanButton = new GreenfootImage("SwordsmanFrontLeft.png");
    private static GreenfootImage archerButton = new GreenfootImage("ArcherRight.png");
    private static GreenfootImage pikemanButton = new GreenfootImage("PikemanRight.png");
    private static GreenfootImage musketeerButton = new GreenfootImage("MusketeerRight.png");
    private static GreenfootImage colonialArcherButton = new GreenfootImage("ColonialArcherFrontRight.png");
    private static GreenfootImage riflemanButton = new GreenfootImage("RiflemanRight.png");
    private static GreenfootImage machineGunButton = new GreenfootImage("MachineGunRight.png");
    private static GreenfootImage sniperButton = new GreenfootImage("SniperRight.png");
    private static GreenfootImage rpgButton = new GreenfootImage("RPGRight.png");

    //Spawning Instance variables
    private LinkedList<Unit> queue; //Spawn queue
    private Unit currentSpawn;//The current Unit to spawn
    private int counter;//Timer for object spawning

    /**
     * Sets the Barracks's image and location
     * 
     * @param owner     The Player that owns this
     * @param x         The x index of the cell on the map
     * @param y         The y index of the cell on the map
     * @param cellSize  The size of each side 
     */
    public Barracks (Player owner, int x, int y, SpawnPoint spawnPoint, int cellSize) {
        super(owner, x, y, cellSize);

        this.spawnPoint = spawnPoint;
        queue = new LinkedList();

        //Information about this building
        structureName = "Barracks";           
        health = 1200;
    } 

    /**
     * Act method, spawns the Unit(s)
     */
    public void act() {
        spawnUnits();
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

        if (owner.getID() == ((Game)getWorld()).getUser().getID()){
            updateImage();

            //Adds buttons onto Actions Panel
            if (owner.getAge() == 1) { //Medieval Age
                list.add(new Button(spearmanButton){ //Spawns Spearman
                        public void click() {
                            spawnSpearman();
                            updateImage();
                        }

                        public String getHoverInfo() {
                            return "Food: " + Spearman.FOOD_COST + " | Wood:" + Spearman.WOOD_COST + " | Gold:" + Spearman.GOLD_COST + " | Training time: " + Spearman.SPAWN_TIME;
                        }
                    });
                list.add(new Button(swordsmanButton){ //Spawns Swordsman
                        public void click() {
                            spawnSwordsman();
                            updateImage();
                        }

                        public String getHoverInfo() {
                            return "Food: " + Swordsman.FOOD_COST + " | Wood:" + Swordsman.WOOD_COST + " | Gold:" + Swordsman.GOLD_COST + " | Training time: " + Swordsman.SPAWN_TIME;
                        }
                    });
                list.add(new Button(archerButton){ //Spawns Archer
                        public void click() {
                            spawnArcher();
                            updateImage();
                        }

                        public String getHoverInfo() {
                            return "Food: " + Archer.FOOD_COST + " | Wood:" + Archer.WOOD_COST + " | Gold:" + Archer.GOLD_COST + " | Training time: " + Archer.SPAWN_TIME;
                        }
                    }); 
            } else if (owner.getAge() == 2) { //Colonial Age
                list.add(new Button(pikemanButton){ //Spawns Pikeman
                        public void click() {
                            spawnPikeman();
                            updateImage();
                        }

                        public String getHoverInfo() {
                            return "Food: " + Pikeman.FOOD_COST + " | Wood:" + Pikeman.WOOD_COST + " | Gold:" + Pikeman.GOLD_COST + " | Training time: " + Pikeman.SPAWN_TIME;
                        }
                    });
                list.add(new Button(musketeerButton){ //Spawns Musketeer
                        public void click() {
                            spawnMusketeer();
                            updateImage();
                        }

                        public String getHoverInfo() {
                            return "Food: " + Musketeer.FOOD_COST + " | Wood:" + Musketeer.WOOD_COST + " | Gold:" + Musketeer.GOLD_COST + " | Training time: " + Musketeer.SPAWN_TIME;
                        }
                    });
                list.add(new Button(colonialArcherButton){ //Spawns Colonial Archer
                        public void click() {
                            spawnColonialArcher();
                            updateImage();
                        }

                        public String getHoverInfo() {
                            return "Food: " + ColonialArcher.FOOD_COST + " | Wood:" + ColonialArcher.WOOD_COST + " | Gold:" + ColonialArcher.GOLD_COST + " | Training time: " + ColonialArcher.SPAWN_TIME;
                        }
                    }); 
            } else if (owner.getAge() == 3) { //Modern Age
                list.add(new Button(riflemanButton){ //Spawns Rifleman
                        public void click() {
                            spawnRifleman();
                            updateImage();
                        }

                        public String getHoverInfo() {
                            return "Food: " + Rifleman.FOOD_COST + " | Wood:" + Rifleman.WOOD_COST + " | Gold:" + Rifleman.GOLD_COST + " | Training time: " + Rifleman.SPAWN_TIME;
                        }
                    });
                list.add(new Button(machineGunButton){ //Spawns Machine Gun
                        public void click() {
                            spawnRPG();
                            updateImage();
                        }

                        public String getHoverInfo() {
                            return "Food: " + RPG.FOOD_COST + " | Wood:" + RPG.WOOD_COST + " | Gold:" + RPG.GOLD_COST + " | Training time: " + RPG.SPAWN_TIME;
                        }
                    });

                list.add(new Button(sniperButton){ //Spawns Sniper
                        public void click() {                    
                            spawnSniper();
                            updateImage();
                        }

                        public String getHoverInfo() {
                            return "Food: " + Sniper.FOOD_COST + " | Wood:" + Sniper.WOOD_COST + " | Gold:" + Sniper.GOLD_COST + " | Training time: " + Sniper.SPAWN_TIME;
                        }
                    }); 
            } else { //This should never be called
                throw new IllegalStateException();
            }
        }

        return list;
    }

    /**
     * Spawns a Unit, each Unit has a spawn time, a queue is used to ensure only one Unit spawns at a time
     */
    public void spawnUnits() {
        if (currentSpawn != null) { //Currently spawning something
            if (counter == 0){ //Ready to spawn
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
            if (owner.getAge() == 1) {
                if (currentSpawn instanceof Spearman){
                    counter = Spearman.SPAWN_TIME;
                } else if (currentSpawn instanceof Swordsman){
                    counter = Swordsman.SPAWN_TIME;
                } else if (currentSpawn instanceof Archer){
                    counter = Archer.SPAWN_TIME;
                }
            } else if (owner.getAge() == 2){
                if (currentSpawn instanceof Pikeman){
                    counter = Pikeman.SPAWN_TIME;
                } else if (currentSpawn instanceof Musketeer){
                    counter = Musketeer.SPAWN_TIME;
                } else if (currentSpawn instanceof ColonialArcher){
                    counter = ColonialArcher.SPAWN_TIME;
                }
            } else if (owner.getAge() == 3){
                if (currentSpawn instanceof Rifleman){
                    counter = Rifleman.SPAWN_TIME;
                }else if (currentSpawn instanceof Sniper){
                    counter = Sniper.SPAWN_TIME;
                }
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
     * Spawns a Spearman
     */
    public void spawnSpearman() {
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= Spearman.FOOD_COST 
        && owner.getWood() >= Spearman.WOOD_COST && owner.getGold() >= Spearman.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -Spearman.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Spearman.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Spearman.GOLD_COST);

            queue.add(new Spearman(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Spawns a Swordsman
     */
    public void spawnSwordsman() {
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= Swordsman.FOOD_COST 
        && owner.getWood() >= Swordsman.WOOD_COST && owner.getGold() >= Swordsman.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -Swordsman.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Swordsman.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Swordsman.GOLD_COST);

            queue.add(new Swordsman(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Spawns a Archer
     */
    public void spawnArcher() {
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= Archer.FOOD_COST 
        && owner.getWood() >= Archer.WOOD_COST && owner.getGold() >= Archer.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -Archer.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Archer.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Archer.GOLD_COST);

            queue.add(new Archer(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Spawns a Pikeman
     */
    public void spawnPikeman() {
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= Pikeman.FOOD_COST 
        && owner.getWood() >= Pikeman.WOOD_COST && owner.getGold() >= Pikeman.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -Pikeman.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Pikeman.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Pikeman.GOLD_COST);

            queue.add(new Pikeman(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Spawns a RPG
     */
    public void spawnRPG() {
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= RPG.FOOD_COST 
        &&  owner.getWood() >= RPG.WOOD_COST && owner.getGold() >= RPG.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -RPG.FOOD_COST);
            owner.changeWood((Game)getWorld(), -RPG.WOOD_COST);
            owner.changeGold((Game)getWorld(), -RPG.GOLD_COST);

            queue.add(new RPG(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Spawns a Musketeer
     */
    public void spawnMusketeer() {
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= Musketeer.FOOD_COST 
        && owner.getWood() >= Musketeer.WOOD_COST && owner.getGold() >= Musketeer.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -Musketeer.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Musketeer.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Musketeer.GOLD_COST);

            queue.add(new Musketeer(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Spawns a ColonialArcher
     */
    public void spawnColonialArcher() {
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= ColonialArcher.FOOD_COST 
        && owner.getWood() >= ColonialArcher.WOOD_COST && owner.getGold() >= ColonialArcher.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -ColonialArcher.FOOD_COST);
            owner.changeWood((Game)getWorld(), -ColonialArcher.WOOD_COST);
            owner.changeGold((Game)getWorld(), -ColonialArcher.GOLD_COST);

            queue.add(new ColonialArcher(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Spawns a Rifleman
     */
    public void spawnRifleman() {
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= Rifleman.FOOD_COST 
        && owner.getWood() >= Rifleman.WOOD_COST && owner.getGold() >= Rifleman.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -Rifleman.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Rifleman.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Rifleman.GOLD_COST);

            queue.add(new Rifleman(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Spawns a Sniper
     */
    public void spawnSniper() {
        //Cannot spawn more Units if max population is reached, if there are not enough resources
        if (owner.getMaxPopulation() > owner.getPopulation() && owner.getFood() >= Sniper.FOOD_COST 
        && owner.getWood() >= Sniper.WOOD_COST && owner.getGold() >= Sniper.GOLD_COST) {
            //Consumes resources
            owner.changeFood((Game)getWorld(), -Sniper.FOOD_COST);
            owner.changeWood((Game)getWorld(), -Sniper.WOOD_COST);
            owner.changeGold((Game)getWorld(), -Sniper.GOLD_COST);

            queue.add(new Sniper(owner, spawnPoint, ((Game) getWorld()).getMap()));
            Game game = (Game)getWorld();
            if (game.isMultiplayer() && !game.isHosting()) { //Bug Fix for clients
                Unit.IDCounter--;
            }
        }
    }

    /**
     * Updates the various button images. 
     * Button will appear transluscent if there are not enough resources to spawn the Unit.
     */
    private void updateImage() {
        //For Spearman
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < Spearman.FOOD_COST || 
        owner.getWood() < Spearman.WOOD_COST || owner.getGold() < Spearman.GOLD_COST) {
            spearmanButton.setTransparency(100);
        } else {
            spearmanButton.setTransparency(255);
        }

        //For Swordsman
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < Swordsman.FOOD_COST || 
        owner.getWood() < Swordsman.WOOD_COST || owner.getGold() < Swordsman.GOLD_COST) {
            swordsmanButton.setTransparency(100);
        } else {
            swordsmanButton.setTransparency(255);
        }

        //For Archer
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < Archer.FOOD_COST || 
        owner.getWood() < Archer.WOOD_COST || owner.getGold() < Archer.GOLD_COST) {
            archerButton.setTransparency(100);
        } else {
            archerButton.setTransparency(255);
        }

        //For Pikeman
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < Pikeman.FOOD_COST || 
        owner.getWood() < Pikeman.WOOD_COST || owner.getGold() < Pikeman.GOLD_COST) {
            pikemanButton.setTransparency(100);
        } else {
            pikemanButton.setTransparency(255);
        }

        //For RPG
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < RPG.FOOD_COST ||
        owner.getWood() < RPG.WOOD_COST || owner.getGold() < RPG.GOLD_COST) {
            rpgButton.setTransparency(100);
        } else {
            rpgButton.setTransparency(255);
        }

        //For Musketeer
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < Musketeer.FOOD_COST || 
        owner.getWood() < Musketeer.WOOD_COST || owner.getGold() < Musketeer.GOLD_COST) {
            musketeerButton.setTransparency(100);
        } else {
            musketeerButton.setTransparency(255);
        }

        //For Colonial Archer
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < ColonialArcher.FOOD_COST || owner.getWood() < ColonialArcher.WOOD_COST 
        || owner.getGold() < ColonialArcher.GOLD_COST) {
            colonialArcherButton.setTransparency(100);
        } else {
            colonialArcherButton.setTransparency(255);
        }

        //For Rifleman
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < Rifleman.FOOD_COST || owner.getWood() < Rifleman.WOOD_COST 
        || owner.getGold() < Rifleman.GOLD_COST) {
            riflemanButton.setTransparency(100);
        } else {
            riflemanButton.setTransparency(255);
        }

        //For Sniper
        if (owner.getMaxPopulation() <= owner.getPopulation() || owner.getFood() < Sniper.FOOD_COST || owner.getWood() < Sniper.WOOD_COST 
        || owner.getGold() < Sniper.GOLD_COST) {
            sniperButton.setTransparency(100);
        } else {
            sniperButton.setTransparency(255);
        }
    }

    /**
     * Sets the image of the building according to the age
     * 
     * @param age   The new age of the Building
     */
    public void setAge(int age) {
        if (age == 1) { //Medieval Age
            setImage("Barracks.png");
        } else if (age == 2) { //Colonial Age
            setImage("ColonialBarracks.png");
        } else if (age == 3) { //Modern age
            setImage("ModernBarracks.png");
        }
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
            return new GreenfootImage("Barracks.png");
        } else if (owner.getAge() == 2) {
            return new GreenfootImage("ColonialBarracks.png");
        } else {
            return new GreenfootImage("ModernBarracks.png");
        }
    }

    /**
     * Returns the image of this building
     * 
     * @return      A maximum resolution image of this Building according
     * to the current age
     */
    public GreenfootImage getOriginalImage(){
        return getImage(owner);
    }
}

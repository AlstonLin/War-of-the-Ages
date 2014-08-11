import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import greenfoot.*;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * An Artificial Intellegence that will run concurrently with the Game and
 * base it's strategy on copying the User.
 * 
 * @author Daniel Chung, Alston Lin
 * @version Beta 2.0
 */
public class AI extends Player 
{
    private static final int REFRESH_RATE = 5000; //How many ms it should wait in between runs
    private static final int BARRACKS = 1, STABLE = 2, WORKSHOP = 3, HOUSE = 4, CENTER = 5, WALL = 6;

    private int resourcePriority;
    private int civiliansInTraining;
    private int counter;
    private boolean enoughPop;
    private Barracks barracks;
    private Stable stable;
    private Workshop workshop;

    private int currentPopulation, maxPopulation;

    private transient Thread updateThread;
    private Player user;

    private Game game;
    private Center townCenter;
    private Random random;

    /**
     * Creates a new AI that will run concurrently with the Game for efficiency.
     * 
     * @param user The opponent of this Player
     */
    public AI(Player user, Game game){
        this.user = user;
        this.game = game;

        food = 4000;
        wood = 4000;
        gold = 4000;
        age = 1;
        counter = 0;

        currentPopulation = 0;
        maxPopulation = 0;
        enoughPop = false;
        units = new LinkedList();
        random = new Random();

        updateThread = new Thread(){
            public void run(){
                try{
                    Thread.currentThread().sleep(1000); //Allows the main thread to setup first
                    while (true){ //Thread will run until it is stopped
                        act();
                        Thread.currentThread().sleep(REFRESH_RATE);
                    }
                } catch(InterruptedException e){
                }
            }
        };
        updateThread.start();
    }

    /**
     * Entry point for the AI.
     */
    public void act(){
        counter++;
        enoughPop = enoughSpace();

        //Find the town center the first turn
        if (townCenter == null){
            //Find the center around which the buildings shall be built
            for (Building building : buildings){
                if (building instanceof Center && building.getOwner() == this){
                    townCenter = (Center) building;
                    break;
                }
            }

            barracks = ((Grass)(game.getMap().getCells()[townCenter.getMapX() - 15][townCenter.getMapY() - 10])).buildBarracksAuto((Player)this, townCenter.getMapX(), townCenter.getMapY());
            stable = ((Grass)(game.getMap().getCells()[townCenter.getMapX() - 10][townCenter.getMapY() - 3])).buildStableAuto((Player)this, townCenter.getMapX(), townCenter.getMapY());
            workshop = ((Grass)(game.getMap().getCells()[townCenter.getMapX() - 8][townCenter.getMapY() - 20])).buildWorkshopAuto((Player)this, townCenter.getMapX(), townCenter.getMapY());
        }

        if(user.getAge() != age){
            setAge(game, user.getAge());
        }

        if(counter % 3 == 0){
            manageCivilians();
        }
        matchUser();
        spawnArmy();
        deployArmy();
        buildWall();
    }

    /**
     * Randomly build walls
     */
    private void buildWall(){
        if(Greenfoot.getRandomNumber(5) == 0){
            place(WALL);
        }
    }

    /**
     * Determines what needs to be done.
     */
    private void matchUser(){
        //Determines if civilians need to be spawned
        int enemies = user.getUnits().size();
        int population = units.size();

        //The AI has significantly less Civilians than the User
        if (population < enemies){
            if (currentPopulation + 1 <= maxPopulation){
                center.spawnCivilian();
            } else {
                place(HOUSE);
            }
        }
    }

    /**
     * Manages the idle civilians
     */
    private void manageCivilians(){
        LinkedList<Civilian> civilians = new LinkedList();
        //Populates the list of idle civilians
        for (Unit unit : units){
            if (unit instanceof Civilian){
                civilians.add((Civilian)unit);
            }
        }
        //Tasks all of the idle civilians to something
        for (Civilian civilian : civilians){
            if (food < 500){
                BerryBush bush = (BerryBush)getNearestCell(civilian, BerryBush.class);
                if (bush != null){
                    civilian.selectCell(bush);
                }
            } else if (wood < 500){
                Tree tree = (Tree)getNearestCell(civilian, Tree.class);
                if (tree != null){
                    civilian.selectCell(tree);
                }
            } else if (gold < 500){
                Mine mine = (Mine)getNearestCell(civilian, Mine.class);
                if (mine != null){
                    civilian.selectCell(mine);
                }
            } else {
                if (civilian.getCurrentResource() == Civilian.NONE){
                    Random random = new Random();
                    int choice = random.nextInt(3);
                    switch(choice){
                        case 0:
                        BerryBush bush = (BerryBush)getNearestCell(civilian, BerryBush.class);
                        civilian.selectCell(bush);
                        break;
                        case 1:
                        Tree tree = (Tree)getNearestCell(civilian, Tree.class);
                        civilian.selectCell(tree);
                        break;
                        case 2:
                        Mine mine = (Mine)getNearestCell(civilian, Mine.class);
                        civilian.selectCell(mine);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Finds the Cell of the given type nearest to the given unit.
     * 
     * @param unit The Unit to find a Cell close to
     * @param type The Class of the type of Cell to find
     */
    private Cell getNearestCell(Unit unit, Class type){
        Game game = (Game)unit.getWorld();
        List<Cell> list = game.getObjects(type);
        Cell nearestCell = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (Cell cell : list){
            int dx = cell.getAbsoluteX(game.getMap()) - unit.getAbsX();
            int dy = cell.getAbsoluteY(game.getMap()) - unit.getAbsY();
            double distance = Math.sqrt(dx * dx + dy * dy); //Uses pythagorean thereom
            if (distance < nearestDistance){
                nearestCell = cell;
                nearestDistance = distance;
            }
        }
        return nearestCell;
    }

    /**
     * Finds a Cell of Grass to build on near the base (Center).
     * 
     * @param map The Map to find the Cell on
     * @return a Cell that the AI can build on
     */
    private Cell getNearbyGrass(Map map){
        int tries = 10;
        while (true){ //Keeps going until it returns
            int xOffset = random.nextInt(30) - 15;
            int yOffset = random.nextInt(30) - 15;
            try{
                Cell cell = map.getCells()[center.getMapX() + xOffset][center.getMapY() + yOffset];
                if (cell instanceof Grass){ //Found grass!
                    return cell;
                }
            } catch (ArrayIndexOutOfBoundsException e){ //Cell deos not exist
            }
            if(tries-- < 0){
                return null;
            }
        }
    }

    /**
     * Called during deserialization to restore transient fields.
     * 
     * @param stream    The input stream given during deserialization
     */
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject(); //Performs default object read first
        updateThread = new Thread(){
            public void run(){
                try{
                    Thread.currentThread().sleep(1000); //Allows the main thread to setup first 
                    while (true){ //Thread will run until it is stopped
                        act();
                        Thread.currentThread().sleep(REFRESH_RATE);
                    }
                } catch(InterruptedException e){
                }
            }
        };
        updateThread.start();
    }

    /**
     * Places a building close to the town center.
     * 
     * @param   building    The building that needs to be built
     */
    private void place(int building){
        Grass site = null;
        Map map = game.getMap();

        site = (Grass)getNearbyGrass(map);

        if(site != null){
            switch(building){
                case BARRACKS:
                if(wood >= Barracks.WOOD_COST && gold >= Barracks.GOLD_COST){
                    site.buildBarracks((Player)this);
                }
                break;
                case STABLE:
                if(wood >= Stable.WOOD_COST && gold >= Stable.GOLD_COST){
                    site.buildStable((Player)this);
                }
                break;
                case WORKSHOP:
                if(wood >= Workshop.WOOD_COST && gold >= Workshop.GOLD_COST){
                    site.buildWorkshop((Player)this);
                }
                break;
                case HOUSE:
                if(wood >= House.WOOD_COST && gold >= House.GOLD_COST){
                    site.buildHouse((Player)this);
                }
                break;
                case WALL:
                if(wood >= Wall.WOOD_COST && gold >= Wall.GOLD_COST){
                    site.buildWall((Player)this);
                }
            }
        }
    }

    /**
     * - Trains an army randomly
     */
    private void spawnArmy(){
        if (currentPopulation < POP_CAP){
            try{
                switch(age){
                    case 1 :
                    switch(Greenfoot.getRandomNumber(3)){
                        case 0 :
                        if(enoughResources(Spearman.FOOD_COST, Spearman.WOOD_COST, Spearman.GOLD_COST)){
                            barracks.spawnSpearman();
                        }
                        break;
                        case 1 :
                        if(enoughResources(Swordsman.FOOD_COST, Swordsman.WOOD_COST, Swordsman.GOLD_COST)){
                            barracks.spawnSwordsman();
                        }
                        break;
                        default :
                        if(enoughResources(Archer.FOOD_COST, Archer.WOOD_COST, Archer.GOLD_COST)){
                            barracks.spawnArcher();
                        }
                        break;
                    }
                    break;
                    case 2 :
                    switch(Greenfoot.getRandomNumber(3)){
                        case 0 :
                        if(enoughResources(Pikeman.FOOD_COST, Pikeman.WOOD_COST, Pikeman.GOLD_COST)){
                            barracks.spawnPikeman();
                        }
                        break;
                        case 1 :
                        if(enoughResources(ColonialArcher.FOOD_COST, ColonialArcher.WOOD_COST, ColonialArcher.GOLD_COST)){
                            barracks.spawnColonialArcher();
                        }
                        break;
                        default :
                        if(enoughResources(Musketeer.FOOD_COST, Musketeer.WOOD_COST, Musketeer.GOLD_COST)){
                            barracks.spawnMusketeer();
                        }
                        break;
                    }
                    break;
                    case 3 :
                    switch(Greenfoot.getRandomNumber(4)){
                        case 0 :
                        if(enoughResources(Sniper.FOOD_COST, Sniper.WOOD_COST, Sniper.GOLD_COST)){
                            barracks.spawnSniper();
                        }
                        break;
                        case 1 :
                        if(enoughResources(Rifleman.FOOD_COST, Rifleman.WOOD_COST, Rifleman.GOLD_COST)){
                            barracks.spawnRifleman();
                        }
                        break;
                        case 2 :
                        if(enoughResources(RPG.FOOD_COST, RPG.WOOD_COST, RPG.GOLD_COST)){
                            barracks.spawnRPG();
                        }
                        break;
                        default :
                        if(enoughResources(MachineGun.FOOD_COST, MachineGun.WOOD_COST, MachineGun.GOLD_COST)){
                            barracks.spawnMachineGun();
                        }
                        break;
                    }
                    break;
                }

                switch(age){
                    case 1 :
                    switch(Greenfoot.getRandomNumber(3)){
                        case 0 :
                        if(enoughResources(Knight.FOOD_COST, Knight.WOOD_COST, Knight.GOLD_COST)){
                            stable.spawnKnight();
                        }
                        break;
                        case 1 :
                        if(enoughResources(Lancer.FOOD_COST, Lancer.WOOD_COST, Lancer.GOLD_COST)){
                            stable.spawnLancer();
                        }
                        break;
                        default :
                        if(enoughResources(MountedArcher.FOOD_COST, MountedArcher.WOOD_COST, MountedArcher.GOLD_COST)){
                            stable.spawnMountedArcher();
                        }
                        break;
                    }
                    break;
                    case 2 :
                    switch(Greenfoot.getRandomNumber(2)){
                        case 0 :
                        if(enoughResources(Horseman.FOOD_COST, Horseman.WOOD_COST, Horseman.GOLD_COST)){
                            stable.spawnHorseman();
                        }
                        break;
                        default :
                        if(enoughResources(Dragoon.FOOD_COST, Dragoon.WOOD_COST, Dragoon.GOLD_COST)){
                            stable.spawnDragoon();
                        }
                        break;
                    }
                    break;
                    case 3 :
                    switch(Greenfoot.getRandomNumber(2)){
                        case 0 :
                        if(enoughResources(LAV.FOOD_COST, LAV.WOOD_COST, LAV.GOLD_COST)){
                            stable.spawnLAV();
                        }
                        break;
                        default :
                        if(enoughResources(Tank.FOOD_COST, Tank.WOOD_COST, Tank.GOLD_COST)){
                            stable.spawnTank();
                        }
                        break;
                    }
                    break;
                }

                switch(age){
                    case 1 :
                    switch(Greenfoot.getRandomNumber(2)){
                        case 0 :
                        if(enoughResources(BatteringRam.FOOD_COST, BatteringRam.WOOD_COST, BatteringRam.GOLD_COST)){
                            workshop.spawnBatteringRam();
                        }
                        break;
                        default :
                        if(enoughResources(Catapult.FOOD_COST, Catapult.WOOD_COST, Catapult.GOLD_COST)){
                            workshop.spawnCatapult();
                        }
                        break;
                    }
                    break;
                    case 2 :
                    switch(Greenfoot.getRandomNumber(2)){
                        case 0 :
                        if(enoughResources(Cannon.FOOD_COST, Cannon.WOOD_COST, Cannon.GOLD_COST)){
                            workshop.spawnCannon();
                        }
                        break;
                        default :
                        if(enoughResources(OrganGun.FOOD_COST, OrganGun.WOOD_COST, OrganGun.GOLD_COST)){
                            workshop.spawnOrganGun();
                        }
                        break;
                    }
                    break;
                    case 3 :
                    if(enoughResources(Mortar.FOOD_COST, Mortar.WOOD_COST, Mortar.GOLD_COST)){
                        workshop.spawnMortar();
                    }
                    break;
                }
            } catch(Exception e){
                //Do nothing; there were not enough resources to train
            }
        }
    }

    /**
     * - Launch army to attack enemy units
     */
    private void deployArmy(){
        int armySize = 0;
        int counter = 0;
        LinkedList soldiers = new LinkedList();
        LinkedList enemies = new LinkedList();

        for (Unit unit : (List<Unit>)game.getObjects(Unit.class)){
            if (!(unit instanceof Civilian) && unit.getOwner() == this){
                armySize++;
                soldiers.add(unit);
            } else if (unit.getOwner() != this){
                enemies.add(unit);
            }
        }

        if (armySize > 2){
            for(Unit enemy : (List<Unit>)enemies){
                try{
                    (((Unit)soldiers.get(armySize - counter++))).selectUnit(enemy);
                } catch(Exception e){
                    //Do nothing; there are no units left to attack
                }
            }
        }
    }

    /**
     * @return  if there is enough population space for another building or not
     */
    private boolean enoughSpace(){
        currentPopulation = this.getPopulation();

        if (currentPopulation + 1 <= maxPopulation){
            return true;
        } else {
            if (maxPopulation < POP_CAP){
                place(HOUSE);
            }
            return false;
        }
    }

    /**
     * Check if there are enough resources to train a unit
     */
    private boolean enoughResources(int foodCost, int woodCost, int goldCost){
        boolean enough = true;

        if (food < foodCost || wood < woodCost || gold < goldCost){
            enough = false;
        }

        return enough;
    }

    /**
     * Stops the updateThread before the GC erases this object.
     */
    protected void finalize() throws Throwable{
        updateThread.interrupt();
        super.finalize();
    }
}
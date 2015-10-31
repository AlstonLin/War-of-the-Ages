import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
/**
 * Handles the basic features that all Units will have, mainly pathfinding.
 *
 * @author Alston Lin, Daniel Chung, James Ly, Jesmin Hondell, Koko Deng
 * @version 1.0
 */
public abstract class Unit extends Actor implements Selectable, Serializable
{
    //CONSTANTS
    public static final int CIVILIAN = 0, MELEE_INFANTRY = 1, RANGED_INFANTRY = 2, MELEE_CAVALRY = 3, RANGED_CAVALRY = 4, SIEGE = 5;
    public static final int UP = 1, RIGHT = 2, DOWN = 3, LEFT = 4;
    private static final int PATHFINDER_REFRESH_RATE = 25; 
    //STATIC VARIABLES
    public static int IDCounter; //Counts the IDs given to avoid collision

    //INSTANCE VARIABLES
    //Movement
    private int debugger = 0;
    protected int speed = 5;
    protected int facingDirection;
    protected Unit target;
    protected Building targetBuilding;
    protected LinkedList<Cell> path; //Cells to go to; the next Cell to visit should be the first index
    protected Cell currentCell; //The cell the unit is currently on
    protected Cell selectedCell; //The cell the unit has been selected on 
    protected Cell targetCell; //The cell that the unit needs to reach
    protected int absoluteX, absoluteY; //The absolute values of the Unit's position relative to the map NOT the screen
    protected boolean hidden;
    protected boolean isMoving;
    protected boolean following;
    protected boolean teamPlayer;
    protected int pathfinderCounter = 0; //Counts to pathfinderRefreshRate before refreshing this unit's path if it is chasing another unit
    private HealthBar healthBar;
    private boolean runFirstTime = false;
    //Description
    protected int team, type;
    protected int ID; //Used for multiplayer
    protected String unit = "";
    protected boolean spawned; //If this unit has spawned yet
    //Stats
    protected double health, currentHealth, attack, meleeArmour, rangedArmour, siegeArmour;
    protected int range, attackSpeed;
    protected Player owner; //The owner of the unit
    protected int attackTimer;
    //Multipliers
    protected double meleeInfantry, rangedInfantry, meleeCavalry, rangedCavalry, siege, building;

    /**
     * Creates a new Unit; this should only be called from a subclass.
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put in
     */
    public Unit(Player owner, Cell cell, Map map) {
        int cellSize = map.getCellSize();
        ID = IDCounter;
        IDCounter++;
        currentCell = cell;
        absoluteX = cellSize * (cell.getMapX() + 1);
        absoluteY = cellSize * (cell.getMapY() + 1);
        calculateNewLocation(map);
        updateImage(map, new GreenfootImage(getFrontImage()));
        this.owner = owner;
        owner.getUnits().add(this);
        Game temp = (Game)getWorld();

        if (owner.isUser((Game)map.getWorld())) {
            teamPlayer = true;
            //tile = new TeamColor(true);
            ///////////////////////////////////////////////FIX THIS
            Game tempWorld = (Game)getWorld();
            //tempWorld.addObject(tile, getX(), getY());
        } else {
            teamPlayer = false;
            //tile = new TeamColor(false);
            //getWorld().addObject(tile, getX(), getY());
        }
    }

    /**
     * Called before every frame is rendered; every Unit's automatic
     * behavior:
     * Moves to new cell if free cell is available
     * Empties the old cell to make it traversable
     */
    public void act() {
        Game game = (Game)getWorld();

        if (!runFirstTime) {
            Game temp = (Game)getWorld();
            if (this.owner == temp.getUser()) {
                healthBar = new HealthBar ((int)health, (int)currentHealth, 30, 5, true);
            } else {
                healthBar = new HealthBar ((int)health, (int)currentHealth, 30, 5, false);
            }
            temp.addObject(healthBar, getX(), getY() - 30);
            runFirstTime = true;
        } else {
            healthBar.moveTo(this.getX(), this.getY() - 30);
            healthBar.updateHealth((int)currentHealth);
        }

        if (!(game.isMultiplayer() && !game.isHosting())) { //Not a client in multiplayer mode
            if (attackTimer > 0) { 
                attackTimer--;
            }
            if (following) {
                if (pathfinderCounter == PATHFINDER_REFRESH_RATE) { //Time to refresh
                    if (target == null || target.getCurrentHealth() <= 0) { //Target died
                        //cease doing everything tasked to do.
                        path = null; 
                        following = false;
                        target = null;
                        targetCell = null;
                        //Search for new enemy to slaughter
                        List<Unit> scanning = getObjectsInRange(500, Unit.class); //this makes it so that we try to find the least amount of units by only getting the ones ever possible to hit, this makes the unit more efficent at scanning and parsing
                        for (Unit lookingAt : scanning) {
                            if (lookingAt.getOwner() != owner) {
                                double distanceToTarget = Math.sqrt((double)((Math.pow((double)(this.getAbsX() - lookingAt.getAbsX()), 2.0)+Math.pow((double)(this.getAbsY() - lookingAt.getAbsY()), 2.0))));
                                //because of the scrolling in the game, and greenfoot's tendency to keep images in the world, we must look for the true location of the unit
                                if (distanceToTarget <= 500) {
                                    //if the unit is in range, begin the slaughter
                                    slaughterByStanders(lookingAt);
                                }
                            }
                        }
                    } else {
                        targetCell = target.getCell();
                        pathfinderCounter = 0;
                        if (!(targetCell.getMapX() >= currentCell.getMapX() - range - 1 && targetCell.getMapX() <= currentCell.getMapX() + range + 1
                            && targetCell.getMapY() >= currentCell.getMapY() - range - 1 && targetCell.getMapY() <= currentCell.getMapY() + range + 1)) { //Not in range
                            //Ensures that noone can be on the same Cell
                            currentCell.setTraversable(true);
                            try {
                                findPath();
                            } catch (NoPossiblePathException e) { 
                                path = null; //No possible path; gives up 
                                targetCell = null; 
                                target = null;
                                following = false;
                            }
                            currentCell.setTraversable(false);
                            return;
                        }else{ //In range
                            attack();
                        }
                    }
                }   
                pathfinderCounter++;
            }
        }
        if (path != null) {
            //Ensures that noone can be on the same Cell
            currentCell.setTraversable(true);
            if (((Game)getWorld()).getMap().getChanged() && !(game.isMultiplayer() && !game.isHosting())) { //If the Map changed since the last act
                try {
                    findPath();
                } catch (NoPossiblePathException e) { 
                    path = null; //No possible path; gives up 
                    targetCell = null; 
                }
            }
            if (path != null) {
                travelPath();
            }
            currentCell.setTraversable(false);
        }
    }

    /**
     * Attacks the target if the attack timer is up
     */
    public void attack() {
        Game game = (Game)getWorld();
        if (!(game.isMultiplayer() && !game.isHosting())) { //Not client
            if (attackTimer == 0) { //Attack ready
                if (target != null) { //Currently attacking someone
                    if (target.getCurrentHealth() <= 0) { //Target is dead
                        target = null;
                    } else {
                        if (this instanceof RangedInfantry || this instanceof RangedCavalry || this instanceof RangedArtillery){
                            fireProjectile(target);
                        }
                        target.takeDamage(calculateDamageToInflict());
                        attackTimer = 50 * attackSpeed;
                        if (game.isHosting()) { //Server 
                            Object[] data = new Object[2];
                            data[0] = this; //First data point is the attacker
                            data[1] = target; //Second data point is the target
                            game.sendUpdate(new Update(Update.UNIT_ATTACK, data));
                        }
                    }
                } else if (targetBuilding != null) { //Attacking a building
                    if (targetBuilding.getHealth() <= 0) { //Building destroyed
                        targetBuilding = null;
                    }else {
                        if (this instanceof RangedInfantry || this instanceof RangedCavalry || this instanceof RangedArtillery){
                            fireProjectile(targetBuilding);
                        }
                        targetBuilding.changeHealth(-(int)(attack * building));
                        attackTimer = 50 * attackSpeed;
                        if (game.isMultiplayer() && game.isHosting()) { //Server
                            Object[] data = new Object[2];
                            data[0] = this; //First data point is the attacker
                            data[1] = targetBuilding; //Second data point is the target
                            game.sendUpdate(new Update(Update.UNIT_ATTACK, data));
                        }
                    }
                }
            }
        }
    }

    /**
     * Attacks the given Building or Unit. Should be used
     * for multiplayer clients only.
     * 
     * @param object The target of the attack
     */
    public void clientAttack(Object object) {
        if (object instanceof Building) {
            //TODO
        }else if (object instanceof Unit) {
            target = (Unit)object;
            fireProjectile(target);
            if (target.getCurrentHealth() <= 0) { //Target is dead
                target = null;
            }else{
                target.takeDamage(calculateDamageToInflict());
                attackTimer = 1000 / attackSpeed;
                Game game = (Game)getWorld();
                if (game.isHosting()) { //Server 
                    game.sendUpdate(new Update(Update.UNIT_ATTACK, this));
                }
            }
        }
    }

    /**
     * Deducts the given damage from this unit's
     * hitpoints. Unit is destroyed if out of hitpoints
     * 
     * @param  damage   Amount of damage
     * @param  type     Type of damage
     */
    public void takeDamage(double damage) {
        currentHealth -= damage;
        Game game = (Game)getWorld();

        if (currentHealth <= 0.0 && getWorld() != null) {
            owner.getUnits().remove(this);
            game.removeObject(healthBar);
            game.removeObject(this);
        }
    }

    /**
     * Calculates the damage that the unit will deal to its current target, by multiplying
     * its base attack by the multiplier associated with the enemy's type.
     * 
     * @return      The amount of damage that is to be dealt on the unit's current target
     */
    private double calculateDamageToInflict() {
        int enemyType = target.getType();
        double multiplier = 1, damageToInflict;

        switch(enemyType) {
            case MELEE_INFANTRY:
            multiplier = meleeInfantry;
            break;
            case RANGED_INFANTRY:
            multiplier = rangedInfantry;
            break;
            case MELEE_CAVALRY:
            multiplier = meleeCavalry;
            break;
            case RANGED_CAVALRY:
            multiplier = rangedCavalry;
            break;
            case SIEGE:
            multiplier = siege;
            break;
        }

        damageToInflict = multiplier * attack;

        return damageToInflict;
    }

    /**
     * Returns the number of units
     * 
     * @return      The unit's team
     */
    public int getTeam() {
        return team;
    }

    /**
     * Returns the maximum health of the unit
     * 
     * @return      The maximum health of the unit
     */
    public double getHealth() {
        return health;
    }

    /**
     * When the user selects the Unit to go to a specific Cell:
     * - the selected cell is set as a target
     * - the unit is classified '!following', i.e. it is not tracking another unit
     * - a path is calculated to the cell
     * Will ignore multiple selections of the same cell.
     * 
     * @param cell      The cell that is to be set as a destination
     */
    public void selectCell(Cell cell) {
        if(targetCell != cell) {
            try {
                selectedCell = cell;
                targetCell = cell;
                following = false;
                target = null;
                if (cell instanceof Building && ((Building)cell).getOwner() != owner) { //Selected enemy Building
                    targetBuilding = (Building)cell;
                    cell.setTraversable(true);
                    findPath();
                    cell.setTraversable(false);
                } else{
                    findPath();
                    targetBuilding = null;
                }
            } catch (NoPossiblePathException e) { //No possible path
                targetCell = null;
                selectedCell = null;
                targetBuilding = null;
            }
        }
    }

    /**
     * When the user selects the Unit to go to a specific unit:
     * - the selected unit is set as a target
     * - the unit is classified 'following', i.e. it is tracking another unit
     * - a path is calculated to the unit   
     * Will ignore multiple selections of the same unit.
     * 
     * @param unit      The unit that is to be set as a destination
     */
    public void selectUnit(Unit unit) {
        if (unit != target && unit.getOwner() != owner) { //Not friendly Unit
            try {
                target = unit;
                targetCell = target.getCell();
                following = true;
                pathfinderCounter = 0;
                targetBuilding = null;
                findPath();
            } catch (NoPossiblePathException e) { //No possible path
                target = null;
                targetCell = null;
            }
        }
    }

    /**
     * Calculates the new position of this Unit after the map has changed.
     *
     * @param       The map that this Unit is in
     */
    public void calculateNewLocation(Map map) {
        int x = absoluteX - map.getViewX();
        int y = absoluteY - map.getViewY();
        setLocation(x, y);
        updateHiding(x, y);
    }

    /**
     * Resizes the Unit and repositions it approriately based on the percent
     * changed.
     *
     * @param percentChange     The percent of the size of the new map compared to
     * the old map
     * @param map       The Map that this Unit is in
     */
    public void resize(double percentChange, Map map) {
        GreenfootImage image = null;
        switch(facingDirection){
            case LEFT:
            image = getLeftImage();
            break;
            case RIGHT:
            image = getRightImage();
            break;
            case UP:
            image = getBackImage();
            break;
            case DOWN:
            image = getFrontImage();
            break;
        }
        updateImage(map, new GreenfootImage(image));
        absoluteX *= percentChange;
        absoluteY *= percentChange;
        calculateNewLocation(map);
    }

    /**
     * Updates the image according to the current map zoom.
     *
     * @param map       The Map that this Unit is in
     * @param image     The GreenfootImage that this Unit will appear as
     */
    public void updateImage(Map map, GreenfootImage image) {
        double percentZoom = map.getCellSize() / (double) Map.MAX_CELL_SIZE;
        image.scale((int) (image.getWidth() * percentZoom + 1), (int) (image.getHeight() * percentZoom + 1)); //Scales image depending on current zoom
        setImage(image);
    }

    /**
     * Creates a path using Breadth First Search to the given cell; attempts to
     * find the shortest possible path, or throws an IllegalStateException if
     * not possible.
     *
     * @throws NoPossiblePathException      The Cell cannot be reached
     * @throws IllegalArgumentException     The destination Cell deos not exist
     * @throws NoPossiblePathException      There is no possible path to the given Cell
     */
    protected void findPath() throws NoPossiblePathException {
        if (targetCell == null || targetCell.getWorld() == null) {
            path = null;
            targetCell = null;
            target = null;
            following = false;
            return;
        }
        int x = targetCell.getMapX();
        int y = targetCell.getMapY();

        LinkedList<Cell> list = new LinkedList();

        //Sanity check; throws exception if Cell deos not exist
        if (x < 0 || x >= Map.CELLS_X || y < 0 || y >= Map.CELLS_Y) {
            throw new IllegalArgumentException("The given Cell deos not exist");
        }
        //Current Cell acting as the rootnode
        list.add(currentCell);
        currentCell.visit(null);
        while (!list.isEmpty()) { //Keeps going until it runs outs of Cells-Nodes to visit
            Cell cell = list.remove(); //Removes the very first index and stores the reference in this variable
            LinkedList<Cell> neighbours = cell.getNeighbours();
            for (Cell neighbour : neighbours) { //Adds each neighbour to the queue if it was not visited
                if (!neighbour.getVisited() && neighbour.isTraversable()) { //Unvisited Cell that can be traversed through
                    list.add(neighbour);
                    neighbour.visit(cell);
                    if ((neighbour.getMapX() == x && neighbour.getMapY() == y && !following) //Found the wanted cell
                    || (following && neighbour.getMapX() >= x - range && neighbour.getMapX() <= x + range && neighbour.getMapY() >= y - range && neighbour.getMapY() <= y + range)) { //Near the target
                        //Now it finds the way to get to the desired destination by going through it's parents and appending it to the path
                        Cell wayPoint = neighbour;
                        path = new LinkedList();
                        try {
                            do {
                                path.addFirst(wayPoint);
                                wayPoint = wayPoint.getParent();
                            } while (wayPoint.getParent() != null);
                        } catch (NullPointerException e) { //Only one item
                        }
                        list.clear(); //Clears the neighbours to visit as the path has been found
                        //Updates in multiplayer
                        Game game = (Game)getWorld();
                        if (game.isHosting()) {
                            //Format for data: 1st index is the Unit ID, everything after is map coordinates
                            LinkedList<int[]> data = new LinkedList();
                            int[] IDdata = new int [1];
                            IDdata[0] = ID;
                            data.addFirst(IDdata);
                            //Creates a list of map coordinates to follow
                            for (Cell point : path) {
                                int[] coordinate = new int[2];
                                coordinate[0] = point.getMapX();
                                coordinate[1] = point.getMapY();
                                data.add(coordinate);
                            }
                            game.sendUpdate(new Update(Update.UNIT_PATH_CHANGE, data));
                        }
                    }
                }
            }
        }
        Cell.resetPathfinding((Game) getWorld()); //Resets the pathfinding variables in all Cells
        if (path == null) { //No path was found
            throw new NoPossiblePathException();
        }
    }

    /**
     * Travels on the set path. If this unit is chasing a moving unit, 
     * the path is refreshed every few frames (pathfinderRefreshRate).
     * If there are obstructions, the path will be reworked.
     */
    private void travelPath() {
        Cell cell = path.get(0);
        int pixelRange = range * ((Game)getWorld()).getMap().getCellSize() + 5; //The range in pixels
        if (target != null && getAbsX() + pixelRange > target.getAbsX() && getAbsX() - pixelRange < target.getAbsX() 
        && getAbsY() + pixelRange >  target.getAbsY() && getAbsY() - pixelRange < target.getAbsY()) { //The target is in range
            attack();
        } else if (targetBuilding != null) {
            Map map = ((Game)getWorld()).getMap();
            int targetAbsX = map.getCellSize() * targetBuilding.getMapX();
            int targetAbsY = map.getCellSize() * targetBuilding.getMapY();
            if (getAbsX() + pixelRange > targetAbsX && getAbsX() - pixelRange < targetAbsX 
            && getAbsY() + pixelRange >  targetAbsY && getAbsY() - pixelRange < targetAbsY) { //The target building in range
                attack();
            } else {
                travelToCell(cell);
            } 
        } else { //Only if the Cell can be traversed through
            travelToCell(cell);
        }
    }

    /**
     * Travels towards the given Cell.
     * 
     * @param cell      The Cell to move towards
     */
    private void travelToCell(Cell cell) {
        if (cell.isTraversable()) { //Only if the Cell can be traversed through
            Map map = ((Game) getWorld()).getMap();
            if (cell.getAbsoluteX(map) < absoluteX - speed) { //Waypoint is to the left
                move(-speed, 0);
                if (facingDirection != LEFT){
                    facingDirection = LEFT;
                    updateImage(map, new GreenfootImage(getLeftImage()));
                }
            } else if (cell.getAbsoluteX(map) > absoluteX + speed) { //Right
                move(speed, 0);
                if (facingDirection != RIGHT){
                    facingDirection = RIGHT;
                    updateImage(map, new GreenfootImage(getRightImage()));
                }
            } else if (cell.getAbsoluteY(map) < absoluteY - speed) { //Up
                move(0, -speed);
                if (facingDirection != UP){
                    facingDirection = UP;
                    updateImage(map, new GreenfootImage(getBackImage()));
                }
            } else if (cell.getAbsoluteY(map) > absoluteY + speed) { //Down
                move(0, speed);
                if (facingDirection != DOWN){
                    facingDirection = DOWN;
                    updateImage(map, new GreenfootImage(getFrontImage()));
                }
            } else { //Reaches the destination
                currentCell = cell;
                path.remove(0);
                if (path.size() == 0) { //Reached final destination
                    path = null;
                    arrive(); 
                    return;
                }
            }
        }
    }

    /**
     * Updates whether or not the Unit should be hidden
     *
     * @param x     The x coordinate in pixels relative to the screen
     * @param y     The y coordinate in pixels relative to the screen
     */
    private void updateHiding(int x, int y) {
        if (x <= 0 || x >= Map.SIZE_X || y <= 0 || y >= Map.SIZE_Y) { //Off the map
            if (!hidden) {
                getImage().setTransparency(0);
                hidden = true;
            }
        } else { //On the map
            if (hidden) {
                getImage().setTransparency(255);
                hidden = false;
            }
        }
    }

    /**
     * Moves this Unit the given x and y changes and applies it
     * safely.
     * 
     * @param xChange       the change in pixels on the x axis
     * @param yChange       the change in pixels on the y axis
     */
    private void move (int xChange, int yChange) {
        absoluteX += xChange;
        absoluteY += yChange;
        calculateNewLocation(((Game)getWorld()).getMap());
    }    

    /**
     * Fires a projectile to the target
     * 
     * @param targetOfInterest   the target of the attack
     */
    private void fireProjectile(Actor targetOfInterest) {
        if (this.range > 1) {
            Game world = (Game)getWorld();
            Projectile shot;
            if (this instanceof Cannon || this instanceof Mortar || this instanceof Catapult) {
                shot = new Projectile(targetOfInterest, true);
            } else {
                shot = new Projectile(targetOfInterest);
            }
            world.addObject(shot, getX(), getY());
        }
    }

    /**
     * Checks to see if the unit selected is an enemy, and if so, attack
     * 
     * @param unit      The Unit that will be attacked
     */
    public void slaughterByStanders(Unit unit) {
        try {
            target = unit;
            targetCell = target.getCell();
            following = true;
            pathfinderCounter = 0;
            targetBuilding = null;
            findPath();
        } catch (NoPossiblePathException e) { //No possible path
            target = null;
            targetCell = null;
        }
    }

    /**
     * Returns the image of the Unit facing left
     * 
     * @return      A refrence to the original image of this Unit facing left (can be shared)
     */
    public abstract GreenfootImage getLeftImage();

    /**
     * Returns the image of the Unit facing right
     * 
     * @return      A refrence to the original image of this Unit facing right (can be shared)
     */
    public abstract GreenfootImage getRightImage();

    /**
     * Returns the image of the Unit facing front
     * 
     * @return      A refrence to the original image of this Unit facing front (can be shared)
     */
    public abstract GreenfootImage getFrontImage();

    /**
     * Returns the image of the Unit facing back (Away from screen)
     * 
     * @return      A refrence to the original image of this Unit facing back (can be shared)
     */
    public abstract GreenfootImage getBackImage();

    /**
     * Returns the health bar of this Unit
     * 
     * @return The health bar of this Unit
     */
    public HealthBar getHealthBar() {
        return healthBar;
    }

    /**
     * Returns the current health of the Unit
     * 
     * @return      The remaining health of the unit
     */
    public double getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Returns the Unit's resistance to melee damage
     * 
     * @return      The Unit's resistance to melee damage
     */
    public double getMeleeArmour() {
        return meleeArmour;
    }

    /**
     * Returns the Unit's resistance to ranged damage
     * 
     * @return      The unit's resistance to ranged damage
     */
    public double getRangedArmour() {
        return meleeArmour;
    }

    /**
     * Returns the Unit's resistance to siege damage
     * 
     * @return      The Unit's resistance to siege damage
     */
    public double getSiegeArmour() {
        return siegeArmour;
    }

    /**
     * Returns the Unit's range of attack
     * 
     * @return      The unit's range of attack
     */
    public int getRange() {
        return range;
    }

    /**
     * Returns the Unit's attacking frequency
     * 
     * @return      The Unit's attacking frequency (counted in turns)
     */
    public int getAttackSpeed() {
        return attackSpeed;
    }

    /**
     * Returns the Unit's attack multiplier (for melee infantry types)
     * 
     * @return      The Unit's attack multiplier when striking melee infantry types
     */
    public double getMeleeInfantryMultiplier() {
        return meleeInfantry;
    }

    /**
     * What happens when the Unit arrives at the destination Cell
     */
    public abstract void arrive();

    /**
     * Returns the Unit's attack multiplier (for ranged infantry types)
     * 
     * @return      The Unit's attack multiplier when striking ranged infantry types
     */
    public double getRangedInfantryMultiplier() {
        return rangedInfantry;
    }

    /**
     * Returns the unit's attack multiplier (melee cavalry types)
     * 
     * @return      The unit's attack multiplier when striking melee cavalry types
     */
    public double getMeleeCavalryMultiplier() {
        return meleeInfantry;
    }

    /**
     * Returns the unit's attack multiplier (for ranged cavalry types).
     * 
     * @return      The unit's attack multiplier when striking ranged cavalry types
     */
    public double getRangedCavalryMultiplier() {
        return rangedCavalry;
    }

    /**
     * Returns the unit's attack multiplier (for striking siege types).
     * 
     * @return      The unit's attack multiplier when striking siege types
     */
    public double getSiegeMultiplier() {
        return siege;
    }

    /**
     * Returns the unit's attack multiplier (for striking buildings).
     * 
     * @return      The unit's attack multiplier when striking buildings
     */
    public double getBuildingMultiplier() {
        return building;
    }

    /**
     * Returns the type to which a selected unit belongs.
     * 
     * @return      The type to which a selected unit belongs
     */
    public int getType() {
        return type;
    }

    /**
     * Returns the name of a selected unit.
     * 
     * @return      The name of a selected unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Returns the absolute x coordinate of this unit
     * 
     * @return      The absolute x coordinate of this unit
     */
    public int getAbsX() {
        return absoluteX;
    }

    /**
     * Returns the absolute y coordinate of this unit
     * 
     * @return      The absolute y coordinate of this unit
     */
    public int getAbsY() {
        return absoluteY;
    }

    /**
     * Returns the cell of this unit
     * 
     * @@return     The cell of this unit
     */
    public Cell getCell() {
        return currentCell;
    }

    /**
     * Returns the owner of this unit
     * 
     * @return      The owner of this Unit
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Returns the cell that this Unit is on
     * 
     * @return      The Cell that this Unit is on
     */
    public Cell getCurrentCell() {
        return currentCell;
    }

    /**
     * Returns the variable hidden
     * 
     * @return      True if it is hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Returns the ID of this Unit
     * 
     * @return      The ID of this Unit
     */
    public int getID() {
        return ID;
    }    

    /** 
     * Returns amount of gold needed to spawn unit
     * 
     * @return      amount of gold needed to spawn unit
     */
    public int getGoldCost() {
        return 0;
    }

    /**
     * Returns amount of wood needed to spawn unit
     * 
     * @return      amount of wood needed to spawn unit
     */
    public int getWoodCost() {
        return 0;
    }

    /**
     * Returns amount of food needed to spawn unit
     * 
     * @return      amount of food needed to spawn unit
     */
    public int getFoodCost() {
        return 0;
    }

    /**
     * Returns if this Unit is currently spawned
     * 
     * @return      If this Unit is currently spawned
     */
    public boolean isSpawned() {
        return spawned;
    }

    /**
     * Sets the ID of the Unit
     * 
     * @param ID        The new ID of this Unit
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Sets the path of the Cell
     * 
     * @param path      The new path of this Cell
     */
    public void setPath(LinkedList<Cell> path) {
        this.path = path;
    }

    /**
     * Sets the spawned
     * 
     * @param spawned   The new value of spawn
     */
    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    /**
     * Returns the current target of this Unit
     * 
     * @return      The current target of this Unit
     */
    public Unit getTarget() {
        return target;
    }

    /**
     * Returns the current Building that this Unit is targeting
     * 
     * @return The current Building that this Unit is targeting
     */
    public Building getTargetBuilding() {
        return targetBuilding;
    }
}

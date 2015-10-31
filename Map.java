import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
/**
 * Displays a portion of the items in the game, taking up the majority of
 * the screen. In addition to displaying the Cells and Units, this class
 * also handles user input as well. 
 * 
 * @author Alston Lin
 * @version 1.0
 */
public class Map extends Actor implements Serializable
{   
    //Constant variables
    public static final int SIZE_X = 1280, SIZE_Y = 468; //Pixel size of the map
    public static final int CELLS_X = 101, CELLS_Y = 51; //Cell size of the map
    public static final int MIN_CELL_SIZE = 48, MAX_CELL_SIZE = 64; //Minimum and maximum sizes of the cells possible when zooming

    //Instance variables
    private Cell[][] cells; //Buildings, resources, ect on the map
    private transient Selector selector;
    private transient List<Unit> selectedUnits; //The units that have been selected
    private int viewX, viewY; //The minimum values of the x and y coordinates of the viewing area
    private int cellSize = MIN_CELL_SIZE; //The size on both dimentions per cell
    private int selectorStartX, selectorStartY; //The coordinates of the start of the selector for the UI
    public boolean changed; //If the Map has changed during this act
    
    /**
     * Constructor for the Map. Scales the image and populates world with Cells
     * 
     * @param world     The World that the map will be added in 
     */
    public Map(World world) {
        getImage().scale(SIZE_X, SIZE_Y);
        setupCells(world);
        setupFrame(world);
    }

    /**
     *  Checks if the map required panning, as well as handling user input.
     *  Called before every frame is rendered.
     */
    public void act() {
        MouseInfo info = Greenfoot.getMouseInfo();
        if (info != null) { //Prevents a NullPointerException from MouseInfo being null for some reason (Greenfoot bug)
            if (Greenfoot.mousePressed(null)) { //Mouse press initiated
                if (info.getButton() == 1 && info.getActor() instanceof Selectable) { //Left mouse button on the appropriate situation
                    selectorStartX = info.getX();
                    selectorStartY = info.getY();

                    //Create new selector into world
                    selector = new Selector();
                    getWorld().addObject(selector, selectorStartX, selectorStartY);
                    List toBeRemoved = getWorld().getObjects(TeamColor.class);
                    for (int i = 0; i < toBeRemoved.size(); i++){
                        getWorld().removeObjects(toBeRemoved);
                    }
                }
                else if(info.getButton() == 3) { //Right mouse button
                    selector = null;
                    //getWorld().removeObject(selector);
                    Actor actor = info.getActor();
                    if (actor != null && selectedUnits != null && selectedUnits.size() != 0 && (actor instanceof Cell || actor instanceof Unit)) { 
                        Game game = (Game)getWorld();
                        if (game.isMultiplayer() && !game.isHosting()) { //Multiplayer client
                            Object[] data = new Object[2];
                            data[0] = actor;
                            data[1] = selectedUnits;
                            game.sendRequest(new Request(Request.SELECT, data));
                        }else{
                            if (actor instanceof Cell) { //Right clicked on a Cell 
                                for (Unit unit : selectedUnits) {
                                    unit.selectCell((Cell)actor);
                                }
                            }else if (actor instanceof Unit) { //Right clicked on a Unit
                                for (Unit unit : selectedUnits) {
                                    unit.selectUnit((Unit)actor);
                                }
                            }
                        }
                    }
                }
                //System.out.println(info.getButton());
            }else if (Greenfoot.mouseClicked(null)) { //Mouse button released
                if (info.getButton() == 1 && selector != null) { //Left mouse button released
                    Selectable selected = null; //The selected Object

                    //Greenfoot's collision detected is horrible; must do a custom one
                    List<Unit> units = getWorld().getObjects(Unit.class); 

                    //Checks every existing Unit for collision
                    selectedUnits = new LinkedList();
                    //get the world so we can add tiles to it
                    Game world = (Game)getWorld();
                    TeamColor tile;
                    for (Unit unit : units) {
                        if (!unit.isHidden()) { //Visible
                            int selectorHalfWidth = selector.getImage().getWidth() / 2;
                            int selectorHalfHeight = selector.getImage().getHeight() / 2;
                            if (unit.getX() > selector.getX() - selectorHalfWidth && unit.getX() < selector.getX() + selectorHalfWidth
                            && unit.getY() > selector.getY() - selectorHalfHeight && unit.getY() < selector.getY() + selectorHalfHeight && unit.getOwner() == ((Game)getWorld()).getUser()) { //Collision
                                selectedUnits.add(unit);
                            }
                        }
                    }
                    for (Unit unit : selectedUnits) {
                        if(unit.getOwner() == world.getUser()){
                            tile = new TeamColor(true, unit);
                            getWorld().addObject(tile, unit.getX(), unit.getY());
                        }
                    }
                    if (selectedUnits.isEmpty()) { //No Units selected
                        selected = (Selectable)selector.getOneIntersectingObject(Unit.class); //If there is a unit over such a cell Selects a Unit
                        if((Selectable)selector.getOneIntersectingObject(Unit.class) == null) {
                            selected = (Selectable)selector.getOneIntersectingObject(Cell.class); //Selects a Cell
                        }
                    }

                    //Displays the Buttons and information of the selected Selectable, if any
                    if (selected != null) {
                        Game game = (Game)getWorld();
                        game.getActionsPanel().setButtons(selected.getButtons());
                        game.getInformationPanel().display(selected.getInformation());
                    }

                    //Sends the selector to GC
                    getWorld().removeObject(selector);
                    selector = null;
                } else if (info.getButton() == 3) { //Right mouse button released
                    Selectable selected = null;
                    getWorld().removeObject(selector);
                    selector = null;
                }
            }else { //No presses/releases
                if (selector != null) { //Updates selector if there is one
                    selector.resize(selectorStartX, selectorStartY, info.getX(), info.getY()); 
                } else { //No selector; checks for panning
                    int mouseX = info.getX();
                    int mouseY = info.getY();
                    if (mouseY <= SIZE_Y && (mouseX < 100 || mouseX > SIZE_X - 100 || mouseY < 100 || mouseY > SIZE_Y - 100)) { //Mouse is on the map borders
                        //Uses variables for neatness
                        int panX = (mouseX - SIZE_X / 2) / 20;
                        int panY = (mouseY - SIZE_Y / 2) / 20;
                        pan(panX, panY);
                    }
                }
            }
        }
        changed = false;
    }

    /**
     * Sets up the Frame for the Map.
     */
    public void setupFrame(World world) {
        world.addObject(new FrameSide(), 10, SIZE_Y / 2);
        world.addObject(new FrameSide(), SIZE_X - 12, SIZE_Y / 2);
        world.addObject(new FrameBottom(), SIZE_X / 2, SIZE_Y - 12);
        world.addObject(new FrameBottom(), SIZE_X / 2, 12);
    }

    /**
     * Sets up the initial cells within the map. Should be during initialization only.
     * 
     * @param world     The World that the Map will be added to
     */
    private void setupCells(World world) {
        cells = new Cell[CELLS_X - 1][CELLS_Y - 1];

        //Generates the map
        for (int i = 0; i < cells.length; i++) { 
            for (int j = 0; j < cells[0].length; j++) {
                Random random = new Random();
                int num = random.nextInt(1000);
                if (num < 3) { //1 in 300 are mines 
                    cells[i][j] = new Mine(i, j, cellSize);
                } else if (num < 8) { //1 in 200 are berry bushes
                    cells[i][j] = new BerryBush(i, j, cellSize);
                } else if (num < 30) { //1 in 45 are Trees
                    cells[i][j] = new Tree(i, j, cellSize);
                } else{ //Everything else is grass
                    cells[i][j] = new Grass(i, j, cellSize);
                }
            }
        }

        placeCells(world);
        setupPathfinding();
        changeCellSize(0); //Scales the cells according to the starting value and caclulates the positions
    }

    /**
     * Sets up the Cells for pathfinding by assigning the neighbours.
     */
    private void setupPathfinding() {
        for (int i = 0; i < cells.length; i++) { 
            for (int j = 0; j < cells[0].length; j++) {
                int[][] offsets = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

                //Adds the Cells at the offsets dynamically
                for (int x = 0; x < offsets.length; x++) {
                    try{
                        //Adds the Cell at the defined offsets
                        cells[i][j].addNeighbour(cells[i + offsets[x][0]][j + offsets[x][1]]);
                    } catch (ArrayIndexOutOfBoundsException e) { //Cell does not exist
                    }
                }
            }
        }
    }

    /**
     * Places the Cells in the appropriate location.
     * 
     * @param world     The world to place the Cell in
     */
    public void placeCells(World world) {
        viewX = 0;
        viewY = 0;
        for (int i = 0; i < cells.length; i++) { 
            for (int j = 0; j < cells[0].length; j++) {
                world.addObject(cells[i][j], i * (cellSize + 1) , j * (cellSize + 1)); 
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
        setupPathfinding();
    }

    /**
     * Pans the map to the given x and y coordinate changes if
     * possible (won't go over).
     * 
     * @param xChange       The change in the x axis
     * @param yChange       The change in the y axis
     */
    private void pan(int xChange, int yChange) {
        viewX += xChange;
        viewY += yChange;

        //Ensures the map does not go over the boundaries
        if (viewX < 0) {
            viewX = 0;
        } else if (viewX > CELLS_X * cellSize - SIZE_X) {
            viewX = CELLS_X * cellSize - SIZE_X;
        }
        if (viewY < 0) {
            viewY = 0;
        } else if (viewY > CELLS_Y * cellSize - SIZE_Y) {
            viewY = CELLS_Y * cellSize - SIZE_Y;
        }

        update();
    }

    /**
     * Calculates the position that each cell should be on the screen
     * based on the viewing coordinates and cell size. 
     */
    private void calculateCellPositions() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j].calculateNewLocation(this);
            }
        }
    }

    /**
     * Calculates the new position for all Units after
     * the map has changed.
     */
    private void calculateUnitPositions() {
        for (Unit unit : (List<Unit>)getWorld().getObjects(Unit.class)) {
            unit.calculateNewLocation(this);
        }
    }

    /**
     * Updates the map according to the current viewing area.
     */
    public void update() {
        //First checks if it is out of bounds
        if (viewX < 0) {
            viewX = 0;
        }
        if (viewX > cellSize * CELLS_X - SIZE_X) {
            viewX = cellSize * CELLS_X - SIZE_X;
        }
        if (viewY < 0) {
            viewY = 0;
        }
        if (viewY > cellSize * CELLS_Y - SIZE_Y) {
            viewY = cellSize * CELLS_Y - SIZE_Y;
        }
        //Sets new locations of the map elements
        calculateCellPositions();
        calculateUnitPositions();
    }

    /**
     * Returns the cell size
     * 
     * @return      The size of each side of a cell
     */
    public int getCellSize() {
        return cellSize;
    }

    /**
     * Changes the size of the sides of each cell by the given amount, 
     * if possible. 
     * 
     * @param change      The change in cell size
     */
    public void changeCellSize(int change) {
        int newSize = cellSize + change;
        if (newSize >= MIN_CELL_SIZE && newSize <= MAX_CELL_SIZE) { //Zoom possible
            //Calculates the percentage of the absolute coordinates of the mid-point relative to the map
            double middleXPercentage = (viewX + SIZE_X / 2) / (double)(cellSize * CELLS_X); 
            double middleYPercentage = (viewY + SIZE_Y / 2) / (double)(cellSize * CELLS_Y); 

            //Calculates the cell size change percentage
            double percentChange = (double)newSize / (double)cellSize;
            cellSize = newSize;

            //Calculates the new viewing area
            viewX = (int)(cellSize * CELLS_X * middleXPercentage - SIZE_X / 2);
            viewY = (int)(cellSize * CELLS_Y * middleYPercentage - SIZE_Y / 2);

            //Prevents showing outside of the map
            if (viewX < 0) {
                viewX = 0;
            }
            if (viewX > cellSize * CELLS_X - SIZE_X) {
                viewX = cellSize * CELLS_X - SIZE_X;
            }
            if (viewY < 0) {
                viewY = 0;
            }
            if (viewY > cellSize * CELLS_Y - SIZE_Y) {
                viewY = cellSize * CELLS_Y - SIZE_Y;
            }
            //Resizes all the Cells
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[0].length; j++) {
                    cells[i][j].resize(newSize);
                    cells[i][j].calculateNewLocation(this);
                }   
            }

            //Resizes all the Units 
            try{
                for (Unit unit : (List<Unit>)getWorld().getObjects(Unit.class)) {
                    unit.resize(percentChange, this); 
                }
            } catch (NullPointerException e) { //The game has not started yet (the initial resizing of the Cells)
            }
        }
    }

    /**
     * Sets the cell at the given coordinates to the given Cell
     * 
     * @param x         The x co-ordinate of the Cell
     * @param y         The y co-ordinate of the Cell
     * @param cell      The Cell object
     */
    public void setCell(int x, int y, Cell cell) {
        Game game = (Game)getWorld();
        Object[] data = new Object[2];
        int[] coordinates = {x, y};
        data[0] = coordinates;
        data[1] = cell;
        if (game.isMultiplayer() && !game.isHosting()) { //Client
            game.sendRequest(new Request(Request.CHANGE_CELL, data));
        }else{
            //Gets the positions of cells to remove
            Cell cellToRemove = cells[x][y];
            int xPos = cellToRemove.getX();
            int yPos = cellToRemove.getY();
            //Copies over data, removes Cells
            Cell.copyCellData(cellToRemove, cell);
            getWorld().removeObject(cells[x][y]);
            cells[x][y] = cell;
            //Adds new cells into the world
            getWorld().addObject(cell, xPos, yPos);
            changed = true;
            if (game.isHosting()) { //Server
                game.sendUpdate(new Update(Update.CHANGE_CELL, data));
            }
        }
    }

    /**
     * Sets the Cell at the given coordinates for clients in multiplayer
     * mode.
     * 
     * @param x     The x coordinate on the map
     * @param y     The y coordiante on the map
     * @param cell  The Cell object
     */
    public void setClientCell(int x, int y, Cell cell) {
        //Gets the positions of cells to remove
        Cell cellToRemove = cells[x][y];
        int xPos = cellToRemove.getX();
        int yPos = cellToRemove.getY();
        //Copies over data, removes Cells
        Cell.copyCellData(cellToRemove, cell);
        getWorld().removeObject(cells[x][y]);
        cells[x][y] = cell;
        //Resizes the image
        cell.resize(cellSize);
        //Adds new cells into the world
        getWorld().addObject(cell, xPos, yPos);
        changed = true;
    }

    /**
     * Returns if the Map has changed or not
     * 
     * @return          True if the Map has changed during this act
     */
    public boolean getChanged() {
        return changed;
    }

    /**
     * Sets the value for changed
     * 
     * @param changed   The new value for changed
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * Returns all Cells within the Map
     * 
     * @return      All the Cells contained within this Map
     */
    public Cell[][] getCells() {
        return cells;
    }

    /**
     * Returns the minimum x value of the viewing area
     * 
     * @return      The minimum x value of the viewing area
     */
    public int getViewX() {
        return viewX;
    }

    /**
     * Returns the minimum y value of the viewing area
     * 
     * @return      The minimum y value of the viewing area
     */
    public int getViewY() {
        return viewY;
    }

    /**
     * @param viewX     The min x value for the view area
     */
    public void setViewX(int viewX) {
        this.viewX = viewX;
    }

    /**
     * Returns the minimum y value for the view area
     * 
     * @param viewX     The min y value for the view area
     */
    public void setViewY(int viewY) {
        this.viewY = viewY;
    }

    public void manualImageResize(Unit temp) {
    }
}

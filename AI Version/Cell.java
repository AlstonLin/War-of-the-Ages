import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * The generic unit of anything that occupies a space on the Map, such as
 * a Building. The class controls the basic functionality of the interaction
 * between the Cell itself and the map, whereas subclasses of this would control
 * specific interaction between itself and the user. 
 * 
 * @author Alston lin
 * @version Beta 2.1
 */
public abstract class Cell extends Actor implements Selectable, Serializable
{   
    //Instance variables
    private int x, y;
    private boolean hidden;
    protected boolean traversable; //If a Unit can pass through this freely
    protected String stats;

    //Pathfinding variables
    private boolean visited;
    private Cell parent;
    private transient LinkedList<Cell> neighbours; 

    /**
     * Constructor for the Cell object. Sets the size, and location
     * 
     * @param x         The x index of the cell on the map
     * @param y         The y index of the cell on the map
     * @param cellSize  The size of each side 
     */
    public Cell(int x, int y, int cellSize) {
        this.x = x;
        this.y = y;
        traversable = true;
        neighbours = new LinkedList();
    }
    
    /**
     * Returns a refrence to the highest resolution image of 
     * this Cell (can be shared)
     * 
     * @return      A refrence to the highest resolution image of 
     * this Cell (can be shared)
     */
    public abstract GreenfootImage getOriginalImage();
    
    /**
     * Resets the pathfinding variables once it's finished 
     * 
     * @param game      The Game in which the Cells are contained in
     */
    public static void resetPathfinding(Game game) {
        Cell[][] cells = game.getMap().getCells();

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j].visited = false;
                cells[i][j].parent = null;
            }
        }
    }

    /**
     * Copys important data from one Cell to another to prepare
     * for replacement.
     * 
     * @param original      The original Cell to copy from
     * @param copy          The replacement Cell to copy to
     */
    public static void copyCellData(Cell original, Cell copy) {
        copy.hidden = original.hidden;
        copy.neighbours = original.neighbours;
        for (Cell cell: original.neighbours) { //Updates the pathfinding neighbours
            cell.replaceNeighbour(original, copy); 
        }
    }

    /**
     * Calculates the new location of the cell on the World 
     * when the user has scrolled the map. Culls the image if
     * it will not be visible
     * 
     * @param map       The Map that this will be placed in
     */
    public void calculateNewLocation(Map map) {
        int xPos = getAbsoluteX(map) - map.getViewX();
        int yPos = getAbsoluteY(map) - map.getViewY();
        setLocation(xPos, yPos);

        //Determines whether or not this Object should be hidden
        if (xPos <= 0 || xPos >= Map.SIZE_X || yPos <= 0 || yPos >= Map.SIZE_Y) { //Off the map
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
     * Scales the image of the Cell to the given side for both
     * dimentions, if it is within the legal range. 
     * 
     * @param side      The dimention of each side of the Cell 
     */
    public void resize(int side) {
        if (side <= Map.MAX_CELL_SIZE && side >= Map.MIN_CELL_SIZE) { //If the resizing is legal
            setImage(new GreenfootImage(getOriginalImage())); //Uses copy constructor so each Cell has it's own image
            getImage().scale(side, side);
        }
    }

    /**
     * Returns the x coordinate of this Cell
     * 
     * @return      The x coordinate of this Cell on the Map
     */
    public int getMapX() {
        return x;
    }

    /**
     * Returns the y coordinate of this Cell
     * 
     * @return      The y coordinate of this Cell on the Map
     */
    public int getMapY() {
        return y;
    }

    /**
     * Calculates the absolute value of the x coordinate in pixels relative to the Map
     * 
     * @param map       The Map that the Cell is contained in 
     * @return          The absolute x coordinate in pixels relative to the Map
     */
    public int getAbsoluteX(Map map) {
        return map.getCellSize() * (x + 1);
    }

    /**
     * Calculates the absolute value of the y coordinate in pixels relative to the Map
     * 
     * @param map       The Map that the Cell is contained in 
     * @return          The absolute y coordinate in pixels relative to the Map
     */
    public int getAbsoluteY(Map map) {
        return map.getCellSize() * (y + 1);
    }

    /**
     * Replaces a neighbour with another when a Cell has changed. 
     * 
     * @param toRemove      the Cell to remove
     * @param toAdd         the Cell that will replace the other
     */
    private void replaceNeighbour(Cell toRemove, Cell toAdd) {
        neighbours.remove(toRemove);
        neighbours.add(toAdd);
    }

    /**
     * Changes the Cell back to grass.
     */
    public void destroy() {
        Map map = ((Game)getWorld()).getMap();
        map.setCell(getMapX(), getMapY(), new Grass(getMapX(), getMapY(), map.getCellSize()));
    }

    /**
     * Adds a neighbour of the Cell for pathfinding purposes
     * 
     * @param neighbour     The Cell that is directly adjacent (above/below/beside) to this Cell
     */
    public void addNeighbour(Cell neighbour) {
        neighbours.add(neighbour);
    }

    /**
     * Should only be called from the pathfinding algorithm; visits
     * the Cell in BFS with the given parent Cell.
     * 
     * @param parent        The Cell that that comes before this one
     */
    public void visit(Cell parent) {
        visited = true;
        this.parent = parent;
    }

    /**
     * Sets if the unit is traversable
     * 
     * @param t     True if unit is traversable
     * @return      If the unit is traversable
     */
    public void setTraversable(boolean t) {
        traversable = t;
        World world = getWorld();
        try{
            Map map = ((Game)world).getMap();
            ((Game)getWorld()).getMap().setChanged(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a list of all the cells directly adjacent to this Cell
     * 
     * @return      A list of all the cells directly adjacent to (directly above/below/beside) 
     * this Cell
     */
    public LinkedList<Cell> getNeighbours() {
        return neighbours;
    }

    /**
     * Called during deserialization to restore transient fields.
     * 
     * @param stream    The input stream given during deserialization
     */
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject(); //Performs default object read first
        neighbours = new LinkedList();
    }

    /**
     * Returns is the Cell was visited by pathfinding
     * 
     * @return      If the Cell was visited by pathfinding
     */
    public boolean getVisited() {
        return visited;
    }

    /**
     * Returns if the unit is traversable
     * 
     * @return      If the unit is traversable
     */
    public boolean isTraversable() {
        return traversable;
    }

    /**
     * Returns the parent of this Cell stored from pathfinding
     * 
     * @return      The parent of this Cell stored from pathfinding
     */
    public Cell getParent() {
        return parent;
    }
}

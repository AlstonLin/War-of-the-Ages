import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * The spawnpoint of variou buildings and units
 * 
 * @author Alston Lin
 * @version 1.0
 */
public class SpawnPoint extends Cell
{   
    //Contants    
    public static final GreenfootImage ORIGINAL = new GreenfootImage("Grass.png");

    /**
     * Sets the Spawnpoint's image and location
     * 
     * @param x         The x index of the cell on the map
     * @param y         The y index of the cell on the map
     * @param cellSize  The size of each side 
     */
    public SpawnPoint(int x, int y, int cellSize) {
        super(x, y, cellSize);
        setImage(ORIGINAL);
        traversable = true;
        resize(cellSize);
    }

    /**
     * Returns information of this SpawnPoint   
     * 
     * @return      Information to display
     */
    public LinkedList<Button> getButtons() {
        return new LinkedList(); //No buttons
    }

    /**
     * Returns information of this Workshop
     * 
     * @return      Information to display
     */
    public String getInformation() {
        String lines = " ";
        return lines;
    }

    /**
     * Returns the original image
     * 
     * @return      The original image
     */
    public GreenfootImage getOriginalImage() {
        return ORIGINAL;    
    }

    /**
     * Changes the Cell back to Grass
     */
    public void destroy() {
        Map map = ((Game)getWorld()).getMap();
        map.setCell(getMapX(), getMapY(), new Grass(getMapX(), getMapY(), map.getCellSize()));
    }
}

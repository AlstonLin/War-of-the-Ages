import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.List;

/**
 * An overview of the entire in-game world map
 * 
 * @author Alston Lin
 * @version Beta 2.1
 */
public class Minimap extends Actor
{       
    //Constansts    
    public static final Color GRASS_COLOR = new Color(42, 120, 42), NATURE_COLOR = new Color(165, 120, 42), FRIENDLY_BUILDING_COLOR = Color.BLUE, FRIENDLY_UNIT_COLOR = Color.CYAN, ENEMY_BUILDING_COLOR = Color.RED, ENEMY_UNIT_COLOR = Color.MAGENTA; 
    public static final int SIZE_X = 560, SIZE_Y = 220;
    public static final int REFRESH_RATE = 5; //Refreshes every 5 acts

    //Instance variables
    private int counter; 

    /**
     * Updates the contents every 100 acts.
     */
    public void act() {
        MouseInfo info = Greenfoot.getMouseInfo();
        if (info != null) {
            if (info.getActor() == this && info.getButton() == 1) { //Clicked on the minimap, starts fast scrolling
                Map map = ((Game)getWorld()).getMap();
                //The percentage of the map coordinates that the mouse is currently at relative to the minimap
                double mapXPercent = (info.getX() - getX() + SIZE_X / 4) / (double) SIZE_X; 
                double mapYPercent = (info.getY() - getY() + SIZE_Y / 4) / (double) SIZE_Y;

                map.setViewX((int)(mapXPercent * map.getCellSize() * Map.CELLS_X + Map.SIZE_X / 2));
                map.setViewY((int)(mapYPercent * map.getCellSize() * Map.CELLS_Y + Map.SIZE_Y / 2));
                map.update();
            }
        }
        if (counter == 0) { //Time to update
            update();
            counter = REFRESH_RATE;
        } else{
            counter--;
        }
    }

    /**
     * Updates the contents of the minimap.
     */
    public void update() {
        GreenfootImage image = new GreenfootImage(SIZE_X, SIZE_Y);
        int cellXSize = SIZE_X / Map.CELLS_X + 1;
        int cellYSize = SIZE_Y / Map.CELLS_Y + 1;
        int unitXSize = SIZE_X / (2 * Map.CELLS_X) + 1;
        int unitYSize = SIZE_Y / (2 * Map.CELLS_Y) + 1;
        Map map = ((Game)getWorld()).getMap();
        Cell[][] cells = map.getCells();
        //Shows the cells on the minimap
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                Color cellColor = GRASS_COLOR;
                Cell cell = cells[i][j];
                if (cell instanceof Nature && !(cell instanceof Grass)) { //Part of nature
                    cellColor = NATURE_COLOR;
                } else if (cell instanceof Building) { //A Building
                    Player owner = ((Building)cell).getOwner();
                    if (owner == ((Game)getWorld()).getUser()) { //Friendly Building
                        cellColor = FRIENDLY_BUILDING_COLOR;
                    } else{ //Enemy Building
                        cellColor = ENEMY_BUILDING_COLOR;
                    }
                } 
                //Draws the box onto the minimap
                image.setColor(cellColor);
                image.fillRect(i * cellXSize, j * cellYSize, cellXSize, cellYSize);
            }
        }
        //Shows the units
        List<Unit> units = getWorld().getObjects(Unit.class);
        for (Unit unit : units) {
            Color color = ENEMY_UNIT_COLOR;
            Player owner = unit.getOwner();
            if (owner == ((Game)getWorld()).getUser()) {
                color = FRIENDLY_UNIT_COLOR;
            }
            //Draws the box onto the minimap
            image.setColor(color);
            image.fillRect(unit.getCurrentCell().getMapX() * cellXSize, unit.getCurrentCell().getMapY() * cellYSize, unitXSize, unitYSize);
        }
        //Draws the viewing box
        int mapXSize = map.getCellSize() * Map.CELLS_X; //Total pixel size of the x side
        int mapYSize = map.getCellSize() * Map.CELLS_Y;
        double viewXMinPercentage = map.getViewX() / (double) mapXSize;
        double viewYMinPercentage = map.getViewY() / (double) mapYSize;
        image.setColor(Color.BLACK);
        image.drawRect((int)(viewXMinPercentage * SIZE_X), (int)(viewYMinPercentage * SIZE_Y), (int)(Map.SIZE_X /  (double) mapXSize * SIZE_X), (int)(Map.SIZE_Y /  (double) mapYSize * SIZE_Y));
        setImage(image);
    }
}

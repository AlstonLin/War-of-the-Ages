import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * A Cell that would represent the majority of the Map at the start. 
 * It is capable having Buildings being built on it. In addition, 
 * destroyed Buildings will turn into Grass as well.
 * 
 * @author Alston Lin, Koko Deng
 * @version Beta 2.2
 */
public class Grass extends Nature
{   
    //Contants
    public static final GreenfootImage ORIGINAL = new GreenfootImage("Grass.png");

    //Button pictures
    private static GreenfootImage wallButton;
    private static GreenfootImage houseButton;
    private static GreenfootImage barracksButton;
    private static GreenfootImage stableButton;
    private static GreenfootImage workshopButton;

    /**
     * Sets the Grass' image and location
     * 
     * @param x     The x index of the cell on the map
     * @param y     The y index of the cell on the map
     * @param cellSize The size of each side 
     */
    public Grass(int x, int y, int cellSize) {
        super(x, y, cellSize);
    }

    /**
     * Returns information of this object
     * 
     * @return      Information to display
     */
    public String getInformation() {
        return "Grass";
    }

    /**
     * Returns a LinkedList of this objects's buttons
     * 
     * @return      LinkedList of this objects's buttons
     */
    public LinkedList<Button> getButtons() {
        LinkedList list = new LinkedList();
        final Player player = ((Game)getWorld()).getUser();
        if (traversable) {
            updateImage(player);

            list.add(new Button(wallButton){//Wall
                    public void click() {
                        ((Game)getWorld()).getActionsPanel().setButtons(new LinkedList());
                        buildWall(player);
                        updateImage(player);
                    }

                    public String getHoverInfo(){
                        return "{Wall} Wood Cost: " + Wall.WOOD_COST + " Gold Cost: " + Wall.GOLD_COST;
                    }
                });
            list.add(new Button(houseButton) {//House
                    public void click(){
                        ((Game)getWorld()).getActionsPanel().setButtons(new LinkedList());
                        buildHouse(player);
                        updateImage(player);
                    }

                    public String getHoverInfo() {
                        return "{House} Wood Cost: " + House.WOOD_COST + " Gold Cost: " + House.GOLD_COST;
                    }
                });
            try {
                Cell cellBelow = ((Game)getWorld()).getMap().getCells()[getMapX()][getMapY() + 1];
                if (cellBelow instanceof Grass) { //Room for spawn point 
                    list.add(new Button(barracksButton){//Barracks
                            public void click(){ 
                                ((Game)getWorld()).getActionsPanel().setButtons(new LinkedList());
                                buildBarracks(player);
                                updateImage(player);
                            }

                            public String getHoverInfo(){
                                return "{Barracks} Wood Cost: " + Barracks.WOOD_COST + " Gold Cost: " + Barracks.GOLD_COST;
                            }
                        });
                    list.add(new Button(stableButton){//Stable
                            public void click(){
                                ((Game)getWorld()).getActionsPanel().setButtons(new LinkedList());
                                buildStable(player);
                                updateImage(player);
                            }

                            public String getHoverInfo(){
                                return "{Stable} Wood Cost: " + Stable.WOOD_COST + " Gold Cost: " + Stable.GOLD_COST;
                            }
                        });
                    list.add(new Button(workshopButton){//Workshop
                            public void click() {
                                ((Game)getWorld()).getActionsPanel().setButtons(new LinkedList());
                                buildWorkshop(player);
                                updateImage(player);
                            }

                            public String getHoverInfo(){
                                return "{Workshop} Wood Cost: " + Workshop.WOOD_COST + " Gold Cost: " + Workshop.GOLD_COST;
                            }
                        });
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                //No cell below
            }
        }

        return list;
    }

    /**
     * Builds a House onto the Map
     */
    public void buildHouse(Player player) {
        //Map where object will spawn
        Map map = ((Game)getWorld()).getMap();

        if (player.getWood() >= House.WOOD_COST && player.getGold() >= House.GOLD_COST) { //Can afford
            //Consume resources
            player.changeWood((Game)getWorld(),-House.WOOD_COST);
            player.changeGold((Game)getWorld(),-House.GOLD_COST);

            //Create a new House onto the Map
            House house = new House(player, getMapX(), getMapY(), map.getCellSize());
            map.setCell(getMapX(), getMapY(), new ConstructionSite(player, getMapX(), getMapY(), House.BUILD_TIME, house, null, map.getCellSize())); 
        }
    }

    /**
     * Builds a Wall onto the Map
     */
    public void buildWall(Player player) {
        //Map where object will spawn
        Map map = ((Game)getWorld()).getMap();

        if (player.getWood() >= Wall.WOOD_COST && player.getGold() >= Wall.GOLD_COST) { //Can afford
            //Consume resources
            player.changeWood((Game)getWorld(),-Wall.WOOD_COST);
            player.changeGold((Game)getWorld(),-Wall.GOLD_COST);

            //Create a new Wall onto the Map
            Wall wall = new Wall(player, getMapX(), getMapY(), map.getCellSize());
            map.setCell(getMapX(), getMapY(), new ConstructionSite(player, getMapX(), getMapY(), Wall.BUILD_TIME, wall, null, map.getCellSize())); 
        }
    }

    /**
     * Builds a Town Center onto the Map
     */
    public void buildTownCenter(Player player) {
        //Map where object will spawn
        Map map = ((Game)getWorld()).getMap();
        Cell cellBelow = map.getCells()[getMapX()][getMapY() + 1];

        if (player.getWood() >= Center.WOOD_COST && player.getGold() >= Center.GOLD_COST) { //Can afford
            //Consume resources
            player.changeWood((Game)getWorld(),-Center.WOOD_COST);
            player.changeGold((Game)getWorld(),-Center.GOLD_COST);

            //Set location
            SpawnPoint sp = new SpawnPoint(cellBelow.getMapX(), cellBelow.getMapY(), map.getCellSize());
            Center center = new Center(player, getMapX(), getMapY(), sp, map.getCellSize());

            //Create a new Town Center onto the Map
            map.setCell(cellBelow.getMapX(), cellBelow.getMapY(), sp);
            map.setCell(getMapX(), getMapY(), new ConstructionSite(player, getMapX(), getMapY(), Center.BUILD_TIME, center, sp, map.getCellSize())); 
        }
    }

    /**
     * Builds a Barrack onto the Map
     */
    public void buildBarracks(Player player) {
        //Map where object will spawn
        Map map = ((Game)getWorld()).getMap();
        Cell cellBelow = map.getCells()[getMapX()][getMapY() + 1];

        if (player.getWood() >= Barracks.WOOD_COST && player.getGold() >= Barracks.GOLD_COST) { //Can afford
            //Consume resources
            player.changeWood((Game)getWorld(),-Barracks.WOOD_COST);
            player.changeGold((Game)getWorld(),-Barracks.GOLD_COST);

            //Set location
            SpawnPoint sp = new SpawnPoint(cellBelow.getMapX(), cellBelow.getMapY(), map.getCellSize());
            Barracks barracks = new Barracks(player, getMapX(), getMapY(), sp, map.getCellSize());

            //Create a new Barrack onto the Map
            map.setCell(cellBelow.getMapX(), cellBelow.getMapY(), sp);
            map.setCell(getMapX(), getMapY(), new ConstructionSite(player, getMapX(), getMapY(), Barracks.BUILD_TIME, barracks, sp, map.getCellSize())); 
        }
    }

    /**
     * Allows the AI to build its workshop
     * 
     * @return  The Barracks that has been built
     * @param   Player  The player building the Barracks
     * @param   int The x coordinate of the AI's town center
     * @param   int The y coordinate of the AI's town center
     */
    public Barracks buildBarracksAuto(Player player, int townCenterX, int townCenterY){
        //Map where objectwill spawn
        Map map = ((Game)getWorld()).getMap();
        Cell cellBelow = map.getCells()[townCenterX - 15][townCenterY - 10];

        //Set location
        SpawnPoint sp = new SpawnPoint(cellBelow.getMapX() + 1, cellBelow.getMapY(), map.getCellSize());
        Barracks barracks = new Barracks(player, cellBelow.getMapX(), cellBelow.getMapY(), sp, map.getCellSize());

        //Create a new Barracks onto the Map
        map.setCell(cellBelow.getMapX() + 1, cellBelow.getMapY(), sp);
        map.setCell(cellBelow.getMapX(), cellBelow.getMapY(), barracks);
        return barracks;
    }

    /**
     * Builds a Stable onto the Map
     */
    public void buildStable(Player player) {
        //Map where object will spawn
        Map map = ((Game)getWorld()).getMap();
        Cell cellBelow = map.getCells()[getMapX()][getMapY() + 1];

        if (player.getWood() >= Stable.WOOD_COST && player.getGold() >= Stable.GOLD_COST) { //Can afford
            //Consume resources
            player.changeWood((Game)getWorld(),-Stable.WOOD_COST);
            player.changeGold((Game)getWorld(),-Stable.GOLD_COST);

            //Set location
            SpawnPoint sp = new SpawnPoint(cellBelow.getMapX(), cellBelow.getMapY(), map.getCellSize());
            Stable stable = new Stable(player, getMapX(), getMapY(), sp, map.getCellSize());

            //Create a new Stable onto the Map
            map.setCell(cellBelow.getMapX(), cellBelow.getMapY(), sp);
            map.setCell(getMapX(), getMapY(), new ConstructionSite(player, getMapX(), getMapY(), Stable.BUILD_TIME, stable, sp, map.getCellSize())); 
        }
    }

    /**
     * Allows the AI to build its workshop
     * 
     * @return  The Stable that has been built
     * @param   Player  The player building the Stable
     * @param   int The x coordinate of the AI's town center
     * @param   int The y coordinate of the AI's town center
     */
    public Stable buildStableAuto(Player player, int townCenterX, int townCenterY){
        //Map where object will spawn
        Map map = ((Game)getWorld()).getMap();
        Cell cellBelow = map.getCells()[townCenterX - 10][townCenterY - 3];

        //Set location
        SpawnPoint sp = new SpawnPoint(cellBelow.getMapX() + 1, cellBelow.getMapY(), map.getCellSize());
        Stable stable = new Stable(player, cellBelow.getMapX(), cellBelow.getMapY(), sp, map.getCellSize());

        //Create a new Stable onto the Map
        map.setCell(cellBelow.getMapX() + 1, cellBelow.getMapY(), sp);
        map.setCell(cellBelow.getMapX(), cellBelow.getMapY(), stable);
        return stable;
    }

    /**
     * Builds a Workshop onto the Map
     */
    public void buildWorkshop(Player player) {
        //Map where object will spawn
        Map map = ((Game)getWorld()).getMap();
        Cell cellBelow = map.getCells()[getMapX()][getMapY() + 1];

        if (player.getWood() >= Workshop.WOOD_COST && player.getGold() >= Workshop.GOLD_COST) { //Can afford
            //Consume resources
            player.changeWood((Game)getWorld(),-Workshop.WOOD_COST);
            player.changeGold((Game)getWorld(),-Workshop.GOLD_COST);

            //Set location
            SpawnPoint sp = new SpawnPoint(cellBelow.getMapX(), cellBelow.getMapY(), map.getCellSize());
            Workshop workshop = new Workshop(player, getMapX(), getMapY(), sp, map.getCellSize());

            //Create a new Workshop onto the Map
            map.setCell(cellBelow.getMapX(), cellBelow.getMapY(), sp);
            map.setCell(getMapX(), getMapY(), new ConstructionSite(player, getMapX(), getMapY(), Workshop.BUILD_TIME, workshop, sp, map.getCellSize())); 
        }
    }

    /**
     * Allows the AI to build its workshop
     * 
     * @return  The Workshop that has been built
     * @param   Player  The player building the Workshop
     * @param   int The x coordinate of the AI's town center
     * @param   int The y coordinate of the AI's town center
     */
    public Workshop buildWorkshopAuto(Player player, int townCenterX, int townCenterY){
        //Map where object will spawn
        Map map = ((Game)getWorld()).getMap();
        Cell cellBelow = map.getCells()[townCenterX - 8][townCenterY - 20];

        //Set location
        SpawnPoint sp = new SpawnPoint(cellBelow.getMapX() + 1, cellBelow.getMapY(), map.getCellSize());
        Workshop workshop = new Workshop(player, cellBelow.getMapX(), cellBelow.getMapY(), sp, map.getCellSize());

        //Create a new Barracks onto the Map
        map.setCell(cellBelow.getMapX() + 1, cellBelow.getMapY(), sp);
        map.setCell(cellBelow.getMapX(), cellBelow.getMapY(), workshop);
        return workshop;
    }

    /**
     * Updates the various button images. 
     * Button will appear transluscent if there are not enough resources to spawn the Building.
     */
    private void updateImage(Player player) {
        //For Wall
        wallButton = Wall.getImage(player);
        if (player.getWood() < Wall.WOOD_COST || player.getGold() < Wall.GOLD_COST) {
            wallButton.setTransparency(100);
        } else {
            wallButton.setTransparency(255);
        }

        //For House
        houseButton = House.getImage(player);
        if (player.getWood() < House.WOOD_COST || player.getGold() < House.GOLD_COST) {
            houseButton.setTransparency(100);
        } else {
            houseButton.setTransparency(255);
        }

        //For Barracks
        barracksButton = Barracks.getImage(player);
        if (player.getWood() < Barracks.WOOD_COST || player.getGold() < Barracks.GOLD_COST) {
            barracksButton.setTransparency(100);
        } else {
            barracksButton.setTransparency(255);
        }

        //For Stable
        stableButton = Stable.getImage(player);
        if (player.getWood() < Stable.WOOD_COST || player.getGold() < Stable.GOLD_COST) {
            stableButton.setTransparency(100);
        } else {
            stableButton.setTransparency(255);
        }

        //For Workshop
        workshopButton = Workshop.getImage(player);
        if (player.getWood() < Workshop.WOOD_COST || player.getGold() < Workshop.GOLD_COST) {
            workshopButton.setTransparency(100);
        } else {
            workshopButton.setTransparency(255);
        }
    }

    /**
     * Returns the original image
     * 
     * @return      The original image
     */
    public GreenfootImage getOriginalImage() {
        return ORIGINAL;    
    }
}

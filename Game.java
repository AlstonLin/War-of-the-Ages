import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.io.*;
import java.net.ServerSocket;

/**
 * Entry point of the program and initializes all major components.
 * 
 * @author Alston Lin, Jesmin Hondell
 * @version 1.0
 */
public class Game extends ChildWorld implements Serializable
{   
    //Constants
    public static final String SAVE_FILE_NAME = "Save.txt";
    public static final int PLAYER_UPDATE_RATE = 100;
    public static final int MULTIPLAYER_GREENFOOT_SPEED = 50; //Set speed for multiplayer

    //Objects (Singleplayer)
    private Player user;
    private AI enemy;

    //Objects (Multiplayer)
    private transient boolean multiplayer, hosting;
    private transient int userID;
    private transient LinkedList<Player> players;

    //Objects (Multiplayer, server)
    private transient int updateCounter = 0;
    private transient Thread requestsThread;
    private transient ServerSocket serverSocket;
    private transient List<ObjectOutputStream> outStreams;
    private transient List<ObjectInputStream> inStreams;

    //Objects (Multiplayer, client)
    private transient Thread updateThread;
    private transient ObjectOutputStream outStream;
    private transient ObjectInputStream inStream;

    //Objects (all types)
    private Map map;
    private transient ActionsPanel actionsPanel;
    private transient InformationPanel informationPanel;

    /**
     * Constructor for a single player Game; there should only be one instance of this
     * at any given time.
     */
    public Game() {    
        configureGreenfoot();
        setup();
    }   

    /**
     * Creates a new multiplayer game, where this side is the server.
     * 
     * @param socket        The connection socket of the server
     * @param inStreams     A List of the input streams to all the clients
     * @param outStreams    A List of output streams to all the clients
     */
    public Game(ServerSocket serverSocket, List<ObjectInputStream> inStreams, List<ObjectOutputStream> outStreams) {
        this.serverSocket = serverSocket;
        this.inStreams = inStreams;
        this.outStreams = outStreams;
        multiplayer = true;
        hosting = true;
        configureGreenfoot();
        setupServer();
        Greenfoot.setSpeed(MULTIPLAYER_GREENFOOT_SPEED);
    }

    /**
     * Creates a new multiplayer game, where this side is the client.
     * 
     * @param inStream      The input stream to the server
     * @param outStream     The output stream to the server
     */
    public Game(ObjectInputStream inStream, ObjectOutputStream outStream) {
        this.inStream = inStream;
        this.outStream = outStream;
        multiplayer = true;
        configureGreenfoot();
        setupClient();
        Greenfoot.setSpeed(MULTIPLAYER_GREENFOOT_SPEED);
    }

    /**
     * Configures Greenfoot paint and act orders.
     */
    private void configureGreenfoot() {
        setPaintOrder(EndTitle.class, Popup.class, FrameSide.class, FrameBottom.class, StatisticsPanel.class, InformationPanel.class, Button.class, Minimap.class, 
            StatisticsPanel.class, ActionsPanel.class, OptionsPanel.class, Unit.class, HealthBar.class, TeamColor.class, Projectile.class, Selector.class, Cell.class);
        setActOrder(Unit.class, Cell.class, Player.class, Map.class, UIBackground.class);
    }

    /**
     * Sets up the major components of the game. Should be called only
     * upon initiation.
     */
    private void setup() {
        //Initate objects
        map = new Map(this);
        user = new Player();
        enemy = new AI(user, this);
        addObject(new UIBackground(), 200, 345);

        setupUI();

        //Creates a clearing for the center
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                map.setCell(3 + i, 3 + j, new Grass(3 + i, 3 + j, map.getCellSize()));
            }
        }

        //Player town center
        SpawnPoint sp1 = new SpawnPoint(5, 6, map.getCellSize());
        map.setCell(5, 6, sp1);
        user.setCenter(new Center(user, 5, 5, sp1, map.getCellSize()));
        map.setCell(5, 5, user.getCenter());

        //Spawns 6 Civilians
        for (int i = 0; i < 6; i++) {
            spawnUnit(new Civilian (user, sp1, map), sp1);
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                map.setCell(87 + i, 37 + j, new Grass(87 + i, 37 + j, map.getCellSize()));
            }
        }

        //Enemy town center
        SpawnPoint sp2 = new SpawnPoint(90, 41, map.getCellSize());
        map.setCell(90, 41, sp2);
        enemy.setCenter(new Center(enemy, 90, 40, sp2, map.getCellSize()));
        map.setCell(90, 40, enemy.getCenter());

        //Spawns 6 Civilians
        for (int i = 0; i < 6; i++) {
            spawnUnit(new Civilian (enemy, sp2, map), sp2);
        }
    }

    /**
     * Sets up the game for a server.
     */
    private void setupServer() {
        //Initiates the objects
        map = new Map(this);
        players = new LinkedList();
        user = new Player();
        players.add(user);
        addObject(map, 0, 0);

        //First sends the clients which player they are
        for (ObjectOutputStream stream : outStreams) {
            try{
                Player player = new Player();
                players.add(player);
                stream.writeObject(player);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        setupMultiplayerMap();

        //Then sends the clients the rest of the data
        for (ObjectOutputStream stream : outStreams) {
            try{
                stream.writeObject(players);
                stream.writeObject(map);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        setupUI();

        requestsThread = new Thread() {
            public void run() {
                handleRequests();
            }
        };
        requestsThread.start();

        for (Player player : players) {
            Center center = player.getCenter();
            for (int i = 0; i < 6; i++) {
                Civilian civilian = new Civilian(player, center.getSpawnPoint(), map);
                spawnUnit(civilian, center.getSpawnPoint());
                center.addUnitToMultiplayer(civilian);
            }
        }
    }

    /**
     * Sets up the game for a client.
     */
    private void setupClient() {
        boolean ready = false;

        //Waits for client to send what player this person is
        while(!ready) { 
            try{
                user = (Player)inStream.readObject();
                userID = user.getID(); 
                ready = true;
            } catch (IOException e) { //Nothing recieved yet
            } catch (ClassNotFoundException e) { //This should not happen
                e.printStackTrace();
            }
        }

        ready = false;

        //Waits for client to send the players in this game
        while(!ready) { 
            try{
                players = (LinkedList<Player>)(inStream.readObject());
                ready = true;
            } catch (IOException e) { //Nothing recieved yet
            } catch (ClassNotFoundException e) { //This should not happen
                e.printStackTrace();
            }
        }

        ready = false;

        //Waits for client to send the map
        while(!ready) { 
            try{
                map = (Map)inStream.readObject();
                addObject(map, 0, 0);
                map.placeCells(this);
                map.changeCellSize(0);
                map.setupFrame(this);
                ready = true;
            } catch (IOException e) { //Nothing recieved yet
            } catch (ClassNotFoundException e) { //This should not happen
                e.printStackTrace();
            }
        }

        setupUI();

        updateThread = new Thread(){
            public void run() {
                handleUpdates();
            }
        };
        updateThread.start();
    }

    /**
     * Used in multiplayer mode for clients; handles any updates from the server.
     * This method is only to be used concurrently.
     */
    private void handleUpdates() {
        while (true) { //Continues until thread is stopped
            try {
                Update update = (Update)inStream.readObject();
                switch (update.getType()) {
                    case Update.PLAYER_UPDATE:{ //Player data update
                        int[] data = (int[])update.getData();
                        for (Player player : players){
                            if (player.getID() == data[0]){ //This version of the player
                                player.updateField(data[1], data[2]); 
                            }
                        }
                        break; //Unit path change update
                    }
                    case Update.UNIT_PATH_CHANGE:
                    updateUnitPath((List<int[]>)update.getData());
                    break; //Unit creation update
                    case Update.UNIT_CREATION:
                    createUnit((Unit)update.getData());
                    break;
                    case Update.CHANGE_CELL:
                    changeCell((Object[])update.getData());
                    break;
                    case Update.AGE_UP:
                    List<Center> centers = getObjects(Center.class);
                    for (Center center : centers){
                        if (center.getOwner().getID() == ((Player)update.getData()).getID()){
                            center.clientAgeUp();
                        }
                    }
                    break;
                    case Update.UNIT_ATTACK:
                    Object[] data = (Object[])update.getData();
                    Unit attacker = getTwinUnit((Unit)data[0]);
                    Object target = data[1];
                    if (target instanceof Unit) {
                        attacker.clientAttack(getTwinUnit((Unit)target));
                    } else if (target instanceof Building) {
                        Building targetBuilding = (Building)target;
                        attacker.clientAttack(map.getCells()[targetBuilding.getMapX()][targetBuilding.getMapY()]); 
                    }
                }   
            } catch (IOException e) {
            } catch (ClassNotFoundException e) { //This should not happen
                e.printStackTrace();
            }
        }
    }

    /**
     * Changes the given Cell on the Map for multiplayer mode.
     * 
     * @param data      The data on the change
     */
    private void changeCell(Object[] data) {
        int[] coordinates = (int[])data[0];
        map.setClientCell(coordinates[0], coordinates[1], cloneCell((Cell)data[1]));
    }

    /**
     * Clones the given Cell to create a version that will work with this Game,
     * for multiplayer purposes.
     * 
     * @param toClone   The cell to clone
     * @return          The cloned Cell
     */
    private Cell cloneCell(Cell toClone) {
        Cell newCell = null;
        if (toClone == null) { //Sanity check
            return null;
        }

        //Now creates a clone of the Cell from the data given
        try{
            if (toClone instanceof Building) { 
                if (toClone instanceof ConstructionSite) {
                    ConstructionSite original = (ConstructionSite) toClone;
                    SpawnPoint sp = original.getSpawnPoint();
                    newCell = new ConstructionSite(getTwinPlayer(original.getOwner()), toClone.getMapX(), toClone.getMapY(), 
                        original.getBuildTime(), (Building) cloneCell(original.getBuildTo()), sp == null ? null : (SpawnPoint) map.getCells()[original.getSpawnPoint().getMapX()][original.getSpawnPoint().getMapY()], map.getCellSize());
                } else if (toClone instanceof Wall || toClone instanceof House) {
                    newCell = toClone.getClass().getConstructor(Player.class, int.class, int.class, int.class).newInstance(getTwinPlayer(((Building)toClone)
                            .getOwner()), toClone.getMapX(), toClone.getMapY(), map.getCellSize());
                } else{
                    Cell sp = ((Building)toClone).getSpawnPoint();
                    newCell = toClone.getClass().getConstructor(Player.class, int.class, int.class, SpawnPoint.class, int.class).newInstance(getTwinPlayer(((Building)toClone)
                            .getOwner()), toClone.getMapX(), toClone.getMapY(), (SpawnPoint)map.getCells()[sp.getMapX()][sp.getMapY()], map.getCellSize());
                }
            } else{
                newCell = toClone.getClass().getConstructor(int.class, int.class, int.class).newInstance(toClone.getMapX(), toClone.getMapY(), map.getCellSize());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newCell;
    }

    /**
     * Creates a clone Unit in this World based on the given Unit;
     * for multiplayer mode only.
     */
    private void createUnit(Unit unit) {
        //Manipulates the class Object to make a new instance of the same class
        try{
            Cell cell = unit.getCurrentCell();
            Unit newUnit = unit.getClass().getConstructor(Player.class, Cell.class, Map.class).newInstance(getTwinPlayer(unit.getOwner()), map.getCells()[cell.getMapX()][cell.getMapY()], map);
            spawnUnit(newUnit, newUnit.getCurrentCell());
            if (hosting) { //Sends an update to all the Clients that a new Unit has been created
                sendUpdate(new Update(Update.UNIT_CREATION, unit));
            } else{
                newUnit.setID(unit.getID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets this Game's version of the given player; for multiplayer purposes.
     * 
     * @param otherPlayer   The Player from the other Game
     * @return              This Game's version of the player
     */
    private Player getTwinPlayer(Player otherPlayer) {
        //Linear search as the size would be less than 7
        for (Player player : players) {
            if (otherPlayer.getID() == player.getID()) {
                return player;
            }
        }
        return null;
    }

    /**
     * Updates the path of a Unit for multiplayer.
     * 
     * @param data      The data given by the server, where
     * the first index is the Unit ID, and the following are
     * coordinates of the path.
     */
    private void updateUnitPath(List<int[]> data) throws ClassCastException{
        List<Unit> allUnits = getObjects(Unit.class);
        //Linear search, as the list will be constantly updated and constantly sorting it would be more inefficient
        for (Unit unit : allUnits) {
            if (unit.getID() == data.get(0)[0]) { //Same ID
                LinkedList<Cell> path = new LinkedList();
                for (int i = 1; i < data.size(); i++) {
                    path.add(map.getCells()[data.get(i)[0]][data.get(i)[1]]);
                }
                unit.setPath(path);
            }
        }
    }

    /**
     * Sends an update to all the clients in multiplayer mode.
     * 
     * @param update    The update to send
     */
    public void sendUpdate(Update update) {
        for (ObjectOutputStream stream : outStreams) { //Sends the update to every stream open
            try{
                stream.writeObject(update);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a request to the server in multiplayer mode.
     * 
     * @param request   The request to send
     */
    public void sendRequest(Request request) {
        try{
            outStream.writeObject(request);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds this Game's version of the given unit, for
     * multiplayer purposes.
     * 
     * @param otherUnit     The Unit that deos not belong to this Game
     * @return              This Game's version of the Unit
     */
    private Unit getTwinUnit(Unit otherUnit) {
        if (otherUnit == null) { //Sanity check
            return null;
        }
        List<Unit> allUnits = getObjects(Unit.class);
        for(Unit unit : allUnits) {
            if (unit.getID() == otherUnit.getID()) {
                return unit;
            }   
        }
        return null;
    }

    /** 
     * Used in multiplayer mode for servers; handles any requests from the client.
     * This method is only to be used concurrently.
     */
    private void handleRequests() {
        while (true) { //Continues until thread is stopped
            for (ObjectInputStream stream : inStreams) {
                try{
                    Request request = (Request)stream.readObject();
                    switch(request.getType()) {
                        case Request.SELECT: //Request for a list of units to select something
                        selectUnits((Object[])request.getData());
                        break;
                        case Request.CREATE_UNIT:
                        createUnit((Unit)request.getData());
                        break;
                        case Request.AGE_UP:
                        getTwinPlayer((Player)request.getData()).getCenter().serverAgeUp();
                        break;
                        case Request.CHANGE_CELL:
                        Object[] data = (Object[])request.getData();
                        int[] coordinates = (int[])data[0];
                        map.setCell(coordinates[0], coordinates[1], cloneCell((Cell)data[1]));
                    }
                } catch (IOException e) {
                } catch (ClassNotFoundException e) { //This should not happen
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Selects the Units to the appropriate Object based
     * on the data provided.
     * 
     * @param data      An Object of raw information
     */
    private void selectUnits(Object[] data) {
        Object target = data[0];
        List<Unit> unitsToSelect = (List<Unit>)data[1];
        for (Unit unitToSelect : unitsToSelect) {
            Unit unit = getTwinUnit(unitToSelect);
            if (unit == null) { //This unit deos not exist
                return;
            }
            //Linear search as this list is constantly updated and sorting would be redundant
            if (target instanceof Cell) {
                Cell cell = map.getCells()[((Cell)target).getMapX()][((Cell)target).getMapY()];
                unit.selectCell(cell);
            } else if (target instanceof Unit) {
                unit.selectUnit(getTwinUnit((Unit)target));
            }
        }
    }

    /**
     * Sets up the map for multiplayer.
     */
    private void setupMultiplayerMap() {
        Random random = new Random();
        for (Player player : players) {
            int centerX = random.nextInt(Map.CELLS_X - 20) + 10;
            int centerY = random.nextInt(Map.CELLS_Y - 20) + 10;
            SpawnPoint sp = new SpawnPoint(centerX, centerY + 1, map.getCellSize());
            //Makes a clearing
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    int x = centerX - 2 + i;
                    int y = centerY - 2 + j;
                    if (!(map.getCells()[x][y] instanceof Center)) { //Safety check
                        map.setClientCell(x, y, new Grass(x, y, map.getCellSize()));
                    }
                }
            }
            map.setClientCell(centerX, centerY + 1, sp);
            Center center =  new Center(player, centerX, centerY, sp, map.getCellSize());
            map.setClientCell(centerX, centerY, center);
            player.setCenter(center);
        }
    }

    /** 
     * Sets up the user interface Objects, including the placement of the Map, panels, ect.
     */
    private void setupUI() {
        actionsPanel = new ActionsPanel();
        informationPanel = new InformationPanel();

        Minimap minimap = new Minimap();

        addObject(map, 640, 234);
        addObject(new StatisticsPanel(), 359, 484);
        addObject(actionsPanel, 359, 630);
        addObject(minimap, 1000, 610);
        addObject(new ZoomInButton(), 1050, 150);
        addObject(new ZoomOutButton(), 1050, 350);
        addObject(informationPanel, 359, 520);
        addObject(new OptionsPanel(), 1000, 484);
        addObject(new SaveButton(), 1100, 485);
        addObject(new MenuButton(), 891, 485);

        minimap.update();
    }

    /**
     * Saves the game into a file.
     */
    public void save() {
        try {
            List list = user.getBuildings();
            FileOutputStream fileStream = new FileOutputStream(SAVE_FILE_NAME);
            ObjectOutputStream stream = new ObjectOutputStream(fileStream);
            stream.writeObject(this); //Serializes the Game
            stream.close();
            fileStream.close();
        } catch (IOException e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
        }
    }

    /**
     * Called during deserialization to restore transient fields.
     * 
     * @param stream    The input stream given during deserialization
     */
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject(); //Performs default object read first
        configureGreenfoot();
        //Reconstructs the transient fields
        actionsPanel = new ActionsPanel();
        informationPanel = new InformationPanel();
        setupUI();
        map.placeCells(this);
        map.changeCellSize(0);
        map.setupFrame(this);
        restoreUnits();
    }

    /**
     * Spawns the given Unit into the world.
     * 
     * @param unit      The Unit to spawn
     * @param cell      The Cell that it will spawn onto
     */
    private void spawnUnit(Unit unit, Cell cell) {
        unit.setSpawned(true);
        addObject(unit, cell.getX(), cell.getY());
    }

    /**
     * Restores Units from de-serialization by adding them
     * back to the World.
     */
    private void restoreUnits() {
        //Creates a List of all the Units from the Players
        List<Unit> units = user.getUnits();
        units.addAll(enemy.getUnits());
        //Adds every Unit found to the World and updates the location
        for (Unit unit : units) {
            unit.updateImage(map, new GreenfootImage(unit.getFrontImage()));
            if (unit.isSpawned()) { //Only spawned units will be added
                addObject(unit, 0, 0);
                addObject(unit.getHealthBar(), unit.getX(), unit.getY() - 30);
                unit.calculateNewLocation(map);
            }
        }
    }

    /**
     * Safely disconnects the connection and data streams.
     */
    public void stopped() {
        if (multiplayer) {
            if (hosting) {
                requestsThread.interrupt();
                for (ObjectInputStream stream : inStreams) {
                    try{
                        stream.close();
                    } catch (IOException e) {
                    }
                }
                for (ObjectOutputStream stream : outStreams) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                    }
                }
                try {
                    serverSocket.close();
                } catch (IOException e) {
                }
            } else{
                updateThread.interrupt();
                try {
                    inStream.close();
                    outStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Returns the Player.
     * 
     * @return      The Player that the user of this program will be using
     */
    public Player getUser() {
        return user;
    }

    /**
     * Returns the enemy.
     * 
     * @return      A computer-controlled Player that will be the opposing force
     * of the user
     */
    public Player getEnemy() {
        return enemy;
    }

    /**
     * Returns the Map.
     * 
     * @return      The Map of this Game 
     */
    public Map getMap() {
        return map;
    }

    /**
     * Returns if this game is in multiplayer mode.
     * 
     * @return      If this game is in multiplayer mode
     */
    public boolean isMultiplayer() {
        return multiplayer;
    }

    /**
     * Returns if this game is the server in multiplayer mode.
     * 
     * @return      True when this game is the server in multiplayer mode
     */
    public boolean isHosting() {
        return hosting;
    }

    /**
     * Returns the ActionsPanel.
     * 
     * @return      The ActionsPanel of this Game
     */
    public ActionsPanel getActionsPanel() {
        return actionsPanel;
    }

    /**
     * Returns the Information Panel of this game.
     * 
     * @return      The InformationPanel of this Game
     */
    public InformationPanel getInformationPanel() {
        return informationPanel;
    }
}

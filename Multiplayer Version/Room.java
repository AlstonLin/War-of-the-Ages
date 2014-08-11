import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.net.*;
import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;

/**
 * A Room for Multiplayer mode that will be used while waiting for players.
 * 
 * @author Alston Lin   
 * @version Beta 2.2
 */
public class Room extends World
{   
    //Constants
    public static final int PORT_NUMBER = 56342;
    public static final int REFRESH_RATE = 10;
    public static final Font FONT = new Font("Serif", Font.PLAIN, 48);
    public static final GreenfootImage ORIGINAL = new GreenfootImage("MultiplayerWaitingRoomBackground.png");

    //Instant variables
    private boolean changed;

    //Network variables
    private String hostname;
    private boolean hosting; //True = server, False = client

    //Server variables
    private ServerSocket serverSocket;
    private LinkedList<Socket> clients;
    private LinkedList<ObjectOutputStream> outStreams;
    private LinkedList<ObjectInputStream> inStreams;
    private Thread listeningThread;

    //Client variables
    private long lastUpdateTime;
    private Socket socket; 

    //Other variables
    private int counter;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;

    /**
     * Constructor for objects of class Room.
     * 
     * @param hosting   If the user is hosting the room
     * @param name      The IP Address of the host you are trying to connect to, or null if hosting
     */
    public Room(boolean hosting, String hostname) {    
        super(1280, 720, 1); 
        setBackground(new GreenfootImage(ORIGINAL));
        this.hosting = hosting;
        outStreams = new LinkedList();
        inStreams = new LinkedList();

        if (hosting) {
            setupServer();
            final Room room = this;
            //Start game button
            addObject(new Button(new GreenfootImage("MultiplayerStartButton.png")) {

                    public void click() {
                        //First, sends each client an update
                        for (ObjectOutputStream stream : outStreams) {
                            try{
                                stream.writeObject(new Update(Update.START_GAME, null));
                            } catch (IOException e) {
                            }
                        }
                        //Safety preccautions
                        listeningThread.interrupt();
                        //Starts the game
                        Greenfoot.setWorld(new Game(serverSocket, room.getInputStreams(), room.getOutputStreams()));
                    }

                    public String getHoverInfo() {
                        return "";
                    }
                }, 640, 550);
        } else{
            this.hostname = hostname;
            setupClient();
        }
    }

    /**
     * Checks updates and draws the frame
     */
    public void act() {
        //Check if an update is needed from the network
        if (counter == REFRESH_RATE) {
            if (hosting) {
                updateClients();
            } else {
                update();
            }
            counter = 0;
        } else {
            counter++;
        }

        //Changes the display if needed
        if (changed) {
            //Redraws the frame as it has changed
            setBackground(new GreenfootImage(ORIGINAL));
            drawHostName();
            drawClientNames();
            changed = false;
        }
    }

    /**
     * Called by Greenfoot when the program has stopped.
     */
    public void stopped() {
        if (hosting) {
            listeningThread.interrupt();
            closeServer();
        } else{
            closeClient();
        }
    }

    /**
     * Sets up the sever side.
     */
    private void setupServer() {
        try{
            //Variables
            hostname = InetAddress.getLocalHost().getHostName();
            serverSocket = new ServerSocket(PORT_NUMBER);
            clients = new LinkedList();
            drawHostName();
            //Creates a thread for the sever socket to listen for clients continuously
            listeningThread = new Thread() {
                public void run() {
                    listenForSockets();
                }
            };
            listeningThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the client side.
     */
    private void setupClient() {
        lastUpdateTime = System.nanoTime();
        try{
            socket = new Socket(hostname, PORT_NUMBER);
            inStream = new ObjectInputStream(socket.getInputStream());
            outStream = new ObjectOutputStream(socket.getOutputStream());
            drawHostName();
        } catch (UnknownHostException e) {
            JOptionPane pane = new JOptionPane();
            pane.showMessageDialog(null, "The given hostname deos not exist in the Local Area Network");
        } catch (IOException e) { 
            JOptionPane pane = new JOptionPane();
            pane.showMessageDialog(null, "Could not connect to the given hostname");
        }
    }

    /**
     * Will continuously listen for client socket connections and add them to
     * the list. This method is meant to run in parrallel only.
     */
    private void listenForSockets() {
        while(true) {
            try{
                Socket socket = serverSocket.accept();
                clients.add(socket);
                outStreams.add(new ObjectOutputStream(socket.getOutputStream()));
                inStreams.add(new ObjectInputStream(socket.getInputStream()));
                changed = true;
            } catch (IOException e) {
            }
        }
    }

    /**
     * Updates as a client from the server by checking
     * for information to recieve.
     */
    private void update() {
        //Reads the object from the stream
        try{
            Object object = inStream.readObject();
            if (object != null) {
                if (object instanceof List) { //Data available
                    List list = (List)object;
                    for (int i = 0; i < list.size(); i++) {
                        getBackground().drawString((String)list.get(i), 400, 300 + 100 * i);
                    }
                } else if (object instanceof Update) { //Update available
                    if (((Update)object).getType() == Update.START_GAME) { //Start the game
                        Greenfoot.setWorld(new Game(inStream, outStream));
                    }

                }
            }
            lastUpdateTime = System.nanoTime();
        } catch (IOException e) { //Problem with the socket
            if (System.nanoTime() > lastUpdateTime + 10e9) { //More than 10 seconds from last update; timeout
                closeClient();
            }
        } catch (NullPointerException e) { //Never loaded
            closeClient();
        } catch (ClassNotFoundException e) { //This should not happen
            e.printStackTrace();
        }
    }

    /**
     * Updates the client sockets on people connected
     * to this room. 
     */
    private void updateClients() {
        LinkedList<String> textToDisplay = new LinkedList();
        //First creates a list of text to display from the names of the clients
        for (Socket socket : clients) {
            textToDisplay.add(socket.getLocalAddress().getHostName());
        }
        //Sends the list to every client
        for (ObjectOutputStream stream : outStreams) {
            //Sets up the output stream to send the list of strings to display to clients
            try{
                stream.writeObject(textToDisplay);
            } catch (IOException e) { //Something wrong with the socket
                changed = true;
                try{
                    Socket socket = clients.get(outStreams.indexOf(stream));
                    socket.close();
                    clients.remove(socket);
                } catch (IOException ex) { //Already closed
                    clients.remove(clients.get(outStreams.indexOf(stream)));
                }
            }
        }
    }

    /**
     * Safely closes the connection for a server.
     */
    private void closeServer() {
        try {
            serverSocket.close();
            for (Socket socket : clients) {
                socket.close();
            }
        } catch (IOException e) {
        } finally {
            Greenfoot.setWorld(new TitleScreen());
        }
    }

    /**
     * Safely closes all the network connections for a client.
     */
    private void closeClient() {
        Greenfoot.setWorld(new TitleScreen());
        try {
            inStream.close();
            socket.close();
        } catch (IOException e) {
        } catch (NullPointerException e) { //Never existed in the first place
        } finally {
            Greenfoot.setWorld(new TitleScreen());
        }
    }

    /**
     * Draws the host name of the room on the top of the screen
     */
    private void drawHostName() {
        getBackground().setFont(FONT);
        getBackground().setColor(Color.BLACK);
        String toDisplay = "Host Name: " + hostname;
        getBackground().drawString(toDisplay , 640 - toDisplay.length() * 12, 120);
    }

    /**
     * Draws the names of the clients
     */
    private void drawClientNames() {
        for (int i = 0; i < clients.size(); i++) {
            Socket socket = clients.get(i);
            getBackground().drawString(socket.getLocalAddress().getHostName(), 400, 300 + 100 * i);
        }
    }

    /**
     * Returns the list of the output streams of the clients
     * 
     * @return      The list of the output streams of the clients
     */
    public List<ObjectOutputStream> getOutputStreams() {
        return outStreams;
    }

    /**
     * Returns the list of the input streams of the clients
     * 
     * @return      The list of the input streams of the clients
     */
    public List<ObjectInputStream> getInputStreams() {
        return inStreams;
    }
}

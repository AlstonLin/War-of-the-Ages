import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;

/**
 * A packet of data sent by the server to clients in server in multiplayer mode,
 * and when recieved, the clients will process it depending on the type of data
 * 
 * @author Alston Lin
 * @version 1.0
 */
public class Update implements Serializable
{
    //Constants
    public static final int START_GAME = 0, END_GAME = 1, PLAYER_UPDATE = 2, CHANGE_CELL = 3, UNIT_PATH_CHANGE = 4, UNIT_CREATION = 5, UNIT_DESTRUCTION = 6, UNIT_ATTACK = 7, AGE_UP = 8; //To keep track of the types of request
    
    //Instance variables
    private int type;
    private Object data;

    /**
     * Creates a new update to send to a client.
     * 
     * @param type      The type of update
     * @param data      The relavant data that needs to be sent with it
     */
    public Update(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    /**
     * Returns the type of update this is
     * 
     * @return      The type of update this is
     */
    public int getType() {
        return type;
    }

    /**
     * Returns the relavent data for this update
     * 
     * @return      The relavent data for this update
     */
    public Object getData() {
        return data;
    }
}

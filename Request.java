import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * A packet of data for the multiplayer mode in which the client sends
 * a request to the server to perform a task. 
 * 
 * @author Alston Lin
 * @version 1.0
 */
public class Request implements Serializable
{
    public static final int SELECT = 1, CHANGE_CELL = 2, DELETE_UNIT = 3, CREATE_UNIT = 4, AGE_UP = 5; //To keep track of the types of request

    //Instance variables
    private int type;
    private Object data;

    /**
     * Creates a new request to the server that can be sent
     * and be handled.
     * 
     * @param type      The type of request
     * @param data      Whatever data that is required for the request to be handled
     */
    public Request(int type, Object data) {
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
     * Returns the relaventdata for this update
     * 
     * @return      The relavent data for this update
     */
    public Object getData() {
        return data;
    }
}

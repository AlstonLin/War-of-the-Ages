import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Simply a subclass of World that allows for a no argument conrstuctor for World 
 * upon deserialization
 * 
 * @author Alston Lin   
 * @version Beta 2.1
 */
public class ChildWorld extends World
{
    /**
     * Allows for a no argument constructor for de-serialization.
     */
    public ChildWorld() {    
        super(1280, 720, 1); 
    }
}

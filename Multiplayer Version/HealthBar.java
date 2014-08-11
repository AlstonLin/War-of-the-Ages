import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.Serializable;
import java.awt.Color;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * HealthBar is a Greenfoot Actor that displays text.
 * It creates a Very Generic Healthbar that can scale to any size depending on the user's needs
 * 
 * @author James Ly
 * @Version 1.0
 */
public class HealthBar extends Actor implements Serializable
{
    //Instance variables
    private int health;
    private int maxHealth;
    private int x = 0;
    private int y = 0;
    private double healthBarWidthMax = 50;
    private double healthBarWidth = 50;
    private int healthBarHeight = 10;
    private boolean colorSet = false;
    private boolean friendly = false;
    //Creates Greenfoot image
    private transient GreenfootImage healthBar = new GreenfootImage((int)healthBarWidth, healthBarHeight);
    //declaring colors for the bar that are either, red yellow or green
    //private Color healthColor1 = new Color (0, 255, 0);
    //private Color healthColor2 = new Color (255, 255, 0);
    //private Color healthColor3 = new Color (255, 0, 0);
    private Color backColor = new Color (50, 50, 50);

    /**
     * This will create a generic, 50 by 10 pixel HP bar. The position of the HP Bar will be dependent on where the object belongs in the world
     *
     *@param maxHealthTemp  The maximum HP for whatever this HP bar belongs to
     *@param currentHealth  The Current HP the object spawns with
     */
    public HealthBar (int maxHealthTemp, int currentHealth) {
        health = currentHealth;
        maxHealth = maxHealthTemp;
        healthBar = new GreenfootImage((int)healthBarWidth, healthBarHeight);
        drawHealthBar();
    }

    /**
     * This will create a more flexible HP bar with parameters to change how big one would like the HP bar to be. The position of the HP Bar will be dependent on where the object belongs in the world
     *
     *@param maxHealthTemp  The maximum HP for whatever this HP bar belongs to
     *@param currentHealth  The Current HP the object spawns with
     *@param width          The Width boundry of the HP bar
     *@param hieght         The Hiegth boundry of the HP bar
     */
    public HealthBar (int maxHealthTemp, int currentHealth, int width, int height) {
        health = currentHealth;
        maxHealth = maxHealthTemp;
        healthBarWidthMax = (double)width;
        healthBarHeight = height;
        healthBar = new GreenfootImage((int)healthBarWidthMax, healthBarHeight);
        drawHealthBar();
    }

    /**
     * This will create a more flexible HP bar with parameters to change how big one would like the HP bar to be. The position of the HP Bar will be dependent on where the object belongs in the world and what color it ought to be
     *
     *@param maxHealthTemp  The maximum HP for whatever this HP bar belongs to
     *@param currentHealth  The Current HP the object spawns with
     *@param width          The Width boundry of the HP bar
     *@param hieght         The Hiegth boundry of the HP bar
     */
    public HealthBar (int maxHealthTemp, int currentHealth, int width, int height, boolean color) {
        colorSet = true;
        friendly = color;
        health = currentHealth;
        maxHealth = maxHealthTemp;
        healthBarWidthMax = (double)width;
        healthBarHeight = height;
        healthBar = new GreenfootImage((int)healthBarWidthMax, healthBarHeight);
        drawHealthBar();
    }
    //nessesary public methods
    /**Use this to update the health bar, this will accept a new Health Value for the HP Bar, this new health value will instantly replace the old one.
     * The damage calculations must be done elsewhere
     *
     *@param newHealth      The New Health Value of the HP Bar
     */
    public void updateHealth (int newHealth) {
        health = newHealth;
        checkInRange();
    }

    /** 
     * Every frame, the Health Bar will update its image.
     */
    public void act() {
        drawHealthBar();
    }    

    /**
     * Called during deserialization to restore transient fields.
     * 
     * @param stream                    The input stream given during deserialization
     * @throws IOException              Input, output exception
     * @throws ClassNotFoundException   Cannot find class
     */
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject(); //Performs default object read first
        healthBar = new GreenfootImage(50, 10);
    }

    //Private Methods
    /** 
     * This will force the health value to stay within the predefined health maximun and zero
     */
    private void checkInRange() {
        if (health <= 0) {
            health = 0;
        }
        if (health >= maxHealth) {
            health = maxHealth;
        }
    }

    /**
     * Performs the nessesary calculations and sets the image of the actor to the health bar
     */
    private void drawHealthBar() {
        //Gets a percentage of how much HP is left and sets the width of the health bar to this percentage
        healthBarWidth = (healthBarWidthMax * ((double) health / (double) maxHealth));
       
        //Clears old image
        healthBar.clear();
        
        //Draws the background
        healthBar.setColor(backColor);
        healthBar.fillRect((int)(healthBarHeight / 2) + 1, 0, (int)healthBarWidthMax - healthBarHeight, healthBarHeight);
        
        //Draws those little cirlces at either side of image
        healthBar.fillOval(0, 0, healthBarHeight, healthBarHeight);
        healthBar.fillOval((int)healthBarWidthMax - healthBarHeight, 0, healthBarHeight, healthBarHeight);
        
        //Checks the HP value and sets the color of the HP bar before it is drawn
        if (!colorSet) {
            colorChange();
        } else {
            if (friendly) {
                Color healthColor = new Color (0, 150, 255);
                healthBar.setColor(healthColor);
            } else {
                Color healthColor = new Color (255, 0, 0);
                healthBar.setColor(healthColor);
            }
        }
        //Draws new rectangle image
        healthBar.fillRect(0 + (int)(healthBarHeight / 2), 0, (int)healthBarWidth - healthBarHeight, healthBarHeight);
        
        //Sets image
        this.setImage (healthBar);
        
        //TROUBLESHOOTING CODE
        //System.out.print ("BarH: ");
        //System.out.println (health);
        //System.out.println (healthBarWidth);
    }

    /** 
     * Nifty little color changer for the HP Bar that makes it so that when the health drops below a certain value, the color changes
     */
    private void colorChange() {
        /**
         * //code for the alternate color scheme
        double healthLeft = ((double) health / (double) maxHealth);
        if (healthLeft > 0.5)
        {
        healthBar.setColor(healthColor1);
        }
        else if (healthLeft <= 0.25)
        {
        healthBar.setColor(healthColor3);
        }
        else if (healthLeft <= 0.5)
        {
        healthBar.setColor(healthColor2);
        }
         */
        double red = 255 - (255 * ((double)health / (double)maxHealth));
        double green = (255 * ((double)health / (double)maxHealth));
        Color healthColor = new Color ((int)red, (int)green, 0);
        healthBar.setColor(healthColor);
    }

    /** 
     * Move healthbar things around
     * 
     * @param x     xcoordinate
     * @param y     ycoordinate
     */
    public void moveTo(int x, int y) {
        this.setLocation(x, y);
    }
}

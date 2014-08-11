import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The team colour depending on the player.
 * 
 * @author James Ly
 * @version 1.0
 */
public class TeamColor extends Actor
{
    //Image of the Team
    private GreenfootImage image;
    private Unit unitToFollow;

    /**
     * Sets up the team colour
     * 
     * @param blue      Indicates if the unit is friendly
     */
    public TeamColor (boolean blue, Unit unitToFollow) {
        this.unitToFollow = unitToFollow;
        if (blue) {
            image = new GreenfootImage("SelectionRing.png");
        } else {
            image = new GreenfootImage("enemyTile.png");
        }
        GreenfootImage unitImage = unitToFollow.getImage();
        image.scale(unitImage.getWidth(),unitImage.getHeight());
        setImage(image);
    }

    /**
     * Follows the Unit around
     */
    public void act()
    {
        try{
            moveTo(unitToFollow.getX(), unitToFollow.getY());
        } catch (Exception e) { //If the object is no longer in the world, or otherwise inaccessible
            this.getWorld().removeObject(this);
        }
    }

    /**
     * Sets the location of the Tile
     * 
     * @param x     The x co-ordinate of the tile
     * @param y     The y co-ordinate of the tile
     */
    public void moveTo (int x, int y) {
        setLocation(x,y + 10);
    }

    /**
     * Arrow is at the border
     */
    private void arrowsAtBorder() {
        if (getX() <= 30) {
            setLocation(30, getY());
        }
    }
}
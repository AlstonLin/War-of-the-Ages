import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The team colour depending on the player.
 * 
 * @author James Ly
 * @version Beta 2.1
 */
public class TeamColor extends Actor
{
    //Image of the Team
    private GreenfootImage image;
    private Unit unitToFollow;
    private boolean imageSwapped= false;
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
        image.scale(unitToFollow.getImage().getWidth(),unitToFollow.getImage().getHeight());
        setImage(image);
    }

    public void act()
    {
        moveTo(unitToFollow.getX(), unitToFollow.getY());
        if (atBorder()){
            if (!imageSwapped){
                setToArrow();
                imageSwapped = true;
            }
            turnTowards(unitToFollow.getX(), unitToFollow.getY());
        }
        else{
            if (imageSwapped){
                setRotation(0);
                setToRing();
                imageSwapped = false;
            }
        }
    }

    /**
     * Sets the location of the Tile
     * 
     * @param x     The x co-ordinate of the tile
     * @param x     The y co-ordinate of the tile
     */
    public void moveTo (int x, int y) {
        setLocation(x,y + 10);
    }

    private boolean atBorder(){
        boolean temp = false;
        if (getX() <= 50)
        {
            setLocation(50, getY());
            temp = true;
        }
        else if (getX() >= 1230)
        {
            setLocation(1230, getY());
            temp = true;
        }
        if (getY() <= 30)
        {
            setLocation(getX(), 30);
            temp = true;
        }
        else if (getY() >= 400)
        {
            setLocation(getX(), 400);
            temp = true;
        }
        return temp;
    }

    private void setToArrow(){
        image = new GreenfootImage("Arrow2.png");
        setImage(image);
        turnTowards(unitToFollow.getX(), unitToFollow.getY());
    }

    private void setToRing(){
        image = new GreenfootImage("SelectionRing.png");
        image.scale(unitToFollow.getImage().getWidth(),unitToFollow.getImage().getHeight());
        setImage(image);
    }
}
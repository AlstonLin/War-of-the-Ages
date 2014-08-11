import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
/**
 * Does nothing except looking asthetically pleasing
 * 
 * @author James Ly
 * @version 1.0
 */
public class Projectile extends Actor
{
    //Instance variables
    private GreenfootImage image = new GreenfootImage("Bullet.png");
    private GreenfootImage bloodyMess = new GreenfootImage("Blood.png");
    private int lifeSpan = 0;
    private int bloodSplatter = 0;
    private double distanceToTarget;
    private Actor target;
    private int tX, tY;
    private boolean reachedTarget;
    
    /**
     * Initializes the Projectile
     * 
     * @param       The actor to be targeted
     */
    public Projectile(Actor target) {
        //Intializes objects and variables
        reachedTarget = false;
        this.setImage(image);
        this.target = target;
        this.turnTowards(target.getX(), target.getY());
        tX = target.getX();
        tY = target.getY();
    }

    /**
     * Initializes the Projectile
     * 
     * @param target    The target
     * @param return    
     */
    public Projectile(Actor target, boolean temp) {
        reachedTarget = false;
        image = new GreenfootImage("CannonBall.png");
        this.setImage(image);
        this.target = target;
        this.turnTowards(target.getX(), target.getY());
        tX = target.getX();
        tY = target.getY();
    }

    /**
     * Act - do whatever the Projectile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        if (!reachedTarget) {
            lifeSpan++;
            move(30);
            distanceToTarget = Math.sqrt((double)((Math.pow((double)(this.getX() - tX), 2.0)+Math.pow((double)(this.getY() - tY), 2.0))));
            this.turnTowards(tX, tY);
            
            if (lifeSpan > 30) {
                getWorld().removeObject(this);
            } else if (intersects(target)) {
                reachedTarget = true;
                bloodyMess.scale(30,30);
                this.setImage(bloodyMess);
                this.setLocation(tX, tY);
            } else if ((int)distanceToTarget <= 30) {
                reachedTarget = true;
                bloodyMess.scale(30,30);
                this.setImage(bloodyMess);
                this.setLocation(tX, tY);
            }
        } else {
            bloodSplatter++;
            this.setLocation(tX, tY);
            if (bloodSplatter > 5) {
                getWorld().removeObject(this);
            }
        }
    }
}

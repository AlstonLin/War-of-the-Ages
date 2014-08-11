import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Font;

/**
 * The Statistics Panel displays various resource inventory count, 
 * i.e Food, wood, Gold, Population, of the User
 * 
 * @author Jesmin Hondell, Koko Deng
 * @version 1.0
 */
public class StatisticsPanel extends Actor
{
    //Constants
    private static final GreenfootImage ORIGINAL = new GreenfootImage("StatsPanelWithIcon.png");

    /**
     * Constructor for StatisticsPanel, sets the size
     */
    public StatisticsPanel() {
        setImage(ORIGINAL);
    }

    /**
     * Updates the statistics of the User.
     */
    public void act() {
        Player user = ((Game)getWorld()).getUser();
        GreenfootImage image = new GreenfootImage(ORIGINAL);
        setImage(image); //Sets the image as the clone

        getImage().setFont(new Font("Serif", Font.PLAIN, 24));

        String FoodStat = "Food: " + user.getFood();
        String WoodStat = "Wood: " + user.getWood();
        String GoldStat = "Gold: " + user.getGold();
        String PopulationStat = "Population: " + user.getPopulation();
        image.drawString(GoldStat, 48, 25);
        image.drawString(WoodStat, 205, 25);
        image.drawString(FoodStat, 370, 25);
        image.drawString(PopulationStat, 532, 25);
    }
}

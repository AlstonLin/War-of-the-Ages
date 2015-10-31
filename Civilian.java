import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.LinkedList;

/**
 * Civlians are the starting Units of the Game
 * 
 * @author Alston Lin, Daniel Chung, James Ly, Jesmin Hondell
 * @version 1.0
 */
public class Civilian extends Unit
{   
    //Constants
    public static final int FOOD_COST = 100, WOOD_COST = 0, GOLD_COST = 0;
    public static final int NONE = 0, FOOD = 1, WOOD = 2, GOLD = 3;
    private static final GreenfootImage FRONT = new GreenfootImage("CivilianFront.png");
    private static final GreenfootImage RIGHT = new GreenfootImage("CivilianRight.png");
    private static final GreenfootImage LEFT = new GreenfootImage("CivilianLeft.png");
    private static final GreenfootImage BACK = new GreenfootImage("CivilianBack.png");

    //Instance variables
    private Cell gatheringCell; //Cell being gathered from 
    private int resourceTimer;
    private int gatherTime = 30; //Number of acts to gather 1 resource
    static int SPAWN_TIME = 100;
    private int currentResource;

    /**
     * Constructor for Horseman, sets its cell and map
     *
     * @param cell      The Cell that the unit will spawn in
     * @param map       The Map that this will be put into
     */
    public Civilian(Player owner, Cell cell, Map map) {
        super(owner, cell, map);
        health = 50;
        attack = 5;
        attackSpeed = 2;
        currentHealth = health;
        unit = ("Civilian");
    }  

    public void act() {
        super.act();
        if (gatheringCell != null) { //Currently gathering
            if (resourceTimer == 0) { //Time to gather
                //Makes sure it's in range first
                if (gatheringCell.getMapX() + 1 >= currentCell.getMapX() && gatheringCell.getMapX() - 1 <= currentCell.getMapX() 
                && gatheringCell.getMapY() + 1 >= currentCell.getMapY() && gatheringCell.getMapY() - 1 <= currentCell.getMapY()){

                    if (gatheringCell instanceof BerryBush) {
                        if (((BerryBush)gatheringCell).getResources() > 0) { //Ensures theres resources left
                            owner.changeFood((Game)getWorld(), 1);
                            ((BerryBush)gatheringCell).removeResource(1);
                            currentResource = FOOD;
                        } else{ //None left
                            gatheringCell = null;
                            currentResource = NONE;
                        }
                    } else if (gatheringCell instanceof Tree) {
                        if (((Tree)gatheringCell).getResource(1) > 0) { //Ensures theres resources left
                            owner.changeWood((Game)getWorld(), 1);
                            ((Tree)gatheringCell).removeResource(1);
                            currentResource = WOOD;
                        }else{ //None left
                            gatheringCell = null;
                            currentResource = NONE;
                        }
                    } else if (gatheringCell instanceof Mine) {
                        if (((Mine)gatheringCell).getResource() > 0) { //Ensures theres resources left
                            owner.changeGold((Game)getWorld(), 1);
                            ((Mine)gatheringCell).removeResource(1);
                            currentResource = GOLD;
                        }else{ //None left
                            gatheringCell = null;
                            currentResource = NONE;
                        }
                    }
                    resourceTimer = gatherTime;
                } else {
                    gatheringCell = null; //Not in range
                    //Searches for nearby resources to go to
                    for (int i = 0; i < 7; i++){
                        for (int j = 0; j < 7; j++){
                            try{
                                Cell cell = ((Game)getWorld()).getMap().getCells()[currentCell.getMapX() - 3 + i][currentCell.getMapY() - 3 + j];
                                if (cell instanceof Gatherable){
                                    selectCell(cell);
                                }
                            } catch (ArrayIndexOutOfBoundsException e){ //Deos not exist
                            }
                        }
                    }
                }
            }else {
                resourceTimer--;
            }
        } else {
            currentResource = NONE;
        }
    }

    /**
     * Overrides the Cell selection to allow for resource gathering.
     */
    public void selectCell(Cell cell) {
        if (cell instanceof Gatherable) { //Can be gathered from
            try {
                targetCell = null;
                selectedCell = cell;
                //Looks for an adjacent Cell that can be traveled to 
                int[][] offsets = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

                for (int i = 0; i < offsets.length; i++) {
                    try{
                        Cell adjacentCell = ((Game)getWorld()).getMap().getCells()[cell.getMapX() + offsets[i][0]] [cell.getMapY() + offsets[i][1]]; 
                        if (adjacentCell.isTraversable()) { //Only targets the traversable ones
                            targetCell = adjacentCell;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) { //Cell deos not exist
                    }
                }
                if (targetCell == null) { //Cant access the resource
                    return;
                }
                following = false;
                target = null;
                findPath();
            } catch (NoPossiblePathException e) { //No possible path
                targetCell = null;
            }
        } else{ //Normal Cell
            super.selectCell(cell);
        }
    }

    /**
     * What happens when the Unit arrives at the destination Cell.
     */
    public void arrive() {
        if (selectedCell instanceof Gatherable) {
            gatheringCell = selectedCell;
        }
    }

    /**
     * Returns Buttons when unit is selected, also determines what happens once buttons are pressed
     * 
     * @return      The buttons when unit is selected
     */
    public LinkedList<Button> getButtons() {
        LinkedList list = new LinkedList();
        //Adds a Button allowing the unit to Commit suicide
        GreenfootImage image = new GreenfootImage("DestroyButton.png");
        image.scale(64, 64);
        list.add(new Button(image){
                public void click() {
                    takeDamage(9001.0);
                }

                public String getHoverInfo() {
                    return "Kill Yo'self";
                }
            });
        return list;
    }

    /**
     * Returns the current resource that is being gathered, as an integer.
     * 
     * @return      The current resource being gathered. FOOD = 1, WOOD = 2, GOLD = 3.
     */
    public int getCurrentResource() {
        return currentResource;
    }

    /**
     * Returns basic information to be used in the information Panel
     * 
     * @return      The unit's basic information
     */
    public String getInformation() {
        String lines = ("{" + unit + "} Health: " + currentHealth + " | Attack Damage: " + attack);
        InformationPanel informationPanel = new InformationPanel();
        informationPanel.display(lines);

        return lines;
    }

    /**
     * Returns the image when object is facing left
     * 
     * @return      The image when object is facing left
     */
    public GreenfootImage getLeftImage() {
        return LEFT;
    }

    /**
     * Returns the image when object is facing right
     * 
     * @return      The image when object is facing right
     */
    public GreenfootImage getRightImage() {
        return RIGHT;
    }

    /**
     * Returns the image when object is facing the front
     * 
     * @return      The image when object is facing the front
     */
    public GreenfootImage getFrontImage() {
        return FRONT;
    }

    /**
     * Returns the image when object is facing back (away)
     * 
     * @return      The image when object is facing back (away)
     */
    public GreenfootImage getBackImage() {
        return BACK;
    }
}

// import java.util.LinkedList;
// 
// /**
//  * An automated player whose goal is to slay the player and raze all of their buildings.
//  * 
//  * @author Daniel Chung
//  * @version Alpha 2.0
//  */
// public class TempAI extends Player 
// {
//     private static final int CIVILIAN = 1, MILITARY = 2, BUILDING = 3;
//     private static final int BARRACKS = 1, STABLE = 2, WORKSHOP = 3, HOUSE = 4, CENTER = 5, WALL = 6;
//     private static final int TOWN_SIZE = 3;
// 
//     private double spearmanPts, swordsmanPts, pikemanPts, sniperPts, colonialArcherPts,
//     musketeerPts, riflemanPts, archerPts, knightPts, lancerPts, horsemanPts, LAVPts,
//     tankPts, dragoonPts, mountedArcherPts, RPGPts, batteringRamPts,
//     cannonPts, mortarPts, catapultPts, machineGunPts, organGunPts;
//     private double trainPts;
// 
//     private int priority;
//     private int civiliansInTraining;
//     private int civiliansReq;
//     private boolean skipCivilians = false, skipBuildings = false, 
//     skipMilitary = true, skipResources = false;
//     private int refreshCivilians, refreshBuildings, refreshMilitary, refreshResources;
//     private int counter = 0;
// 
//     private int foodReq, woodReq, goldReq;
//     private int barracksReq, stablesReq, workshopsReq, housesReq, centersReq;
//     private int population;
//     private int barracks, stables, workshops, houses, centers, walls;
//     private int meleeInfantry, rangedInfantry, meleeArtillery, rangedArtillery;
// 
//     public TempAI(){
//         buildings = new LinkedList();
//         barracks = new LinkedList();
//         stables = new LinkedList();
//         workshops = new LinkedList();
//         houses = new LinkedList();
//         centers = new LinkedList();
// 
//         foodReq = 0;
//         woodReq = 0;
//         goldReq = 0;
// 
//         food = 400;
//         wood = 100;
//         gold = 0;
//         age = 1;
// 
//         civiliansReq = 5;
// 
//         barracksReq = 0;
//         stablesReq = 0;
//         workshopsReq = 0;
//         housesReq = 1;
//         centersReq = 0;
// 
//         barracks = 0;
//         stables = 0;
//         workshops = 0;
//         houses = 0;
//         centers = 1;
//         walls = 0;
// 
//         meleeInfantry = 0;
//         rangedInfantry = 0;
//         meleeInfantry = 0;
//         rangedInfantry = 0;
//         meleeArtillery = 0;
//         rangedArtillery = 0;
// 
//         trainPts = 50;
// 
//         refreshCivilians = 5;
//         refreshBuildings = 5;
//         refreshMilitary = 5;
// 
//         units = new LinkedList();
//         civilians = new LinkedList();
//         population = 0;
//         priority = CIVILIAN;
//     }
// 
//     public void act(){
//         counter++;
//         if (counter % refreshCivilians == 0)
//             manageCivilians();
//         if (counter % refreshBuildings == 0)
//             manageBuildings();
//         if (counter % refreshMilitary == 0)
//             manageMilitary();
//     }
// 
//     /**
//      * - Assign settlers to resources as required
//      * - Train settlers if settlers are required
//      */
//     private void manageCivilians(){
//         for (Unit unit : Game.getObjects(Unit.class)){
//             if (unit.getOwner() == this && unit instanceof Civilian){
//                 if ((Civilian)unit.getCurrentResource() == NOTHING)
//                     employCivilian((Civilian) unit);
//             }
//         }
//         if (civilians.size() < civiliansReq && priority == CIVILIAN){
//             trainCivilians();
//         } else {
//             priority = MILITARY;   //Only proceed to the military after the civilian needs are satisfied
//         }
//     }
// 
//     /**
//      * - Check buildings that are currently built and determine which are required
//      * - Build new buildings if there are enough resources and new buildings are required
//      */
//     private void manageBuildings(){
//         inventoryBuildings();
//         build();
//     }
// 
//     /**
//      * - Spawns new units if there are enough resources
//      * - Deploys units if there are enough
//      */
//     private void manageMilitary(){
//         if(priority == MILITARY){
//             //Default army
//             spawnArmy();
//             deployArmy();
//         }
//     }
// 
//     /**
//      * - Moves all idle civilians to resources required.
//      * 
//      * @param   civilian    The civilian to be assigned a resource
//      */
//     private void employCivilian(Civilian civilian){
//         if (foodReq > 0){
//             List berryBushes = civilian.getObjectsInRange(5, BerryBush.class);
//             if(berryBushes.size() > 0){
//                 civilian.selectCell((Cell)berryBushes.get(1));
//             }
//         }
//         if (woodReq > 0){
//             List trees = civilian.getObjectsInRange(5, Tree.class);
//             if(trees.size() > 0){
//                 civilian.selectCell((Cell)trees.get(1));
//             }
//         }
//         if (goldReq > 0){
//             List mines = civilian.getObjectsInRange(5, Mine.class);
//             if(mines.size() > 0){
//                 civilian.selectCell((Cell)mines.get(1));
//             }
//         }
//     }
// 
//     /**
//      * Trains civilians if there is enough food and enough town centers.
//      */
//     private void trainCivilians(){
//         for(Center center : Game.getObjects(Building.class)){
//             if(center.getOwner() == this && civiliansReq > 0){
//                 if (food > Civilian.FOOD_COST){
//                     center.spawnCivilian();
//                     civiliansReq = civiliansReq - 1 < 0 ? 0 : civiliansReq - 1;
//                 } else {
//                     foodReq += Civilian.FOOD_COST;
//                 }
//             }
//         }
//     }
// 
//     /**
//      * Updates building counts for buildings belonging to the AI.
//      */
//     private void inventoryBuildings(){
//         workshops = 0;
//         stables = 0;
//         centers = 0;
//         houses = 0;
//         barracks = 0;
// 
//         for (Building building : Game.getObjects(Building.class)){
//             if (building.getOwner() == this){
//                 if (building instanceof Workshop){
//                     workshops++;
//                 }else if (building instanceof Stable){
//                     stables++;
//                 }else if (building instanceof Center){
//                     centers++;
//                 }else if (building instanceof House){
//                     houses++;
//                 }else if (building instanceof Barracks){
//                     barracks++;
//                 }else if (building instanceof Wall){
//                     walls++;
//                 }
//             }
//         }
//     }
// 
//     /**
//      * Builds buildings as required, close to a town center so as to construct a town.
//      */
//     private void build(){
//         if(priority = BUILDING){
//             Center townCenter;
//             //Cell[][] = Map.getCells();
// 
//             //Find the center around which the buildings shall be built
//             for (Center center : Game.getObjects(Center.class)){
//                 if (center.getOwner() == this){
//                     townCenter = (Center) center;
//                     break;
//                 }
//             }
// 
//             //Build buildings as required
//             if (barracksReq > 0){
//                 if(place(BARRACKS))
//                     barracksReq -= 1;
//             } else if (stablesReq > 0){
//                 if(place(STABLE))
//                     stablesReq -= 1;
//             } else if (workshopsReq > 0){
//                 if(place(WORKSHOP))
//                     workshopsReq -= 1;
//             } else if (housesReq > 0){
//                 if(place(HOUSE))
//                     housesReq -= 1;
//             }else if (centersReq > 0){
//                 if(place(CENTER))
//                     centersReq -= 1;
//             }
//         }
//     }
// 
//     /**
//      * Places a building close to the present town center.
//      * 
//      * @param   building    The building that needs to be built
//      * @param   center      The town center that the buildings are to be built around
//      * @return  Whether the building was successfully built or not
//      */
//     private boolean place(int building, Center center){
//         Cell site;
//         boolean cannotBuild = true;
// 
//         //The enemy town develops in a square around the center, of this layout:
//         //  0 0 0 0 0
//         //  0 0 0 0 0
//         //  0 0 C 0 0
//         //  0 0 0 0 0
//         //  0 0 0 0 0
//         for (int xDistance = -4; xDistance <= TOWN_SIZE * 2; xDistance += 2){
//             for (int yDistance = -4; yDistance <= TOWN_SIZE * 2; yDistance += 2){
//                 if(Map.getCells()[center.getMapX() + xDistance()][center.getMapY() + yDistance()] == Grass.class){
//                     site = Map.getCells()[center.getMapX() + xDistance()][center.getMapY() + yDistance()];
//                     break;
//                 }
//             }
//         }
// 
//         if(site != null){
//             switch(building){
//                 case BARRACKS:
//                 if(wood >= Barracks.WOOD_COST && gold >= Barracks.GOLD_COST){
//                     ((Grass)site).buildBarracks(this);
//                     cannotBuild = false;
//                 }
//                 break;
//                 case STABLE:
//                 if(wood >= Stable.WOOD_COST && gold >= Stable.GOLD_COST){
//                     ((Grass)site).buildStable(this);
//                     cannotBuild = false;
//                 }
//                 break;
//                 case WORKSHOP:
//                 if(wood >= Workshop.WOOD_COST && gold >= Workshop.GOLD_COST){
//                     ((Grass)site).buildWorkshop(this);
//                     cannotBuild = false;
//                 }
//                 break;
//                 case HOUSE:
//                 if(wood >= House.WOOD_COST && gold >= House.GOLD_COST){
//                     ((Grass)site).buildHouse(this);
//                     cannotBuild = false;
//                 }
//                 break;
//                 case CENTER:
//                 if(wood >= Center.WOOD_COST && gold >= Center.GOLD_COST){
//                     ((Grass)site).buildCenter(this);
//                     cannotBuild = false;
//                 }
//                 break;
//             }
//         }
//     }
// 
//     private void spawnArmy(){
//         for (Unit unit : Game.getObjects(Unit.class)){
//             if (unit.getOwner != this){
//                 if (unit instanceof MeleeInfantry){
//                     meleeInfantry++;
//                 } else if (unit instanceof RangedInfantry){
//                     rangedInfantry++;
//                 } else if (unit instanceof MeleeCavalry){
//                     meleeCavalry++;
//                 } else if (unit instanceof RangedCavalry){
//                     rangedCavalry++;
//                 } else if (unit instanceof MeleeArtillery){
//                     meleeArtillery++;
//                 } else if (unit instanceof RangedArtillery){
//                     rangedArtillery++;
//                 }
//             }
//         }
// 
//         //Calculate the demand for each type of unit
//         switch(age){
//             case 1:
//             if (meleeInfantry > 0){
//                 archerPts += (double)meleeInfantry;
//                 swordsmanPts += (double)meleeInfantry;
//                 catapultPts += (double)meleeInfantry / 2.0;
//             }
//             if (rangedInfantry > 0){
//                 knightPts += (double)rangedInfantry;
//             }
//             if (meleeCavalry > 0){
//                 spearmanPts += (double)meleeCavalry;
//                 mountedArcherPts += (double)meleeCavalry / 2.0;
//             }
//             if (rangedCavalry > 0){
//                 spearmanPts += (double)rangedCavalry;
//                 catapultPts += (double)rangedCavalry / 2.0;
//             }
//             if (meleeArtillery > 0){
//                 knightPts += (double)meleeArtillery;
//             }
//             if (rangedArtillery > 0){
//                 knightPts += (double)rangedArtillery;
//             }
//             break;
//             case 2:
//             if (meleeInfantry > 0){
//                 colonialArcherPts += (double)meleeInfantry;
//                 musketeerPts += (double)meleeInfantry;
//                 cannonPts += (double)meleeInfantry / 2.0;
//             }
//             if (rangedInfantry > 0){
//                 colonialArcherPts += (double)rangedInfantry;
//                 horsemanPts += (double)rangedInfantry;
//                 cannonPts += (double)rangedInfantry / 2.0;
//             }
//             if (meleeCavalry > 0){
//                 pikemanPts += (double)meleeCavalry;
//                 musketeerPts += (double)meleeCavalry;
//                 lancerPts += (double)meleeCavalry / 2.0;
//                 dragoonPts += (double)meleeCavalry / 2.0;
//             }
//             if (rangedCavalry > 0){
//                 pikemanPts += (double)rangedCavalry;
//                 musketeerPts += (double)rangedCavalry / 2.0;
//             }
//             if (meleeArtillery > 0){
//                 horsemanPts += (double)meleeArtillery;
//             }
//             if (rangedArtillery > 0){
//                 horsemanPts += (double)rangedArtillery;
//             }
//             break;
//             case 3:
//             if (meleeInfantry > 0){
//                 sniperPts += (double)meleeInfantry;
//                 riflemanPts += (double)meleeInfantry;
//                 machineGunPts += (double)meleeInfantry;
//                 tankPts += (double)meleeInfantry / 2.0;
//             }
//             if (rangedInfantry > 0){
//                 sniperPts += (double)meleeInfantry;
//                 tankPts += (double)meleeInfantry / 2.0;
//             }
//             if (meleeCavalry > 0){
//                 LAVPts += (double)meleeCavalry;
//                 RPGPts += (double)meleeCavalry;
//             }
//             if (rangedCavalry > 0){
//                 RPGPts += (double)rangedCavalry;
//                 Pts += (double)rangedCavalry / 2.0;
//             }
//             if (meleeArtillery > 0){
//                 LAVPts += (double)meleeArtillery;
//             }
//             if (rangedArtillery > 0){
//                 LAVPts += (double)rangedArtillery;
//             }
//             break;
//         }
// 
//         for (Barracks barracks : Game.getObjects(Barracks.class)){
//             if (barracks.getOwner() == this){
//                 if (spearmanPts > trainPts){
//                     if(enoughResources(Spearman.FOOD_COST, Spearman.WOOD_COST, Spearman.GOLD_COST)){
//                         barracks.spawnSpearman();
//                         spearmanPts -= trainPts;
//                     }
//                 }
//                 if (swordsmanPts > trainPts){
//                     if(enoughResources(Swordsman.FOOD_COST, Swordsman.WOOD_COST, Swordsman.GOLD_COST)){
//                         barracks.spawnSwordsman();
//                         swordsmanPts -= trainPts;
//                     }
//                 }
//                 if (pikemanPts > trainPts){
//                     if(enoughResources(Pikeman.FOOD_COST, Pikeman.WOOD_COST, Pikeman.GOLD_COST)){
//                         barracks.spawnPikeman();
//                         pikemanPts -= trainPts;
//                     }
//                 }
//                 if (sniperPts > trainPts){
//                     if(enoughResources(Sniper.FOOD_COST, Sniper.WOOD_COST, Sniper.GOLD_COST)){
//                         barracks.spawnSniper();
//                         sniperPts -= trainPts;
//                     }
//                 }
//                 if (colonialArcherPts > trainPts){
//                     if(enoughResources(ColonialArcher.FOOD_COST, ColonialArcher.WOOD_COST, ColonialArcher.GOLD_COST)){
//                         barracks.spawnColonialArcher();
//                         colonialArcherPts -= trainPts;
//                     }
//                 }
//                 if (musketeerPts > trainPts){
//                     if(enoughResources(Musketeer.FOOD_COST, Musketeer.WOOD_COST, Musketeer.GOLD_COST)){
//                         barracks.spawnMusketeer();
//                         musketeerPts -= trainPts;
//                     }
//                 }
//                 if (riflemanPts > trainPts){
//                     if(enoughResources(Rifleman.FOOD_COST, Rifleman.WOOD_COST, Rifleman.GOLD_COST)){
//                         barracks.spawnRifleman();
//                         riflemanPts -= trainPts;
//                     }
//                 }
//                 if (archerPts > trainPts){
//                     if(enoughResources(Archer.FOOD_COST, Archer.WOOD_COST, Archer.GOLD_COST)){
//                         barracks.spawnArcher();
//                         archerPts -= trainPts;
//                     }
//                 }
//                 if (RPGPts > trainPts){
//                     if(enoughResources(RPG.FOOD_COST, RPG.WOOD_COST, RPG.GOLD_COST)){
//                         barracks.spawnRPG();
//                         RPGPts -= trainPts;
//                     }
//                 }
//                 if (machineGunPts > trainPts){
//                     if(enoughResources(MachineGun.FOOD_COST, MachineGun.WOOD_COST, MachineGun.GOLD_COST)){
//                         workshops.spawnMachineGun();
//                         machineGunPts -= trainPts;
//                     }
//                 }
//             }
//         }
// 
//         for (Stable stables : Game.getObjects(Stable.class)){
//             if (stables.getOwner() == this){
//                 if (knightPts > trainPts){
//                     if(enoughResources(Knight.FOOD_COST, Knight.WOOD_COST, Knight.GOLD_COST)){
//                         stables.spawnKnight();
//                         knightPts -= trainPts;
//                     }
//                 }
//                 if (lancerPts > trainPts){
//                     if(enoughResources(Lancer.FOOD_COST, Lancer.WOOD_COST, Lancer.GOLD_COST)){
//                         stables.spawnLancer();
//                         lancerPts -= trainPts;
//                     }
//                 }
//                 if (horsemanPts > trainPts){
//                     if(enoughResources(Horseman.FOOD_COST, Horseman.WOOD_COST, Horseman.GOLD_COST)){
//                         stables.spawnHorseman();
//                         horsemanPts -= trainPts;
//                     }
//                 }
//                 if (LAVPts > trainPts){
//                     if(enoughResources(LAV.FOOD_COST, LAV.WOOD_COST, LAV.GOLD_COST)){
//                         stables.spawnLAV();
//                         LAVPts -= trainPts;
//                     }
//                 }
//                 if (tankPts > trainPts){
//                     if(enoughResources(Tank.FOOD_COST, Tank.WOOD_COST, Tank.GOLD_COST)){
//                         stables.spawnTank();
//                         tankPts -= trainPts;
//                     }
//                 }
//                 if (dragoonPts > trainPts){
//                     if(enoughResources(Dragoon.FOOD_COST, Dragoon.WOOD_COST, Dragoon.GOLD_COST)){
//                         stables.spawnDragoon();
//                         dragoonPts -= trainPts;
//                     }
//                 }
//                 if (mountedArcherPts > trainPts){
//                     if(enoughResources(MountedArcher.FOOD_COST, MountedArcher.WOOD_COST, MountedArcher.GOLD_COST)){
//                         stables.spawnMountedArcher();
//                         mountedArcherPts -= trainPts;
//                     }
//                 }
//             }
//         }
// 
//         for (Workshop workshops : Game.getObjects(Workshop.class)){
//             if (workshops.getOwner() == this){
//                 if (batteringRamPts > trainPts){
//                     if(enoughResources(BatteringRam.FOOD_COST, BatteringRam.WOOD_COST, BatteringRam.GOLD_COST)){
//                         workshops.spawnBatteringRam();
//                         batteringRamPts -= trainPts;
//                     }
//                 }
//                 if (cannonPts > trainPts){
//                     if(enoughResources(Cannon.FOOD_COST, Cannon.WOOD_COST, Cannon.GOLD_COST)){
//                         workshops.spawnCannon();
//                         cannonPts -= trainPts;
//                     }
//                 }
//                 if (mortarPts > trainPts){
//                     if(enoughResources(Mortar.FOOD_COST, Mortar.WOOD_COST, Mortar.GOLD_COST)){
//                         workshops.spawnMortar();
//                         mortarPts -= trainPts;
//                     }
//                 }
//                 if (catapultPts > trainPts){
//                     if(enoughResources(Catapult.FOOD_COST, Catapult.WOOD_COST, Catapult.GOLD_COST)){
//                         workshops.spawnCatapult();
//                         catapultPts -= trainPts;
//                     }
//                 }
//                 if (organGunPts > trainPts){
//                     if(enoughResources(OrganGun.FOOD_COST, OrganGun.WOOD_COST, OrganGun.GOLD_COST)){
//                         workshops.spawnOrganGun();
//                     }
//                 }
//             }
//         }
//     }
// 
//     private void deployArmy(){
//         for (Unit unit : Game.getObjects(Unit.class)){
//             if(unit.getOwner()){
//             }
//         }
//     }
// 
//     private boolean enoughResources(int foodCost, int woodCost, int goldCost){
//         int enough = true;
// 
//         if (food < foodCost){
//             enough = false;
//             foodReq += foodCost;
//         }
//         if (wood < woodCost){
//             enough = false;
//             woodReq += woodCost;
//         }
//         if (gold < goldCost){
//             enough = false;
//             goldReq += goldReq;
//         }
//     }
// }
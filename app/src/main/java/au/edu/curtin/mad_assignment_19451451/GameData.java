package au.edu.curtin.mad_assignment_19451451;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import au.edu.curtin.mad_assignment_19451451.DbSchema.GameDataVariableTable;
import au.edu.curtin.mad_assignment_19451451.DbSchema.GameDataMapTable;

import java.util.Random;

/**
 * Represents the overall map, and contains a grid of MapElement objects (accessible using the
 * get(row, col) method). The two static constants WIDTH and HEIGHT indicate the size of the map.
 *
 * There is a static get() method to be used to obtain an instance (rather than calling the
 * constructor directly).
 *
 * There is also a regenerate() method. The map is randomly-generated, and this method will invoke
 * the algorithm again to replace all the map data with a new randomly-generated grid.
 */
public class GameData {
    // static values representing the height and width of the 2d array
    private static int WIDTH = 30;
    private static int HEIGHT = 10;

    // id of terrain elements
    private static final int WATER = R.drawable.ic_water;
    private static final int[] GRASS = {R.drawable.ic_grass1, R.drawable.ic_grass2,
            R.drawable.ic_grass3, R.drawable.ic_grass4};

    //random number generator and mapelement array
    private static final Random rng = new Random();
    private MapElement[][] grid;

    private static GameData instance = null;

    private static int id = 1;
    private static int idMultiplier = 100;

    //Database Class fields
    private SQLiteDatabase mapDb;
    private SQLiteDatabase varDb;

    private GameDataMapCursor mapCursor;
    private GameDataVariableCursor varCursor;

    //Money and Time variables
    private int money;
    private int time;

    // town variables
    private int population;
    private double income;

    private int nResidential;
    private int nCommercial;

    private double employmentRate;

    //Settings
    private Settings settings;

    /**
     * Singleton get function, builds a new GameData class if none exists
     * @return - returns instance of the GameData
     */
    public static GameData get() {
        if (instance == null) {
            instance = new GameData(generateGrid());
        }
        return instance;
    }

    /**
     * sets width of the map
     * @param width - map width
     */
    public static void setWidth(int width)
    {
        WIDTH = width;
    }

    /**
     * sets height of the map
     * @param height - map height
     */
    public static void setHeight(int height)
    {
        HEIGHT = height;
    }

    /**
     * Generates randomised map
     * Code is based on Prac 3 worksheet, minor changes have been made indicated by the presence of
     * comments
     * @return map element 2d array
     */
    private static MapElement[][] generateGrid() {
        final int HEIGHT_RANGE = 256;
        final int WATER_LEVEL = 112;
        final int INLAND_BIAS = 24;
        final int AREA_SIZE = 1;
        final int SMOOTHING_ITERATIONS = 2;

        int[][] heightField = new int[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                heightField[i][j] =
                        rng.nextInt(HEIGHT_RANGE)
                                + INLAND_BIAS * (
                                Math.min(Math.min(i, j), Math.min(HEIGHT - i - 1, WIDTH - j - 1)) -
                                        Math.min(HEIGHT, WIDTH) / 4);
            }
        }

        int[][] newHf = new int[HEIGHT][WIDTH];
        for (int s = 0; s < SMOOTHING_ITERATIONS; s++) {
            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    int areaSize = 0;
                    int heightSum = 0;

                    for (int areaI = Math.max(0, i - AREA_SIZE);
                         areaI < Math.min(HEIGHT, i + AREA_SIZE + 1);
                         areaI++) {
                        for (int areaJ = Math.max(0, j - AREA_SIZE);
                             areaJ < Math.min(WIDTH, j + AREA_SIZE + 1);
                             areaJ++) {
                            areaSize++;
                            heightSum += heightField[areaI][areaJ];
                        }
                    }

                    newHf[i][j] = heightSum / areaSize;
                }
            }

            int[][] tmpHf = heightField;
            heightField = newHf;
            newHf = tmpHf;
        }

        MapElement[][] grid = new MapElement[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                MapElement element;

                if (heightField[i][j] >= WATER_LEVEL) {
                    boolean waterN = (i == 0) || (heightField[i - 1][j] < WATER_LEVEL);
                    boolean waterE = (j == WIDTH - 1) || (heightField[i][j + 1] < WATER_LEVEL);
                    boolean waterS = (i == HEIGHT - 1) || (heightField[i + 1][j] < WATER_LEVEL);
                    boolean waterW = (j == 0) || (heightField[i][j - 1] < WATER_LEVEL);

                    boolean waterNW = (i == 0) || (j == 0) || (heightField[i - 1][j - 1] < WATER_LEVEL);
                    boolean waterNE = (i == 0) || (j == WIDTH - 1) || (heightField[i - 1][j + 1] < WATER_LEVEL);
                    boolean waterSW = (i == HEIGHT - 1) || (j == 0) || (heightField[i + 1][j - 1] < WATER_LEVEL);
                    boolean waterSE = (i == HEIGHT - 1) || (j == WIDTH - 1) || (heightField[i + 1][j + 1] < WATER_LEVEL);

                    boolean coast = waterN || waterE || waterS || waterW ||
                            waterNW || waterNE || waterSW || waterSE;

                    grid[i][j] = new MapElement(
                            !coast,
                            choose(waterN, waterW, waterNW,
                                    R.drawable.ic_coast_north, R.drawable.ic_coast_west,
                                    R.drawable.ic_coast_northwest, R.drawable.ic_coast_northwest_concave),
                            choose(waterN, waterE, waterNE,
                                    R.drawable.ic_coast_north, R.drawable.ic_coast_east,
                                    R.drawable.ic_coast_northeast, R.drawable.ic_coast_northeast_concave),
                            choose(waterS, waterW, waterSW,
                                    R.drawable.ic_coast_south, R.drawable.ic_coast_west,
                                    R.drawable.ic_coast_southwest, R.drawable.ic_coast_southwest_concave),
                            choose(waterS, waterE, waterSE,
                                    R.drawable.ic_coast_south, R.drawable.ic_coast_east,
                                    R.drawable.ic_coast_southeast, R.drawable.ic_coast_southeast_concave),
                            null, ( (i * idMultiplier)+ j)); // id determined by position in array
                            // determined by current height location multiplied against the idMultiplier that can be stored in that array index
                            // finally current width index is added onto to determine the id
                } else {
                    grid[i][j] = new MapElement(
                            false, WATER, WATER, WATER, WATER, null, (i * idMultiplier + j));
                            //ocean tiles id determined in the same manner
                }
            }
        }
        return grid;
    }

    /**
     * choose which structure to place int terrain element
     * Code taken from Prac 3, no changes have been made from code
     */
    private static int choose(boolean nsWater, boolean ewWater, boolean diagWater,
                              int nsCoastId, int ewCoastId, int convexCoastId, int concaveCoastId) {
        int id;
        if (nsWater) {
            if (ewWater) {
                id = convexCoastId;
            } else {
                id = nsCoastId;
            }
        } else {
            if (ewWater) {
                id = ewCoastId;
            } else if (diagWater) {
                id = concaveCoastId;
            } else {
                id = GRASS[rng.nextInt(GRASS.length)];
            }
        }
        return id;
    }

    /**
     * Sets map grid to new parameter grid passed to function
     * @param grid - 2d map element array
     */
    protected GameData(MapElement[][] grid) {
        this.grid = grid;
    }

    //resets all values of Game data to default, also regenerates map
    public void regenerate() {
        //resets all variables to default
        this.money = 0;
        this.time = 0;
        this.population = 0;
        this.income = 0;
        this.income = 0;
        this.nResidential = 0;
        this.nCommercial = 0;
        this.employmentRate = 0.0;
        this.grid = generateGrid();
    }

    /**
     * Get map element via row and height location
     * @param i - 2d array height - 1
     * @param j - 2d array width - 1
     * @return MapElement
     */
    public MapElement get(int i, int j) {
        return grid[i][j];
    }

    //ACCESSORS
    public int getMoney() {
        return money;
    }

    public int getTime() {
        return time;
    }

    public int getPopulation() {
        return population;
    }

    public double getEmploymentRate() {
        return employmentRate;
    }

    public double getIncome() {
        return income;
    }

    public Settings getSettings() {
        return settings;
    }

    public int getnResidential() {
        return nResidential;
    }

    public int getnCommercial() {
        return nCommercial;
    }

    public static int getId() {
        return id;
    }

    public static int getHEIGHT()
    {
        return HEIGHT;
    }

    public static int getWIDTH()
    {
        return WIDTH;
    }

    //checks adjacent map elements for a road, if true, return a boolean value to represent that
    public boolean isAdjacentToRoad(int ownerID)
    {
        boolean isAdjacent = false;

        //heightIndex originally determined by ownerID * 100
        //heightIndex determined from integer division of ownerID by id Multiplier
        int heightIndex = ownerID / idMultiplier;

        //width determined by the remaining added value that is not divisible
        int widthIndex = ownerID % idMultiplier;

        //check if within array index, and check if adjacent to road
        if(heightIndex > 0)
            if (grid[heightIndex-1][widthIndex].getStructure() instanceof Road)
                isAdjacent = true;
        if (heightIndex < HEIGHT-1)
            if (grid[heightIndex + 1][widthIndex].getStructure() instanceof Road)
                isAdjacent = true;
        if (widthIndex > 0)
            if(grid[heightIndex][widthIndex - 1].getStructure() instanceof Road )
                isAdjacent = true;
        if (widthIndex < WIDTH-1)
            if(grid[heightIndex][widthIndex + 1].getStructure() instanceof Road)
                isAdjacent = true;

        //return boolean value indicating if map element next to a road
        return isAdjacent;
    }

    //MUTATORS
    // changes value by increasing by imported population - no indication of use
    public void setPopulation(int population) {
        this.population += population;
    }

    // sets Setting's initial values onto gamedata, currently only affects money
    public void setSettings(Settings settings) {
        this.settings = settings;
        setMoney(settings.getInitialMoney());
    }

    //NOT USED
    private void setWIDTH(int width) {
        WIDTH = width;
    }

    //NOT USED
    private void setHEIGHT(int height) {
        HEIGHT = height;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    //calculate the newly generate income
    private void calcIncome()
    {
        this.income = Math.round((double)population * (employmentRate * (double)settings.getSalary() * settings.getTaxRate() -
                (double)settings.getServiceCost()));
    }

    //calc population according
    private int calcPopulation()
    {
        return (settings.getFamilySize() * nResidential);
    }

    private double calcEmploymentRate()
    {
        employmentRate = Math.min(1, (double) nCommercial * (double) settings.getShopSize()
                / (double) population);

        return employmentRate;
    }

    //NOT USED
    public void setnCommercial(int nCommercial)
    {
        this.nCommercial = nCommercial;
    }

    //NOT USED
    public void setnResidential(int nResidential) {
        this.nResidential = nResidential;
    }

    //increments the time passed by, constantly forces calculation of other variables
    public void run() {
        this.time++;
        calcPopulation();
        calcEmploymentRate();
        calcIncome();
        if(population > 0)
            setMoney(( money + (int)income));
    }

    //increase corresponding building if it is built
    public void countBuilding(Structure structure)
    {
        //if structure is residential, increase number of residential and calc population
        //other wise increase commercial and calc employment rate
        if(structure instanceof Residential)
        {
            nResidential++;
            this.population = calcPopulation();
        }
        else if(structure instanceof Commercial)
        {
            nCommercial++;
            calcEmploymentRate();
        }
    }

    //if object is deleted decrease amount of corresponding building
    public void removeBuilding(Structure structure)
    {
        //decrease value of corresponding structure type, recalculate corresponding value
        if(structure instanceof Residential)
        {
            nResidential--;
            this.population = calcPopulation();
        }
        else if(structure instanceof Commercial)
        {
            nCommercial--;
            calcEmploymentRate();
        }
    }

    //DATABASE FUNCTIONS]

    //Load Map and Variables
    public void load(Context context)
    {
        loadMap(context);
        loadVariables(context);
    }

    //private load methods
    private void loadMap(Context context)
    {
        //current height and width
        int height = 0, width = 0;

        //get map database and cursor
        this.mapDb = new GameDataMapTableDb(context.getApplicationContext()).getWritableDatabase();
        this.mapCursor = new GameDataMapCursor(mapDb.query(DbSchema.GameDataMapTable.NAME,
                null, null, null, null, null,
                null));

        try {
            mapCursor.moveToFirst();
            while (!mapCursor.isAfterLast())
            {
                //move to next height index and start from 0 width index
                if(width > WIDTH-1)
                {
                    width = 0;
                    height++;
                }
                this.grid[height][width] = mapCursor.getMapElement();

                width++;
                mapCursor.moveToNext();
            }
        }
        finally {
            mapCursor.close();
        }
    }

    //load variables
    private void loadVariables(Context context)
    {
        //get var database and cursor
        this.varDb = new GameDataVariableTableDb(context.getApplicationContext()).getWritableDatabase();
        this.varCursor = new GameDataVariableCursor(varDb.query(DbSchema.GameDataVariableTable.NAME,
                null, null, null, null, null,
                null));

        try {
            varCursor.moveToFirst();
            while (!varCursor.isAfterLast())
            {
                Number[] array = varCursor.getMapValues();

                //return number array from cursor and set map variables
                this.money = array[0].intValue();
                this.time = array[1].intValue();
                this.population = array[2].intValue();
                this.nResidential = array[3].intValue();
                this.nCommercial = array[4].intValue();
                this.employmentRate = array[5].doubleValue();
                this.income = array[6].doubleValue();

                varCursor.moveToNext();
            }
        }
        finally {
            varCursor.close();
        }
    }

    //adds new map variables
    public void add(GameData data)
    {
        if(varCursor.getCount() < 1) {
            ContentValues cv = new ContentValues();

            cv.put(GameDataVariableTable.Cols.ID, data.getId());
            cv.put(GameDataVariableTable.Cols.MONEY, data.getMoney());
            cv.put(GameDataVariableTable.Cols.TIME, data.getTime());
            cv.put(GameDataVariableTable.Cols.POPULATION, data.getPopulation());
            cv.put(GameDataVariableTable.Cols.NUM_RES, data.getnResidential());
            cv.put(GameDataVariableTable.Cols.NUM_COM, data.getnCommercial());
            cv.put(GameDataVariableTable.Cols.EMPLOYMENT_RATE, data.getEmploymentRate());

            varDb.insert(GameDataVariableTable.NAME, null, cv);
        }
    }

    //modifies old map variables
    public void edit(GameData data)
    {
        ContentValues cv = new ContentValues();

        cv.put(GameDataVariableTable.Cols.ID, data.getId());
        cv.put(GameDataVariableTable.Cols.MONEY, data.getMoney());
        cv.put(GameDataVariableTable.Cols.TIME, data.getTime());
        cv.put(GameDataVariableTable.Cols.POPULATION, data.getPopulation());
        cv.put(GameDataVariableTable.Cols.NUM_RES, data.getnResidential());
        cv.put(GameDataVariableTable.Cols.NUM_COM, data.getnCommercial());
        cv.put(GameDataVariableTable.Cols.EMPLOYMENT_RATE, data.getEmploymentRate());

        String[] whereValue = { String.valueOf(data.getId()) }; // make a default single id?
        varDb.update(GameDataVariableTable.NAME, cv,
                GameDataVariableTable.Cols.ID + " = ?", whereValue);
    }

    // deletes old map variables
    public void removeVariables()
    {
        if(varCursor.getCount() > 0)
        {
            String[] whereValue = { String.valueOf(getId()) };

            varDb.delete(GameDataVariableTable.NAME, GameDataVariableTable.Cols.ID + " = ?", whereValue);
        }
    }

    //saves every Map Element in 2d array
    public void save()
    {
        if(mapCursor.getCount() < 1) {
            for (int ii = 0; ii < HEIGHT; ii++) {
                for (int jj = 0; jj < WIDTH; jj++) {
                    add(this.grid[ii][jj]);
                }
            }
        }
    }

    private void add(MapElement element)
    {
        int buildable = 1;

        ContentValues cv = new ContentValues();

        //check boolean value and set to integer equivalent
        if(element.isBuildable())
            buildable = 1;
        else
            buildable = 0;

        cv.put(GameDataMapTable.Cols.ID, element.getOwnerID());
        cv.put(GameDataMapTable.Cols.OWNER_NAME, element.getOwnerName());
        cv.put(GameDataMapTable.Cols.BUILDABLE, buildable);
        cv.put(GameDataMapTable.Cols.TERRAIN_NW, element.getNorthWest());
        cv.put(GameDataMapTable.Cols.TERRAIN_NE, element.getNorthEast());
        cv.put(GameDataMapTable.Cols.TERRAIN_SW, element.getSouthWest());
        cv.put(GameDataMapTable.Cols.TERRAIN_SE, element.getSouthEast());

        Structure structure = element.getStructure();

        //check if structure is null
        if(structure == null) {
            cv.put(GameDataMapTable.Cols.STRUCTURE_TYPE, "");
            cv.put(GameDataMapTable.Cols.DRAWABLE_ID, 0);
            cv.put(GameDataMapTable.Cols.LABEL, "");
        }
        else
        {
            //if structure exists, check its object type and save its values
            if (structure instanceof Residential)
                cv.put(GameDataMapTable.Cols.STRUCTURE_TYPE, "residential");
            else if (structure instanceof Commercial)
                cv.put(GameDataMapTable.Cols.STRUCTURE_TYPE, "commercial");
            else if (structure instanceof Road)
                cv.put(GameDataMapTable.Cols.STRUCTURE_TYPE, "road");

            cv.put(GameDataMapTable.Cols.DRAWABLE_ID, structure.getDrawableId());
            cv.put(GameDataMapTable.Cols.LABEL, structure.getLabel());
        }

        //add to database
        mapDb.insert(GameDataMapTable.NAME, null, cv);
    }

    public void edit(MapElement element)
    {
        int buildable = 1;

        ContentValues cv = new ContentValues();

        //convert boolean to integer
        if(element.isBuildable() == true)
            buildable = 1;
        else
            buildable = 0;

        cv.put(GameDataMapTable.Cols.ID, element.getOwnerID());
        cv.put(GameDataMapTable.Cols.OWNER_NAME, element.getOwnerName());
        cv.put(GameDataMapTable.Cols.BUILDABLE, buildable);
        cv.put(GameDataMapTable.Cols.TERRAIN_NW, element.getNorthWest());
        cv.put(GameDataMapTable.Cols.TERRAIN_NE, element.getNorthEast());
        cv.put(GameDataMapTable.Cols.TERRAIN_SW, element.getSouthWest());
        cv.put(GameDataMapTable.Cols.TERRAIN_SE, element.getSouthEast());

        Structure structure = element.getStructure();

        //get structure values
        if(structure == null) { //if their is no structure
            cv.put(GameDataMapTable.Cols.STRUCTURE_TYPE, "");
            cv.put(GameDataMapTable.Cols.DRAWABLE_ID, 0);
            cv.put(GameDataMapTable.Cols.LABEL, "");
        }
        else
        {
            //check which type of structure, save as a key that can be used to rebuild to specific object type
            if (structure instanceof Residential)
                cv.put(GameDataMapTable.Cols.STRUCTURE_TYPE, "residential");
            else if (structure instanceof Commercial)
                cv.put(GameDataMapTable.Cols.STRUCTURE_TYPE, "commercial");
            else if (structure instanceof Road)
                cv.put(GameDataMapTable.Cols.STRUCTURE_TYPE, "road");

            cv.put(GameDataMapTable.Cols.DRAWABLE_ID, structure.getDrawableId());
            cv.put(GameDataMapTable.Cols.LABEL, structure.getLabel());
        }

        //use arrayid as key, owner id derived from arrayHeight * 100 with the arrayWidth being the remaining added
        String[] whereValue = { String.valueOf(element.getOwnerID()) };
        mapDb.update(GameDataMapTable.NAME, cv, GameDataMapTable.Cols.ID + " = ?",
                whereValue);

    }

    //drops all array elements from previous iteration of the map
    public void removeMap()
    {
        mapDb.delete(GameDataMapTable.NAME, null, null);
    }

    //deletes entire save game
    public void remove()
    {
        removeVariables();
        removeMap();
    }
}

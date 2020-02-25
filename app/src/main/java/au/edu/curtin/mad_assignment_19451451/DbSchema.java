package au.edu.curtin.mad_assignment_19451451;

/**
 * Database Schema
 * Contains a number of different tables and columns found within the database
 */
public class DbSchema
{
    /**
     * Stores the settings of the game
     * values stored are the @parameter - id of the saved settings, the
     * @parameter map width - when building the application
     * @parameter map height - when building the application
     * @parameter initial money - when loading a new saved game
     */
    public static class SettingsTable
    {
        public static final String NAME = "settings";
        public static class Cols
        {
            public static final String ID = "id";
            public static final String MAP_WIDTH= "map_width";
            public static final String MAP_HEIGHT = "map_height";
            public static final String INITIAL_MONEY = "initial_money";
        }
    }

    /**
     * Stores the map variables needed to run a game
     * @parameter variable id - id of the specific saved map game
     * @parameter money - currently acquired amount of money
     * @parameter time - current time span of the game
     * @parameter population - current population of the town
     * @parameter num res - number of residential buildings
     * @parameter num com - number of commercial buildings
     * @parameter employment rate - employment rate of the town, calculated as a double
     * @parameter income - current income of the town, stored as an integer
     */
    public static class GameDataVariableTable
    {
        public static final String NAME = "mapVariables";
        public static class Cols
        {
            public static final String ID = "variable_id"; // kept as constant value to search for

            public static final String MONEY = "money";
            public static final String TIME = "time";
            public static final String POPULATION = "population";

            public static final String NUM_RES = "number_residential";
            public static final String NUM_COM = "number_commercial";
            public static final String EMPLOYMENT_RATE = "employment_rate";
            public static final String INCOME = "income"; //calculated from other variables, frequently changed
        }
    }

    /**
     * Stores the map variables needed to run a game
     * @parameter id - id of the specific map element
     * @parameter owner name - owner of the building
     * @parameter buildable - boolean value which indicates if you can build on it
     * @parameter terrain NW - north west element id
     * @parameter terrain NE - north east element id
     * @parameter terrain SW - south west element id
     * @parameter terrain SE - south east element id
     * @parameter structure type - the type of building structure, stored as string e.g Commercial
     *                              Residential, Road
     * @parameter drawable id - id of the structure on the map element
     * @parameter label - the description of the structure
     */
    public static class GameDataMapTable
    {
        public static final String NAME = "mapElement";
        public static class Cols
        {
            public static final String ID = "element_id";
            public static final String OWNER_NAME = "owner_name";
            public static final String BUILDABLE = "buildable";
            public static final String TERRAIN_NW = "north_west";
            public static final String TERRAIN_NE = "north_east";
            public static final String TERRAIN_SW = "south_west";
            public static final String TERRAIN_SE = "south_east";
            public static final String STRUCTURE_TYPE = "structure_type";
            public static final String DRAWABLE_ID = "drawable_id";
            public static final String LABEL = "label";

        }
    }
}

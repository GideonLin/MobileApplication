package au.edu.curtin.mad_assignment_19451451;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import au.edu.curtin.mad_assignment_19451451.DbSchema.SettingsTable;

/**
 * Represents the settings of the saved games and values when creating a new game
 * Values stored consist of Map Constraints and gameplay variables
 */
public class Settings
{
    //MAP GAME VARIABLE ELEMENTS
    private int mapWidth = 50;
    private int mapHeight = 10;
    private int initialMoney = 1000;
    private int familySize = 4;
    private int shopSize = 6;
    private int salary = 10;
    private double taxRate = 0.3;
    private int serviceCost = 2;
    private int houseBuildingCost = 100;
    private int commBuildingCost = 500;
    private int roadBuildingCost = 20;

    //singleton instance of settings
    private static Settings instance = null;
    private final int id = 1; // table key, may change (multiple save games?)

    //database
    private SQLiteDatabase database;
    private SettingsCursor cursor;

    private Settings()
    {
    }

    /**
     * Returns instance of settings, static
     * @return Settings - one and only settings object
     */
    public static Settings get()
    {
        if (instance == null)
        {
            instance = new Settings();
        }
        return instance;
    }

    //MUTATORS
    public void setMapWidth(int mapWidth)
    {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(int mapHeight)
    {
        this.mapHeight = mapHeight;
    }

    public void setInitialMoney(int initialMoney)
    {
        this.initialMoney = initialMoney;
    }

    //ACCESSORS
    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getInitialMoney() {
        return initialMoney;
    }

    public int getFamilySize() {
        return familySize;
    }

    public int getShopSize() {
        return shopSize;
    }

    public int getSalary() {
        return salary;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public int getServiceCost() {
        return serviceCost;
    }

    public int getHouseBuildingCost() {
        return houseBuildingCost;
    }

    public int getCommBuildingCost() {
        return commBuildingCost;
    }

    public int getRoadBuildingCost() {
        return roadBuildingCost;
    }

    public int getId()
    {
        return id;
    }

    //DATABASE FUNCTIONS
    public void load(Context context)
    {
        //initialise database and cursor
        this.database = new SettingsDb(context.getApplicationContext()).getWritableDatabase();
        cursor = new SettingsCursor(database.query(SettingsTable.NAME, null, null,
                null, null, null, null));

        try
        {
            cursor.moveToFirst();
            //get settings object from database
            while (!cursor.isAfterLast())
            {
                instance = cursor.getSettings();
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
    }

    /**
     * Add newSettings object into the currently existing database
     * @param newSettings - new set of Settings
     */
    public void add(Settings newSettings)
    {
        //only add if there are no previous saves of Settings, only 1 saved game
        if(cursor.getCount() < 1) {
            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.Cols.ID, newSettings.getId());
            cv.put(DbSchema.SettingsTable.Cols.MAP_WIDTH, newSettings.getMapWidth());
            cv.put(DbSchema.SettingsTable.Cols.MAP_HEIGHT, newSettings.getMapHeight());
            cv.put(DbSchema.SettingsTable.Cols.INITIAL_MONEY, newSettings.getInitialMoney());
            database.insert(DbSchema.SettingsTable.NAME, null, cv);
        }
    }

    /**
     * passed current settings and edit current values of Settings saved in the database
     * @param newSettings - modified Settings object
     */
    public void edit(Settings newSettings)
    {
        ContentValues cv = new ContentValues();
        cv.put(SettingsTable.Cols.ID, newSettings.getId());
        cv.put(SettingsTable.Cols.MAP_WIDTH, newSettings.getMapWidth());
        cv.put(SettingsTable.Cols.MAP_HEIGHT, newSettings.getMapHeight());
        cv.put(SettingsTable.Cols.INITIAL_MONEY, newSettings.getInitialMoney());

        String whereValue[] = { String.valueOf(newSettings.getId()) };
        database.update(SettingsTable.NAME, cv, SettingsTable.Cols.ID + " = ?",
                whereValue);
    }

    //delete settings and return to default
    public void remove(Settings settings) {
        if (cursor.getCount() > 0) {
            String[] whereValue = {String.valueOf(settings.getId())};
            database.delete(SettingsTable.NAME, SettingsTable.Cols.ID + " = ?", whereValue);
        }
    }
}

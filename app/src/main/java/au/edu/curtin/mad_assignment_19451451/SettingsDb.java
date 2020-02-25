package au.edu.curtin.mad_assignment_19451451;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import au.edu.curtin.mad_assignment_19451451.DbSchema.SettingsTable;

/**
 * Database of Settings, stores value listed below
 * MAP_WIDTH - width of the map
 * MAP_HEIGHT - height of the map
 * INITIAL_MONEY - initial money game starts with
 */
public class SettingsDb extends SQLiteOpenHelper
{
    //current version of database
    private static final int VERSION = 1;
    //name of database
    private static final String DATABASE_NAME = "settings.db";

    public SettingsDb(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + SettingsTable.NAME + "("
            + SettingsTable.Cols.ID + " INTEGER, "
            + SettingsTable.Cols.MAP_WIDTH + " INTEGER, "
            + SettingsTable.Cols.MAP_HEIGHT + " INTEGER, "
            + SettingsTable.Cols.INITIAL_MONEY + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

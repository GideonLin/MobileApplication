package au.edu.curtin.mad_assignment_19451451;

import android.database.Cursor;
import android.database.CursorWrapper;
import au.edu.curtin.mad_assignment_19451451.DbSchema.SettingsTable;

public class SettingsCursor extends CursorWrapper
{
    //CONSTRUCTOR
    public SettingsCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Return settings object saved in local database
     * @return Settings - saved settings of saved game
     */
    public Settings getSettings()
    {
        Settings settings = Settings.get();

        // get map constraints and initial money of new game
        int mapWidth = getInt(getColumnIndex(SettingsTable.Cols.MAP_WIDTH));
        int mapHeight = getInt(getColumnIndex(SettingsTable.Cols.MAP_HEIGHT));
        int initialMoney = getInt(getColumnIndex(SettingsTable.Cols.INITIAL_MONEY));

        //set current singleton settings value to saved settings
        settings.setMapWidth(mapWidth);
        settings.setMapHeight(mapHeight);
        settings.setInitialMoney(initialMoney);

        return settings;
    }
}

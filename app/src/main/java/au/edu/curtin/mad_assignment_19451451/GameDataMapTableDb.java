package au.edu.curtin.mad_assignment_19451451;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import au.edu.curtin.mad_assignment_19451451.DbSchema.GameDataMapTable;

public class GameDataMapTableDb extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "mapElement";

    public GameDataMapTableDb(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Build database
     * @param sqLiteDatabase - empty database, assumed
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + GameDataMapTable.NAME + "("
        + GameDataMapTable.Cols.ID + " INTEGER, "
        + GameDataMapTable.Cols.OWNER_NAME + " TEXT, "
        + GameDataMapTable.Cols.BUILDABLE + " INTEGER, "
        + GameDataMapTable.Cols.TERRAIN_NW + " INTEGER, "
        + GameDataMapTable.Cols.TERRAIN_NE + " INTEGER, "
        + GameDataMapTable.Cols.TERRAIN_SW + " INTEGER, "
        + GameDataMapTable.Cols.TERRAIN_SE + " INTEGER, "
        + GameDataMapTable.Cols.STRUCTURE_TYPE + " TEXT, "
        + GameDataMapTable.Cols.DRAWABLE_ID + " INTEGER, "
        + GameDataMapTable.Cols.LABEL + " TEXT)");
    }

    // Do nothing when upgrading
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

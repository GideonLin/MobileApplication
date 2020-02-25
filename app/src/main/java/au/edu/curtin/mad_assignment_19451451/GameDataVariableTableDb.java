package au.edu.curtin.mad_assignment_19451451;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import au.edu.curtin.mad_assignment_19451451.DbSchema.GameDataVariableTable;

public class GameDataVariableTableDb extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "mapVariables";

    public GameDataVariableTableDb(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Build variable table database
     * @param sqLiteDatabase - assumed empty database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + GameDataVariableTable.NAME + "("
                + GameDataVariableTable.Cols.ID + " INTEGER, "
                + GameDataVariableTable.Cols.MONEY + " INTEGER, "
                + GameDataVariableTable.Cols.TIME + " INTEGER, "
                + GameDataVariableTable.Cols.POPULATION + " INTEGER, "
                + GameDataVariableTable.Cols.NUM_RES + " INTEGER, "
                + GameDataVariableTable.Cols.NUM_COM + " INTEGER, "
                + GameDataVariableTable.Cols.EMPLOYMENT_RATE + " Real, "
                + GameDataVariableTable.Cols.INCOME + " REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

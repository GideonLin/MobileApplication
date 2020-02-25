package au.edu.curtin.mad_assignment_19451451;

import android.database.Cursor;
import android.database.CursorWrapper;
import au.edu.curtin.mad_assignment_19451451.DbSchema.GameDataVariableTable;

public class GameDataVariableCursor extends CursorWrapper
{
    public GameDataVariableCursor(Cursor cursor)
    {
        super(cursor);
    }

    /**
     * Get all values of the game
     * @return array of numbers containing game data variables
     */
    public Number[] getMapValues()
    {
        Number[] array = new Number[7];

        // get game data variables
        array[0] = getInt(getColumnIndex(GameDataVariableTable.Cols.MONEY));
        array[1] = getInt(getColumnIndex(GameDataVariableTable.Cols.TIME));
        array[2] = getInt(getColumnIndex(GameDataVariableTable.Cols.POPULATION));
        array[3] = getInt(getColumnIndex(GameDataVariableTable.Cols.NUM_RES));
        array[4] = getInt(getColumnIndex(GameDataVariableTable.Cols.NUM_COM));
        array[5] = getDouble(getColumnIndex(GameDataVariableTable.Cols.EMPLOYMENT_RATE));
        array[6] = getDouble(getColumnIndex(GameDataVariableTable.Cols.INCOME));

        return array;
    }
}

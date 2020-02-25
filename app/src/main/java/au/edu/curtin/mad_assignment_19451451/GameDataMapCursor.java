package au.edu.curtin.mad_assignment_19451451;

import android.database.Cursor;
import android.database.CursorWrapper;
import au.edu.curtin.mad_assignment_19451451.DbSchema.GameDataMapTable;

public class GameDataMapCursor extends CursorWrapper
{
    public GameDataMapCursor(Cursor cursor)
    {
        super(cursor);
    }

    public MapElement getMapElement()
    {
        MapElement element = null;
        Structure structure = null;

        //get all Mapelement values
        int ownerId = getInt(getColumnIndex(GameDataMapTable.Cols.ID));
        String ownerName = getString(getColumnIndex(GameDataMapTable.Cols.OWNER_NAME));
        boolean buildable = (1 == getInt(getColumnIndex(GameDataMapTable.Cols.BUILDABLE)));
        int northWest = getInt(getColumnIndex(GameDataMapTable.Cols.TERRAIN_NW));
        int northEast = getInt(getColumnIndex(GameDataMapTable.Cols.TERRAIN_NE));
        int southWest = getInt(getColumnIndex(GameDataMapTable.Cols.TERRAIN_SW));
        int southEast = getInt(getColumnIndex(GameDataMapTable.Cols.TERRAIN_SE));

        //get structure values
        String structureType = getString(getColumnIndex(GameDataMapTable.Cols.STRUCTURE_TYPE));
        int drawableId = getInt(getColumnIndex(GameDataMapTable.Cols.DRAWABLE_ID));
        String label = getString(getColumnIndex(GameDataMapTable.Cols.LABEL));

        //check structure type and build structure, otherwise leave it null
        if(drawableId != 0 && !label.equals(""))
        {
            if(structureType.equals("commercial"))
            {
                structure = new Commercial(drawableId, label);
            }
            else if (structureType.equals("residential"))
            {
                structure = new Residential(drawableId, label);
            }
            else if(structureType.equals("road"))
            {
                structure = new Road(drawableId, label);
            }
        }

        //build map element
        element = new MapElement(buildable, northWest, northEast, southWest, southEast, structure,
                ownerId);
        //set the owner of the structure
        element.setOwnerName(ownerName);
        return element;
    }
}

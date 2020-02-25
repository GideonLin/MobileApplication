package au.edu.curtin.mad_assignment_19451451;

import android.graphics.Bitmap;

/**
 * Represents a single grid square in the map. Each map element has both terrain and an optional
 * structure.
 *
 * The terrain comes in four pieces, as if each grid square was further divided into its own tiny
 * 2x2 grid (north-west, north-east, south-west and south-east). Each piece of the terrain is
 * represented as an int, which is actually a drawable reference. That is, if you have both an
 * ImageView and a MapElement, you can do this:
 *
 * ImageView iv = ...;
 * MapElement me = ...;
 * iv.setImageResource(me.getNorthWest());
 *
 * This will cause the ImageView to display the grid square's north-western terrain image,
 * whatever it is.
 *
 * (The terrain is broken up like this because there are a lot of possible combinations of terrain
 * images for each grid square. If we had a single terrain image for each square, we'd need to
 * manually combine all the possible combinations of images, and we'd get a small explosion of
 * image files.)
 *
 * Meanwhile, the structure is something we want to display over the top of the terrain. Each
 * MapElement has either zero or one Structure} objects. For each grid square, we can also change
 * which structure is built on it.
 */

/**
 * Code taken from pre made code provided by Practical 3 worksheet
 * changes made have been noted by comments
 */
public class MapElement
{
    private final boolean buildable;
    private final int terrainNorthWest;
    private final int terrainSouthWest;
    private final int terrainNorthEast;
    private final int terrainSouthEast;
    private Structure structure;

    //possible name of the owner, is not initially known
    private String ownerName;

    //id of map element, used in database as a unique key calculated by (MAP_HEIGHT*100) + (MAP_WIDTH)
    private int ownerID;

    //Bitmap image taken, is not initially set, as image must be taken by user
    private Bitmap image;

    public MapElement(boolean buildable, int northWest, int northEast,
                      int southWest, int southEast, Structure structure,
                      int ownerID)
    {
        this.buildable = buildable;
        this.terrainNorthWest = northWest;
        this.terrainNorthEast = northEast;
        this.terrainSouthWest = southWest;
        this.terrainSouthEast = southEast;
        this.structure = structure;
        this.ownerID = ownerID;
    }

    public boolean isBuildable()
    {
        return buildable;
    }

    public int getNorthWest()
    {
        return terrainNorthWest;
    }

    public int getSouthWest()
    {
        return terrainSouthWest;
    }

    public int getNorthEast()
    {
        return terrainNorthEast;
    }

    public int getSouthEast()
    {
        return terrainSouthEast;
    }

    public int getOwnerID()
    {
        return ownerID;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    /**
     * Sets the image of the structure built on the map element
     * @param image - new image of the map element
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * Retrieves the structure built on this map element.
     * @return The structure, or null if one is not present.
     */
    public Structure getStructure()
    {
        return structure;
    }

    public void setStructure(Structure structure)
    {
        this.structure = structure;
    }

    /**
     * Sets the name of the owner of this map element
     * @param name - new name of the owner
     */
    public void setOwnerName(String name)
    {
        this.ownerName = name;
    }

    /**
     * Retrieves the image that the map element possesses
     * @return Bitmap images, null if not present
     */
    public Bitmap getImage() {
        return image;
    }
}

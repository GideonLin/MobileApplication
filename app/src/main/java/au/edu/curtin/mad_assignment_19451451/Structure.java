package au.edu.curtin.mad_assignment_19451451;

/**
 * Represents a possible structure to be placed on the map. A structure simply contains a drawable
 * int reference, and a string label to be shown in the selector.
 */

/**
 * Code Acquired from Practical 3 Worksheet, Modifications will be found wherever comments have been indicated
 */
public abstract class Structure
{
    private final int drawableId;
    private String label;

    public Structure(int drawableId, String label)
    {
        this.drawableId = drawableId;
        this.label = label;
    }

    public int getDrawableId()
    {
        return drawableId;
    }

    public String getLabel()
    {
        return label;
    }
}

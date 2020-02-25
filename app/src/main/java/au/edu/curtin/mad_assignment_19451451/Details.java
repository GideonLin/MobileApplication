package au.edu.curtin.mad_assignment_19451451;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Details - separate activity that displays the values found within the specific map element
 */
public class Details extends AppCompatActivity {

    /**
     * static string labels to acquire from intents
     */
    private static final String ROW = "row";
    private static final String COLUMN = "column";
    private static final String STRUCTURE = "structure";
    private static final String NAME = "name";

    /**
     * values to obtain images taken, request code and static string value to get thumbnail
     */
    private static final int REQUEST_THUMBNAIL = 1;
    private static final String THUMBNAIL = "thumbnail";

    /**
     * UI Elements
     */
    private TextView rowValue;
    private TextView columnValue;
    private TextView structureValue;
    private EditText editName;
    private Button change;
    private Button photoBtn;
    private Intent returnData;

    /**
     * Bitmap element obtained from taking picture
     */
    private Bitmap bitmap;

    /***
     * onCreate - inflates the view, and acquires intent passed to activity
     * if any of changes are made within element return intent containing all possibly changed data
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // acquiring intent
        final Intent intent = getIntent();
        returnData = new Intent();

        //find UI elements
        rowValue = findViewById(R.id.currVertical);
        columnValue = findViewById(R.id.currHorizontal);
        structureValue = findViewById(R.id.currStructure);
        editName = findViewById(R.id.currName);
        change = findViewById(R.id.modifyElement);
        photoBtn = findViewById(R.id.takePhoto);

        //set values to current map element values, if no row or column value set to 0
        rowValue.setText(Integer.toString(intent.getIntExtra(ROW, 0)));
        columnValue.setText(Integer.toString(intent.getIntExtra(COLUMN, 0)));
        structureValue.setText(intent.getStringExtra(STRUCTURE));
        editName.setText(intent.getStringExtra(NAME));

        //when change is clicked, pass all possibly changed values to intent
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = editName.getText().toString();
                returnData.putExtra(ROW, Integer.parseInt(rowValue.getText().toString()));
                returnData.putExtra(COLUMN, Integer.parseInt(columnValue.getText().toString()));
                returnData.putExtra(NAME, newName);
                returnData.putExtra(THUMBNAIL, bitmap);
                //set result as being OK
                setResult(RESULT_OK, returnData);
            }
        });

        //photo button to acquire image
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //start mobile phone application and get returned image
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoIntent, REQUEST_THUMBNAIL);
            }
        });
    }

    /**
     * getIntent, passes details of the mapElement, puts values into intent and return to calling
     * function
     * @param context - calling activity
     * @param row - location along the row of 2d array
     * @param column - location along the column of 2d array
     * @param structure - type of structure that fills map element
     * @param name - name of the house owner
     * @return - returns intent filled with above values
     */
    public static Intent getIntent(Context context, int row, int column, String structure,
                                   String name)
    {
        Intent intent = new Intent(context, Details.class);

        intent.putExtra(ROW, row);
        intent.putExtra(COLUMN, column);
        intent.putExtra(STRUCTURE, structure);
        intent.putExtra(NAME, name);

        return intent;
    }

    /**
     * On bitmap image being taken, acquire image and set private class field bitmap element
     * @param requestCode - request code to start activity
     * @param resultCode - result code to define success
     * @param data - string value to search for bitmap image taken
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_THUMBNAIL)
        {
            bitmap = (Bitmap)data.getExtras().get("data");
        }
    }

    /**
     * accepts intent, and acquires bitmap image from the intent
     * @param returnData - Intent containing possible bitmap image
     * @return - returns bitmap image
     */
    public static Bitmap getBitmap(Intent returnData)
    {
        return (Bitmap) returnData.getExtras().get(THUMBNAIL);
    }

    /**
     * accepts intent and returns map element row location in the 2d array
     * @param returnData - intent containing row location
     * @return - returns map element row location
     */
    public static int getRow(Intent returnData)
    {
        return returnData.getIntExtra(ROW, 0);
    }

    /**
     * accepts intent and returns map element column location in the 2d array
     * @param returnData - intent containing column location
     * @return - returns map element column location
     */
    public static int getColumn(Intent returnData)
    {
        return returnData.getIntExtra(COLUMN, 0);
    }

    /**
     * accepts intent and returns map element owner name
     * @param returnData - intent containing owner's name
     * @return - returns map element owner's name
     */
    public static String getName(Intent returnData)
    {
        return returnData.getStringExtra(NAME);
    }
}

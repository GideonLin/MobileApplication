package au.edu.curtin.mad_assignment_19451451;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity
{
    //static string keys to find values from intents
    private static final String MAPW = "map_width";
    private static final String MAPH = "map_height";
    private static final String MONEY = "money";

    //get settings singleton
    private Settings settings = Settings.get();

    //UI ELEMENTS
    private EditText mapWidth;
    private EditText mapHeight;
    private EditText initialMoney;

    private Button newGameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //find UI ELEMENTS
        mapWidth = findViewById(R.id.mapWidthID);
        mapHeight = findViewById(R.id.mapHeightID);
        initialMoney = findViewById(R.id.moneyID);

        newGameBtn = findViewById(R.id.newGameBtn);

        //find and display current map constraints and gameplay values
        mapWidth.setText(Integer.toString(settings.getMapWidth()));
        mapHeight.setText(Integer.toString(settings.getMapHeight()));
        initialMoney.setText(Integer.toString(settings.getInitialMoney()));

        //change values
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get modified constraints of map and money when a new game is started
                int value = Integer.parseInt(mapWidth.getText().toString());
                settings.setMapWidth(value);
                value = Integer.parseInt(mapHeight.getText().toString());
                settings.setMapHeight(value);
                value = Integer.parseInt(initialMoney.getText().toString());

                //change settings values
                settings.setInitialMoney(value);
                settings.edit(Settings.get());

                //modify static width and hieght
                GameData.setWidth(settings.getMapWidth());
                GameData.setHeight(settings.getMapHeight());

                final GameData map = GameData.get();
                map.setMoney(settings.getInitialMoney());
                map.remove(); // removes all old information from database

                //loading map values, and change map constraints and gameplay values
                map.regenerate();
                map.setSettings(settings);
                map.load(getApplication());
                map.add(map);
                map.save();
            }
        });
    }
}

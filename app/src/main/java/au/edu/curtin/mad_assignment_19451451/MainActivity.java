package au.edu.curtin.mad_assignment_19451451;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    private static final String MAPW = "map_width";
    private static final String MAPH = "map_height";
    private static final String MONEY = "money";

    //MAP ELEMENTS
    private Button playBtn;
    private Button settingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = findViewById(R.id.playID);
        settingsBtn = findViewById(R.id.settingsID);

        //get instance of settings, load settings database if already exists
        final Settings settings = Settings.get();
        settings.load(getApplication());
        settings.add(settings);

        // set game data width height and settings, get instance of game data
        GameData.setWidth(settings.getMapWidth());
        GameData.setHeight(settings.getMapHeight());
        GameData init = GameData.get();
        init.setSettings(settings);

        //load gamedata from database if already existing
        init.load(getApplication());
        init.add(init);
        init.save();

        //change to MapScreen Activity
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, MapScreen.class));
            }
        });

        //change to Settings activity
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }
}

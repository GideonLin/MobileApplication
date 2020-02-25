package au.edu.curtin.mad_assignment_19451451;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MapScreen extends AppCompatActivity
{
    //UI ELEMENTS
    private Button demoBtn;
    private Button buildBtn;
    private Button detBtn;

    //Fragments
    private Map fragA;
    private Selector fragB;

    /**
     * Inflates and creates new view, consists of UI elements, and fragments listed above
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screen);

        //find map elements
        demoBtn = findViewById(R.id.demolition);
        buildBtn = findViewById(R.id.build);
        detBtn = findViewById(R.id.detail);

        //get fragment manager and map
        FragmentManager fm = getSupportFragmentManager();
        fragA = (Map) fm.findFragmentById(R.id.mapF);

        //check if frag is not empty, if empty, register fragA as a map
        if (fragA == null)
        {
            fragA = new Map();
            fm.beginTransaction().add(R.id.mapF, fragA).commit();
        }

        //check if frag for selector is not empty, if true, register fragB as a selector
        fragB = (Selector) fm.findFragmentById(R.id.selectF);
        if (fragB == null)
        {
            fragB = new Selector();
            fm.beginTransaction().add(R.id.selectF, fragB).commit();
        }
        //set fragA selector's fragment as fragB
        fragA.setSelectorF(fragB);

        //set current actions of user when selecting one of the three buttons below
        demoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragA.setBuild(false);
                fragA.setDestroy(true);
                fragA.setDetails(false);
            }
        });

        buildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragA.setBuild(true);
                fragA.setDestroy(false);
                fragA.setDetails(false);
            }
        });

        detBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragA.setBuild(false);
                fragA.setDestroy(false);
                fragA.setDetails(true);
            }
        });
    }
}

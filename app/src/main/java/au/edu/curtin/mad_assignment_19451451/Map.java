package au.edu.curtin.mad_assignment_19451451;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Map extends Fragment
{
    //REQUEST CODE DETAILS
    private static final int REQUEST_CODE_DETAILS = 0;

    //private instance of map
    private GameData currMap;
    private MapAdapter mapAdapter;

    //recycler view
    private RecyclerView rvM;
    private Selector selectorF;

    //boolean values to indicate building, destroying or seeing details
    private boolean build;
    private boolean destroy;
    private boolean details;

    //UI ELEMENTS
    private TextView time;
    private TextView money;
    private TextView income;
    private TextView currPop;
    private TextView currEmp;

    private Button runBtn;

    //TIMER to simulate application running
    private Timer runTime;
    private TimerTask task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container,false);

        time = view.findViewById(R.id.currTime);
        money = view.findViewById(R.id.currMoney);
        income = view.findViewById(R.id.currIncome);
        currPop = view.findViewById(R.id.currPopulation);
        currEmp = view.findViewById(R.id.currEmployment);

        runBtn = view.findViewById(R.id.run);

        //button increments time passed
        runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //working function however does not update the textboxes, may need to be moved to map
                currMap.run();
                currMap.edit(currMap);
                update();
            }
        });

        //inflate map recyclerview
        rvM = (RecyclerView) view.findViewById(R.id.mapRecyclerView);

        //set to grid layout manager
        rvM.setLayoutManager(new GridLayoutManager(
                getActivity(),
                GameData.getHEIGHT(),
                GridLayoutManager.HORIZONTAL,
                false
        ));

        //if money is found below $0 - send toast to indicate losing the game
        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            //only check if value is in the negatives
            @Override
            public void afterTextChanged(Editable editable)
            {
                if(currMap.getMoney() < 0)
                {
                    Toast toast = Toast.makeText(getContext(), "You're Bankrupt!\n You Lose!",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        //create map adapter
        mapAdapter = new MapAdapter();

        //get instance of map
        currMap = GameData.get();
        //set initial action to build
        setBuild(true); // true by default

        rvM.setAdapter(mapAdapter);

        //Initialise timer function
        runTime = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                currMap.run();
                //save map variables
                currMap.edit(currMap);
                //update UI elements
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
            }
        };
        //schedule to run every 1000ms after 1000ms
        runTime.schedule(task, 1000, 1000);

        return view;
    }

    /**
     * update UI elements to current map values
     */
    public void update()
    {
        time.setText("Time: " + Integer.toString(currMap.getTime()));
        money.setText("Money: " + Integer.toString(currMap.getMoney()));
        income.setText("Income: " + Double.toString(currMap.getIncome()));
        currPop.setText("Population: " + Integer.toString(currMap.getPopulation()));
        currEmp.setText("Employment: " + Double.toString(currMap.getEmploymentRate()));
    }

    /**
     * Set selector to imported selector fragment
     * @param selectorF
     */
    public void setSelectorF(Selector selectorF) {
        this.selectorF = selectorF;
    }

    /**
     * Set action to either build, destroy or seeing details
     */
    public void setBuild(boolean build) // build - represented by true, destroy - false
    {
        this.build = build;
    }

    public void setDestroy(boolean destroy)
    {
        this.destroy = destroy;
    }

    public void setDetails(boolean details)
    {
        this.details = details;
    }

    /**
     * On activity results update mapelement and notify adapter of change to data
     * @param requestCode - request code to details screen
     * @param resultCode - result code indicating success
     * @param data - intent filled with mapelement data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == REQUEST_CODE_DETAILS)
        {
            MapElement element = currMap.get(Details.getRow(data), Details.getColumn(data));
            element.setOwnerName(Details.getName(data));
            element.setImage(Details.getBitmap(data));
            mapAdapter.notifyDataSetChanged();
        }
    }

    /**
     * On pause, stop timer function
     */
    @Override
    public void onPause()
    {
        super.onPause();
        runTime.cancel();
    }

    public class MapDataViewHolder extends RecyclerView.ViewHolder
    {
        //UI ELEMENTS
        private MapElement currElement;
        private ImageView northWest;
        private ImageView northEast;
        private ImageView southWest;
        private ImageView southEast;
        private ImageView structure;


        public MapDataViewHolder(LayoutInflater li, ViewGroup vp) {
            super(li.inflate(R.layout.grid_cell, vp, false));

            //find size of array
            int size = vp.getMeasuredHeight() / currMap.getHEIGHT() + 1;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();

            //set layout parameters height and width
            lp.width = size;
            lp.height = size;

            //find UI elements
            northWest = itemView.findViewById(R.id.imageNW);
            northEast = itemView.findViewById(R.id.imageNE);
            southWest = itemView.findViewById(R.id.imageSW);
            southEast = itemView.findViewById(R.id.imageSE);
            structure = itemView.findViewById(R.id.structure);
            structure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    int buildingCost = 0;
                    String structure_type;

                    // check type of structure selected from selector fragment
                    if(selectorF.getSelectedStructure()!= null)
                    {
                        if(selectorF.getSelectedStructure() instanceof Residential) {
                            buildingCost = currMap.getSettings().getHouseBuildingCost();
                            structure_type = "residential";
                        }
                        else if (selectorF.getSelectedStructure() instanceof Commercial) {
                            buildingCost = currMap.getSettings().getCommBuildingCost();
                            structure_type = "commercial";
                        }
                        else {
                            buildingCost = currMap.getSettings().getRoadBuildingCost();
                            structure_type = "road";
                        }
                    }

                    //check if current element is buildable, that there is enough money to build structure and
                    //current action is to build
                    if(currElement.isBuildable() && selectorF.getSelectedStructure() != null && currElement.getStructure() == null
                            && build && (buildingCost <= currMap.getMoney()))
                    {
                        if(currMap.isAdjacentToRoad(currElement.getOwnerID()) || selectorF.getSelectedStructure() instanceof Road)
                        {
                            //reduce money, increase building count, set image resource, and save change to database
                            currMap.setMoney(currMap.getMoney() - buildingCost);
                            currMap.countBuilding(selectorF.getSelectedStructure());
                            structure.setImageResource(selectorF.getSelectedStructure().getDrawableId());
                            currElement.setStructure(selectorF.getSelectedStructure());
                            currMap.edit(currElement);
                            //notify changes to adapter and update UI elementws
                            rvM.getAdapter().notifyItemChanged(getAdapterPosition());
                            update();
                        }
                    } //check if current action is destroy
                    else if(currElement.isBuildable() && destroy)
                    {
                        //remove structure image resource, remove structure from map, save changes to database
                        structure.setImageResource(0);
                        currMap.removeBuilding(currElement.getStructure());
                        currElement.setStructure(null);
                        currMap.edit(currElement);
                        //notify adapter of changes and update UI elements
                        rvM.getAdapter().notifyItemChanged(getAdapterPosition());
                        update();
                    }//check id action is details and if structure is not null
                    else if(details && currElement.getStructure() != null)
                    {
                        String structure = "";
                        //check structure type
                        if(currElement.getStructure() instanceof Residential)
                            structure = "Residential";
                        else if (currElement.getStructure() instanceof Commercial)
                            structure = "Commercial";
                        else if (currElement.getStructure() instanceof Road)
                            structure = "Road";

                        //pass mapelement values to intent, start activity for results
                        Intent details = Details.getIntent(getActivity(),
                                currElement.getOwnerID()/100,
                                currElement.getOwnerID()%100, structure, currElement.getOwnerName());
                        startActivityForResult(details, REQUEST_CODE_DETAILS);
                    }
                }
            });
        }

        //bind image resources
        public void bind(MapElement currElement)
        {
            //set map element and image resources
            this.currElement = currElement;
            northWest.setImageResource(currElement.getNorthWest());
            northEast.setImageResource(currElement.getNorthEast());
            southWest.setImageResource(currElement.getSouthWest());
            southEast.setImageResource(currElement.getSouthEast());

            // if bitmap is present , set bitmap image otherwise structure, if both are null
            //set to empty image resource
            if(currElement.getImage() != null)
            {
                structure.setImageBitmap(currElement.getImage());
            }
            else if(currElement.getStructure() != null)
            {
                structure.setImageResource(currElement.getStructure().getDrawableId());
            }
            else
            {
                structure.setImageResource(0);
            }
        }
    }

    public class MapAdapter extends RecyclerView.Adapter<MapDataViewHolder>
    {
        /**
         * constructs and inflates the viewholder and map adapter
         * @param viewGroup
         * @param i - location in adapter
         * @return - returns new map view holder object
         */
        @Override
        public MapDataViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());

            return new MapDataViewHolder(li, viewGroup);
        }

        //bind image resources to view, pass location of mep element

        /**
         * gets location in adapter and sends mapData to rebind UI element
         * @param mapData - current viewholder
         * @param i - location in adapter
         */
        @Override
        public void onBindViewHolder(@NonNull MapDataViewHolder mapData, int i)
        {
            int row = i % GameData.getHEIGHT();
            int col = i / GameData.getHEIGHT();
            mapData.bind(currMap.get(row, col));
        }

        //get number of items in array

        /**
         * Number of items found in the adapter
         * @return number of elements in adapter
         */
        @Override
        public int getItemCount() {
            return (GameData.getHEIGHT() * GameData.getWIDTH());
        }
    }
}

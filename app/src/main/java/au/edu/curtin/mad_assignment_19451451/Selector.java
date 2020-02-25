package au.edu.curtin.mad_assignment_19451451;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Selector Fragment, consists of list of object structures that may be built
 */
public class Selector extends Fragment
{
    //Class fields - list of structure, as a structureData object
    private StructureData structureList;
    //Currently selected structure
    private Structure selectedStructure;
    //Adapter
    private SelectorAdapter structureAdapter;

    //Recycler view
    private RecyclerView rvS;

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
    }

    /**
     * inflates the image and instantiates adapters, recycler views, and sets layout manager
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selector, container,false);

        //get lists of structure from singleton
        structureList = StructureData.get();

        //set the adaptor
        structureAdapter = new SelectorAdapter();

        //find recycler view and set layout manager constraints, set new adaoter to adaptor made above
        rvS = (RecyclerView) view.findViewById(R.id.selectorRecyclerView);
        rvS.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                false));
        rvS.setAdapter(structureAdapter);

        return view;
    }

    /**
     * Get the current selected structure, used by other fragments such as maps
     * @return Structure - currently selected structure
     */
    public Structure getSelectedStructure() {
        return selectedStructure;
    }

    public class SelectorViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView currImage;
        private TextView imageDesc;
        private boolean selected = true;

        /**
         * Constructs view holder
         * @param li - layout inflater
         * @param parent - calling parent's viewgroup
         */
        public SelectorViewHolder(LayoutInflater li, ViewGroup parent) {
            super(li.inflate(R.layout.list_selection, parent, false));
            //find UI elements
            currImage = (ImageView) itemView.findViewById(R.id.currStruct);
            imageDesc = (TextView) itemView.findViewById(R.id.structDesc);

            //set on click action
            currImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    //check if object is selected or deselected (DOES NOT WORK PERFECTLY)
                    if(selectedStructure != null) //checks if object is selected and if prev object is same object
                    {
                        //if object is the same, then deselect the structure
                        if (selectedStructure.equals(structureList.get(getAdapterPosition())))
                            selected = false;
                        else
                            selected = true;
                    }

                    // if selected, get object otherwise let no object is selected
                    if (selected)
                    {
                        selectedStructure = structureList.get(getAdapterPosition());
                    }
                    else
                    {
                        selectedStructure = null;
                    }
                }
            });
        }

        /**
         * Rebind image to current structure selected, set image resource and description of the image
         * @param currStructure
         */
        public void bind(Structure currStructure) {
            selectedStructure = currStructure;
            currImage.setImageResource(currStructure.getDrawableId());
            imageDesc.setText(currStructure.getLabel());
        }
    }


    public class SelectorAdapter extends RecyclerView.Adapter<SelectorViewHolder>
    {
        @Override
        public SelectorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            return new SelectorViewHolder(LayoutInflater.from(getActivity()), viewGroup);
        }

        /**
         * Rebind image resource
         * @param selectorViewHolder - current view holder
         * @param i - current adapter element
         */
        @Override
        public void onBindViewHolder(SelectorViewHolder selectorViewHolder, int i)
        {
            selectorViewHolder.bind(structureList.get(i));
        }

        /**
         * Retrieve number of items stored in adaptor
         * @return integer - number of items found in list
         */
        @Override
        public int getItemCount() {
            return structureList.size();
        }
    }
}


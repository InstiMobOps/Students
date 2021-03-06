package in.ac.iitm.students.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.ac.iitm.students.Adapters.EventsAdapter;
import in.ac.iitm.students.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {
    RecyclerView mRecyclerView;

    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_events, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.hot_fragment_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(new EventsAdapter(getActivity()));
        // Inflate the layout for this fragment
        return v;

    }


}

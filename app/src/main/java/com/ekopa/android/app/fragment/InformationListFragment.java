package com.ekopa.android.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ekopa.android.app.R;
import com.ekopa.android.app.adapter.MoreInfoRecyclerAdapter;
import com.ekopa.android.app.util.DividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationListFragment extends Fragment {
    String[] information_titles;

    MoreInfoRecyclerAdapter mAdapter;

    public InformationListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_information_list, container, false);

        information_titles = getResources().getStringArray(R.array.information);
        mAdapter = new MoreInfoRecyclerAdapter(information_titles, getContext());
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycleview_more_info);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        return v;
    }

}

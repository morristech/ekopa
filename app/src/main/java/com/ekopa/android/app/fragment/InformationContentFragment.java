package com.ekopa.android.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekopa.android.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationContentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "content";

    // TODO: Rename and change types of parameters
    private String mTitle;
    private String mContent;


    public InformationContentFragment() {
        // Required empty public constructor
    }

    /**
     * @param title Parameter 1.
     * @param content Parameter 2.
     * @return A new instance of fragment InformationContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InformationContentFragment newInstance(String title, String content) {
        InformationContentFragment fragment = new InformationContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_PARAM1);
            mContent = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_information_content, container, false);
        TextView _title = (TextView) v.findViewById(R.id.tv_fragment_content_title);
        TextView _content = (TextView) v.findViewById(R.id.tv_fragment_content_text);

        _title.setText(mTitle);
        _content.setText(mContent);

        return v;
    }


}

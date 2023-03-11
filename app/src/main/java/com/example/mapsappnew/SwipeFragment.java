package com.example.mapsappnew;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mapsappnew.R;

public class SwipeFragment extends Fragment {

    private static final String ARG_TEXT = "text";
    private String mText;

    public SwipeFragment() {
        // Required empty public constructor
    }

    public static SwipeFragment newInstance(String text) {
        SwipeFragment fragment = new SwipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mText = getArguments().getString(ARG_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (mText.equals("Fragment 1")) {
            // Inflate the layout for the first fragment
            view = inflater.inflate(R.layout.fragment_swipe, container, false);
        } else {
            // Inflate a different layout for the second fragment that includes a button
            view = inflater.inflate(R.layout.fragment_2, container, false);
            Button button = view.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click event

                     Intent intent = new Intent(getActivity(), MapActivity.class);
                     startActivity(intent);

                }
            });
        }
        return view;
    }
}
package com.example.mapsappnew;

import static android.content.Context.MODE_PRIVATE;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

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


        // Reference to the fade In animation file
        Animation fadeIn = AnimationUtils.loadAnimation(this.getActivity(), R.anim.fade_in_animation);

        // Reference to the start_pages.xml file
        View view = inflater.inflate(R.layout.start_pages, container, false);

        // Reference to the view in the start_pages.xml file
        View view1 = view.findViewById(R.id.view);

        // Reference to the string array in the string.xml file
        String[] MainText = getResources().getStringArray(R.array.StartPagesText);

        // Reference to the image view in the start_pages.xml file
        ImageView imageView = view.findViewById(R.id.iconImage);
        imageView.setColorFilter(getResources().getColor(R.color.white));

        Button button = view.findViewById(R.id.button);

        // Reference to the text view in the start_pages.xml file
        TextView textView = view.findViewById(R.id.textView);

        if (mText.equals("Fragment 1")) {

            // Reference to the start_pages.xml file

            textView.setText(MainText[0]);

            imageView.setImageResource(R.drawable.ic_lock_icon);

            view1.startAnimation(fadeIn);



        }
        if (mText.equals("Fragment 2")) {

            view1.startAnimation(fadeIn);

            imageView.setImageResource(R.drawable.ic_wifi_warning);

            textView.setText(MainText[1]);


        }

        if (mText.equals("Fragment 3")) {

            view1.startAnimation(fadeIn);

               imageView.setImageResource(R.drawable.ic_wifi_secure);

            textView.setText(MainText[2]);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            textView.setGravity(Gravity.START);

        }

        if (mText.equals("Fragment 4")) {

            view1.startAnimation(fadeIn);

            textView.setText(MainText[3]);

            button.setVisibility(View.VISIBLE);

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Get the SharedPreferences object
                    SharedPreferences prefs = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);

                    prefs.edit().putBoolean("isFirstLaunch", false).apply();

                    Intent intent = new Intent(getContext(), MapActivity.class);

                     startActivity(intent);


                }



            });

        }
        return view;
    }
}
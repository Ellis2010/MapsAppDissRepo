package com.example.mapsappnew;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Map;

public class CustomSearchView extends SearchView {

    private AutoCompleteTextView autoCompleteTextView;
    private ImageView mSearchIcon;
    private ArrayAdapter<String> adapter;

    public CustomSearchView(Context context) {
        super(context);
        init();
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Get a reference to the AutoCompleteTextView
        int autoCompleteTextViewId = getResources().getIdentifier("search_src_text", "id", getContext().getPackageName());
        autoCompleteTextView = findViewById(autoCompleteTextViewId);

        // Set up the adapter with an empty list
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        autoCompleteTextView.setAdapter(adapter);


        MapActivity mapActivity = new MapActivity();

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected item
            // Get the selected item
            String selectedItem = adapter.getItem(position);

            // Fill the search view with the selected item
            setQuery(selectedItem, false);
        });
    }

    public void updateAutocompleteData(ArrayAdapter<String> adapter) {
        this.adapter = adapter;
        autoCompleteTextView.setAdapter(adapter);
    }

    public TextView getAutoCompleteTextView() {
        return autoCompleteTextView;
    }
}
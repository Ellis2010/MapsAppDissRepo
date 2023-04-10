package com.example.mapsappnew;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ListData> {
    public ListAdapter(@NonNull Context context, ArrayList<ListData> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View View, @NonNull ViewGroup parent) {
        ListData data = getItem(position);

        if(View == null) {
            View = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }



        TextView name = View.findViewById(R.id.Name);

        TextView address = View.findViewById(R.id.Address);

        TextView distance = View.findViewById(R.id.Distance);

        TextView status = View.findViewById(R.id.Status);

        name.setText(data.name);
        address.setText(data.address);
        distance.setText(data.distance);
        status.setText(data.status);
        if(status.getText().toString().equals("Open")) {
            status.setTextColor(Color.GREEN);
        } else {
            status.setTextColor(Color.RED);
        }

        return View;

    }
}

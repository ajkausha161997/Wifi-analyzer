package com.example.ajay.wifianalyzer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ajay Kaushal
 */

public class PropertyAdapter extends ArrayAdapter {

    public PropertyAdapter(Activity context, ArrayList<Properties> properties) {
        super(context,0,properties);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.property_list_item, parent, false);
        }

        Properties currentProperty = (Properties) getItem(position);

        TextView propView = (TextView)listItemView.findViewById(R.id.text1);
        propView.setText(currentProperty.getProperty());

        TextView valView = (TextView)listItemView.findViewById(R.id.text2);
        valView.setText(currentProperty.getValue());

        return listItemView;
    }
}

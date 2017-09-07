package com.example.parthdoshi.articleshub;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Parth on 30-07-2017.
 */

public class SelectPageCustomAdapter extends ArrayAdapter {
    SelectPageModel[] TagList = null;
    Context context;

    public SelectPageCustomAdapter(@NonNull Context context, SelectPageModel[] resource) {
        super(context, R.layout.activity_row_select_tag_page);
        this.context = context;
        this.TagList = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.activity_row_select_tag_page, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.select_page_textview);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.select_page_checkbox);
        name.setText(TagList[position].getTagName());
        if(TagList[position].getTagValue() == 1)
            cb.setChecked(true);
        else
            cb.setChecked(false);
        return convertView;
    }
}
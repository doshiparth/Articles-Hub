package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

class SelectTagPageCustomAdapter extends ArrayAdapter<SelectTagPageModel> {
    //SelectTagPageModel[] TagList = null;
    //Context context;

    public SelectTagPageCustomAdapter(@NonNull Context context, SelectTagPageModel[] TagList) {
        super(context, R.layout.activity_select_tag_page_custom_row, TagList);
        //this.context = context;
        //this.TagList = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        //convertView = inflater.inflate(R.layout.activity_select_tag_page_custom_row, parent, false);
        SelectTagPageModel currentTag = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_select_tag_page_custom_row, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.select_page_textview);
        name.setText(currentTag.getTagName());
        return convertView;
    }
}
package com.example.parthdoshi.articleshub;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Parth on 30-07-2017.
 */

public class HomeAboutPage extends Fragment {

    View v1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v1 = inflater.inflate(R.layout.home_about_page, container, false);
        return v1;
    }
}

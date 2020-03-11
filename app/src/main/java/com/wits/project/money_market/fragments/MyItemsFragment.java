package com.wits.project.money_market.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wits.project.money_market.R;
import com.wits.project.money_market.manager.UiManager;

import org.json.JSONArray;

public class MyItemsFragment extends Fragment {

    @SuppressLint("StaticFieldLeak") public static View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_items, container, false);
        return v;
    }

    public static void populateViews(JSONArray jsonArray){
        UiManager.addItemsToLinearLayout(jsonArray, (LinearLayout) v.findViewById(R.id.my_items_holder));
    }
}

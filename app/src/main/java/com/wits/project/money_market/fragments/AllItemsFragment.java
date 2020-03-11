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
import android.widget.SearchView;

import com.wits.project.money_market.R;
import com.wits.project.money_market.manager.UiManager;

import org.json.JSONArray;

public class AllItemsFragment extends Fragment {

    @SuppressLint("StaticFieldLeak") public static View v;
    private static JSONArray original = new JSONArray();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_all_items, container, false);

        SearchView searchView = v.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                UiManager.filterList(original, newText,(LinearLayout) v.findViewById(R.id.all_items_holder));
                return false;
            }
        });

        return v;
    }

    public static void populateViews(JSONArray jsonArray){
        original = jsonArray;
        UiManager.addItemsToLinearLayout(jsonArray, (LinearLayout) v.findViewById(R.id.all_items_holder));
    }

}

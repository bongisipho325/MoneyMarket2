package com.wits.project.money_market.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wits.project.money_market.R;
import com.wits.project.money_market.fragments.AllItemsFragment;
import com.wits.project.money_market.fragments.MyItemsFragment;
import com.wits.project.money_market.main.SectionsPagerAdapter;
import com.wits.project.money_market.manager.DbManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragmentAndTitle(new AllItemsFragment(), "All Items");
        sectionsPagerAdapter.addFragmentAndTitle(new MyItemsFragment(), "My Items");

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SellItemActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateChildrenWithItems(swipeRefreshLayout);
            }
        });
        populateChildrenWithItems(swipeRefreshLayout);
    }

    private static void populateChildrenWithItems(final SwipeRefreshLayout swipeRefreshLayout) {
        ContentValues cv = new ContentValues();
        cv.put("choice", "readAll");
        new DbManager("http://lamp.ms.wits.ac.za/~s1520337/ITEM.php", cv) {
            @Override
            protected void onPostExecute(String output) {
              try {
                  swipeRefreshLayout.setRefreshing(false);
                  SharedPreferences sp = swipeRefreshLayout.getContext()
                          .getSharedPreferences("Money_Market_PREF", MODE_PRIVATE);
                  String myId = sp.getString("USER_ID", "-1");

                  JSONArray myItems = new JSONArray();
                  JSONArray otherItems = new JSONArray();

                  JSONArray items = new JSONArray(output);
                  for (int i = 0; i < items.length(); i++){
                      JSONObject item = items.getJSONObject(i);
                      if (myId.equals(item.getString("MONEY_MARKET_USER_ID"))){
                          myItems.put(item);
                      }
                      else otherItems.put(item);
                  }

                  AllItemsFragment.populateViews(otherItems);
                  MyItemsFragment.populateViews(myItems);

              } catch (JSONException e) {
                  e.printStackTrace();
              }
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.log_out) {
            getSharedPreferences("Money_Market_PREF", MODE_PRIVATE).edit().clear().apply();
            startActivity(new Intent(this, LogInActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
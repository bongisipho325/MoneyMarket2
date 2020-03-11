package com.wits.project.money_market.activities;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.wits.project.money_market.R;
import com.wits.project.money_market.manager.DbManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SellItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        findViewById(R.id.sell_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText itemName = findViewById(R.id.sell_item_name),
                        itemDesc = findViewById(R.id.sell_item_desc),
                        itemPrice = findViewById(R.id.sell_item_price);

                String sItemName = itemName.getText().toString().trim();
                String sItemDesc = itemDesc.getText().toString().trim();
                String sItemPrice = itemPrice.getText().toString().trim();

                boolean allInputsAdded = true;

                if (TextUtils.isEmpty(sItemName)){
                    itemName.setError("Enter item name");
                    allInputsAdded = false;
                }

                if (TextUtils.isEmpty(sItemDesc)){
                    itemDesc.setError("Enter item description");
                    allInputsAdded = false;
                }
                if (TextUtils.isEmpty(sItemPrice)){
                    itemPrice.setError("Enter item price");
                    allInputsAdded = false;
                }

                if (allInputsAdded){
                    sItemDesc = sItemDesc.replace("\n", "\\n");
                    SharedPreferences sp = getSharedPreferences("Money_Market_PREF", MODE_PRIVATE);
                    ContentValues cv = new ContentValues();
                    SimpleDateFormat smd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    cv.put("choice", "post");
                    cv.put("item_name", sItemName);
                    cv.put("item_desc", sItemDesc);
                    cv.put("item_price", Double.parseDouble(sItemPrice));
                    cv.put("item_date", smd.format(new Date()));
                    cv.put("user_id", Integer.parseInt(sp.getString("USER_ID", "-1")));
                    sellItem(SellItemActivity.this, cv);
                }

            }
        });
    }

    private static void sellItem(final SellItemActivity c, ContentValues cv){
        new DbManager("http://lamp.ms.wits.ac.za/~s1520337/ITEM.php", cv) {
            @Override
            protected void onPostExecute(String output) {
                if (output.equals("1")){
                    Toast.makeText(c, "Item posted", Toast.LENGTH_SHORT).show();
                    c.finish();
                }
                else{
                    Toast.makeText(c, "Failed to post item", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}

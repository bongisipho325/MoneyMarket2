package com.wits.project.money_market.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wits.project.money_market.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ItemViewActivity extends AppCompatActivity {

    public static JSONObject item;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.item_card_view);

        try {
            ((TextView) findViewById(R.id.item_name)).setText(item.getString("MONEY_MARKET_ITEM_NAME"));
            String soldBy = String.format("Sold by: %s %s", item.getString("MONEY_MARKET_USER_NAME"),
                    item.getString("MONEY_MARKET_USER_SURNAME"));
            ((TextView) findViewById(R.id.textView2)).setText(soldBy);
            String itemDesc = item.getString("MONEY_MARKET_ITEM_DESC");
            itemDesc = itemDesc.replace("\\n", "\n");
            ((TextView) findViewById(R.id.item_desc)).setText(itemDesc);

            double rating = Double.parseDouble(item.getString("AVE_RATING"));
            String sRating = String.format(Locale.getDefault(),"%.1f", rating);
            ((TextView) findViewById(R.id.item_average)).setText(sRating);

            String price = String.format("R%s", item.getString("MONEY_MARKET_ITEM_PRICE"));
            ((TextView) findViewById(R.id.item_price)).setText(price);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

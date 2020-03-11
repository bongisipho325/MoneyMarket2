package com.wits.project.money_market.manager;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wits.project.money_market.R;
import com.wits.project.money_market.activities.ItemViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class UiManager {

    public static void addItemsToLinearLayout(JSONArray jsonArray, final LinearLayout holder) {
        holder.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);

        try {
            for (int i = 0; i < jsonArray.length(); i++){
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                View itemVIew = View.inflate(holder.getContext(), R.layout.item_mini_card_view, null);
                ((TextView) itemVIew.findViewById(R.id.item_mini_name)).setText(jsonObject.getString("MONEY_MARKET_ITEM_NAME"));
                String price = String.format("R%s", jsonObject.getString("MONEY_MARKET_ITEM_PRICE"));
                ((TextView) itemVIew.findViewById(R.id.item_mini_price)).setText(price);

                itemVIew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ItemViewActivity.item = jsonObject;
                        holder.getContext().startActivity(new Intent(holder.getContext(), ItemViewActivity.class));
                    }
                });

                itemVIew.findViewById(R.id.item_mini_rate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            View rateView = View.inflate(holder.getContext(), R.layout.rate_view, null);
                            final RatingBar ratingBar = rateView.findViewById(R.id.item_rating);
                            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                    if (rating == 0) ratingBar.setRating((float) 0.5);
                                }
                            });

                            final int itemId = jsonObject.getInt("MONEY_MARKET_ITEM_ID");
                            SharedPreferences sp = holder.getContext().getSharedPreferences("Money_Market_PREF", MODE_PRIVATE);
                            final int userId = Integer.parseInt(sp.getString("USER_ID", "-1"));

                            new AlertDialog.Builder(holder.getContext())
                                    .setIcon(R.mipmap.ic_launcher)
                                    .setTitle(String.format("Rate %s", jsonObject.getString("MONEY_MARKET_ITEM_NAME")))
                                    .setPositiveButton("rate", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            double rating = ratingBar.getRating();
                                            ContentValues cv = new ContentValues();
                                            cv.put("choice", "rate");
                                            cv.put("user_id", userId);
                                            cv.put("item_id", itemId);
                                            cv.put("rating", rating);
                                            postRating(cv, holder.getContext());
                                        }
                                    })
                                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setView(rateView)
                                    .create()
                                    .show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                holder.addView(itemVIew, params);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static void postRating(ContentValues cv, final Context context) {
        new DbManager("http://lamp.ms.wits.ac.za/~s1520337/ITEM.php", cv) {
            @Override
            protected void onPostExecute(String output) {
                if (output.equals("1")){
                    Toast.makeText(context, "rating success", Toast.LENGTH_SHORT).show();
                }
                else if (output.equals("0")){
                    Toast.makeText(context, "You have already rated item", Toast.LENGTH_SHORT).show();
                }

                else Toast.makeText(context, "Item rating failed", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    public static void filterList(JSONArray original, String newText, LinearLayout holder) {
        if (TextUtils.isEmpty(newText.trim())){
            addItemsToLinearLayout(original, holder);
            return;
        }

        JSONArray temp = new JSONArray();
        try {
            for (int i = 0; i < original.length(); i++){
                JSONObject jsonObject = original.getJSONObject(i);
                if (jsonObject.getString("MONEY_MARKET_ITEM_NAME").toLowerCase().contains(newText.toLowerCase())
                        || jsonObject.getString("MONEY_MARKET_ITEM_DESC").toLowerCase().contains(newText.toLowerCase())){
                    temp.put(jsonObject);
                }
            }
            addItemsToLinearLayout(temp, holder);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

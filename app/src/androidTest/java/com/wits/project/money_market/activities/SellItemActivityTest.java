package com.wits.project.money_market.activities;

import android.content.ContentValues;
import android.support.design.widget.TextInputEditText;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.wits.project.money_market.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SellItemActivityTest {

    @Rule
    public ActivityTestRule<SellItemActivity> activityTestRule = new ActivityTestRule<>(SellItemActivity.class);

    @Test
    public void sellFailToSubmitToServer(){
        try{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //pressing the button without previously inputting text
                    activityTestRule.getActivity().findViewById(R.id.sell_button).performClick();
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Test
    public void sellPassToSubmitToServer(){
        try{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //input text then press sell button
                    TextInputEditText itemName = activityTestRule.getActivity().findViewById(R.id.sell_item_name),
                            itemDesc = activityTestRule.getActivity().findViewById(R.id.sell_item_desc),
                            itemPrice = activityTestRule.getActivity().findViewById(R.id.sell_item_price);

                    itemName.setText("sddfg");
                    itemDesc.setText("dsfdsf");
                    itemPrice.setText("12.5");

                    activityTestRule.getActivity().findViewById(R.id.sell_button).performClick();
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    //
    @Test
    public void add() {
        int expectation = 10;
        int a = 2;
        int b = 8;

        int result = SellItemActivity.add(a, b);
        assertEquals(expectation, result);
    }
    //Testing functions
    @Test
    public void test(){
        try{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SellItemActivity.sellItem(activityTestRule.getActivity(), new ContentValues());
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    /*@Test
    public void test(){
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {

            }
        });
    }*/
}
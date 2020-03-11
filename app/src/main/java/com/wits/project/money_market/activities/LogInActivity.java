package com.wits.project.money_market.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wits.project.money_market.R;
import com.wits.project.money_market.manager.DbManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends Activity {

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);

        SharedPreferences sp = getSharedPreferences("Money_Market_PREF", MODE_PRIVATE);
        if (sp.getBoolean("LOGGED_IN", false)){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        final TextInputEditText userEmail = findViewById(R.id.user_email);
        final TextInputEditText userPass = findViewById(R.id.user_password);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sUserEmail = userEmail.getText().toString().trim();
                String sUserPass = userPass.getText().toString().trim();

                boolean allInputsProvided = true;

                if (TextUtils.isEmpty(sUserEmail)){
                    userEmail.setError("Enter email");
                    allInputsProvided = false;
                }

                if (TextUtils.isEmpty(sUserPass)){
                    userPass.setError("Enter Password");
                    allInputsProvided = false;
                }

                if (allInputsProvided){
                    ContentValues cv = new ContentValues();
                    cv.put("choice", "login");
                    cv.put("username", sUserEmail);
                    cv.put("password", sUserPass);
                    login(LogInActivity.this, cv);
                }
            }
        });

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), RegistrationActivity.class));
            }
        });
    }

    private static void login(final LogInActivity c, final ContentValues cv) {
        new DbManager("http://lamp.ms.wits.ac.za/~s1520337/USER.php", cv) {
            @Override
            protected void onPostExecute(String output) {
                if (!output.equals("0") && !TextUtils.isEmpty(output)){
                    try {
                        JSONObject user = new JSONObject(output);
                        SharedPreferences sp = c.getSharedPreferences("Money_Market_PREF", MODE_PRIVATE);
                        sp.edit().putBoolean("LOGGED_IN", true)
                                .putString("USER_ID", user.getString("MONEY_MARKET_USER_ID"))
                                .apply();

                        Toast.makeText(c, "Login success", Toast.LENGTH_SHORT).show();
                        c.startActivity(new Intent(c, MainActivity.class));
                        c.finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Toast.makeText(c, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}

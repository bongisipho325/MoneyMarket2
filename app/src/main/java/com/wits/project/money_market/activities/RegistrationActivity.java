package com.wits.project.money_market.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.wits.project.money_market.R;
import com.wits.project.money_market.manager.DbManager;

public class RegistrationActivity extends Activity {

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_registration);

        final TextInputEditText
                email = findViewById(R.id.reg_email),
                name = findViewById(R.id.reg_name),
                surname = findViewById(R.id.reg_surname),
                password = findViewById(R.id.reg_pass),
                confPassword = findViewById(R.id.reg_conf_pass);

        findViewById(R.id.reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String
                        sEmail = email.getText().toString().trim(),
                        sName = name.getText().toString().trim(),
                        sSurname = surname.getText().toString().trim(),
                        sPassword = password.getText().toString().trim(),
                        sConfPassword = confPassword.getText().toString().trim();

                boolean allInputsEntered = true;

                if (TextUtils.isEmpty(sEmail)){
                    email.setError("Enter Email");
                    allInputsEntered = false;
                }

                if (TextUtils.isEmpty(sName)){
                    name.setError("Enter Name");
                    allInputsEntered = false;
                }

                if (TextUtils.isEmpty(sSurname)){
                    surname.setError("Enter Surname");
                    allInputsEntered = false;
                }

                if (TextUtils.isEmpty(sPassword)){
                    password.setError("Enter Password");
                    allInputsEntered = false;
                }

                if (TextUtils.isEmpty(sConfPassword)){
                    confPassword.setError("Enter Password");
                    allInputsEntered = false;
                }

                if (allInputsEntered){
                    boolean passwordsMatch = sPassword.equals(sConfPassword);
                    if (passwordsMatch){
                        //TODO: REGISTER
                        ContentValues cv = new ContentValues();
                        cv.put("choice", "register");
                        cv.put("username", sEmail);
                        cv.put("password", sPassword);
                        cv.put("name", sName);
                        cv.put("surname", sSurname);
                        register(RegistrationActivity.this, cv);
                    }
                    else{
                        password.setError("Passwords mismatch");
                        confPassword.setError("Passwords mismatch");
                    }
                }
            }
        });

    }

    private static void register(final RegistrationActivity c, ContentValues cv) {
        new DbManager("http://lamp.ms.wits.ac.za/~s1520337/USER.php", cv) {
            @Override
            protected void onPostExecute(String output) {
                if (output.equals("1")){
                    Toast.makeText(c, "registration success", Toast.LENGTH_SHORT).show();
                    c.finish();
                }
                else{
                    Toast.makeText(c, "registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}

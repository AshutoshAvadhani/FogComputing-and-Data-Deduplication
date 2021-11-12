package com.android.homecloudclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button buttonlogin;
    private Button buttonregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        buttonregister = (Button) findViewById(R.id.registerButton);
        buttonlogin = (Button) findViewById(R.id.LoginButton);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        buttonregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(WelcomeActivity.this,RegistrationActivity.class);
                    startActivity(intent);
                } catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        });
    }
}

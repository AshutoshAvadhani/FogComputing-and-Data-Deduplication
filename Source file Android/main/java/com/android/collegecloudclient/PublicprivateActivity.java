package com.android.homecloudclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PublicprivateActivity extends AppCompatActivity {

    Button btnpublic,btnprivate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicprivate);

        btnpublic=(Button)findViewById(R.id.buttonpublic);
        btnprivate=(Button)findViewById(R.id.buttonprivate);

        btnpublic.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(PublicprivateActivity.this, MenupublicActivity.class);
                startActivity(i);
            }
        });


        btnprivate.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(PublicprivateActivity.this, KeypadActivity.class);
                startActivity(i);
            }
        });
    }
}

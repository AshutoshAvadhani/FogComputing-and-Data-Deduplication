package com.android.homecloudclient;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String TAG="In Home Cloud startActivty List";
    Button start;
    Button IpCancleButton;
    Button IpSubmitButton;
    public static String serverIp;
    public static int portnumberStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start=(Button)findViewById(R.id.button1);
        start.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(!TextUtils.isEmpty(serverIp) && !TextUtils.isEmpty(String.valueOf(portnumberStr))) {
                    Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Please Congfigure the Server Address",Toast.LENGTH_SHORT).show();
                }
                }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        switch (item.getItemId()) {
            case R.id.ip_settings:
            {
                dialog.setContentView(R.layout.activity_configure);
                dialog.setCancelable(true);

                IpCancleButton = (Button) dialog.findViewById(R.id.ipCancelButton);
                IpCancleButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                IpSubmitButton = (Button) dialog.findViewById(R.id.ipOkButton);

                IpSubmitButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {

                            EditText editTextipServer = (EditText) dialog.findViewById(R.id.ipEditText);
                            serverIp = editTextipServer.getText().toString();

                            EditText editPortServer=(EditText) dialog.findViewById(R.id.portEditText);
                            portnumberStr=Integer.parseInt(editPortServer.getText().toString());
                            Toast.makeText(MainActivity.this, "Server Ip:" + serverIp+" "+"Port Number:"+portnumberStr,
                                    Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            Log.d(TAG, "IP address:" + serverIp);
                            //}
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                dialog.show();
            }
            break;

        }

        return super.onOptionsItemSelected(item);
    }// end onOptionsItemSelected
}

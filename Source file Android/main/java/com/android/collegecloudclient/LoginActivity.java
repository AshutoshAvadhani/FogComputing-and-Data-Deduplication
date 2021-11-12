package com.android.homecloudclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.homecloudclient.Context.ApplicationContext;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {


    public static final String TAG = "com.java.homeautomation";
    private Button buttonlogin;
    private Button buttoncancel;
    private EditText username;
    private EditText password;
    Intent intent;
    String user_name,pass_word;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buttonlogin = (Button) findViewById(R.id.LoginButton);
        buttoncancel = (Button) findViewById(R.id.Button_Cancel);
        username = (EditText) findViewById(R.id.editTextUserName);
        password = (EditText) findViewById(R.id.editTextPassword);


        buttoncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LoginActivity.this.finish();
            }
        });


        buttonlogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    user_name = username.getText().toString();
                    pass_word = password.getText().toString();

                    if (TextUtils.isEmpty(user_name)) {
                        username.setError("Invalid User Name");
                    }
                    else if (TextUtils.isEmpty(pass_word)) {
                        password.setError("Invalid Password");
                    }
                    else
                    {
                        LoginTask task = new LoginTask();
                        task.execute(new String[] { "hello" });
                    }
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected String doInBackground(String... urls) {
            @SuppressWarnings("unused")
            String response = urls[0];
            try{
                Socket client = new Socket(MainActivity.serverIp, MainActivity.portnumberStr); // connect to the server
                DataOutputStream dos=new DataOutputStream(client.getOutputStream());
                DataInputStream din=new DataInputStream(client.getInputStream());

                dos.writeUTF("8");
                dos.flush();
                Thread.sleep(100);
                dos.writeUTF(user_name);
                dos.flush();

                dos.writeUTF(pass_word);
                dos.flush();



                //Read value coming from server..
                int result =din.readInt();
                Log.d(TAG,"result"+result);
                ((ApplicationContext)LoginActivity.this.getApplicationContext()).setUserId(result);
                return result+"";

            }catch(Exception e){
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            if(Integer.parseInt(result) >0){


                ApplicationContext appcontext = (ApplicationContext) getApplicationContext();
                appcontext.getUserId();

                Toast.makeText(LoginActivity.this, "Login successful.",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(LoginActivity.this,PublicprivateActivity.class);

                startActivity(i);

            }
            else{
                Toast.makeText(LoginActivity.this, "Login failed.",Toast.LENGTH_SHORT).show();
            }
            username.setText("");
            password.setText("");

        }
    }
}

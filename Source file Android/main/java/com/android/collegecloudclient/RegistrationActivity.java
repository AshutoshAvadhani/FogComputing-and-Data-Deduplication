package com.android.homecloudclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class RegistrationActivity extends AppCompatActivity {

    Button buttonregister;
    private Button buttoncancel;
    EditText firstname;
    EditText lastname;
    EditText username;
    EditText password;
    EditText emailText;
    EditText mobileText;
    EditText pincodeText;
    String fname,lname,UserName,PassWord,email,mobile,pincode;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        buttonregister = (Button) findViewById(R.id.RegisterButton);
        buttoncancel = (Button) findViewById(R.id.Cancel_Button);
        firstname = (EditText) findViewById(R.id.editTextFirstName);
        lastname = (EditText) findViewById(R.id.editTextLastName);
        username = (EditText) findViewById(R.id.editTextUserName);
        password = (EditText) findViewById(R.id.editTextPassword);
        emailText=(EditText) findViewById(R.id.editTextemail);
        mobileText=(EditText) findViewById(R.id.editTextmobile);
        pincodeText=(EditText) findViewById(R.id.editTextpincode);

        buttoncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RegistrationActivity.this.finish();
            }
        });

buttonregister.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        fname =firstname.getText().toString();
        lname= lastname.getText().toString();
         UserName = username.getText().toString();
         PassWord= password.getText().toString();
        email=emailText.getText().toString();
        mobile=mobileText.getText().toString();
        pincode=pincodeText.getText().toString();

        RegisterTask task = new RegisterTask();
        task.execute(new String[] { "hello" });
    }
});

    }


    private class RegisterTask extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(RegistrationActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please Wait...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }
        @Override
        protected String doInBackground(String... urls) {
            try{
                Socket client = new Socket(MainActivity.serverIp, MainActivity.portnumberStr); // connect to the server
                DataOutputStream dos=new DataOutputStream(client.getOutputStream());
                DataInputStream din=new DataInputStream(client.getInputStream());

                dos.writeUTF("7");
                dos.flush();
                Thread.sleep(100);
                dos.writeUTF(fname);
                dos.flush();
                dos.writeUTF(lname);
                dos.flush();
                dos.writeUTF(email);
                dos.flush();
                dos.writeUTF(mobile);
                dos.flush();
                dos.writeUTF(pincode);
                dos.flush();
                dos.writeUTF(UserName);
                dos.flush();
                dos.writeUTF(PassWord);
                dos.flush();
                //read data from server
                int userId =din.readInt();
                Log.d("userID","from server"+userId);
                return userId+"";
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String userId) {
            this.dialog.dismiss();
            if (Integer.parseInt(userId)>0) {

                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
                RegistrationActivity.this.finish();
            } else
                Toast.makeText(RegistrationActivity.this,
                        "Registration failed.", Toast.LENGTH_SHORT).show();
        }
    }
}

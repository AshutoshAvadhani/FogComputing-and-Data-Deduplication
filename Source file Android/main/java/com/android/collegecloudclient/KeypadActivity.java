package com.android.homecloudclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.homecloudclient.Context.ApplicationContext;


public class KeypadActivity extends Activity implements OnClickListener {
	private ArrayList<Button> buttonList = new ArrayList<Button>();
	private Set<Integer> set = new HashSet<Integer>();
	private Button clearButton;
	private Button deleteButton;
	private EditText pinEditText;
	private Button sendButton;
	private int txnId;
	private final String TAG = KeypadActivity.class.getCanonicalName();
	//private ProgressDialog progressDialog;
	private String pin = "";
	private int userID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keypad_layout);

		userID = ((ApplicationContext)this.getApplicationContext()).getUserId();
		buttonList.add((Button)findViewById(R.id.Button00));
		buttonList.add((Button)findViewById(R.id.Button01));
		buttonList.add((Button)findViewById(R.id.Button02));
		buttonList.add((Button)findViewById(R.id.Button03));
		buttonList.add((Button)findViewById(R.id.Button04));
		buttonList.add((Button)findViewById(R.id.Button05));
		buttonList.add((Button)findViewById(R.id.Button06));
		buttonList.add((Button)findViewById(R.id.Button07));
		buttonList.add((Button)findViewById(R.id.Button08));
		buttonList.add((Button)findViewById(R.id.Button10));

		clearButton = (Button) findViewById(R.id.Button09);
		deleteButton = (Button) findViewById(R.id.Button11);

		sendButton = (Button) findViewById(R.id.keypad_layout_SendButton);
		pinEditText = (EditText) findViewById(R.id.keypad_layout_pinEditText);
		pinEditText.setKeyListener(null);
		for(Button b: buttonList) {
			b.setOnClickListener(this);
		}
		clearButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		sendButton.setOnClickListener(this);
		Random r = new Random();
		int i = 0;
		while(true) {
			int result = r.nextInt(10);
			if(set.add(result)) {
				buttonList.get(i).setText(result+"");
				i++;
			}
			if(set.size() == 10)
				break;
		}
	}

	@Override
	public void onClick(View v) {
		if(v == clearButton) {
			pinEditText.setText("");
		}
		else if(v == deleteButton) {
			if(pinEditText.getText().toString().length() != 0) {
				pinEditText.setText(pinEditText.getText().toString().substring(0, pinEditText.getText().toString().length()-1));
			}
		}
		else if(v == sendButton) {
			pin = pinEditText.getText().toString();
			if(pin != null && pin.length() == 4) {
				PINTask task = new PINTask();
				task.execute();
			}
		} else {
			pinEditText.setText(pinEditText.getText().toString().concat(((Button)v).getText().toString()));
		}
	}


	private class PINTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			//@SuppressWarnings("unused")
			//String response = urls[0];
			try{
				Socket client = new Socket(MainActivity.serverIp, MainActivity.portnumberStr); // connect to the server
				DataOutputStream dos=new DataOutputStream(client.getOutputStream());
				DataInputStream din=new DataInputStream(client.getInputStream());

				dos.writeUTF("9");
				dos.flush();
				dos.writeInt(userID);
				Thread.sleep(100);
				dos.writeUTF(pin);
				dos.flush();

				//Read value coming from server..
				int result = din.readInt();
				Log.d(TAG,"result"+result);
				return result+"";

			}catch(Exception e){
				e.printStackTrace();
			}
			return 0 + "";
		}

		@Override
		protected void onPostExecute(String result) {

			if(Integer.parseInt(result) > 0){
				Intent i=new Intent(KeypadActivity.this,MenuActivity.class);
				startActivity(i);
			}
			else{
				Toast.makeText(KeypadActivity.this, "Invalid PIN......",Toast.LENGTH_SHORT).show();
			}
			pinEditText.setText("");
		}
	}
}

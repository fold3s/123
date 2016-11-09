package com.example.hao.smarthome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Timer;
import java.util.concurrent.DelayQueue;

import javax.net.ssl.HttpsURLConnection;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class Sign_In extends AppCompatActivity {

    TextView clickhere;
    EditText fID, pass;
    String ID, Pass;
    private final String str = "";
    private static final String SPF_NAME = "vidslogin"; //  <--- Add this
    private static final String USERNAME = "username";  //  <--- To save username
    private static final String PASSWORD = "password";  //  <--- To save password
    ProgressDialog pDialog;
    CheckBox checkRemember;
   public static BackGround backGround;


    public void listener(){
        backGround.listener= new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message;
                        String msg =  args[0].toString();
                        try {
                            JSONObject data=new JSONObject(msg);
                            message=data.getString("rcode");
                            if(message.equals("200")){
                                Toast.makeText(getApplicationContext(),"login success !",Toast.LENGTH_LONG).show();
                                Intent i=new Intent(Sign_In.this,MainActivity.class);
                                startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        hideDialog();

                    }
                });

            }
        };

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__in);
        checkRemember=(CheckBox)findViewById(R.id.checkBox);

        backGround=new BackGround();
        fID = (EditText) findViewById(R.id.ID_Edit);
        pass = (EditText) findViewById(R.id.Pass_Edit);
        clickhere = (TextView) findViewById(R.id.sign_up_if_2);
        SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME,
                Context.MODE_PRIVATE);
        fID.setText(loginPreferences.getString(USERNAME, ""));
        pass.setText(loginPreferences.getString(PASSWORD, ""));

        Button signIn = (Button) findViewById(R.id.sign_in);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        backGround.mSocket.connect();
        listener();
        backGround.mSocket.on("RMLOGIN",backGround.listener);

        signIn.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                ID = fID.getText().toString().trim();
                Pass = pass.getText().toString().trim();
                if(!ID.isEmpty() && !Pass.isEmpty()){
                    checkLogin();
                }else{
                    Toast.makeText(getApplicationContext(),"Please fill the information",Toast.LENGTH_LONG).show();
                }
                if(checkRemember.isChecked()){
                    SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
                    loginPreferences.edit().putString(USERNAME, ID).putString(PASSWORD, Pass).commit();
                }
                else{
                    SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
                    loginPreferences.edit().clear().commit();
                }

            }
        });

        clickhere.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Sign_In.this, Sign_Up.class);
                startActivity(intent);
            }
        });
    }
    private void checkLogin(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("userID",ID);
            obj.put("pwd",Pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        pDialog.setMessage("Logging in...");
        showDialog();
        backGround.mSocket.emit("MLOGIN",obj.toString());
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}


package com.example.hao.smarthome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import io.socket.emitter.Emitter;

public class Sign_In extends AppCompatActivity {

    TextView clickhere;
    public static EditText fID, pass;
    public static String ID;
    String Pass;
    private final String str = "";
    private static final String SPF_NAME = "vidslogin"; //  <--- Add this
    private static final String USERNAME = "username";  //  <--- To save username
    private static final String PASSWORD = "password";  //  <--- To save password
    ProgressDialog pDialog;
    CheckBox checkRemember;
   int selector;
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
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                        Toast.makeText(getApplicationContext(),"login success !",Toast.LENGTH_LONG).show();
                                        showSelector();
                                    }
                                }, 2000);

                            }
                            else{
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                        Toast.makeText(getApplicationContext(),"login failed !",Toast.LENGTH_LONG).show();
                                    }
                                }, 2000);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
        pDialog = new ProgressDialog(this,R.style.MyProgressDialog);
        pDialog.setCancelable(true);
        pDialog.setMessage("Autheticating...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setIndeterminateDrawable(this.getResources().getDrawable(R.drawable.custom_progress_dialog_animation));


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
    private void showSelector(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your home");
        if(Sign_In.ID.equals("tuyen")){
            selector=R.array.tuyen;
            builder.setItems(selector, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==0){
                        Intent intent=new Intent(Sign_In.this,Home1.class);
                        startActivity(intent);
                    }
                    else if(which==1){
                        Intent intent=new Intent(Sign_In.this,Home2.class);
                        startActivity(intent);
                    }
                }
            });
        }else if(Sign_In.ID.equals("test1")){
            selector=R.array.test1;
            builder.setItems(selector, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }else if(Sign_In.ID.equals("test2")){
            selector=R.array.test2;
        }else if(Sign_In.ID.equals("test3")){
            selector=R.array.test3;
        }
        builder.show();
    }

}


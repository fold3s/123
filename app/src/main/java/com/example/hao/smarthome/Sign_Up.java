package com.example.hao.smarthome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import javax.net.ssl.HttpsURLConnection;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class Sign_Up extends AppCompatActivity {

    TextView clickhere;
    EditText fID, fpass, femail;
    String id, pass, email;
    private ProgressDialog pDialog;
    private final String str = "";

    private Sign_In sign_in;
    public Emitter.Listener onReg = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Sign_Up.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message;
                    String msg =  args[0].toString();
                    try {
                        JSONObject data=new JSONObject(msg);
                        message=data.getString("rcode");
                        if(message.equals("200")){
                            Toast.makeText(getApplicationContext(),"Signup Success !",Toast.LENGTH_LONG).show();
                            Intent i=new Intent(Sign_Up.this,MainActivity.class);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        sign_in = new Sign_In();

        //sign_in.mSocket.connect();
        clickhere = (TextView) findViewById(R.id.Sign_in_if);
        fID = (EditText) findViewById(R.id.ID_Edit_su);
        femail = (EditText) findViewById(R.id.Email_Edit_su);
        fpass = (EditText) findViewById(R.id.Pass_Edit_su);

        Button signUp = (Button) findViewById(R.id.sign_up);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //sign_in.mSocket.on("RMREG", onReg);


        signUp.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                id = fID.getText().toString().trim();
                pass = fpass.getText().toString().trim();
                email = femail.getText().toString().trim();

                if (!id.isEmpty() && !pass.isEmpty() && !email.isEmpty()) {
                    checkReg();
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill the information !", Toast.LENGTH_LONG).show();
                }
            }
        });
        clickhere.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Sign_Up.this, Sign_In.class);
                startActivity(intent);
            }
        });
    }
    private void checkReg(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("userID",id);
            obj.put("pwd",pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        pDialog.setMessage("Regustering User...");
        showDialog();
        //sign_in.mSocket.emit("MREG",obj.toString());
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()){
            pDialog.dismiss();
            pDialog.cancel();
        }
    }
}



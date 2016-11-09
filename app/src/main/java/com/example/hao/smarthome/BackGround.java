package com.example.hao.smarthome;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class BackGround extends AppCompatActivity {



    public static io.socket.client.Socket mSocket;
    {
        try{
            mSocket= IO.socket("http://103.232.120.193:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public static Emitter.Listener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_ground);


        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(BackGround.this, Sign_Up.class);
                startActivity(intent);
            }
        });

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(BackGround.this, Sign_In.class);
                startActivity(intent);
            }
        });
        ImageView img=(ImageView)findViewById(R.id.imageView2);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) img.getLayoutParams();
        params.width = 120;
// existing height is ok as is, no need to edit it
        img.setLayoutParams(params);
    }
}

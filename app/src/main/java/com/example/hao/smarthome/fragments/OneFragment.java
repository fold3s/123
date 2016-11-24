package com.example.hao.smarthome.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hao.smarthome.BackGround;
import com.example.hao.smarthome.History;
import com.example.hao.smarthome.MainActivity;
import com.example.hao.smarthome.R;
import com.example.hao.smarthome.Sign_In;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

import static android.content.Context.*;


public class OneFragment extends Fragment {
    public static ImageView fan,led;
    public static boolean fan_flag,led_flag;
    private Sign_In sign_in;
    private BackGround backGround;
    public String a;




    private Emitter.Listener onSetNode = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message;
                    try {
                        JSONObject data=new JSONObject((String)args[0]);
                        message=data.getString("rcode");
                        Log.d("receive",data.toString());
                        if(message.equals("200")){
                            Toast.makeText(getActivity(),data.toString(),Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    };
    private Emitter.Listener onCheck= new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message,device,status;
                    try{
                        JSONObject data=new JSONObject((String)args[0]);
                        message=data.getString("rcode");
                        device=data.getString("nodeCode");
                        status=data.getString("status");
                        if(message.equals("200")){
                            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                            if(device.equals("1H1")){
                                if(status.equals("1")){
                                    fan.setImageResource(R.mipmap.fan_on);
                                    fan_flag=true;
                                }
                                else if(status.equals("0")){
                                    fan.setImageResource(R.mipmap.fan_off);
                                    fan_flag=false;
                                }
                            }
                            else if(device.equals("1H2")){
                                if(status.equals("1")){
                                    led.setImageResource(R.mipmap.led_on);
                                    led_flag=true;
                                } else if (status.equals("0")) {
                                    led.setImageResource(R.mipmap.led_off);
                                    led_flag=false;
                                }
                            }
                        }
                        else {
                            Toast.makeText(getActivity(),"Error Occured",Toast.LENGTH_LONG).show();
                        }
                        Log.d("check",data.toString());
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        backGround = new BackGround();
        a="Hello";
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        fan=(ImageView)rootView.findViewById(R.id.fan);
        led=(ImageView)rootView.findViewById(R.id.led);
        BackGround.mSocket.on("RMNODE",onCheck);
        BackGround.mSocket.on("RMSETNODE",onSetNode);
        BackGround.mSocket.on("SYNC",BackGround.listener);
        BackGround.mSocket.connect();
        checknode("H1","1H1");
        checknode("H1","1H2");

        fan.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fan_flag==true){
                    fan.setImageResource(R.mipmap.fan_off);
                    fan_flag=false;
                    setnode("H1","1H1","0");

                }
                else if(fan_flag==false){
                    fan.setImageResource(R.mipmap.fan_on);
                    fan_flag=true;
                    setnode("H1","1H1","1");
                }
            }
        });
        led.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(led_flag==true){
                    led.setImageResource(R.mipmap.led_off);
                    led_flag=false;
                    setnode("H1","1H2","0");
                }
                else if(led_flag==false){
                    led.setImageResource(R.mipmap.led_on);
                    led_flag=true;
                    setnode("H1","1H2","1");
                }
            }
        });
        return rootView;

    }
    private void setnode(String homeCode,String nodeCode,String status){
        JSONObject obj = new JSONObject();
        try {
            obj.put("title","@MSETNODE");
            obj.put("userID","tuyen");
            obj.put("homeCode",homeCode);
            obj.put("nodeCode",nodeCode);
            obj.put("status",status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("error","here");
        Log.d("error",obj.toString());

        BackGround.mSocket.emit("MSETNODE",obj.toString());
    }
    private void checknode(String homeCode,String nodeCode){
        JSONObject obj=new JSONObject();
        try{
            obj.put("title","@MNODE");
            obj.put("homeCode",homeCode);
            obj.put("nodeCode",nodeCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BackGround.mSocket.emit("MNODE",obj.toString());
    }
}
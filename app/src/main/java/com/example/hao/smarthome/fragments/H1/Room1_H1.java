package com.example.hao.smarthome.fragments.H1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hao.smarthome.BackGround;
import com.example.hao.smarthome.Home1;
import com.example.hao.smarthome.R;
import com.example.hao.smarthome.Sign_In;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;


public class Room1_H1 extends Fragment {
    public static ImageView fan;
    public static boolean fan_flag,fan_permission;
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
    private Emitter.Listener onGetAllNode = new Emitter.Listener(){

        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data=new JSONObject((String)args[0]);
                        JSONArray device=data.getJSONArray("device");
                        for(int i=0;i<device.length();i++){
                            JSONObject getD=device.getJSONObject(i);
                            String DID=getD.getString("DID");
                            if(DID.equals("1H1")){
                                fan_permission=true;
                                break;
                            }
                            else{
                                fan_permission=false;
                            }

                        }
                        Log.d("ALL",data.toString());
                        Log.d("device",device.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    public Room1_H1() {
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

        View rootView = inflater.inflate(R.layout.room1_h1, container, false);
        fan=(ImageView)rootView.findViewById(R.id.fan);
        BackGround.mSocket.on("RMNODE",onCheck);
        BackGround.mSocket.on("RMSETNODE",onSetNode);
        BackGround.mSocket.on("RMGETNODE",onGetAllNode);
        BackGround.mSocket.connect();
        checknode("H1","1H1");
        getallnode(Sign_In.ID);

        fan.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fan_permission==true){
                    withPermission();
                }
                else if(fan_permission==false){
                    withoutPermission();
                }
            }
        });
        return rootView;

    }
    private void setnode(String homeCode,String nodeCode,String status){
        JSONObject obj = new JSONObject();
        try {
            obj.put("title","@MSETNODE");
            obj.put("userID",Sign_In.ID);
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
    private void getallnode(String userID){
        JSONObject obj=new JSONObject();
        try{
            obj.put("title","@MGETNODE");
            obj.put("userID",userID);
        }catch(JSONException e){
            e.printStackTrace();
        }
        BackGround.mSocket.emit("MGETNODE",obj.toString());
    }
    public void withPermission(){
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
    public void withoutPermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("PERMISSION ?");
        builder.setMessage("You do NOT have a permission to controll this device...");
        builder.show();
    }
}
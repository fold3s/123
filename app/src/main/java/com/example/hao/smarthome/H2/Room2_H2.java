package com.example.hao.smarthome.H2;

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


public class Room2_H2 extends Fragment {
    public static ImageView fan_2;
    public static boolean fan_2_flag,fan_2_permission;



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
                            if(device.equals("2H2")){
                                if(status.equals("1")){
                                    fan_2.setImageResource(R.mipmap.fan_on);
                                    fan_2_flag=true;
                                }
                                else if(status.equals("0")){
                                    fan_2.setImageResource(R.mipmap.fan_off);
                                    fan_2_flag=false;
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
                            if(DID.equals("2H2")){
                                fan_2_permission=true;
                                break;
                            }
                            else{
                                fan_2_permission=false;
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
    public Room2_H2() {
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
        View rootView = inflater.inflate(R.layout.room2_h2, container, false);
        fan_2=(ImageView)rootView.findViewById(R.id.fan_2);
        BackGround.mSocket.on("RMNODE",onCheck);
        BackGround.mSocket.on("RMSETNODE",onSetNode);
        BackGround.mSocket.on("RMGETNODE",onGetAllNode);
        BackGround.mSocket.connect();
        checknode("H2","2H2");
        getallnode(Sign_In.ID);

        fan_2.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fan_2_permission==true){
                    withPermission();
                }
                else if(fan_2_permission==false){
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
        if(fan_2_flag==true){
            fan_2.setImageResource(R.mipmap.fan_off);
            fan_2_flag=false;
            setnode("H2","2H2","0");

        }
        else if(fan_2_flag==false){
            fan_2.setImageResource(R.mipmap.fan_on);
            fan_2_flag=true;
            setnode("H2","2H2","1");
        }
    }
    public void withoutPermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("PERMISSION ?");
        builder.setMessage("You do NOT have a permission to controll this device...");
        builder.show();
    }
}
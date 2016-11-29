package com.example.hao.smarthome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by citee on 11/29/2016.
 */
public class HomeSelector extends DialogFragment {

    int selector;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your home");
        if(Sign_In.ID.equals("tuyen")){
            selector=R.array.tuyen;
            builder.setItems(selector, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==0){
                        Intent intent=new Intent(getActivity(),Home1.class);
                        startActivity(intent);
                    }
                    else if(which==1){
                        Intent intent=new Intent(getActivity(),Home1.class);
                        startActivity(intent);
                    }
                }
            });
        }else if(Sign_In.ID.equals("test1")){
            selector=R.array.test1;
        }else if(Sign_In.ID.equals("test2")){
            selector=R.array.test2;
        }else if(Sign_In.ID.equals("test3")){
            selector=R.array.test3;
        }
        return builder.create();

    }
}

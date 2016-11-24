package com.example.hao.smarthome;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashSreen extends AppCompatActivity {

    public void onAttachedToWindow(){
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /*Call the start-up Activity*/
    Thread splashThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sreen);
        StartAnimation();
    }
    private void StartAnimation(){
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout)findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim=AnimationUtils.loadAnimation(this,R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView)findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashThread = new Thread(){
            @Override
            public void run(){
                try{
                    int waited=0;
                    //SplashScreen delay time
                    while(waited<3500){
                        sleep(100);
                        waited+=100;
                    }
                    Intent i=new Intent(SplashSreen.this,BackGround.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    SplashSreen.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    SplashSreen.this.finish();
                }
            }
        };
        splashThread.start();
    }

}

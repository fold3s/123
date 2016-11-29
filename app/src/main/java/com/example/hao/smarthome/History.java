package com.example.hao.smarthome;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.socket.emitter.Emitter;

public class History extends AppCompatActivity {

    public static RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public TextView Date;
    public static ArrayList<String> myDataset;
    public static ArrayList<Integer> myIconset;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    private Emitter.Listener OnSyncH = new Emitter.Listener(){

        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("SYNC","History");
                }
            });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvRoom1);


        Date today=new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat= new SimpleDateFormat("dd/MM/yyyy");
        String s=timeFormat.format(today.getTime());
        Date = (TextView)findViewById(R.id.txtView);
        Date.setText(s);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myDataset = new ArrayList<>();
        myIconset = new ArrayList<>();
        mAdapter = new MyAdapter(myDataset,myIconset);
        mRecyclerView.setAdapter(mAdapter);



        /**
         *Setup the DrawerLayout and NavigationView
         */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment_H1 as the first Fragment
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //  mDrawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.logout) {
                    Intent intent = new Intent(History.this, Sign_In.class);
                    startActivity(intent);
                    //FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    // fragmentTransaction.replace(R.id.containerView,new SentFragment()).commit();

                }else if(menuItem.getItemId()==R.id.home){
                    Intent intent = new Intent(History.this,Home1.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.history) {
                    Intent intent = new Intent(History.this, History.class);
                    startActivity(intent);
                } /*else if (menuItem.getItemId() == R.id.help) {
                    Intent intent = new Intent(Home1.this, About.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.setting) {
                    Intent intent = new Intent(Home1.this, Setting.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.automode) {
                    Intent intent = new Intent(Home1.this, Mode.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.setting) {
                    Intent intent = new Intent(Home1.this, Setting.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.permissions) {
                    Intent intent = new Intent(Home1.this, Permission.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.Control) {
                    Intent intent = new Intent(Home1.this, Home1.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.user) {
                    Intent intent = new Intent(Home1.this, Account.class);
                    startActivity(intent);
                }*/


                return false;
            }

        });
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);



        mDrawerToggle.syncState();





    }



}

package com.cst2335.soccerapi;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((item)-> {
            onOptionsItemSelected(item);
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });


        ImageView ImageNews = findViewById(R.id.soccerMain);
        ImageNews.setOnClickListener(clk->{
            Intent nextPage = new Intent(MainActivity.this, SoccerMain.class);
            startActivity(nextPage);
        });


        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );
        Log.d( TAG, "Message");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        String message = null;
        switch (item.getItemId()){
            case R.id.goSoccer:
                message = "Soccer Page";
                startActivity(new Intent(MainActivity.this, SoccerMain.class));
                break;
            case R.id.goMovie:
                message= "Movie Page";
             //   startActivity(new Intent(MainActivity.this, MovieActivity.class));
                break;
            case R.id.help:
                message = getResources().getString(R.string.helpPage);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getResources().getString(R.string.help))
                        .setMessage(getResources().getString(R.string.helpPage))
                        .create().show();
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message = null;
        switch (item.getItemId()) {

            case R.id.goSoccer:
                message= getResources().getString(R.string.soccerPage);
                startActivity(new Intent(MainActivity.this, SoccerMain.class));
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        Toast.makeText(this, getResources().getString(R.string.NavigationDrawer)+message, Toast.LENGTH_LONG).show();
        return false;
    }

}
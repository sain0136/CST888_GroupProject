package com.cst2335.soccerapi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;



public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_activity);

        Bundle dataToPass = getIntent().getExtras();

        SFragment soccerFragment = new SFragment();
        soccerFragment.setArguments( dataToPass );
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.soccerFragmentLocation, soccerFragment)
                .commit();
    }
}
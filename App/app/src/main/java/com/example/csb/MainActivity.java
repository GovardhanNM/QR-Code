package com.example.csb;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        new Handler(  ).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity( new Intent( MainActivity.this,HomeActivity.class ) );
                finish();
            }
        } ,SPLASH_TIME);
    }
}

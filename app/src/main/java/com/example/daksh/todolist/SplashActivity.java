package com.example.daksh.todolist;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        /** Duration of wait **/


            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {

                    Intent i = new Intent(SplashActivity.this,MainActivity.class);
                    SplashActivity.this.startActivity(i);
                    SplashActivity.this.finish();

                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }


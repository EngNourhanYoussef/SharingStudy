package com.example.lap.sharingstudy;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread t = new Thread() {
            public void run() {

                try {
                    sleep(2000);

                    Intent intent = new Intent(SplashScreen.this, Register.class);
                    startActivity(intent);

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();
    }
}

package com.example.hppc.tsf_social_media_integration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    View v1,v2,v3,v4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v1=findViewById(R.id.divider511);
        v2=findViewById(R.id.divider5111);
        v3=findViewById(R.id.divider51);
        v4=findViewById(R.id.divider5);
        Handler h=new Handler();
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        v1.startAnimation(animation1);
        v2.startAnimation(animation1);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(MainActivity.this,Login.class);
                startActivity(i);
                finish();




            }
        },2700);

    }
}

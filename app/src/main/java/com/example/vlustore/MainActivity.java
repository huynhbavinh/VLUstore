package com.example.vlustore;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;
    RelativeLayout main_scr;
    LinearLayout linear;
    ImageView img_logo, img_name, img_sologan;
    Animation topAnim, bottomAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.botton_animation);

        //get components
        main_scr = findViewById(R.id.main_activity);
        img_logo = findViewById(R.id.img_splash_scr_logo);
        img_name = findViewById(R.id.img_splash_scr_name);
        img_sologan = findViewById(R.id.img_splash_scr_sologan);
        linear = findViewById(R.id.ln_splash_scr_logo);

        linear.setAnimation(topAnim);
        img_logo.setAnimation(topAnim);
        img_name.setAnimation(topAnim);
        img_sologan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(img_logo,"logo_image");
                pairs[1] = new Pair<View,String>(img_name,"logo_name");
                
                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(intent,options.toBundle());
                finish();
            }
        },SPLASH_SCREEN);
    }
}
package com.example.android.plannertracker.TripDetails;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.plannertracker.MainActivity;
import com.example.android.plannertracker.R;

public class Splash extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private  ImageView imageView2;
    private int width ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView = findViewById(R.id.splashText);
        imageView = findViewById(R.id.splashPic);
        imageView2= findViewById(R.id.logo);
        Animation myanim  = AnimationUtils.loadAnimation(this,R.anim.mytransition);

        getWidth();
        TranslateAnimation animation = new TranslateAnimation(-200, width, 100, 100);
        animation.setDuration(3000);
        animation.setFillAfter(false);
        imageView.startAnimation(animation);
        textView.startAnimation(myanim);
        imageView2.startAnimation(myanim);

       final Intent i = new Intent(this, MainActivity.class);
        Thread timer = new Thread()
        {
            public  void run()
            {
                try
                {
                    sleep(3000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();
    }

    public void getWidth()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
         width = size.x;

    }
}

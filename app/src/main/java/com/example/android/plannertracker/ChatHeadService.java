package com.example.android.plannertracker;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.android.plannertracker.TripDetails.AddNote;

public class ChatHeadService extends Service {
    private WindowManager windowManager;
    private View chatHeadView;
    ImageView closeChatHead,chatHeadImage;

    public ChatHeadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        chatHeadView = LayoutInflater.from(this).inflate(R.layout.chat_head,null);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y=100;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(chatHeadView,params);

        closeChatHead = chatHeadView.findViewById(R.id.closeBtn);
        closeChatHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });

        chatHeadImage = chatHeadView.findViewById(R.id.chatHead);
        chatHeadImage.setOnTouchListener(new View.OnTouchListener() {
            private int lastAction;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //take initial Position :
                        initialX = params.x;
                        initialY = params.y;
                        //get Touch location:
                        initialTouchY = motionEvent.getRawY();
                        initialTouchX = motionEvent.getRawX();

                        lastAction = motionEvent.getAction();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        params.x = (int) (initialX + motionEvent.getRawX() - initialTouchX);
                        params.y = (int) (initialY + motionEvent.getRawY() - initialTouchY);

                        windowManager.updateViewLayout(chatHeadView,params);
                        lastAction = motionEvent.getAction();
                        return true;
                }
                return  false;
            }
        });
        chatHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatHeadService.this,AddNote.class));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHeadView != null){
            windowManager.removeView(chatHeadView);
        }
    }
}

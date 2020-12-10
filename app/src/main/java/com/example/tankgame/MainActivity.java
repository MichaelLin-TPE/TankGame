package com.example.tankgame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import static com.example.tankgame.MapView.DOWN;
import static com.example.tankgame.MapView.LEFT;
import static com.example.tankgame.MapView.RIGHT;
import static com.example.tankgame.MapView.TOP;

public class MainActivity extends AppCompatActivity {

    private boolean isTop,isDown,isLeft,isRight;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final MapView mapView = findViewById(R.id.map);
        mapView.setWillNotDraw(false);

        final Button btnUp,btnDown,btnLeft,btnRight,btnFire;

        btnUp = findViewById(R.id.btn_up);
        btnDown = findViewById(R.id.btn_down);
        btnLeft = findViewById(R.id.btn_left);
        btnRight = findViewById(R.id.btn_right);
        btnFire = findViewById(R.id.btn_fire);

        btnFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.onButtonFireClickListener();
            }
        });

        final GestureDetector detector = new GestureDetector(new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Log.i("Michael","按下了 down");

                if (isDown){
                    mapView.onButtonDownLongPressListener();
                    return false;
                }
                if (isTop){
                    mapView.onButtonUpLongPressListener();
                    return false;
                }
                if (isLeft){
                    mapView.onButtonLeftLongPressListener();
                    return false;
                }
                if (isRight){
                    mapView.onButtonRightLongPressListener();
                }

                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                Log.i("Michael","按下了 showPress");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.i("Michael","按下了 SingleTapUp");

//                if (isDown){
//                    mapView.onButtonDownClickListener();
//                    return false;
//                }
//                if (isTop){
//                    mapView.onButtonUpClickListener();
//                    return false;
//                }
//                if (isLeft){
//                    mapView.onButtonLeftClickListener();
//                    return false;
//                }
//                if (isRight){
//                    mapView.onButtonRightClickListener();
//                    return false;
//                }

                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.i("Michael","按下了 Long Press");



            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        btnUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    mapView.removeHandler();
                }
                checkStatus(TOP);
                return detector.onTouchEvent(event);
            }
        });

        btnDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    mapView.removeHandler();
                }

                checkStatus(DOWN);
                return detector.onTouchEvent(event);
            }
        });

        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    mapView.removeHandler();
                }
                checkStatus(LEFT);
                return detector.onTouchEvent(event);
            }
        });

        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    mapView.removeHandler();
                }
                checkStatus(RIGHT);
                return detector.onTouchEvent(event);
            }
        });
        mapView.startEnemyTankPath();

    }

    private void checkStatus(String status) {
        switch (status){
            case TOP:
                isTop = true;
                isLeft = false;
                isRight = false;
                isDown = false;
                break;
            case DOWN:
                isTop = false;
                isLeft = false;
                isRight = false;
                isDown = true;
                break;
            case LEFT:
                isTop = false;
                isLeft = true;
                isRight = false;
                isDown = false;
                break;
            case RIGHT:
                isTop = false;
                isLeft = false;
                isRight = true;
                isDown = false;
        }
    }


}
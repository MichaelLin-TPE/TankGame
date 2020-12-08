package com.example.tankgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MapView extends ConstraintLayout {

    private float maxRight, maxBottom, middleX, middleY,currentTankX , currentTankY ,moveTankX,moveTankY;


    private boolean isLeft,isRight,isTop,isDown;

    public static final String LEFT = "left";

    public static final String RIGHT = "right";

    public static final String TOP = "top";

    public static final String DOWN = "down";

    private Paint paint;

    private View view;

    private ImageView ivTank,ivUserFire;

    private ConstraintLayout rootView;

    private TranslateAnimation translateAnimation;

    private Handler handler  = new Handler(Looper.getMainLooper());

    public MapView(@NonNull Context context) {
        super(context);
        initView();
    }

    public MapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    public MapView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {

        view = View.inflate(getContext(), R.layout.map_layout, this);
        rootView = view.findViewById(R.id.root_view);
        maxRight = view.getRight();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("Michael", "onDraw");



        Log.i("Michael", "initView left : " + view.getLeft() + " , right : " + view.getRight() + " , top : " + view.getTop() + " , bottom : " + view.getBottom());
        middleX = (float) view.getRight() / 2;
        middleY = (float) view.getBottom() / 2;
        maxBottom = (float) view.getBottom();
        maxRight = (float) view.getRight();

        float leftUpY = middleY - 100;

        float rightUpY = middleY - 100;

        float leftUpX = middleX - 100;

        float rightUpX = middleX + 100;

        float leftDownX = middleX - 100;

        float rightDownX = middleX + 100;

        float leftDownY = middleY + 100;

        paint = new Paint();
        paint.setStrokeWidth(5f);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        //左邊的線
        path.moveTo(0f, leftUpY);
        path.lineTo(leftUpX, leftUpY);
        path.lineTo(leftUpX, 0f);

        //右邊邊的線
        path.moveTo(maxRight, rightUpY);
        path.lineTo(rightUpX, rightUpY);
        path.lineTo(rightUpX, 0f);


        //左下的線
        path.moveTo(leftDownX, maxBottom);
        path.lineTo(leftDownX, leftDownY);
        path.lineTo(0, leftDownY);

        //右下的線
        path.moveTo(rightDownX, maxBottom);
        path.lineTo(rightDownX, leftDownY);
        path.lineTo(maxRight, leftDownY);

        canvas.drawPath(path, paint);

        rootView.removeAllViews();

        ivTank = new ImageView(getContext());
        ivUserFire = new ImageView(getContext());
        rootView.addView(ivUserFire);
        rootView.addView(ivTank);
        ivUserFire.getLayoutParams().width = DpConvertTool.getInstance().getDb(5);

        ivUserFire.getLayoutParams().height = DpConvertTool.getInstance().getDb(5);

        ivTank.getLayoutParams().width = DpConvertTool.getInstance().getDb(50);

        ivTank.getLayoutParams().height = DpConvertTool.getInstance().getDb(50);
        ivTank.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //先隱藏起來
        ivUserFire.setBackgroundResource(R.drawable.user_fire_shape);
        ivUserFire.setVisibility(INVISIBLE);
        currentTankX = 50f;
        currentTankY = middleY - 80 ;
        moveTankX = currentTankX;
        moveTankY = currentTankY;

        ivTank.setX(currentTankX);
        ivTank.setY(currentTankY);
        ivTank.setImageResource(R.drawable.tank);
        ivTank.setRotation(90f);
//        translateAnimation = new TranslateAnimation(0f,10f,0,0f);
//        translateAnimation.setDuration(1000);
//        translateAnimation.setFillAfter(true);
//        ivTank.startAnimation(translateAnimation);
    }

    //按往上的時候
    public void onButtonUpClickListener() {
        moveTop();
    }

    //按往下的時候
    public void onButtonDownClickListener() {
        moveDown();
    }
    //按往左的時候
    public void onButtonLeftClickListener() {
        moveLeft();
    }
    //按往右的時候
    public void onButtonRightClickListener() {
        moveRight();
    }

    //長按系列
    public void onButtonDownLongPressListener() {
        handler.postDelayed(downLongRunnable,200);
    }

    private Runnable downLongRunnable = new Runnable() {
        @Override
        public void run() {
            moveDown();
            handler.postDelayed(this,200);

        }
    };

    public void onButtonUpLongPressListener() {
        handler.postDelayed(upLongRunnable,200);
    }

    private Runnable upLongRunnable = new Runnable() {
        @Override
        public void run() {
            moveTop();
            handler.postDelayed(this,200);
        }
    };

    public void onButtonLeftLongPressListener() {
       handler.postDelayed(leftLongRunnable,200);
    }

    private Runnable leftLongRunnable = new Runnable() {
        @Override
        public void run() {
            moveLeft();
            handler.postDelayed(this,200);
        }
    };

    public void onButtonRightLongPressListener() {
        handler.postDelayed(rightLongRunnable,200);
    }

    private Runnable rightLongRunnable = new Runnable() {
        @Override
        public void run() {
            moveRight();
            handler.postDelayed(this,200);
        }
    };

    private void moveDown(){
        ivTank.setImageResource(R.drawable.tank);
        ivTank.setRotation(180f);

        moveTankY += 20;
        if (moveTankY >= maxBottom){
            moveTankY = currentTankY;
        }

        currentTankY = moveTankY;
        checkStatus(DOWN);
        setImageLocation();
    }

    private void moveTop(){
        ivTank.setImageResource(R.drawable.tank);
        ivTank.setRotation(0f);
        moveTankY -= 20;
        if (moveTankY <= 0){
            moveTankY = currentTankY;
        }
        currentTankY = moveTankY;
        checkStatus(TOP);
        setImageLocation();
    }

    private void moveLeft(){
        ivTank.setImageResource(R.drawable.tank);
        ivTank.setRotation(-90f);

        moveTankX -= 20;
        if (moveTankX <= 0){
            moveTankX = currentTankX;
        }

        currentTankX = moveTankX;
        checkStatus(LEFT);
        setImageLocation();
    }

    private void moveRight(){
        ivTank.setImageResource(R.drawable.tank);
        ivTank.setRotation(90f);
        moveTankX += 20;
        Log.i("Michael","x : "+moveTankX + " , maxX : "+maxRight);
        if (moveTankX >= maxRight){
            moveTankX = currentTankX;
        }
        currentTankX = moveTankX;
        checkStatus(RIGHT);
        setImageLocation();
    }


    private void setImageLocation() {
        ivTank.setX(currentTankX);
        ivTank.setY(currentTankY);
        ivUserFire.setX(currentTankX);
        ivUserFire.setY(currentTankY);
    }
    //按開火的時候
    public void onButtonFireClickListener() {
        ivUserFire.setVisibility(VISIBLE);
        int[] location = new int[2];
        ivTank.getLocationOnScreen(location);

        ivUserFire.setY(location[1]);
        ivUserFire.setX(currentTankX);

        if (isRight){
            TranslateAnimation translateAnimation = new TranslateAnimation(0f,100f,0f,0f);
            translateAnimation.setDuration(1000);
            translateAnimation.setFillAfter(true);
            ivUserFire.startAnimation(translateAnimation);
            return;
        }


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


    public void removeHandler() {
        handler.removeCallbacks(downLongRunnable);
        handler.removeCallbacks(upLongRunnable);
        handler.removeCallbacks(leftLongRunnable);
        handler.removeCallbacks(rightLongRunnable);
    }
}

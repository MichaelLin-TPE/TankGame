package com.example.tankgame;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Canvas;

import android.graphics.Paint;

import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MapView extends ConstraintLayout {

    private float maxRight, maxBottom, middleX, middleY, currentTankX, currentTankY, moveTankX, moveTankY, bulletY, bulletX;

    private float currentEnemyTankX, currentEnemyTankY, moveEnemyTankX, moveEnemyTankY, enemyBulletY, enemyBulletX;

    private boolean isLeft, isRight, isTop, isDown, isUserBulletComing, isEnemyLeft, isEnemyRight, isEnemyTop, isEnemyDown;

    public static final String LEFT = "left";

    public static final String RIGHT = "right";

    public static final String TOP = "top";

    public static final String DOWN = "down";

    public static final int MOVE_SPEED = 50;

    public static final int BULLET_SPEED = 20;

    public static final int FIRE_SPEED = 2000;

    private static final int ENEMY_TOP = 0;

    private static final int ENEMY_DOWN = 1;

    private static final int ENEMY_LEFT = 2;

    private static final int ENEMY_RIGHT = 3;

    private int secondCount = 0;

    private Paint paint;

    private View view;

    private ImageView ivTank, ivUserFire, ivEnemyTank, ivExplosion, ivEnemyFire;

    private ConstraintLayout rootView;

    private Handler handler = new Handler(Looper.getMainLooper());

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
        rootView.removeAllViews();

        //設置 View出現需要出現的東西
        ivTank = new ImageView(getContext());
        ivUserFire = new ImageView(getContext());
        ivEnemyTank = new ImageView(getContext());
        ivExplosion = new ImageView(getContext());
        ivEnemyFire = new ImageView(getContext());
        rootView.addView(ivExplosion);
        rootView.addView(ivEnemyTank);
        rootView.addView(ivTank);
        rootView.addView(ivUserFire);
        rootView.addView(ivEnemyFire);
        //爆炸效果設置
        ivExplosion.getLayoutParams().width = DpConvertTool.getInstance().getDb(40);
        ivExplosion.getLayoutParams().height = DpConvertTool.getInstance().getDb(40);
        ivExplosion.setImageResource(R.drawable.explosion);
        ivExplosion.setVisibility(INVISIBLE);

        //敵人的子彈
        ivEnemyFire.getLayoutParams().width = DpConvertTool.getInstance().getDb(5);
        ivEnemyFire.getLayoutParams().height = DpConvertTool.getInstance().getDb(5);
        ivEnemyFire.setBackgroundResource(R.drawable.user_fire_shape);
        ivUserFire.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ivEnemyFire.setVisibility(INVISIBLE);

        //自己的子彈
        ivUserFire.getLayoutParams().width = DpConvertTool.getInstance().getDb(5);
        ivUserFire.getLayoutParams().height = DpConvertTool.getInstance().getDb(5);
        ivUserFire.setBackgroundResource(R.drawable.user_fire_shape);
        ivUserFire.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ivUserFire.setVisibility(INVISIBLE);

        //自己坦克設定高寬
        ivTank.getLayoutParams().width = DpConvertTool.getInstance().getDb(50);
        ivTank.getLayoutParams().height = DpConvertTool.getInstance().getDb(50);
        ivTank.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //敵人坦克設定高寬
        ivEnemyTank.getLayoutParams().width = DpConvertTool.getInstance().getDb(50);
        ivEnemyTank.getLayoutParams().height = DpConvertTool.getInstance().getDb(50);
        ivEnemyTank.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

        //先去掉線

//        float leftUpY = middleY - 100;
//
//        float rightUpY = middleY - 100;
//
//        float leftUpX = middleX - 100;
//
//        float rightUpX = middleX + 100;
//
//        float leftDownX = middleX - 100;
//
//        float rightDownX = middleX + 100;
//
//        float leftDownY = middleY + 100;
//
//        paint = new Paint();
//        paint.setStrokeWidth(5f);
//        paint.setColor(Color.GRAY);
//        paint.setStyle(Paint.Style.STROKE);
//        Path path = new Path();
//        //左邊的線
//        path.moveTo(0f, leftUpY);
//        path.lineTo(leftUpX, leftUpY);
//        path.lineTo(leftUpX, 0f);
//
//        //右邊邊的線
//        path.moveTo(maxRight, rightUpY);
//        path.lineTo(rightUpX, rightUpY);
//        path.lineTo(rightUpX, 0f);
//
//
//        //左下的線
//        path.moveTo(leftDownX, maxBottom);
//        path.lineTo(leftDownX, leftDownY);
//        path.lineTo(0, leftDownY);
//
//        //右下的線
//        path.moveTo(rightDownX, maxBottom);
//        path.lineTo(rightDownX, leftDownY);
//        path.lineTo(maxRight, leftDownY);
//
//        canvas.drawPath(path, paint);

        //敵人坦克的位置
        Log.i("Michael", "enemyTank width : " + ivEnemyTank.getWidth());
        currentEnemyTankX = maxRight - ivEnemyTank.getWidth();
        currentEnemyTankY = 0 + 80f;
        moveEnemyTankX = currentEnemyTankX;
        moveEnemyTankY = currentEnemyTankY;
        ivEnemyTank.setX(currentEnemyTankX);
        ivEnemyTank.setY(currentEnemyTankY);
        ivEnemyTank.setImageResource(R.drawable.tank);
        ivEnemyTank.setRotation(-90f);


        //自己坦克的位置
        currentTankX = 50f;
        currentTankY = middleY - 80;
        moveTankX = currentTankX;
        moveTankY = currentTankY;
        ivTank.setX(currentTankX);
        ivTank.setY(currentTankY);
        ivTank.setImageResource(R.drawable.tank);
        ivTank.setRotation(90f);

    }

    //自定義敵人坦克的路徑
    public void startEnemyTankPath() {

        //PK mode
        handler.postDelayed(pkModeTankRunnable, 2000);
        //自定義路線

        randomChosePath();

        //子彈來襲進行閃躲
//        dodgeBullet();
    }


    //這個還有很大的問題
    private void dodgeBullet() {
        handler.postDelayed(checkBulletClosingRunnable, 50);
    }

    private Runnable checkBulletClosingRunnable = new Runnable() {
        @Override
        public void run() {
            //子彈來襲進行閃躲
            if (!isUserBulletComing){
                handler.post(this);
                return;
            }
            boolean isSameY = Math.abs(bulletY - currentEnemyTankY) <= ivEnemyTank.getHeight() * 2;

            boolean isSameX = Math.abs(bulletX - currentEnemyTankX) <= ivEnemyTank.getHeight() * 2;

            Log.i("Michael", "是否碰到Ｙ：" + isSameY + " , 是否碰到Ｘ：" + isSameX);


            if (isEnemyLeft) {

                if (isSameX){
                    doCheckTurnRightOrLeft();
                    return;
                }

                if (isSameY) {
                    handler.removeCallbacks(enemyTankLeftPathRunnable);
                    moveEnemyTop();
                    isUserBulletComing = false;
                    handler.removeCallbacks(this);
                    return;
                }
                //
                handler.postDelayed(this, 50);
                return;
            }

            if (isEnemyRight) {

                if (isSameX){
                    doCheckTurnRightOrLeft();
                    return;
                }

                if (isSameY) {
                    handler.removeCallbacks(enemyTankRightPathRunnable);
                    moveEnemyDown();
                    isUserBulletComing = false;
                    handler.removeCallbacks(this);
                    return;
                }

                handler.postDelayed(this, 50);
                return;
            }

            if (isEnemyTop) {

                if (isSameX){
                    doCheckTurnRightOrLeft();
                    return;
                }


                if (isSameY) {
                    handler.removeCallbacks(enemyTankTopPathRunnable);
                    moveEnemyDown();
                    isUserBulletComing = false;
                    handler.removeCallbacks(this);
                    return;
                }

                handler.postDelayed(this, 50);
                return;
            }

            if (isEnemyDown) {

                if (isSameX){
                    doCheckTurnRightOrLeft();
                    return;
                }

                if (isSameY) {
                    handler.removeCallbacks(enemyTankDownPathRunnable);
                    moveEnemyTop();
                    isUserBulletComing = false;
                    handler.removeCallbacks(this);

                }
                handler.postDelayed(this, 50);
            }


        }
    };

    private void doCheckTurnRightOrLeft() {

        boolean isCloseLeft = bulletX - currentTankX <= 0;

        boolean isCloseRight = bulletX - currentTankX >= 0;

        Log.i("Michael","靠近左邊："+isCloseLeft + " , 靠近右邊 ："+isCloseRight);

        if (isCloseLeft) {
            secondCount = 0;
            clearEnemyTankPathHandler();
            moveEnemyRight();
            isUserBulletComing = false;
            handler.removeCallbacks(checkBulletClosingRunnable);
            return;
        }
        if (isCloseRight) {
            secondCount = 0;
            clearEnemyTankPathHandler();
            moveEnemyLeft();
            isUserBulletComing = false;
            handler.removeCallbacks(checkBulletClosingRunnable);
        }
    }

    private void randomChosePath() {
        int randomPath = (int) Math.floor(Math.random() * 4);
        Log.i("Michael", "random : " + randomPath);
        switch (randomPath) {
            case ENEMY_DOWN:
                moveEnemyDown();
                break;
            case ENEMY_TOP:
                moveEnemyTop();
                break;
            case ENEMY_RIGHT:
                moveEnemyRight();
                break;
            case ENEMY_LEFT:
                moveEnemyLeft();
                break;
        }
    }

    private void moveEnemyLeft() {
        checkEnemyStatus(LEFT);
        handler.postDelayed(enemyTankLeftPathRunnable, MOVE_SPEED);
    }


    private Runnable pkModeTankRunnable = new Runnable() {
        @Override
        public void run() {

            float fireRangeY = Math.abs((currentEnemyTankY - currentTankY));
            Log.i("Michael", "myTank : " + currentTankX + " enemyTankY : " + currentEnemyTankX + " fireRangeY : " + fireRangeY);
            //如果 X 在同層的話，還要判斷是在上下左右發射

            boolean isSameY = fireRangeY <= ivTank.getHeight();

            boolean isTankLeft = currentEnemyTankX >= currentTankX;

            boolean isTankRight = currentEnemyTankX <= currentTankX;

            if (isSameY) {
                Log.i("Michael", "Y在同一層上");
                enemyFire(isTankLeft, isTankRight, false, false);
                handler.postDelayed(pkModeTankRunnable, 2000);
                return;
            }

            float fireRangeX = Math.abs((currentEnemyTankX - currentTankX));

            boolean isSameX = fireRangeX <= ivTank.getHeight();

            boolean isTankTop = currentEnemyTankY >= currentTankY;

            boolean isTankDown = currentEnemyTankY <= currentTankY;
            if (isSameX) {
                Log.i("Michael", "X 在同一層上");
                enemyFire(false, false, isTankTop, isTankDown);
            }


            handler.postDelayed(pkModeTankRunnable, 2000);


        }
    };

    //敵人射擊 判斷方向
    private void enemyFire(boolean isTankLeft, boolean isTankRight, boolean isTankTop, boolean isTankDown) {

        ivEnemyFire.setVisibility(VISIBLE);

        if (isTankLeft) {
            ivEnemyTank.setImageResource(R.drawable.tank);
            ivEnemyTank.setRotation(-90f);

            enemyBulletX = ivEnemyTank.getX();
            enemyBulletY = ivEnemyTank.getY() + (ivEnemyTank.getHeight() / 2f) - (ivEnemyFire.getHeight() / 2f);
            handler.postDelayed(enemyFireLeftBulletRunnable, BULLET_SPEED);

            return;
        }

        if (isTankRight) {
            ivEnemyTank.setImageResource(R.drawable.tank);
            ivEnemyTank.setRotation(90f);
            enemyBulletY = ivEnemyTank.getY() + (ivEnemyTank.getHeight() / 2f) - (ivEnemyFire.getHeight() / 2f);
            enemyBulletX = ivEnemyTank.getX() + (ivEnemyTank.getWidth());
            handler.postDelayed(enemyFireRightBulletRunnable, BULLET_SPEED);

            return;

        }

        if (isTankDown) {
            ivEnemyTank.setImageResource(R.drawable.tank);
            ivEnemyTank.setRotation(180f);
            enemyBulletY = ivEnemyTank.getY() + ivEnemyTank.getHeight();
            enemyBulletX = ivEnemyTank.getX() + (ivEnemyTank.getWidth() / 2f) - (ivEnemyFire.getHeight() / 2f);
            handler.postDelayed(enemyFireDownBulletRunnable, BULLET_SPEED);
            return;
        }

        if (isTankTop) {
            ivEnemyTank.setImageResource(R.drawable.tank);
            ivEnemyTank.setRotation(0f);
            enemyBulletY = ivEnemyTank.getY();
            enemyBulletX = ivEnemyTank.getX() + (ivEnemyTank.getWidth() / 2f) - (ivEnemyFire.getHeight() / 2f);
            handler.postDelayed(enemyFireTopBulletRunnable, BULLET_SPEED);
            return;
        }

    }

    //針對上下射擊 敵人命中玩家
    private boolean isTankHorizontalHit() {


        float hitRangeRightX = enemyBulletX - currentTankX;

        boolean isHitRightX = enemyBulletX >= currentTankX && hitRangeRightX <= ivTank.getHeight();

        boolean isTouchTankY = enemyBulletY <= currentTankY + ivTank.getHeight() && enemyBulletY >= currentTankY;

        boolean isHitRight = isHitRightX && isTouchTankY && enemyBulletX != 0 && enemyBulletY != 0;

        if (isHitRight) {
            clearEnemyTankPathHandler();
            currentTankY = 0;
            currentTankX = 0;
            ivTank.setVisibility(INVISIBLE);
            showEnemyExplosion();

            Log.i("Michael", "命中右部");
            return true;
        }
        float leftX = currentTankX + ivTank.getHeight();

        float hitRangeLeftX = Math.abs(enemyBulletX - leftX);

        boolean isHitLeftX = enemyBulletX <= leftX && hitRangeLeftX <= ivTank.getHeight();

        boolean isHitLeft = isHitLeftX && isTouchTankY && enemyBulletX != 0 && enemyBulletY != 0;

        if (isHitLeft) {
            clearEnemyTankPathHandler();
            currentTankY = 0;
            currentTankX = 0;
            ivTank.setVisibility(INVISIBLE);
            showEnemyExplosion();
            Log.i("Michael", "命中底部");
            return true;
        }

        return false;
    }


    //針對左右射擊 敵人命中玩家
    private boolean isTankStraightHit() {


        float hitRangeTopY = enemyBulletY - currentTankY;

        boolean isHitTopY = enemyBulletY >= currentTankY && hitRangeTopY < ivTank.getHeight();

        boolean isTouchTankX = enemyBulletX <= currentTankX + ivTank.getHeight() && enemyBulletX >= currentTankX;


        //do here
        boolean isHitTop = isHitTopY && isTouchTankX && enemyBulletX != 0 & enemyBulletY != 0;
        if (isHitTop) {
            clearEnemyTankPathHandler();
            currentTankY = 0;
            currentTankX = 0;
            ivTank.setVisibility(INVISIBLE);
            showEnemyExplosion();

            Log.i("Michael", "命中上部");
            return true;
        }

        float bottomY = currentTankY + ivTank.getHeight() - 20f;

        float hitRangeDownY = Math.abs(enemyBulletY - bottomY);

        boolean isHitDownY = enemyBulletY <= bottomY && hitRangeDownY <= ivTank.getHeight();


        boolean isHitDown = isHitDownY && isTouchTankX && enemyBulletX != 0 & enemyBulletY != 0;

        if (isHitDown) {
            clearEnemyTankPathHandler();
            currentTankY = 0;
            currentTankX = 0;
            ivTank.setVisibility(INVISIBLE);
            showEnemyExplosion();
            Log.i("Michael", "命中底部");
            return true;
        }


        return false;
    }

    private void showEnemyExplosion() {
        ivExplosion.setX(ivTank.getX());
        ivExplosion.setY(ivTank.getY());
        ivExplosion.setVisibility(VISIBLE);
        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.scale_anim);
        animator.setTarget(ivExplosion);
        animator.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivExplosion.setVisibility(INVISIBLE);
                handler.removeCallbacks(this);
            }
        }, 1500);
    }

    //清除敵人的Handler
    private void stopEnemyBullet() {
        handler.removeCallbacks(enemyFireLeftBulletRunnable);
        handler.removeCallbacks(enemyFireRightBulletRunnable);
        handler.removeCallbacks(enemyFireTopBulletRunnable);
        handler.removeCallbacks(enemyFireDownBulletRunnable);
        handler.removeCallbacks(pkModeTankRunnable);
        ivEnemyFire.setVisibility(INVISIBLE);
    }

    //敵人子彈往左飛
    private Runnable enemyFireLeftBulletRunnable = new Runnable() {
        @Override
        public void run() {

            enemyBulletX -= 20;

            if (isTankStraightHit()) {

                stopEnemyBullet();
                return;
            }

            if (enemyBulletX <= 0) {
                stopEnemyBullet();
                return;
            }

            ivEnemyFire.setY(enemyBulletY);
            ivEnemyFire.setX(enemyBulletX);
            handler.postDelayed(this, BULLET_SPEED);
        }
    };

    //敵人子彈往右飛
    private Runnable enemyFireRightBulletRunnable = new Runnable() {
        @Override
        public void run() {
            enemyBulletX += 20;

            if (isTankStraightHit()) {
                stopEnemyBullet();
                return;
            }

            if (enemyBulletX >= maxRight) {
                stopEnemyBullet();
                return;
            }
            ivEnemyFire.setY(enemyBulletY);
            ivEnemyFire.setX(enemyBulletX);
            handler.postDelayed(this, BULLET_SPEED);
        }
    };


    //敵人子彈往上飛
    private Runnable enemyFireTopBulletRunnable = new Runnable() {
        @Override
        public void run() {
            enemyBulletY -= 20;

            if (isTankHorizontalHit()) {
                stopEnemyBullet();
                return;
            }

            if (enemyBulletX <= 0) {
                stopEnemyBullet();
                return;
            }
            ivEnemyFire.setY(enemyBulletY);
            ivEnemyFire.setX(enemyBulletX);
            handler.postDelayed(this, BULLET_SPEED);
        }
    };

    //敵人子彈往下飛
    private Runnable enemyFireDownBulletRunnable = new Runnable() {
        @Override
        public void run() {
            enemyBulletY += 20;

            if (isTankHorizontalHit()) {
                stopEnemyBullet();
                return;
            }

            if (enemyBulletX >= maxBottom) {
                stopEnemyBullet();
                return;
            }
            ivEnemyFire.setY(enemyBulletY);
            ivEnemyFire.setX(enemyBulletX);
            handler.postDelayed(this, BULLET_SPEED);
        }
    };

    private boolean isTurnWay() {
        return (int) Math.floor(Math.random() * 2) == 0;
    }


    //自定義的路線
    private Runnable enemyTankLeftPathRunnable = new Runnable() {
        @Override
        public void run() {

            //先測試

            ivEnemyTank.setImageResource(R.drawable.tank);
            ivEnemyTank.setRotation(-90f);

            moveEnemyTankX -= 20;

            secondCount += 50;

            if (secondCount >= 2000 && isTurnWay()) {


                handler.removeCallbacks(this);
                randomChosePath();

                secondCount = 0;
                return;
            }

            if (moveEnemyTankX <= 0f) {
                handler.removeCallbacks(this);
                randomChosePath();
                secondCount = 0;
                return;
            }
            currentEnemyTankX = moveEnemyTankX;
            setEnemyImageLocation();
            handler.postDelayed(this, MOVE_SPEED);
        }
    };

    private void moveEnemyTop() {
        checkEnemyStatus(TOP);
        handler.postDelayed(enemyTankTopPathRunnable, MOVE_SPEED);
    }

    private Runnable enemyTankTopPathRunnable = new Runnable() {
        @Override
        public void run() {
            ivEnemyTank.setImageResource(R.drawable.tank);
            ivEnemyTank.setRotation(0f);
            moveEnemyTankY -= 20;
            secondCount += 50;

            if (secondCount >= 2000 && isTurnWay()) {


                handler.removeCallbacks(this);
                randomChosePath();

                secondCount = 0;
                return;
            }

            if (moveEnemyTankY <= 0) {
                handler.removeCallbacks(this);
                randomChosePath();
                secondCount = 0;
                return;
            }
            currentEnemyTankY = moveEnemyTankY;
            setEnemyImageLocation();
            handler.postDelayed(this, MOVE_SPEED);
        }
    };

    private void moveEnemyDown() {
        checkEnemyStatus(DOWN);
        handler.postDelayed(enemyTankDownPathRunnable, MOVE_SPEED);
    }

    private void checkEnemyStatus(String status) {
        switch (status) {
            case DOWN:
                isEnemyDown = true;
                isEnemyTop = false;
                isEnemyLeft = false;
                isEnemyRight = false;
                break;
            case TOP:
                isEnemyDown = false;
                isEnemyTop = true;
                isEnemyLeft = false;
                isEnemyRight = false;
                break;
            case LEFT:
                isEnemyDown = false;
                isEnemyTop = false;
                isEnemyLeft = true;
                isEnemyRight = false;
                break;
            case RIGHT:
                isEnemyDown = false;
                isEnemyTop = false;
                isEnemyLeft = false;
                isEnemyRight = true;
                break;
        }
    }

    private Runnable enemyTankDownPathRunnable = new Runnable() {
        @Override
        public void run() {
            ivEnemyTank.setImageResource(R.drawable.tank);
            ivEnemyTank.setRotation(180f);
            moveEnemyTankY += 20;
            secondCount += 50;


            if (secondCount >= 2000 && isTurnWay()) {
                handler.removeCallbacks(this);
                randomChosePath();
                secondCount = 0;
                return;
            }

            if (moveEnemyTankY >= maxBottom - ivEnemyTank.getHeight()) {
                handler.removeCallbacks(this);
                secondCount = 0;
                randomChosePath();
                return;
            }
            currentEnemyTankY = moveEnemyTankY;
            setEnemyImageLocation();
            handler.postDelayed(this, MOVE_SPEED);
        }
    };

    private void moveEnemyRight() {
        checkEnemyStatus(RIGHT);
        handler.postDelayed(enemyTankRightPathRunnable, MOVE_SPEED);
    }

    private Runnable enemyTankRightPathRunnable = new Runnable() {
        @Override
        public void run() {

            ivEnemyTank.setImageResource(R.drawable.tank);
            ivEnemyTank.setRotation(90f);
            moveEnemyTankX += 20;
            secondCount += 50;

            if (secondCount >= 2000 && isTurnWay()) {


                handler.removeCallbacks(this);
                randomChosePath();

                secondCount = 0;
                return;
            }

            if (moveEnemyTankX >= maxRight - ivEnemyTank.getHeight()) {
                handler.removeCallbacks(this);
                randomChosePath();
                secondCount = 0;
                return;
            }

            currentEnemyTankX = moveEnemyTankX;
            setEnemyImageLocation();
            handler.postDelayed(this, MOVE_SPEED);
        }
    };


    private void clearEnemyTankPathHandler() {
        handler.removeCallbacks(enemyTankTopPathRunnable);
        handler.removeCallbacks(enemyTankLeftPathRunnable);
        handler.removeCallbacks(enemyTankDownPathRunnable);
        handler.removeCallbacks(enemyTankRightPathRunnable);

    }

    private void setEnemyImageLocation() {
        ivEnemyTank.setX(currentEnemyTankX);
        ivEnemyTank.setY(currentEnemyTankY);
    }


    //按往上的時候
    @Deprecated
    public void onButtonUpClickListener() {
        moveTop();
    }

    //按往下的時候
    @Deprecated
    public void onButtonDownClickListener() {
        moveDown();
    }

    //按往左的時候
    @Deprecated
    public void onButtonLeftClickListener() {
        moveLeft();
    }

    //按往右的時候
    @Deprecated
    public void onButtonRightClickListener() {
        moveRight();
    }

    //長按系列
    public void onButtonDownLongPressListener() {
        handler.postDelayed(downLongRunnable, MOVE_SPEED);
    }

    private Runnable downLongRunnable = new Runnable() {
        @Override
        public void run() {
            moveDown();
            handler.postDelayed(this, MOVE_SPEED);

        }
    };

    public void onButtonUpLongPressListener() {
        handler.postDelayed(upLongRunnable, MOVE_SPEED);
    }

    private Runnable upLongRunnable = new Runnable() {
        @Override
        public void run() {
            moveTop();
            handler.postDelayed(this, MOVE_SPEED);
        }
    };

    public void onButtonLeftLongPressListener() {
        handler.postDelayed(leftLongRunnable, MOVE_SPEED);
    }

    private Runnable leftLongRunnable = new Runnable() {
        @Override
        public void run() {
            moveLeft();
            handler.postDelayed(this, MOVE_SPEED);
        }
    };

    public void onButtonRightLongPressListener() {
        handler.postDelayed(rightLongRunnable, MOVE_SPEED);
    }

    private Runnable rightLongRunnable = new Runnable() {
        @Override
        public void run() {
            moveRight();
            handler.postDelayed(this, MOVE_SPEED);
        }
    };

    private void moveDown() {
        ivTank.setImageResource(R.drawable.tank);
        ivTank.setRotation(180f);

        moveTankY += 20;
        if (moveTankY >= maxBottom) {
            moveTankY = currentTankY;
        }

        currentTankY = moveTankY;
        Log.i("Michael", "X : " + currentTankX + " , Y : " + currentTankY);

        checkStatus(DOWN);
        setImageLocation();
    }

    private void moveTop() {
        ivTank.setImageResource(R.drawable.tank);
        ivTank.setRotation(0f);
        moveTankY -= 20;
        if (moveTankY <= 0) {
            moveTankY = currentTankY;
        }
        currentTankY = moveTankY;
        checkStatus(TOP);
        setImageLocation();
    }

    private void moveLeft() {
        ivTank.setImageResource(R.drawable.tank);
        ivTank.setRotation(-90f);

        moveTankX -= 20;
        if (moveTankX <= 0) {
            moveTankX = currentTankX;
        }

        currentTankX = moveTankX;
        checkStatus(LEFT);
        setImageLocation();
    }

    private void moveRight() {
        ivTank.setImageResource(R.drawable.tank);
        ivTank.setRotation(90f);
        moveTankX += 20;

        if (moveTankX >= maxRight) {
            moveTankX = currentTankX;
        }

        currentTankX = moveTankX;
        checkStatus(RIGHT);
        setImageLocation();
    }


    private void setImageLocation() {
        ivTank.setX(currentTankX);
        ivTank.setY(currentTankY);
//        ivUserFire.setX(currentTankX);
//        ivUserFire.setY(currentTankY);
    }

    //按開火的時候
    public void onButtonFireClickListener() {

        ivUserFire.setVisibility(VISIBLE);
        isUserBulletComing = true;
        dodgeBullet();
        if (isRight) {

            bulletY = ivTank.getY() + (ivTank.getHeight() / 2f) - (ivUserFire.getHeight() / 2f);
            bulletX = ivTank.getX() + (ivTank.getWidth());

            doBulletFly();

            return;
        }
        if (isLeft) {

            bulletY = ivTank.getY() + (ivTank.getHeight() / 2f) - (ivUserFire.getHeight() / 2f);
            bulletX = ivTank.getX();


            doBulletFly();
            return;
        }
        if (isTop) {
            bulletY = ivTank.getY();
            bulletX = ivTank.getX() + (ivTank.getWidth() / 2f) - (ivUserFire.getHeight() / 2f);

            doBulletFly();
            return;
        }
        if (isDown) {

            bulletY = ivTank.getY() + ivTank.getHeight();
            bulletX = ivTank.getX() + (ivTank.getWidth() / 2f) - (ivUserFire.getHeight() / 2f);
            doBulletFly();
            return;
        }
        bulletY = ivTank.getY() + (ivTank.getHeight() / 2f) - (ivUserFire.getHeight() / 2f);
        bulletX = ivTank.getX() + (ivTank.getWidth());
        doBulletFly();
    }

    private void doBulletFly() {


        if (isRight) {
            handler.postDelayed(bulletFlyRightRunnable, BULLET_SPEED);
            return;
        }
        if (isLeft) {
            handler.postDelayed(bulletFlyLeftRunnable, BULLET_SPEED);
            return;
        }

        if (isDown) {
            handler.postDelayed(bulletFlyDownRunnable, BULLET_SPEED);
            return;
        }
        if (isTop) {
            handler.postDelayed(bulletFlyTopRunnable, BULLET_SPEED);
            return;
        }
        handler.postDelayed(bulletFlyRightRunnable, BULLET_SPEED);

    }

    private Runnable bulletFlyRightRunnable = new Runnable() {
        @Override
        public void run() {

            bulletX += 20;

            if (isStraightHit()) {
                stopBullet();
                return;
            }
            if (bulletX >= maxRight) {
                stopBullet();
                return;
            }
            ivUserFire.setY(bulletY);
            ivUserFire.setX(bulletX);
            handler.postDelayed(this, BULLET_SPEED);

        }
    };


    private Runnable bulletFlyLeftRunnable = new Runnable() {
        @Override
        public void run() {
            bulletX -= 20;

            if (isStraightHit()) {
                stopBullet();
                return;
            }

            if (bulletX <= 0) {
                stopBullet();
                return;
            }
            ivUserFire.setY(bulletY);
            ivUserFire.setX(bulletX);
            handler.postDelayed(this, BULLET_SPEED);
        }
    };

    private Runnable bulletFlyDownRunnable = new Runnable() {
        @Override
        public void run() {
            bulletY += 20;

            if (isHorizontalHit()) {
                stopBullet();
                return;
            }

            if (bulletY >= maxBottom) {
                stopBullet();
                return;
            }
            ivUserFire.setY(bulletY);
            ivUserFire.setX(bulletX);
            handler.postDelayed(this, BULLET_SPEED);
        }
    };

    private Runnable bulletFlyTopRunnable = new Runnable() {
        @Override
        public void run() {
            bulletY -= 20;
            if (isHorizontalHit()) {
                stopBullet();
                return;
            }
            if (bulletY <= 0) {
                stopBullet();
                return;
            }
            ivUserFire.setY(bulletY);
            ivUserFire.setX(bulletX);
            handler.postDelayed(this, BULLET_SPEED);
        }
    };

    private boolean isHorizontalHit() {

        float hitRangeRightX = bulletX - currentEnemyTankX;

        boolean isHitRightX = bulletX >= currentEnemyTankX && hitRangeRightX < ivTank.getHeight();

        boolean isTouchTankY = bulletY <= currentEnemyTankY + ivEnemyTank.getHeight() && bulletY >= currentEnemyTankY;

        boolean isHitRight = isHitRightX && isTouchTankY && bulletX != 0 && bulletY != 0;

        if (isHitRight) {
            clearEnemyTankPathHandler();
            currentEnemyTankY = 0;
            currentEnemyTankX = 0;
            ivEnemyTank.setVisibility(INVISIBLE);
            showExplosion();
            Log.i("Michael", "命中上部");
            return true;
        }

        float leftX = currentEnemyTankX + ivTank.getHeight();

        float hitRangeLeftX = Math.abs(bulletX - leftX);

        boolean isHitLeftX = bulletX <= leftX && hitRangeLeftX <= ivTank.getHeight();

        boolean isHitLeft = isHitLeftX && isTouchTankY && bulletX != 0 && bulletY != 0;

        if (isHitLeft) {
            clearEnemyTankPathHandler();
            currentEnemyTankY = 0;
            currentEnemyTankX = 0;
            ivEnemyTank.setVisibility(INVISIBLE);
            showExplosion();
            Log.i("Michael", "命中底部");
            return true;
        }
        return false;
    }

    private boolean isStraightHit() {

        float hitRangeTopY = bulletY - currentEnemyTankY;

        boolean isHitTopY = bulletY >= currentEnemyTankY && hitRangeTopY < ivTank.getHeight();

        boolean isTouchTankX = bulletX <= currentEnemyTankX + ivEnemyTank.getHeight() && bulletX >= currentEnemyTankX;

        Log.i("Michael", "上部Log , 子彈 Ｙ：" + bulletY + " , 坦克上部Ｙ：" + currentEnemyTankY);
        //do here
        boolean isHitTop = isHitTopY && isTouchTankX && bulletX != 0 & bulletY != 0;
        if (isHitTop) {
            clearEnemyTankPathHandler();
            currentEnemyTankY = 0;
            currentEnemyTankX = 0;
            ivEnemyTank.setVisibility(INVISIBLE);
            showExplosion();
            Log.i("Michael", "命中上部");
            return true;
        }

        float bottomY = currentEnemyTankY + ivTank.getHeight() - 20f;

        float hitRangeDownY = Math.abs(bulletY - bottomY);

        boolean isHitDownY = bulletY <= bottomY && hitRangeDownY <= ivTank.getHeight();

        boolean isHitDown = isHitDownY && isTouchTankX && bulletX != 0 & bulletY != 0;

        Log.i("Michael", "下部Log , 子彈 Ｙ：" + bulletY + " , 坦克下部Ｙ：" + bottomY + " , rangeY : " + hitRangeDownY);

        if (isHitDown) {
            clearEnemyTankPathHandler();
            currentEnemyTankY = 0;
            currentEnemyTankX = 0;
            ivEnemyTank.setVisibility(INVISIBLE);
            showExplosion();
            Log.i("Michael", "命中底部");
            return true;
        }


        return false;
    }

    private void showExplosion() {
        ivExplosion.setX(ivEnemyTank.getX());
        ivExplosion.setY(ivEnemyTank.getY());
        ivExplosion.setVisibility(VISIBLE);
        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.scale_anim);
        animator.setTarget(ivExplosion);
        animator.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivExplosion.setVisibility(INVISIBLE);
                handler.removeCallbacks(this);
            }
        }, 1500);
    }

    private void stopBullet() {
        handler.removeCallbacks(bulletFlyLeftRunnable);
        handler.removeCallbacks(bulletFlyDownRunnable);
        handler.removeCallbacks(bulletFlyTopRunnable);
        handler.removeCallbacks(bulletFlyRightRunnable);
        ivUserFire.setVisibility(INVISIBLE);
        isUserBulletComing = false;
    }

    private void checkStatus(String status) {
        switch (status) {
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

package sh.hao.com.animatorlinlayout;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

/**
 * Author:zh
 * QQ:164603384
 * Time:2018/11/8
 * Description:This is 可以拖动 隐藏 显示的 LinearLayout
 */
public class AnimaLinlayout extends LinearLayout implements View.OnTouchListener{
    //移动方向  true = 向下 ，反之 向上
    boolean isMove = true;
    //记录下次动画开始的位置
    float lastY = 0;
    //移动的距离
    float moveY = 0;
    //当前view的高度
    float viewH = 0;
    //是否执行动画
    boolean isAnima = true;
    private GestureDetector gesture; //手势识别
    //露出部分的高度
    int headHeight = 160;
    //回弹距离
    int resDistance  = 0;
    public AnimaLinlayout(Context context) {
        super(context);
    }
    public AnimaLinlayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimaLinlayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public AnimaLinlayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
    //初始化view 添加动画
    public  void addAnima(Context context){
        gesture = new GestureDetector(context, new MyOnGestureListener());
        setOnTouchListener(this);
    }
    //执行动画
    public void startAnima(){
        if (isMove) {
            startAnimator(0F,getHeight()-headHeight);
            lastY = getHeight()-headHeight;
        } else {
            startAnimator(getHeight()-headHeight,0F);
            lastY = 0;
        }
        isMove = !isMove;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //添加手势监听
       boolean touch =  gesture.onTouchEvent(motionEvent);
        if (motionEvent.getAction()==MotionEvent.ACTION_UP){
            if (isAnima)
                if (isMove&&moveY>0&& moveY<viewH/3){
                    startAnimator(lastY, 0);
                    lastY = 0;
                }else  if (!isMove&&moveY<0&& Math.abs(moveY)<viewH/3){
                    startAnimator(lastY, viewH-150);
                    lastY = viewH-150;
                }else {

                }
        }
       return touch;
    }

    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override//此方法必须重写且返回真，否则onFling不起效
        public boolean onDown(MotionEvent e) {
            //因为 这个方法只执行一次 所以在这里初始化
            isAnima = true;
            resDistance = (int) (viewH/3);
            //当前View的高度
            viewH = getHeight();
            return true;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            moveY = e2.getRawY() - e1.getRawY();
            //当拖动到view的一半时 结束拖动动作
            Log.e("onScroll", "    moveY = " + moveY);
            //动画开关
            if (isAnima)
                if (isMove&&moveY>0) {
                    if ( moveY<resDistance){
                        Log.e("onScroll", "    lastY = " + lastY);
                        startAnimator(lastY, moveY);
                        lastY = moveY;
                    }else {
                        isAnima = false;
                        Log.e("onScroll", "    lastY = " + lastY);
                        startAnimator(lastY, viewH-headHeight);
                        isMove = !isMove;
                        lastY = viewH-headHeight;
                    }
                } else if (!isMove&&moveY<0){
                    if (Math.abs(moveY)<resDistance){
                        startAnimator(lastY, viewH-headHeight+moveY);
                        lastY = viewH-headHeight+moveY;
                    }else {
                        startAnimator(lastY, 0);
                        isMove = !isMove;
                        lastY = 0;
                    }
                }else {
                    // onFling 中 执行弹出，或者 隐藏动画
                    //Log.e("onScroll", "    return true = ");
                    // return true;
                }
            // return true;
            return super.onFling(e1, e2, distanceX, distanceY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.e("onShowPress", "onShowPress = " + e.getRawY());
            super.onShowPress(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e("onSingleTapUp", "onSingleTapUp = " + e.getRawY());
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // e1：第1个ACTION_DOWN MotionEvent
            // e2：最后一个ACTION_MOVE MotionEvent
            // velocityX：X轴上的移动速度，像素/秒
            // velocityY：Y轴上的移动速度，像素/秒
            Log.e("onFling", "velocityX = " + velocityX + "    velocityY = " + velocityY);

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
    ObjectAnimator objectAnimator;
    private void startAnimator(float startY, float endY) {
        //linearLayout.setTranslationY(moveY);
         objectAnimator = ObjectAnimator.ofFloat(this, "translationY", startY, endY);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(100);
        objectAnimator.start();
    }
}

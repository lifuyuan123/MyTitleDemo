package com.example.administrator.mytitledemo;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ImageView;


public class DragTextView extends android.support.v7.widget.AppCompatImageView {
    //相对于父控件的触摸位置，用于处理拖拽
    private float xDown,yDown,xUp,yUp;
    private int extra;
    private DisplayMetrics dm;

    public DragTextView(Context context) {
        this(context, null, 0);
    }
    public DragTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public DragTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
          dm = new DisplayMetrics();//获取屏幕宽高，处理越界的时候用到
        Activity activity = (Activity) context;        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
    }
    //重写触摸的方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event);
                break;      
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                isOver();
                break;        
        }
        return true;
    }
    /** 按下 **/
    private void onTouchDown(MotionEvent event){
        xDown = event.getX();
        yDown = event.getY();
        xUp = event.getRawX();
        yUp = event.getRawY();
        extra = (int) (yUp - yDown - getTop());
    }
    /** 拖拽 **/
    private void onTouchMove(MotionEvent event) {
        int left, right, top, bottom;
        top = (int) (yUp - yDown) - extra;
        bottom = top + getHeight();
        left = (int) (xUp - xDown);
        right = left + getWidth();
        //Top position, relative to parent这是该方法其中top参数的官方解释，反正我是被误导了，我不知道这个父亲到底是怎样定义的
        // 我目前的理解是它的所有参数位置是相对于该view放置的那个xml布局的位置，那个xml布局最外面的那个layout才是父view。
        //为什么要说的这么绕，就是它的位置不是相对屏幕的，因为你的应用可能有tab占了位置，那块位置就不能算。如果理解了这些话就能知道为什么会有extra了。
        this.setFrame(left, top, right, bottom);
        xUp = event.getRawX();
        yUp = event.getRawY();
    }

    /**
     * 拖拽时判断是否越界
     */
    private void isOver() {
        int width = this.getWidth()/3;
        int height = this.getHeight()/3;
        int left = getLeft(),right=getRight(),top=getTop(),bottom=getBottom();
        //针对整个可用屏幕的越界,必须还能看到控件的1/3
        if (this.getBottom() < height){
            bottom = height;
            top = bottom - getHeight();
        }
        if (this.getRight() <  width){
            right = width;
            left = right - getWidth();
        }
        if (this.getTop() > dm.heightPixels - extra - height){
            top = dm.heightPixels - extra - height;
            bottom = top + getHeight();
        }
        if (this.getLeft() > dm.widthPixels - width){
            left = dm.widthPixels - width;
            right = left + getWidth();
        }
        if (this.getBottom() < height || this.getLeft() < - width || this.getRight() > dm.widthPixels - width || this.getTop() > dm.heightPixels - extra - height) {
            setFrame(left, top, right, bottom);
        }
    }
}
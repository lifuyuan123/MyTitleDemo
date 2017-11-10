package com.example.administrator.mytitledemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Main4Activity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;//定义的滑动距离    大于25开始头像隐藏的动画
    private boolean mIsAvatarShown = true; //头像显示隐藏

    private ImageView mProfileImage;//头像
    private int mMaxScrollSize;   //最大滑动距离
    private View view;
    private CollapsingToolbarLayout ctl;
    private ImageView imageView;
    private Toolbar toolbar;
    private LinearLayout linearLayout;
    private WaveView waveView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//此FLAG可使状态栏透明，且当前视图在绘制时，从屏幕顶端开始即top = 0开始绘制，这也是实现沉浸效果的基础
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//可不加
        }
        setContentView(R.layout.activity_main4);

        view=findViewById(R.id.view_toolbar);
        ctl= (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        ctl.setTitle("我的");
        ctl.setCollapsedTitleGravity(Gravity.CENTER);//上啦结束文字居中
        ctl.setCollapsedTitleTextColor(Color.WHITE);//上啦结束字体为白色
        ctl.setExpandedTitleGravity(Gravity.TOP|Gravity.LEFT);//
        toolbar= (Toolbar) findViewById(R.id.toolbar_4);
        linearLayout= (LinearLayout) findViewById(R.id.materialup_title_container);
        waveView= (WaveView) findViewById(R.id.waveline);
        waveView.waveAnim();//开始波浪动画

        imageView= (ImageView) findViewById(R.id.profile_backdrop);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.adf);
        Drawable drawable = ImageUtils.bitmap2Drawable(getResources(), ImageUtils.fastBlur(bitmap, 1, 22, true));
        imageView.setImageDrawable(drawable);


        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();//titlebar为添加的顶部的（标题栏上方）占位控件
        layoutParams.height = getStatueBarHeight();
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(Color.TRANSPARENT);//此处也可设置自定义的颜色，设置为透明则会直接看到底层的图片



        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.materialup_appbar);
        mProfileImage = (ImageView) findViewById(R.id.materialup_profile_image);

        appbarLayout.addOnOffsetChangedListener(this);//设置滑动监听
        mMaxScrollSize = appbarLayout.getTotalScrollRange();//获取最大滑动距离


        //拿取CollapsingToolbarLayout的子View并根据版本设置其fitsSystemWindows的属性
        for (int k = 0; k < ctl.getChildCount(); k++) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                ctl.getChildAt(k).setFitsSystemWindows(true);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                ctl.getChildAt(k).setFitsSystemWindows(false);
            } else {//不支持沉浸式状态栏的版本，要把使View向下偏移的占位控件的高度设置为0
                //设置充当TitleBar使用的控件里面的占位空间的高度为0，
            }

        }
    }

    //int verticalOffset  垂直偏移量
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        //Math.abs  取绝对值  没有负数返回的是double型
        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        //判断是否滑动大于25  并且头像显示（mIsAvatarShown）  隐藏头像
        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(500)
                    .start();
            linearLayout.animate().scaleX(0).scaleY(0).setDuration(300).start();
        }

        //相反显示头像
        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();

            linearLayout.animate().scaleX(1).scaleY(1).start();
        }
    }

    private int getStatueBarHeight() {//拿取状态栏的高度
        int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return (int) getResources().getDimension(identifier);
        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        waveView.stop();
    }
}

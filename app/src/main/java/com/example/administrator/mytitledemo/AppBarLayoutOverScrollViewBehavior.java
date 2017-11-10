package com.example.administrator.mytitledemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * author:ggband
 * data:2017/11/1 000110:21
 * email:ggband520@163.com
 * desc:
 */

public class AppBarLayoutOverScrollViewBehavior extends AppBarLayout.Behavior  {
    private static final String TAG = "overScroll";
    private View mTargetView;       // 目标View  必须在布局文件给放大的view设置TAG=overScroll;
    private int mParentHeight;      // AppBarLayout的初始高度
    private int mTargetViewHeight;  // 目标View的高度

    private static final float TARGET_HEIGHT = 500; // 最大滑动距离
    private float mTotalDy;     // 总滑动的像素数
    private float mLastScale;   // 最终放大比例
    private int mLastBottom;    // AppBarLayout的最终Bottom值
    private boolean isAnimate;  //是否有动画


    public AppBarLayoutOverScrollViewBehavior() {}

    public AppBarLayoutOverScrollViewBehavior(Context context, AttributeSet attrs) {
                super(context, attrs);
            }
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
                boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
               // 需要在调用过super.onLayoutChild()方法之后获取
               if (mTargetView == null) {
                       mTargetView = parent.findViewWithTag(TAG);
                        if (mTargetView != null) {
                               initial(abl);
                            }
                   }
              return handled;
           }

           private void initial(AppBarLayout abl) {
             // 必须设置ClipChildren为false，这样目标View在放大时才能超出布局的范围
               abl.setClipChildren(false);
              mParentHeight = abl.getHeight();
              mTargetViewHeight = mTargetView.getHeight();
           }



//    重写onNestedPreScroll()修改AppBarLayou滑动的顶部后的行为
//    此时可以实现下滑越界时目标View放大，AppBarLayout变高的效果。
    @Override
      public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
            // 1.mTargetView不为null
            // 2.是向下滑动，dy<0表示向下滑动
             // 3.AppBarLayout已经完全展开，child.getBottom() >= mParentHeight
//             if (mTargetView != null && dy < 0 && child.getBottom() >= mParentHeight) {
//                    // 累加垂直方向上滑动的像素数
//                    mTotalDy += -dy;
//                    // 不能大于最大滑动距离
//                    mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);
//                    // 计算目标View缩放比例，不能小于1
//                   mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
//                   // 缩放目标View
//                     ViewCompat.setScaleX(mTargetView, mLastScale);
//                     ViewCompat.setScaleY(mTargetView, mLastScale);
//                     // 计算目标View放大后增加的高度
//                    mLastBottom = mParentHeight + (int) (mTargetViewHeight / 2 * (mLastScale - 1));
//                     // 修改AppBarLayout的高度
//                    child.setBottom(mLastBottom);
//               } else {
//
//
//                 //上滑处理
//                 // 1.mTargetView不为null
//                 // 2.是向上滑动，dy>0表示向下滑动
//                 // 3.AppBarLayout尚未恢复到原始高度child.getBottom() > mParentHeight
//                 if (mTargetView != null && dy > 0 && child.getBottom() > mParentHeight) {
//                     // 累减垂直方向上滑动的像素数
//                     mTotalDy -= dy;
//                     // 计算目标View缩放比例，不能小于1
//                     mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
//                     // 缩放目标View
//                     ViewCompat.setScaleX(mTargetView, mLastScale);
//                     ViewCompat.setScaleY(mTargetView, mLastScale);
//                     // 计算目标View缩小后减少的高度
//                     mLastBottom = mParentHeight + (int) (mTargetViewHeight / 2 * (mLastScale - 1));
//                     // 修改AppBarLayout的高度
//                     child.setBottom(mLastBottom);
//                     // 保持target不滑动
//                     target.setScrollY(0);
//                 } else {
//                     super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
//                 }
//                 }

        //判断上下滑动处理
        if (mTargetView != null && ((dy < 0 && child.getBottom() >= mParentHeight) || (dy > 0 && child.getBottom() > mParentHeight))) {
                    scale(child, target, dy);
                } else {
                   super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
               }

    }

       //上下滑动处理
       private void scale(AppBarLayout abl, View target, int dy) {
            mTotalDy += -dy;
            mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);
            mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
            ViewCompat.setScaleX(mTargetView, mLastScale);
            ViewCompat.setScaleY(mTargetView, mLastScale);
            mLastBottom = mParentHeight + (int) (mTargetViewHeight / 2 * (mLastScale - 1));
            abl.setBottom(mLastBottom);
            target.setScrollY(0);
        }

//    还原
//    当AppBarLayout处于越界时，如果用户松开手指，此时应该让目标View和AppBarLayout都还原到原始状态，重写onStopNestedScroll()方法
    @Override
      public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {
           recovery(abl);
             super.onStopNestedScroll(coordinatorLayout, abl, target);
         }

//    优化
//    由于用户在滑动时有可能触发快速滑动，会导致在AppBarLayout收起后触发还原动画，重新修改AppBarLayout的Bottom，从而显示错误，所以当发生快速滑动时需要禁止还原动画，直接还原到初始状态

        @Override
         public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
             // 开始滑动时，启用动画
             isAnimate = true;
             return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
         }

         @Override
         public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
             // 如果触发了快速滚动且垂直方向上速度大于100，则禁用动画
            if (velocityY > 100) {
                    isAnimate = false;
                 }
             return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
         }

         private void recovery(final AppBarLayout abl) {
             if (mTotalDy > 0) {
                     mTotalDy = 0;
                    if (isAnimate) {
                             ValueAnimator anim = ValueAnimator.ofFloat(mLastScale, 1f).setDuration(200);
                             anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                 @Override
                 public void onAnimationUpdate(ValueAnimator animation) {
                                             float value = (float) animation.getAnimatedValue();
                                            ViewCompat.setScaleX(mTargetView, value);
                                            ViewCompat.setScaleY(mTargetView, value);
                                            abl.setBottom((int) (mLastBottom - (mLastBottom - mParentHeight) * animation.getAnimatedFraction()));
                                       }
             });
                            anim.start();
                         } else {
                             ViewCompat.setScaleX(mTargetView, 1f);
                            ViewCompat.setScaleY(mTargetView, 1f);
                           abl.setBottom(mParentHeight);
                         }
                 }
         }
}

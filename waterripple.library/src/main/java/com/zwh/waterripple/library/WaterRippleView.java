package com.zwh.waterripple.library;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import static android.R.attr.logo;
import static android.R.attr.width;
import static android.R.attr.x;
import static android.R.attr.y;

/**
 *正弦型函数解析式：y=Asin(ωx+φ)+b
 *各常数值对函数图像的影响：
 *φ：决定波形与X轴位置关系或横向移动距离（左加右减）//控制波形移动
 *ω：决定周期（最小正周期T=2π/∣ω∣）
 *A：决定峰值（即纵向拉伸压缩的倍数）
 *b：表示波形在Y轴的位置关系或纵向移动距离（上加下减）
 */

public class WaterRippleView extends View{

  private static final int DEFAULT_DURATION = 1800;



  private Paint mPaint,mPaint2,mBgPaint;
  private Path mPath,mPath2, mBgPath;

  private float mFraction = 1f; //控制水深
  private float mFraction2 = 0f; //控制波形的高度
  private float mFraction3 = 0f; //控制波形向右移动
  private float times = 0f; //记录波形周期
  private float depthRate = 0;//水深比例

  private float mWidth, mHeight;
  private float mCenterX, mCenterY;
  private float mCurrentY;

  private int mDuration;//左右移动
  private int mDuration2;//上下移动

  private int backGroundColor = Color.argb(120,48,110,62);
  private int shadowColor = Color.argb(120,2,199,46);
  private int frontColor = Color.argb(255,2,199,46);

  public WaterRippleView(Context context) {
    this(context, null);
  }

  public WaterRippleView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public WaterRippleView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    mDuration = DEFAULT_DURATION;
    mDuration2 = DEFAULT_DURATION;
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setAntiAlias(true);
    mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    mPaint.setColor(frontColor);
    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
    mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint2.setAntiAlias(true);
    mPaint2.setFlags(Paint.ANTI_ALIAS_FLAG);
    mPaint2.setColor(shadowColor);
    mPaint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
    mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mBgPaint.setStyle(Paint.Style.FILL);
    mBgPaint.setColor(backGroundColor);
    mPath = new Path();
    mBgPath = new Path();
    mPath2 = new Path();
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mHeight = h;
    mWidth =w ;
    mCenterX = w / 2;
    mCenterY = h / 2;
    mBgPath.reset();
    mBgPath.addCircle(mCenterX,mCenterY,w/2, Path.Direction.CW);
    mBgPath.close();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    calculatePath();
    canvas.drawCircle(mCenterX,mCenterY,mWidth/2,mBgPaint);
    canvas.drawPath(mPath2,mPaint2);
    canvas.drawPath(mPath,mPaint);

  }

  public void startRefresh() {
    ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(1.f, 100.f);
    valueAnimator2.setDuration(mDuration2);
    valueAnimator2.setInterpolator(new LinearInterpolator());
    valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        if (valueAnimator.getAnimatedFraction()*depthRate <=0.5){
          mFraction2 = (valueAnimator.getAnimatedFraction());
        }else {
          mFraction2 = (1-valueAnimator.getAnimatedFraction());
        }
        mFraction = 1 - valueAnimator.getAnimatedFraction()*depthRate;
        invalidate();
      }
    });
    if (!valueAnimator2.isRunning()) {
      valueAnimator2.start();
    }
  }
  public void startRefresh2() {
    ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.f, 100.f);
    valueAnimator.setDuration(mDuration);
    valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
    valueAnimator.setInterpolator(new LinearInterpolator());
    valueAnimator.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {

      }

      @Override public void onAnimationEnd(Animator animation) {

      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {
        times = times+1;
      }
    });
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        //mFraction = 1 - valueAnimator.getAnimatedFraction();
        //mFraction2 = valueAnimator.getAnimatedFraction();
        mFraction3 =  times + 1 - valueAnimator.getAnimatedFraction();
        //Log.e("mFraction3",mFraction3+"++++"+times);
        invalidate();
      }
    });
    if (!valueAnimator.isRunning()) {
      valueAnimator.start();
    }
  }

  private void calculatePath(){
    mPath.reset();
    mPath2.reset();
    float y1,y2;
    mPath.moveTo(0,mCenterY);
    mPath2.moveTo(0,mCenterY);
    for (float x = 0; x <= mWidth; x=x+1) {
      y1 = (float) (mHeight*0.06f *(1-Math.abs(mHeight*mFraction-0.5f*mHeight)/(0.5f*mHeight))* Math.sin(Math.toRadians(90/(mWidth/3)*x+720*mFraction3)))+mHeight*mFraction;
      mPath.lineTo(x, y1);
    }
    for (float x = 0; x <= mWidth; x=x+1) {
      y2 = (float) (mHeight*0.06f *(1-Math.abs(mHeight*mFraction-0.5f*mHeight)/(0.5f*mHeight))* Math.sin(Math.toRadians(90/(mWidth/3)*x+720*mFraction3+180)))+mHeight*mFraction;
      mPath2.lineTo(x, y2);
    }
    mPath.lineTo(mWidth,mHeight);
    mPath.lineTo(0,mHeight);
    mPath.close();
    mPath2.lineTo(mWidth,mHeight);
    mPath2.lineTo(0,mHeight);
    mPath2.close();
  }
  public void setDepthRate(float f){
    depthRate = f;
    mDuration2 = (int) (3000 * f);
  }

  public void setBackGroundColor(int color){
    backGroundColor = color;
  }

  public void setFrontColor(int color){
    frontColor = color;
    mPaint.setColor(frontColor);
  }
  public void setShadowColor(int color){
    shadowColor = color;
    mPaint2.setColor(shadowColor);
  }


}

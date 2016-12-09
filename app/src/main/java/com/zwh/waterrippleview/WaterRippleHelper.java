package com.zwh.waterrippleview;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zwh.waterripple.library.WaterRippleView;
import java.util.Random;

import static com.zwh.waterrippleview.R.id.root;
import static com.zwh.waterrippleview.R.id.wrv;

/**
 * Created by Zhangwh on 2016/12/8 0008.
 * email:616505546@qq.com
 */

public class WaterRippleHelper {
  private WaterRippleView waterRippleView;
  private TextView tv;
  private Context context;
  private View rootView;
  private int count = 0;
  public int[] shadowColors ={Color.argb(120,2,199,46),Color.argb(120,228,31,98),Color.argb(120,109,140,198),};
  public int[] frontColors ={Color.argb(255,2,199,46),Color.argb(255,228,31,98),Color.argb(255,109,140,198),};
  int position = 0;
  public WaterRippleHelper(Context context){
    this.context = context;
    rootView = View.inflate(context, R.layout.item_waterripple, null);
    waterRippleView = (WaterRippleView) rootView.findViewById(R.id.wrv);
    tv = (TextView) rootView.findViewById(R.id.tv);
  }
  Handler handler = new Handler(){
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (count == 0){
        waterRippleView.setDepthRate((float)500/1000);
        waterRippleView.startRefresh();
        waterRippleView.startRefresh2();
      }
      if(count<500){
        count+=3;
        handler.sendEmptyMessage(0);
      }else if(count>500) {
        count=500;
      }
      tv.setText(count+"");
    }
  };

  /*
  * root 根布局
  * tag 点击控件的tag
  */
  public void showEvent(final ViewGroup root, final String tag){
    if((root.findViewWithTag(tag+"ripple")==null)) {
      seColors();
      rootView.setTag(tag+"ripple");
      rootView.setAlpha(0);
      rootView.setTranslationX(-50f);
      rootView.setTranslationY(-50f);
      rootView.setScaleX(0.1f);
      rootView.setScaleY(0.1f);
      RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
          RelativeLayout.LayoutParams.WRAP_CONTENT);
      params.leftMargin = root.findViewWithTag(tag).getLeft()+150;
      params.topMargin = root.findViewWithTag(tag).getTop() + root.findViewWithTag(tag).getWidth()-200;
      ViewCompat.animate(rootView)
          .setDuration(700)
          .setInterpolator(new DecelerateInterpolator(1.2f))
          .scaleX(1f)
          .scaleY(1f)
          .alpha(1f)
          .translationX(0)
          .translationY(0)
          .start();
      root.addView(rootView, params);
      rootView.postDelayed(new Runnable() {
        @Override public void run() {
          handler.sendEmptyMessage(0);
        }
      },200);
    }else {
      ViewCompat.animate(root.findViewWithTag(tag+"ripple"))
          .setDuration(700)
          .setInterpolator(new DecelerateInterpolator(1.2f))
          .alpha(0f)
          .scaleX(0f)
          .scaleY(0f)
          .translationX(-50f)
          .translationY(-50f)
          .setListener(new ViewPropertyAnimatorListener() {
            @Override public void onAnimationStart(View view) {

            }

            @Override public void onAnimationEnd(View view) {
              root.removeView(root.findViewWithTag(tag+"ripple"));
            }

            @Override public void onAnimationCancel(View view) {

            }
          })
          .start();
    }
  }

  public int getRandomPosition(){
    Random rd = new Random();
    position = rd.nextInt(3);
    return position;
  }

  public void seColors(){
    getRandomPosition();
    waterRippleView.setFrontColor(frontColors[position]);
    waterRippleView.setShadowColor(shadowColors[position]);
  }

}

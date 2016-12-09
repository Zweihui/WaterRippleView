package com.zwh.waterrippleview;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zwh.waterripple.library.WaterRippleView;

public class MainActivity extends AppCompatActivity {
  private Button btn;
  private Button btn1;
  private Button btn2;
  private RelativeLayout root;
  private int count = 0;
  private TextView tv;
  private WaterRippleView wrv;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    btn = (Button) findViewById(R.id.btn);
    btn.setTag("btn");
    btn1 = (Button) findViewById(R.id.btn1);
    btn1.setTag("btn1");
    btn2 = (Button) findViewById(R.id.btn2);
    btn2.setTag("btn2");
    root = (RelativeLayout) findViewById(R.id.root);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        clickEvent(btn.getTag().toString());
      }
    });
    btn1.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        clickEvent(btn1.getTag().toString());
      }
    });
    btn2.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        clickEvent(btn2.getTag().toString());
      }
    });
  }
  public void clickEvent(String tag){
    WaterRippleHelper helper = new WaterRippleHelper(this);
    helper.showEvent(root, tag);
  }

}

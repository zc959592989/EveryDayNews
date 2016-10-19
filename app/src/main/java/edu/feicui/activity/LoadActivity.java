package edu.feicui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import edu.feicui.everydaynews.R;

/**
 * 加载界面
 * Created by zhaoCe on 2016/9/26.
 */

public class LoadActivity extends Activity {
    /**
     * logo图标
     */
    ImageView mImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        mImg= (ImageView) findViewById(R.id.img_logo_load);
        mImg.setImageResource(R.mipmap.logo);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.logo_aphla_animation);
        mImg.startAnimation(animation);
        new CountDownTimer(3000,1000) {//三秒之后，进入主界面
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
            Intent intent=new Intent();
                intent.setClass(LoadActivity.this,HomeActivity.class);
                intent.putExtra("int",2);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}

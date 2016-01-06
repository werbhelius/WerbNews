package com.werber.newsbj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.werber.newsbj.R;
import com.werber.newsbj.utils.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    RelativeLayout splash_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_splash);

        splash_rl = (RelativeLayout) findViewById(R.id.splash_rl);

        startAnim();
    }


    /**
     * 闪屏动画
     */
    private void startAnim() {
        //缩放动画
        ScaleAnimation sa=new ScaleAnimation(2,1,2,1);
        sa.setDuration(1000);
        sa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            //动画结束后
            @Override
            public void onAnimationEnd(Animation animation) {
                jumpToNextPage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splash_rl.startAnimation(sa);
    }

    /**
     * 跳转下一个页面
     */
    private void jumpToNextPage(){
        Boolean isFirstIn= PrefUtils.getBoolean(this,"isFirstIn",true);
        if(isFirstIn){
            startActivity(new Intent(SplashActivity.this,GuideActivity.class));
        }else {
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }
        finish();
    }
}

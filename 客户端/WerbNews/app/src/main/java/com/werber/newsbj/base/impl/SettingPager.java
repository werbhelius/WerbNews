package com.werber.newsbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.werber.newsbj.base.BasePager;

/**
 * Created by acer-pc on 2015/11/13.
 */
public class SettingPager extends BasePager {


    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        mTvTitle.setText("设置");
        mImgBtnMenu.setVisibility(View.INVISIBLE);
        setSlidingMenuEnable(false);

        TextView text=new TextView(mActivity);
        text.setText("设置中心");
        text.setTextSize(25);
        text.setTextColor(Color.RED);
        text.setGravity(Gravity.CENTER);

        //添加到Content中
        mFlContent.addView(text);

    }
}

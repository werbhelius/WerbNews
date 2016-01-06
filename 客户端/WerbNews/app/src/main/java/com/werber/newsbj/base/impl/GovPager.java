package com.werber.newsbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.werber.newsbj.base.BasePager;

/**
 * Created by acer-pc on 2015/11/13.
 */
public class GovPager extends BasePager {


    public GovPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        mTvTitle.setText("人口管理");
        setSlidingMenuEnable(true);

        TextView text=new TextView(mActivity);
        text.setText("政务");
        text.setTextSize(25);
        text.setTextColor(Color.RED);
        text.setGravity(Gravity.CENTER);

        //添加到Content中
        mFlContent.addView(text);

    }
}

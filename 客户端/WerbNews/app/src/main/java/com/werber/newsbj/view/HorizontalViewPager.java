package com.werber.newsbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 11个页签页的viewpager，代码重构后暂时不用
 */
public class HorizontalViewPager extends ViewPager {
    public HorizontalViewPager(Context context) {
        super(context);
    }

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 事件分发, 请求父控件及祖宗控件是否拦截事件
     * 1. 右划, 而且是第一个页面, 需要父控件拦截
     * 2. 左划, 而且是最后一个页面, 需要父控件拦截
     * 3. 上下滑动, 需要父控件拦截
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getCurrentItem()!=0){
            getParent().requestDisallowInterceptTouchEvent(true);//用getParent()去请求
        }else {
            getParent().requestDisallowInterceptTouchEvent(false);//用getParent()去请求
        }

        return super.dispatchTouchEvent(ev);
    }
}

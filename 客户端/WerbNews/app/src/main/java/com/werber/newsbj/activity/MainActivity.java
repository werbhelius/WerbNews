package com.werber.newsbj.activity;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.werber.newsbj.R;
import com.werber.newsbj.fragment.ContentFragment;
import com.werber.newsbj.fragment.LeftMenuFragment;

/**
 * Created by acer-pc on 2015/11/10.
 */
public class MainActivity extends SlidingFragmentActivity {

    private static final String FRAGMENT_CONTENT = "fragment_content";
    private static final String FRAGMENT_LEFT_menu = "fragment_left_menu";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.main_left_menu);//设置侧滑布局
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);//左侧
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸
        slidingMenu.setBehindOffset(500);

        initFragments();

    }

    /**
     * 初始化两个Fragment，（侧边栏和内容）
     */
    private void initFragments() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//开启事物

        transaction.replace(R.id.fl_content, new ContentFragment(), FRAGMENT_CONTENT);
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), FRAGMENT_LEFT_menu);

        transaction.commit();
    }

    /**
     * 获取侧边栏
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm = getFragmentManager();
        LeftMenuFragment fragment= (LeftMenuFragment) fm.findFragmentByTag(FRAGMENT_LEFT_menu);
        return fragment;
    }

    /**
     * 获取内容
     * @return
     */
    public ContentFragment getContentFragment(){
        FragmentManager fm = getFragmentManager();
        ContentFragment fragment= (ContentFragment) fm.findFragmentByTag(FRAGMENT_CONTENT);
        return fragment;
    }

}

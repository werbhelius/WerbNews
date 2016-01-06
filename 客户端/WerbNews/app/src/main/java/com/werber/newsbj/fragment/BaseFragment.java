package com.werber.newsbj.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.werber.newsbj.activity.MainActivity;

/**
 * Fragment的基类
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;

    //fragment的创建
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }

    //实现fragment的布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initViews();
    }

    //依附的Activity创建完成
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    //初始化布局必须实现
    public abstract View initViews();

    //初始化数据，不是必须的
    public  void initData(){}


}

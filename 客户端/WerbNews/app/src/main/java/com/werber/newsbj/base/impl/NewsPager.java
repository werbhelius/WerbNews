package com.werber.newsbj.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.werber.newsbj.activity.MainActivity;
import com.werber.newsbj.base.BaseMenuDetailPager;
import com.werber.newsbj.base.BasePager;
import com.werber.newsbj.base.menudetail.InteractMenuDetailPager;
import com.werber.newsbj.base.menudetail.NewsMenuDetailPager;
import com.werber.newsbj.base.menudetail.PhotoMenuDetailPager;
import com.werber.newsbj.base.menudetail.TopicMenuDetailPager;
import com.werber.newsbj.bean.NewsData;
import com.werber.newsbj.global.GlobalContants;
import com.werber.newsbj.utils.CacheUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer-pc on 2015/11/13.
 */
public class NewsPager extends BasePager {

    private List<BaseMenuDetailPager> mMenuPagerList;
    private NewsData newsData;//网络解析的新闻数据

    public NewsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        setSlidingMenuEnable(true);//侧边栏可以打开

        String cache = CacheUtils.getCache(mActivity, GlobalContants.CATEGORIES_URL);
        //若缓存不为空,则直接从缓存中获取json数据
        if (!TextUtils.isEmpty(cache)){
            parseData(cache);
        }

        getDataFromServer();//无论是否有缓存,都要后台刷新新数据

    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer(){
        //使用xUtils3实现与服务器的交互
        RequestParams params = new RequestParams(GlobalContants.CATEGORIES_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("返回结果：" + result);
                parseData(result);

                //设置缓存
                CacheUtils.setCache(mActivity,GlobalContants.CATEGORIES_URL,result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 解析网络数据
     * @param result
     */
    private void parseData(String result) {
        Gson gson=new Gson();

        newsData = gson.fromJson(result, NewsData.class);
        System.out.println("解析结果：" + newsData);

        //刷新侧边栏数据
        MainActivity mainUI= (MainActivity) mActivity;
        mainUI.getLeftMenuFragment().getMenuData(newsData);

        //准备4个菜单栏详情页面
        mMenuPagerList =new ArrayList<>();
        mMenuPagerList.add(new NewsMenuDetailPager(mActivity,newsData.data.get(0).children));
        mMenuPagerList.add(new TopicMenuDetailPager(mActivity));
        mMenuPagerList.add(new PhotoMenuDetailPager(mActivity,imBtnPhotoType));
        mMenuPagerList.add(new InteractMenuDetailPager(mActivity));

        //第一次进入新闻界面为菜单详情第一页
        setCurrentMenuDetailPager(0);

    }

    /**
     * 设置当前菜单栏详情页
     */
    public void setCurrentMenuDetailPager(int position){
        BaseMenuDetailPager pager=mMenuPagerList.get(position);
        mFlContent.removeAllViews();//移除之前添加的View
        mFlContent.addView(pager.mRootView);

        //设置标题栏与侧边栏菜单详情标题匹配
        mTvTitle.setText(newsData.data.get(position).title);


        //这里调用initData方法
        pager.initData();

        //只有在组图界面才显示切换按钮
        if(pager instanceof PhotoMenuDetailPager){
            imBtnPhotoType.setVisibility(View.VISIBLE);
        }else {
            imBtnPhotoType.setVisibility(View.GONE);
        }
    }
}

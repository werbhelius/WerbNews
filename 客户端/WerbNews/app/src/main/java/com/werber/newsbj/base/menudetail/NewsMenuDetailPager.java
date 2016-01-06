package com.werber.newsbj.base.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;
import com.werber.newsbj.R;
import com.werber.newsbj.activity.MainActivity;
import com.werber.newsbj.base.BaseMenuDetailPager;
import com.werber.newsbj.base.TabDetailPager;
import com.werber.newsbj.bean.NewsData;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单详情页-新闻
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    private ViewPager vpNewsMenu;
    private TabPageIndicator indicator;//Tab标签页
    private List<TabDetailPager> mTabPagerList;//页签详情页
    private List<NewsData.NewsTabData> mNewsTabData;

    public NewsMenuDetailPager(Activity activity, List<NewsData.NewsTabData> children) {
        super(activity);

        mNewsTabData = children;
    }

    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
        vpNewsMenu = (ViewPager) view.findViewById(R.id.vp_news_menu);
        ImageButton nextPager= (ImageButton) view.findViewById(R.id.imbtn_next_pager);
        nextPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = vpNewsMenu.getCurrentItem();
                vpNewsMenu.setCurrentItem(++currentItem);
            }
        });

        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);

        indicator.setOnPageChangeListener(this);//当viewpager与indicator绑定时，滑动监听设置给indicator

        return view;
    }

    @Override
    public void initData() {
        mTabPagerList = new ArrayList<>();
        for (int i = 0; i < mNewsTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, mNewsTabData.get(i));
            mTabPagerList.add(pager);
        }

        vpNewsMenu.setAdapter(new MenuDetailAdapter());
        indicator.setViewPager(vpNewsMenu);//必须设置完viewpager的adapter

    }

    //滑动监听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MainActivity mainUi= (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        if(position==0){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //imbtn_next_pager的点击事件
//    @Event(value = R.id.imbtn_next_pager,
//            type = View.OnClickListener.class/*可选参数, 默认是View.OnClickListener.class*/)
//    public void nextPagerOnclick(View view) {
//        int currentItem = vpNewsMenu.getCurrentItem();
//        vpNewsMenu.setCurrentItem(++currentItem);
//    }

    class MenuDetailAdapter extends PagerAdapter {

        //重写此方法用于，返回页面标题，用于页签显示
        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsTabData.get(position).title;
        }

        @Override
        public int getCount() {
            return mTabPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mTabPagerList.get(position);
            container.addView(pager.mRootView);
            pager.initData();
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

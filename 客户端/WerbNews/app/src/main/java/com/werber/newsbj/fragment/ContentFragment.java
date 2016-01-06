package com.werber.newsbj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.werber.newsbj.R;
import com.werber.newsbj.base.BasePager;
import com.werber.newsbj.base.impl.GovPager;
import com.werber.newsbj.base.impl.HomePager;
import com.werber.newsbj.base.impl.NewsPager;
import com.werber.newsbj.base.impl.SettingPager;
import com.werber.newsbj.base.impl.SmartPager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by acer-pc on 2015/11/11.
 */
public class ContentFragment extends BaseFragment {

    private ViewPager vpContent;
    private RadioGroup rgGroup;
    private List<BasePager> mPageList;


    @Override
    public View initViews() {

        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        rgGroup= (RadioGroup) view.findViewById(R.id.rg_group);
        vpContent= (ViewPager) view.findViewById(R.id.vp_content);

        return view;
    }

    @Override
    public void initData() {
        rgGroup.check(R.id.rb_home);//默认勾选首页

        mPageList=new ArrayList<>();
//        for (int i = 0; i <5 ; i++) {
//            BasePager pager=new BasePager(mActivity);
//            mPageList.add(pager);
//        }
        mPageList.add(new HomePager(mActivity));
        mPageList.add(new NewsPager(mActivity));
        mPageList.add(new SmartPager(mActivity));
        mPageList.add(new GovPager(mActivity));
        mPageList.add(new SettingPager(mActivity));

        vpContent.setAdapter(new ContentAdapter());

        //监听RadioGroup的选择事件
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        vpContent.setCurrentItem(0,false);//设置当前的页面，取消平滑滑动
                        break;
                    case R.id.rb_news:
                        vpContent.setCurrentItem(1,false);//取消平滑滑动
                        break;
                    case R.id.rb_smart:
                        vpContent.setCurrentItem(2,false);//取消平滑滑动
                        break;
                    case R.id.rb_gov:
                        vpContent.setCurrentItem(3,false);//取消平滑滑动
                        break;
                    case R.id.rb_setting:
                        vpContent.setCurrentItem(4,false);//取消平滑滑动
                        break;
                }
            }
        });

        //通过viewPager的选择事件实现预加载数据
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPageList.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //手动加载第一页的数据
        mPageList.get(0).initData();
    }

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager=mPageList.get(position);
//            pager.initData(); 不能在这里初始化数据，因为会预加载
            container.addView(pager.mRootView);
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取新闻界面
     */
    public NewsPager getNewsPager(){
        NewsPager newsPager= (NewsPager) mPageList.get(1);
        return newsPager;
    }
}

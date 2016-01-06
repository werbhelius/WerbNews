package com.werber.newsbj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.werber.newsbj.R;
import com.werber.newsbj.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer-pc on 2015/11/9.
 */
public class GuideActivity extends AppCompatActivity {

    private static final int[] mImageId = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private ViewPager mViewPager;
    private List<ImageView> mImageList;
    private LinearLayout ll_point;//灰色圆点的父控件
    private int mPointWidth;//圆点间距
    private View viewRedPoint;//小红点
    private Button btnStart;//开始按钮

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        ll_point = (LinearLayout) findViewById(R.id.ll_point);
        viewRedPoint=findViewById(R.id.view_red_point);
        btnStart= (Button) findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.setBoolean(GuideActivity.this,"isFirstIn",false);
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
                finish();
            }
        });


        initView();
        mViewPager.setAdapter(new GuideAdapter());
        mViewPager.addOnPageChangeListener(new GuidePageListener());
    }

    /**
     * 初始化三个界面
     */
    private void initView() {
        mImageList = new ArrayList<>();

        //初始化三个viewpager
        for (int i = 0; i < mImageId.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(mImageId[i]);
            mImageList.add(iv);
        }

        //初始化三个灰色圆点
        for (int i = 0; i < mImageId.length; i++) {
            View point = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
            if (i > 0) {
                params.leftMargin = 40;
            }
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.shape_point_gray);
            ll_point.addView(point);
        }

        //获取试图树，对Layout结束时间进行监听
        ll_point.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //当Layout结束后回调此方法
            @Override
            public void onGlobalLayout() {
                System.out.println("Layout 结束");
                ll_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mPointWidth = ll_point.getChildAt(1).getLeft() - ll_point.getChildAt(0).getLeft();
                System.out.println("圆点距离："+mPointWidth);
            }
        });

    }

    //为ViewPager设置监听
    class GuidePageListener implements ViewPager.OnPageChangeListener {

        //滑动事件
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            System.out.println("当前位置：" + position + ";百分比：" + positionOffset + ";移动距离：" + positionOffsetPixels);
            int len= (int) (mPointWidth*positionOffset+mPointWidth*position);
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) viewRedPoint.getLayoutParams();
            params.leftMargin=len;
            viewRedPoint.setLayoutParams(params);
        }

        //某个页面被选中时
        @Override
        public void onPageSelected(int position) {
            if (position ==mImageId.length-1){
                btnStart.setVisibility(View.VISIBLE);
            }else {
                btnStart.setVisibility(View.INVISIBLE);
            }
        }

        //滑动状态发生改变时
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //为ViewPager设置数据适配器
    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageId.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageList.get(position));
            return mImageList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageList.get(position));
        }
    }
}

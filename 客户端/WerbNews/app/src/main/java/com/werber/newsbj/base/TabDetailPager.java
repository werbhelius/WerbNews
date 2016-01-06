package com.werber.newsbj.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;
import com.werber.newsbj.R;
import com.werber.newsbj.activity.NewsDetailActivity;
import com.werber.newsbj.bean.NewsData;
import com.werber.newsbj.bean.TabData;
import com.werber.newsbj.global.GlobalContants;
import com.werber.newsbj.utils.CacheUtils;
import com.werber.newsbj.utils.PrefUtils;
import com.werber.newsbj.view.RefreshListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 页签详情页
 */
public class TabDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    private NewsData.NewsTabData mTabData;//构造方法中的数据
    private TabData mTabDetailData;//网络解析的数据
    private List<TabData.TabNewsData> mTabNewsDataList;//List列表显示的数据
    private ArrayList<TabData.TopNewsData> mTopNewsDates;//头条新闻数据
    private String mMoreUrl;//更多页面地址
    private ImageOptions imageOptions;//xUtils3中关于图片的配置信息
    private RefreshListView mLvList;
    private NewsListAdapter mNewsAdapter;

    @ViewInject(R.id.vp_top_detail)
    private ViewPager mVpTopDetail;

    @ViewInject(R.id.tv_TopTitle)
    private TextView tvTopTitle;

    @ViewInject(R.id.circle_indicator)
    private CirclePageIndicator mIndicator;//头条新闻位置指示器

    private  Handler mHandler;

    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
    }

    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
        View headView=View.inflate(mActivity,R.layout.list_topnews_header,null);

        mLvList= (RefreshListView) view.findViewById(R.id.lv_tabList);

        x.view().inject(this, headView);//使用注解需要注册

        mLvList.addHeaderView(headView);

        //调用下拉刷新和加载更多接口
        mLvList.setOnRefreshListener(new RefreshListView.onRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                if (mMoreUrl != null) {
                    getMoreDataFromServer();
                } else {
                    Toast.makeText(mActivity, "没有内容了哟！", Toast.LENGTH_SHORT).show();
                    mLvList.onRefreshComplete(false);//这里传什么值都无所谓，因为是加载更多阶段，不会走到下拉刷新的逻辑代码
                }
            }
        });

        //listView的每一个点击事件
       mLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               System.out.println("被点击位置：" + position);

               //记录被点击的id，用字符串拼接的方式存起来
               String readIds=PrefUtils.getString(mActivity, "read_ids", "");
               String readId=mTabNewsDataList.get(position).id;
               if(!readIds.contains(readId)){
                   readIds=readIds+readId+",";
                   PrefUtils.setString(mActivity,"read_ids",readIds);
               }
               System.out.println(readIds);

               changeReadState(view);//实现局部刷新Title的颜色

               //跳转新闻详情页
               Intent intent=new Intent();
               intent.setClass(mActivity, NewsDetailActivity.class);
               intent.putExtra("url", GlobalContants.SERVER_URL+mTabNewsDataList.get(position).url);
               mActivity.startActivity(intent);
           }
       });

        return view;
    }

    @Override
    public void initData() {
        //获取缓存
        String cache = CacheUtils.getCache(mActivity, GlobalContants.SERVER_URL + mTabData.url);
        if(!TextUtils.isEmpty(cache)){
            parseData(cache,false);
        }
        getDataFromServer();
    }

    /**
     * 点击后标记已读，局部刷新Title的颜色
     */
    private void changeReadState(View view){
        TextView title= (TextView) view.findViewById(R.id.tv_title);
        title.setTextColor(Color.GRAY);
    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        //使用xUtils3实现与服务器的交互
        RequestParams params = new RequestParams(GlobalContants.SERVER_URL + mTabData.url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("页签详情页返回结果：" + result);
                parseData(result, false);

                mLvList.onRefreshComplete(true);

                //设置缓存
                CacheUtils.setCache(mActivity,GlobalContants.SERVER_URL + mTabData.url,result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), "网络不可用哦~", Toast.LENGTH_SHORT).show();

                mLvList.onRefreshComplete(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_SHORT).show();

                mLvList.onRefreshComplete(false);
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 从服务器获取更多数据
     */
    private void getMoreDataFromServer() {
        //使用xUtils3实现与服务器的交互
        RequestParams params = new RequestParams(mMoreUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("页签详情页返回结果：" + result);
                parseData(result,true);

                mLvList.onRefreshComplete(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(),  "网络不可用哦~", Toast.LENGTH_SHORT).show();

                mLvList.onRefreshComplete(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_SHORT).show();

                mLvList.onRefreshComplete(false);
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 解析网络数据
     *
     * @param result
     */
    private void parseData(String result,boolean isMore) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);
        System.out.println("页签详情页解析结果：" + mTabDetailData);

        mMoreUrl=mTabDetailData.data.more;//获取更多页面地址

        //处理下一页链接
        if (!TextUtils.isEmpty(mMoreUrl)){
            mMoreUrl=GlobalContants.SERVER_URL+mMoreUrl;
        }else {
            mMoreUrl=null;
        }

        if (!isMore){
            mTabNewsDataList=mTabDetailData.data.news;//List列表中显示的数据
            mTopNewsDates = mTabDetailData.data.topnews;//头条新闻数据

            //xUtils3的图片配置
            imageOptions = new ImageOptions.Builder()
                    .setLoadingDrawableId(R.mipmap.news_pic_default)//网络加载中的图片显示
                    .build();

            mVpTopDetail.setAdapter(new TopPagerAdapter());
            mLvList.setAdapter(new NewsListAdapter());

            if (mTopNewsDates != null) {
                tvTopTitle.setText(mTopNewsDates.get(0).title);//手动设置第一个topTitle的值
//        mVpTopDetail.addOnPageChangeListener(this);

                mIndicator.setViewPager(mVpTopDetail);
                mIndicator.setSnap(true);//快照
                mIndicator.setOnPageChangeListener(this);//viewPager和indicator一起时，应把监听加在indicator上
                mIndicator.setCurrentItem(0);//重新加载时，跳到第一个位置
            }

            if (mTabNewsDataList != null) {
                mNewsAdapter = new NewsListAdapter();
                mLvList.setAdapter(mNewsAdapter);
            }

            //实现头条新闻轮播条效果
            if (mHandler ==null){
                mHandler =new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
//                        System.out.println("handle.....");
                        int currentItem = mVpTopDetail.getCurrentItem();
                        if(currentItem<mTopNewsDates.size()-1){
                            currentItem++;
                        }else {
                            currentItem=0;
                        }

                        mVpTopDetail.setCurrentItem(currentItem);//设置当前viewPager的显示的item

                        mHandler.sendEmptyMessageDelayed(0, 3000);//过3秒再发送一条消息,实现轮播效果

                    }
                };

                mHandler.sendEmptyMessageDelayed(0, 3000);
            }

        }else {//如果是加载下一页，则需要把新数据追加到原来的集合上
            List<TabData.TabNewsData> news=mTabDetailData.data.news;
            mTabNewsDataList.addAll(news);//追加

            mNewsAdapter.notifyDataSetChanged();
            System.out.println("下一页地址："+mMoreUrl);
        }

    }

    //ViewPager的滑动监听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvTopTitle.setText(mTopNewsDates.get(position).title);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 头条新闻数据适配器
     */
    class TopPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mTopNewsDates.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(mActivity);
            image.setImageResource(R.mipmap.news_pic_default);
            image.setScaleType(ImageView.ScaleType.FIT_XY);

            //使用xUtils3加载图片
            String topImageUrl = mTopNewsDates.get(position).topimage;
            x.image().bind(image, GlobalContants.SERVER_URL + topImageUrl, imageOptions);//传递imageView对象和图片地址

            container.addView(image);

            image.setOnTouchListener(new TopNewsTouchListener());//设置触摸监听

            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 头条新闻触摸监听
     */
    class TopNewsTouchListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mHandler.removeCallbacksAndMessages(null);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mHandler.sendEmptyMessageDelayed(0,3000);
                    break;
                case MotionEvent.ACTION_UP:
                    mHandler.sendEmptyMessageDelayed(0,3000);
                    break;
            }
            return true;
        }
    }


    /**
     * 新闻列表数据适配器
     */
    class NewsListAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return mTabNewsDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mTabNewsDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewHolder holder;
            if(convertView==null){
                convertView=View.inflate(mActivity,R.layout.list_news_item,null);
                holder=new viewHolder(convertView);
                convertView.setTag(holder);
            }else {
                holder= (viewHolder) convertView.getTag();
            }

            //加载数据
            TabData.TabNewsData tabItem= (TabData.TabNewsData) getItem(position);
            holder.tvTitle.setText(tabItem.title);
            holder.tvDate.setText(tabItem.pubdate);

            x.image().bind(holder.ivPic,GlobalContants.SERVER_URL+tabItem.listimage,imageOptions);

            //标记已读的新闻id,在重新加载时变为已读状态
            String readIds = PrefUtils.getString(mActivity, "read_ids", "");
            if (readIds.contains(((TabData.TabNewsData) getItem(position)).id)) {
                holder.tvTitle.setTextColor(Color.GRAY);
            } else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }

            return convertView;
        }

        class viewHolder {
            private View rootView;
            private ImageView ivPic;
            private TextView tvTitle,tvDate;
            public viewHolder(View view){
                rootView=view;

                ivPic= (ImageView) rootView.findViewById(R.id.iv_pic);
                tvTitle= (TextView) rootView.findViewById(R.id.tv_title);
                tvDate= (TextView) rootView.findViewById(R.id.tv_date);

            }
        }
    }
}

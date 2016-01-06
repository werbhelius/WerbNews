package com.werber.newsbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.werber.newsbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by acer-pc on 2015/11/20.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private static final int STATE_PULL_REFRESH = 0;//下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;//松开刷新
    private static final int STATE_REFRESHING = 2;//正在刷新

    private View mHeaderView;//自定义刷新的refresh_header头布局
    private View mFooterView;//自定义刷新的refresh_footer脚布局
    private int startY = -1;//下拉刷新开始坐标
    private int mHeaderHeight;//下拉刷新的高度
    private int mFooterHeight;//加载更多的高度

    private int mCurrentState = STATE_PULL_REFRESH;//刷新状态


    //自定义的下拉刷新使用到的控件
    private TextView reTvTitle,reTvDate;
    private ImageView reArr;
    private ProgressBar reProgressBar;

    private RotateAnimation animUp,animDown;//向上向下动画

    private onRefreshListener mRefreshListener;

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        this.addHeaderView(mHeaderView);

        reTvTitle= (TextView) mHeaderView.findViewById(R.id.re_tv_title);
        reTvDate= (TextView) mHeaderView.findViewById(R.id.re_tv_date);
        reArr= (ImageView) mHeaderView.findViewById(R.id.refresh_arr);
        reProgressBar= (ProgressBar) mHeaderView.findViewById(R.id.refresh_progressBar);

        //隐藏
        mHeaderView.measure(0, 0);

        mHeaderHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);

        initArrAnim();

        reTvDate.setText("最后刷新时间：" + getCurrentTime());//第一次刷新时
    }

    /**
     *初始化脚布局
     */
    private void initFooterView(){
        mFooterView=View.inflate(getContext(),R.layout.refresh_listview_footer,null);
        this.addFooterView(mFooterView);

        mFooterView.measure(0, 0);
        mFooterHeight=mFooterView.getMeasuredHeight();

        mFooterView.setPadding(0, -mFooterHeight, 0, 0);//隐藏脚布局
        this.setOnScrollListener(this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {// 确保startY有效
                    startY = (int) ev.getRawY();
                }
                int endY = (int) ev.getRawY();
                int dy = endY - startY;// 移动便宜量

                if (dy > 0 && getFirstVisiblePosition() == 0) {// 只有下拉并且当前是第一个item,才允许下拉
                    int padding = dy - mHeaderHeight;// 计算padding
                    mHeaderView.setPadding(0, padding, 0, 0);// 设置当前padding



                    if (padding > 0 && mCurrentState != STATE_RELEASE_REFRESH) {
                        mCurrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_REFRESH) {
                        mCurrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }

                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;// 重置

                if(mCurrentState==STATE_RELEASE_REFRESH){
                    mCurrentState=STATE_REFRESHING;//正在刷新
                    refreshState();
                    mHeaderView.setPadding(0, 0, 0, 0);
                }else if(mCurrentState==STATE_PULL_REFRESH){
                    mHeaderView.setPadding(0,-mHeaderHeight,0,0);//隐藏
                }

                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 更新状态
     */
    private void refreshState() {
        switch (mCurrentState){
            case STATE_PULL_REFRESH:
                reTvTitle.setText("下拉刷新");
                reArr.setVisibility(VISIBLE);
                reArr.startAnimation(animDown);//箭头向下动画
                reProgressBar.setVisibility(INVISIBLE);
                break;
            case STATE_RELEASE_REFRESH:
                reTvTitle.setText("松开刷新");
                reArr.startAnimation(animUp);//箭头向上动画
                break;
            case STATE_REFRESHING:
                reTvTitle.setText("正在刷新");
                reArr.clearAnimation();//必须先清除动画，否则会一直保持动画状态，不会消失
                reArr.setVisibility(INVISIBLE);
                reProgressBar.setVisibility(VISIBLE);

                if(mRefreshListener!=null){
                    mRefreshListener.onRefresh();
                }

                break;
        }
    }

    /**
     * 箭头向上向下的动画
     */
    private void initArrAnim(){
        //箭头向上动画
        animUp=new RotateAnimation(0,-180,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        //箭头向下动画
        animDown=new RotateAnimation(-180,0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }

    /**
     * 收起下拉刷新和加载更多控件
     */
    public void onRefreshComplete(boolean success){
        if (isLoadingMore){
            mFooterView.setPadding(0,-mFooterHeight,0,0);//隐藏脚布局
            isLoadingMore=false;
        }else {
            mCurrentState= STATE_PULL_REFRESH;
            reTvTitle.setText("下拉刷新");
            reArr.setVisibility(VISIBLE);
            reArr.startAnimation(animDown);//箭头向下动画
            reProgressBar.setVisibility(INVISIBLE);

            mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);

            if(success){
                reTvDate.setText("最后刷新时间："+getCurrentTime());
            }
        }


    }

    /**
     * 获取当前时间
     */
    private String getCurrentTime(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    //脚布局实现的接口
    private boolean isLoadingMore=false;//是否加载更多，默认为false

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING){
            if (getLastVisiblePosition()==getCount()-1&&!isLoadingMore){
                System.out.println("到底了.......");
                mFooterView.setPadding(0, 0, 0, 0);//显示脚布局
                setSelection(getCount()-1);//显示到最后一个item

                isLoadingMore=true;

                if (mRefreshListener!=null){
                    mRefreshListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //重写listView的点击事件(因为加了头布局和轮播条，所以每一个item都+2)
    OnItemClickListener mOnItemClickListener;

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this);

        mOnItemClickListener=listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mOnItemClickListener!=null){
            mOnItemClickListener.onItemClick(parent,view,position-getHeaderViewsCount(),id);
        }
    }

    /**
     * 下拉刷新接口
     */
    public interface onRefreshListener{
        void onRefresh();
        void onLoadMore();
    }

    /**
     * 下拉刷新方法
     */
    public void setOnRefreshListener(onRefreshListener listener){
        mRefreshListener=listener;
    }
}

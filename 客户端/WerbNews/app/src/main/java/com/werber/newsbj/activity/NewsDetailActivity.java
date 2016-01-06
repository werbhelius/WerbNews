package com.werber.newsbj.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.werber.newsbj.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;


/**
 * Created by acer-pc on 2015/12/4.
 */
public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView mWebView;
    private ImageButton btnBack;
    private ImageButton btnSize;
    private ImageButton btnShare;

    private ProgressBar pbProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_detail);

        mWebView = (WebView) findViewById(R.id.wv_web);
        btnBack = (ImageButton) findViewById(R.id.Imbtn_back);
        btnSize = (ImageButton) findViewById(R.id.Imbtn_size);
        btnShare = (ImageButton) findViewById(R.id.Imbtn_share);

        //btn点击事件
        btnBack.setOnClickListener(this);
        btnSize.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        pbProgress= (ProgressBar) findViewById(R.id.pb_progress);

        String url = getIntent().getStringExtra("url");

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//支持JS
        settings.setBuiltInZoomControls(true);//显示放大缩小按钮
        settings.setUseWideViewPort(true);//支持双击放大缩小

        mWebView.setWebViewClient(new WebViewClient() {

            /**
             *网页开始加载
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                System.out.println("网页开始加载");
                pbProgress.setVisibility(View.VISIBLE);
            }

            /**
             * 网页加载结束
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("网页加载结束");
                pbProgress.setVisibility(View.INVISIBLE);
            }

            /**
             *所有跳转的链接都在此方法中回调
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            /**
             *网页加载进度改变
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                System.out.println("加载进度："+newProgress);
                super.onProgressChanged(view, newProgress);
                pbProgress.setProgress(newProgress);
            }

            /**
             * 网页title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("网页title："+title);
                super.onReceivedTitle(view, title);
            }
        });

        mWebView.loadUrl(url);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Imbtn_back:
                finish();
                break;
            case R.id.Imbtn_size:
                showChooseDialog();
                break;
            case R.id.Imbtn_share:
                showShare();
                break;
        }
    }

    /**
     * 选择改变文字大小
     */
    private int mCurrentChooseItem;//记录当前选中的item,点击确定前
    private int mCurrentItem=2;//记录当前选中的item,点击确定后

    private void showChooseDialog() {
        String[] items=new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("字号设置")
                .setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCurrentChooseItem=which;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WebSettings settings = mWebView.getSettings();
                        switch (mCurrentChooseItem) {
                            case 0:
                                settings.setTextSize(WebSettings.TextSize.LARGEST);
                                break;
                            case 1:
                                settings.setTextSize(WebSettings.TextSize.LARGER);
                                break;
                            case 2:
                                settings.setTextSize(WebSettings.TextSize.NORMAL);
                                break;
                            case 3:
                                settings.setTextSize(WebSettings.TextSize.SMALLER);
                                break;
                            case 4:
                                settings.setTextSize(WebSettings.TextSize.SMALLEST);
                                break;
                        }
                        mCurrentItem=mCurrentChooseItem;
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    /**
     * 利用第三方ShareSDK实现分享功能
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();

        oks.setTheme(OnekeyShareTheme.CLASSIC);//设置主题

        // 关闭sso授权
        oks.disableSSOWhenAuthorize();

//        // 分享时Notification的图标和文字
//        oks.setNotification(R.drawable.ic_launcher,
//                getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setImageUrl("http://c.hiphotos.baidu.com/image/pic/item/5bafa40f4bfbfbed91fbb0837ef0f736aec31faf.jpg");
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}

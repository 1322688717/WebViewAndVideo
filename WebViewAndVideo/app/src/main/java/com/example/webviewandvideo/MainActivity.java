package com.example.webviewandvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

import me.jingbin.web.ByWebView;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private FrameLayout fullVideo;
    private View customView;
    private ByWebView byWebView;
    private AgentWeb mAgentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initWebView();
        //initByWebView();
        //initGoogleWebView();
    }

    private void initView() {
        webView = findViewById(R.id.wb_view);
        fullVideo = findViewById(R.id.full_video);
    }

    private void initGoogleWebView() {
        com.just.agentweb.WebViewClient mWebViewClient= new com.just.agentweb.WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //do you  work
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        };
        com.just.agentweb.WebChromeClient mWebChromeClient= new com.just.agentweb.WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //do you work
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                //进入全屏
                customView = view;
                fullVideo.setVisibility(View.VISIBLE);
                fullVideo.addView(customView);
                fullVideo.bringToFront();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置横屏
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏

            }

            @Override
            public void onHideCustomView() {
                if (customView == null) {
                    return;
                }
                //移除全屏视图并隐藏
                fullVideo.removeView(customView);
                fullVideo.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//清除全屏
            }
        };

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((FrameLayout) fullVideo, new ViewGroup.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go("https://www.baidu.com/");



    }

    private void initByWebView() {
        byWebView = ByWebView
                .with(this)
                .setWebParent(fullVideo, new LinearLayout.LayoutParams(-1, -1))
                .useWebProgress(ContextCompat.getColor(this, R.color.purple_200))
                .loadUrl("https://www.baidu.com/");
    }

    private void initWebView() {

        //设置WebView属性，能够执行javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;

            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onHideCustomView() {
                //退出全屏
                if (customView == null){
                    return;
                }
                //移除全屏视图并隐藏
                fullVideo.removeView(customView);
                fullVideo.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//清除全屏

            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                //进入全屏
                customView = view;
                fullVideo.setVisibility(View.VISIBLE);
                fullVideo.addView(customView);
                fullVideo.bringToFront();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置横屏
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏

            }
        });
        webView.loadUrl("https://www.baidu.com/");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return false;
    }



}
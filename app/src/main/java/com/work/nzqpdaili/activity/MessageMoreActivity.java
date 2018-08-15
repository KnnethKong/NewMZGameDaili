package com.work.nzqpdaili.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.work.nzqpdaili.R;
import com.work.nzqpdaili.view.TitleBarUtils;

import zuo.biao.library.base.BaseActivity;

/**
 * Created by range on 2017/12/25.
 */

public class MessageMoreActivity extends BaseActivity {
    private WebView webView;
    private String content,tit;
    private TextView title;
    @Override
    public Activity getActivity() {
        return this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagemore);
        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.ll_space);
        int color = 0xffffffff;
        linearLayout.setBackgroundColor(color);
        setImmersiveStatusBar(true, color,linearLayout);

        initView();
        initData();
        initEvent();
    }
    @Override
    public void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("消息详情");
        TitleBarUtils.initBtnBack(this, R.id.tv_back);
     webView= (WebView) findViewById(R.id.webview);
      title= (TextView) findViewById(R.id.title);
    }

    @Override
    public void initData() {
        content=getIntent().getStringExtra("content");
        tit=getIntent().getStringExtra("title");
        title.setText(tit);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(false);
        ws.setAllowFileAccess(true);
        ws.setBuiltInZoomControls(false);
        ws.setSupportZoom(false);
/**
 *  设置网页布局类型：
 *  1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小
 *  2、LayoutAlgorithm.SINGLE_COLUMN: 适应屏幕，内容将自动缩放
 *
 */
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setDefaultTextEncodingName("utf-8"); //设置文本编码
        ws.setAppCacheEnabled(true);
//        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        webView.getSettings().setLoadsImagesAutomatically(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式</span>

        webView.setBackgroundColor(Color.parseColor("#00000000"));

        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                    android.util.Log.e("1result-->",request.getUrl().toString());
                } else {
                    view.loadUrl(request.toString());
                    android.util.Log.e("2result-->",request.toString());
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view,String url)
            {

            }

        });
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }

    @Override
    public void initEvent() {

    }
}

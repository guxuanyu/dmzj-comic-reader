package com.example.ganger.dmzjapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.Date;

import models.ComicCollection;
import models.ComicHistory;
import models.RecentHistory;

public class WebActivity extends AppCompatActivity {

    private int nowPageType=0;
    private WebView webView;
    private String title="";
    private String loadUrl="";
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private CoordinatorLayout rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
    }

    public void initView(){
        rootView= (CoordinatorLayout) findViewById(R.id.web_root);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        webView = new WebView(getApplicationContext());
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        LinearLayout ll= (LinearLayout) findViewById(R.id.weblinear);
        swipeRefreshLayout.addView(webView);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.pink));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(webView.getUrl());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        setSettings();
        setWebView(url);
        floatingActionButton= (FloatingActionButton) findViewById(R.id.floatbutton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(rootView,"收藏此漫画吗?",Snackbar.LENGTH_LONG).setAction("收藏", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveFavorite();
                    }
                }).show();

            }
        });
        floatingActionButton.hide();
    }

    public void saveFavorite(){
        if (DataSupport.where("title=?", title).find(ComicCollection.class).size()==0) {
            Log.i("收藏了", loadUrl + title);
            ComicCollection comicCollection = new ComicCollection();
            comicCollection.setDate(new Date());
            comicCollection.setTitle(title);
            comicCollection.setUrl(loadUrl);
            comicCollection.saveOrUpdate("title=?",comicCollection.getTitle());
            Snackbar.make(rootView,"收藏成功",Snackbar.LENGTH_SHORT).show();
        }
        else {
            Log.i("已经收藏过了", loadUrl + title);
            //Toast.makeText(WebActivity.this, "收藏过了", Toast.LENGTH_SHORT).show();
            Snackbar.make(rootView,"已经收藏过此漫画了",Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.toolbar_share:
                //Snackbar.make(rootView,"分享",Snackbar.LENGTH_SHORT).show();
                Intent intent1=new Intent(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_TEXT,"这个漫画不错哦 "+title+loadUrl);
                intent1.setType("text/plain");
                startActivity(Intent.createChooser(intent1,"share"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }

    public void setSettings(){
        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Log.i("new ","mew ");
                Log.i("url   ",url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("newasdasd ","mewasdasd ");
                Log.i("url   ",url);
                loadUrl=url;
                floatingActionButton.hide();
                if(url.contains("http://m.dmzj.com/view/")){
                    Log.i("观看历史 标题 ",title+url);

                    nowPageType=1;

                }
                else if(url.contains("http://m.dmzj.com/info/")){
                    Log.i("历史 标题 ",title+url);

                    nowPageType=2;

                    floatingActionButton.show();
                }
                else {
                    nowPageType=0;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.i("beforeTitle   ",title);
                String[] sourceStrArray = title.split("-");
                title=sourceStrArray[0];
                Log.i("title   ",title);
                toolbar.setTitle(title);
                WebActivity.this.title=title;
                saveHistory();
            }
        });
    }
    public void setWebView(String url){
//            if(url==null&&url==""){
//                Log.i("兄弟url  ","是空的eeeee");
//                return;
//            }
        webView.loadUrl(url);
            //webView.loadUrl("http://manhua.dmzj.com/tags/search.shtml?s=巨人");
    }
    public void saveHistory(){
        if(nowPageType==1){
            saveRecentHistory();
        }
        else if(nowPageType==2){
            saveComicHistory();
        }
    }
    public void saveRecentHistory() {

        RecentHistory recentHistory = new RecentHistory();
        recentHistory.setUrl(loadUrl);
        recentHistory.setTitle(title);
        recentHistory.setDate(new Date());
        recentHistory.saveOrUpdate("title=?", recentHistory.getTitle());
        //RecentHistory rh = DataSupport.findLast(RecentHistory.class);
        //Toast.makeText(WebActivity.this, "id 11111 " + rh.getTitle(), Toast.LENGTH_SHORT).show();
    }


    public void saveComicHistory() {

        ComicHistory comicHistory = new ComicHistory();
        comicHistory.setUrl(loadUrl);
        comicHistory.setTitle(title);
        comicHistory.setDate(new Date());
        comicHistory.saveOrUpdate("title=?", comicHistory.getTitle());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }



        @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}

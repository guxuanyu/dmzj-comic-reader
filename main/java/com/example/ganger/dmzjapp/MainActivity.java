package com.example.ganger.dmzjapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import layout.FragmentComicHistory;
import layout.FragmentFavorite;
import layout.FragmentRecentHistory;
import layout.FragmentTab;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private CoordinatorLayout coordinatorLayout;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager mLayoutManager;
    private List datalist;
    private MyAdapter myAdapter;
    private AppCompatImageView imageSetting;
    private AppBarLayout appBarLayout;
    private ImageView imageView;
    private boolean isBannerAniming=false;
    private boolean isBannerBig=false;
    private LinearLayout searchLinearLayout;
    private ImageView lunyii;
    private AppCompatImageView imageCollection;
    private ViewPager viewPager;
    private DachshundTabLayout dachshundTabLayout;
    private MyPageAdapter pageAdapter;
    private FragmentTab f2;
    private boolean isHead2=true;
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ButterKnife.bind(this);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Glide.with(this).load(AppConfig.getBigImageSrc()).listener(reListener).into(imageView);
    }

    RequestListener reListener=new RequestListener() {
        @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }
    };

    private void initView(){
        //getNews();
        //searchComic();
        //getImageUrl();
        //relativeLayout= (RelativeLayout) findViewById(R.id.rl);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.coord);
        appBarLayout= (AppBarLayout) findViewById(R.id.appbarlayout);
        //button= (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Snackbar.make(relativeLayout,"这是massage", Snackbar.LENGTH_LONG).setAction("这是action", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(MainActivity.this,"你点击了action", Toast.LENGTH_SHORT).show();
//                    }
//                }).show();
//            }
//        });
        //recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        gridLayoutManager=new GridLayoutManager(MainActivity.this,3,GridLayoutManager.VERTICAL,false);
        mLayoutManager=new LinearLayoutManager(this);
//        recyclerView.addItemDecoration(new SpaceItemD(5));
//        //getData();
//        recyclerView.setLayoutManager(mLayoutManager);

        imageView= (ImageView) findViewById(R.id.imageview);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("111111   ","click!!!!");
//                final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
//                layoutParams.height+=100;
//                appBarLayout.setLayoutParams(layoutParams);
                if (isBannerAniming) {
                    return;
                }
                startBannerAnim();
            }
        });
        SharedPreferences share=getSharedPreferences("setting",MODE_PRIVATE);
        String imgUrl=share.getString("bigimageurl","");
        if(imgUrl!=""){
            Uri url=Uri.parse(imgUrl);
            Glide.with(this).load(url).override(720, 800) .listener(reListener).into(imageView);
        }

        imageSetting= (AppCompatImageView)findViewById(R.id.imagesetting);
        imageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("setting   ","click!!!!");
                //startActivity(new Intent(MainActivity.this,SetActivity.class));

                openGallery();

            }
        });
        searchLinearLayout= (LinearLayout) findViewById(R.id.ll_home_search);
        searchLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("search   ","click!!!!");
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });
        imageCollection= (AppCompatImageView) findViewById(R.id.iv_home_collection);
        imageCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("collection  ","click!!!!");
                //viewPager.setCurrentItem(0);
                SharedPreferences sharedPreferences=getSharedPreferences("setting",MODE_PRIVATE);
                int src=sharedPreferences.getInt("bigimage",R.drawable.head2);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                if(src==R.drawable.head2){
                    editor.putInt("bigimage",R.drawable.head);
                    Toast.makeText(MainActivity.this, "换成了1", Toast.LENGTH_SHORT).show();
                }
                else {
                    editor.putInt("bigimage",R.drawable.head2);
                    Toast.makeText(MainActivity.this, "换成了2", Toast.LENGTH_SHORT).show();
                }
                editor.commit();

                //setBigImg();
//                Intent intent=new Intent(Intent.ACTION_SEND);
//                intent.setType("*/*");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
//                intent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(Intent.createChooser(intent, getTitle()));

            }
        });


        viewPager= (ViewPager) findViewById(R.id.viewpager);

        List<String> titles=new ArrayList<String>();
        titles.add("收藏");
        titles.add("最近更新");
        titles.add("观看历史");
        titles.add("历史");

        List<Fragment> fs=new ArrayList<>();
        FragmentFavorite f1=new FragmentFavorite();
        f2=new FragmentTab();
        FragmentRecentHistory f3=new FragmentRecentHistory();
        FragmentComicHistory f4=new FragmentComicHistory();

        fs.add(f1);
        fs.add(f2);
        fs.add(f3);
        fs.add(f4);



        pageAdapter=new MyPageAdapter(getSupportFragmentManager(),titles,fs);
        viewPager.setAdapter(pageAdapter);
//
        dachshundTabLayout= (DachshundTabLayout) findViewById(R.id.tab_layout);
        dachshundTabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);


//        lunyii= (ImageView) findViewById(R.id.lunyii);
//        lunyii.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("111111   ","click!!!!");
//                Snackbar.make(coordinatorLayout,"您可真厉害",Snackbar.LENGTH_SHORT).show();
//            }
//        });
        //getNews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                //Toast.makeText(MainActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                Log.i("uri11111","1  "+uri.toString());
                Glide.with(MainActivity.this).load(uri).override(720, 800) .listener(reListener).into(imageView);

                SharedPreferences s=getSharedPreferences("setting",MODE_PRIVATE);
                SharedPreferences.Editor editor=s.edit();
                editor.putString("bigimageurl",uri.toString());
                editor.commit();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void openGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    private void setBigImg(){
        String url=f2.getRandomUrl();
        //Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences=getSharedPreferences("setting",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("lasturl",url);
        editor.commit();
        if(url==null&&url==""){
            Snackbar.make(coordinatorLayout,"加载出了点小问题",Snackbar.LENGTH_SHORT).show();
            return;
        }
        Glide.with(this).load(url)
                 .listener(listener)
                .into(imageView);
    }

    RequestListener<String, GlideDrawable> listener = new RequestListener<String, GlideDrawable>(){
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            Log.i("eeeee",e.toString());
            //Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            //Snackbar.make(coordinatorLayout,"图丢了，换一张吧",Snackbar.LENGTH_SHORT).show();
            Glide.with(MainActivity.this).load(R.drawable.head2).override(720, 800) .listener(reListener).into(imageView);
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            Log.i("rrrrr",model.toString());
            return false;
        }
    };

    class MyPageAdapter extends FragmentPagerAdapter{

        List<String> titles;
        private List<Fragment> mFragments;
        public MyPageAdapter(FragmentManager fm,List ts,List fs) {
            super(fm);
            titles=ts;
            mFragments=fs;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

//    public void getData(){
//        datalist=new ArrayList();
//        //new GetData().execute();
//
//        for (int i=0;i<100;i++){
//            datalist.add("data-----------: "+String.valueOf(i));
//            Log.i("----------------- i : ",String.valueOf(i));
//        }
//        myAdapter=new MyAdapter(datalist);
//        Random random=new Random();
//        recyclerView.setAdapter(myAdapter);
//    }

    class MyAdapter extends RecyclerView.Adapter {

        private List data;

        public MyAdapter(List l) {
            data = l;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext ()).inflate(R.layout.list_item, parent, false );
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            View v=holder.itemView;
            TextView textView= (TextView) v.findViewById(R.id.text_item);
            textView.setText(data.get(position).toString());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            protected TextView tv;
            public MyViewHolder(View itemView) {
                super(itemView);
                tv= (TextView) findViewById(R.id.text_item);
            }
        }
    }


    public void startBannerAnim(){
        final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
        ValueAnimator animator;
        if (isBannerBig) {
            animator = ValueAnimator.ofInt(DisplayUtils.getScreenHeight(this), DisplayUtils.dp2px(240, this));
        } else {
            animator = ValueAnimator.ofInt(DisplayUtils.dp2px(240, this), DisplayUtils.getScreenHeight(this));
        }
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.height = (int) valueAnimator.getAnimatedValue();
                appBarLayout.setLayoutParams(layoutParams);


            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isBannerBig = !isBannerBig;
                isBannerAniming = false;
            }
        });
        animator.start();

//        if(!isBannerBig){
//            ObjectAnimator oa=ObjectAnimator.ofFloat(imageSetting,"alpha",0f,1f);
//            oa.setDuration(1000);
//            oa.start();
//        }
//        else {
//            ObjectAnimator oa=ObjectAnimator.ofFloat(imageSetting,"alpha",1f,0f);
//
//            oa.setDuration(1000);
//            oa.start();
//        }
        isBannerAniming = true;
    }

    @Override
    public void onBackPressed() {
        if (isBannerAniming) {
            return;
        }
        if (isBannerBig) {
            startBannerAnim();
        } else {
            super.onBackPressed();
        }
    }

    //    public void setWebView(){
//        webView= (WebView) findViewById(R.id.webView);
//        WebSettings webSettings = webView.getSettings();
//        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
//        webSettings.setJavaScriptEnabled(true);
//        //设置自适应屏幕，两者合用
//        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
//        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//
//        webView.loadUrl("http://manhua.dmzj.com/xieedejiangkoujun/64153.shtml#@page=1");
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
//    }

    public void getNews(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url=new URL("http://manhua.dmzj.com/rss.xml");
                    HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(10000);
                    if(httpURLConnection.getResponseCode()==200){
                        InputStream is=httpURLConnection.getInputStream();
                        List list=XmlParser.parserNews(is);
                        XmlParser.parserItems(list);
                    }

//                    for(){
//                        Log.i("doc",d);
//                    }
//                    Log.i("doc",d);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
//
//    public void searchComic(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Document doc = Jsoup.connect("http://manhua.dmzj.com/tags/search.shtml?s=%E5%B0%91%E5%B9%B4").timeout(10000).get();
//                    Element div = doc.select("div.tcaricature_block.tcaricature_block2").first();
//                    Elements uls=div.children();
//                    for(Element e:uls){
//                        String tem=e.text();
//                        Log.i("divs---- ",tem);
//                    }
////                    String tem=tcaricature_new.text();
////                    Log.i("divs---- ",tem);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    public void getImageUrl(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Document doc = Jsoup.connect("http://manhua.dmzj.com//nizhuanjiandu//60206.shtml#@page=1").timeout(10000).get();
//                    Element div=doc.select("div#center_box").first();
//                    Element img=div.child(0);
//                    String imgUrl=img.attr("src");
//                    Log.i("src------ ","src------ ");
//                    Log.i("src------ ",imgUrl);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

}

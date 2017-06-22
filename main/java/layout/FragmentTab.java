package layout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import models.News;
import com.example.ganger.dmzjapp.R;
import com.example.ganger.dmzjapp.SpaceItemD;
import com.example.ganger.dmzjapp.WebActivity;
import com.example.ganger.dmzjapp.XmlParser;
import com.yalantis.taurus.PullToRefreshView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

/**
 * Created by ganger on 2017/6/14.
 */
public class FragmentTab extends Fragment {
    private List<News> datalist;
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PullToRefreshView mPullToRefreshView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        if(null==view) {
            view = inflater.inflate(R.layout.fragmenttab, container, false);
            swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.pink));
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getNews();
                }
            });
//            mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.pull_to_refresh);
//            mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    mPullToRefreshView.setRefreshing(false);
//                }
//            });

            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new SpaceItemD(2));

            getNews();


            //getData();
        }
        return view;
    }


//    public void getData() {
//        datalist=new ArrayList();
//        //new GetData().execute();
//
//        for (int i=0;i<100;i++){
//            datalist.add("data-----------: "+String.valueOf(i));
//            //Log.i("----------------- i : ",String.valueOf(i));
//        }
//        myAdapter=new MyAdapter(datalist);
//        Random random=new Random();
//        recyclerView.setAdapter(myAdapter);
//    }

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //myAdapter=new MyAdapter(datalist);
            if(myAdapter==null) {
                myAdapter=new MyAdapter(datalist);
                recyclerView.setAdapter(myAdapter);
            }
            else {
                myAdapter.setData(datalist);
                myAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
            return false;
        }
    });

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
                        List list= XmlParser.parserNews(is);
                        datalist=XmlParser.parserItems(list);
                        //Log.i("完了111111","1111");
                        Message m=new Message();
                        handler.sendMessage(m);
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

    public String getRandomUrl(){
        Random random=new Random();
        int n2=random.nextInt(datalist.size());
        return datalist.get(n2).getImageUrl();
    }
    RequestListener<String, GlideDrawable> listener = new RequestListener<String, GlideDrawable>(){

        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            Log.i("eeeee",e.toString());
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            Log.i("rrrrr",model.toString());
            return false;
        }
    };

    class MyAdapter extends RecyclerView.Adapter {

        private List<News> data;
        private boolean isshow=false;
        public MyAdapter(List l) {
            data = l;
        }

        public void setData(List l){
            data=l;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SharedPreferences sharedPreferences=getContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
            isshow=sharedPreferences.getBoolean("isshowimage",false);
            View itemView =null;
            if(isshow) {
                itemView =LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_item_pic, parent, false);
            }
            else {
                 itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_item, parent, false);
            }
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            View v = holder.itemView;
            if(isshow) {
                ImageView iv = (ImageView) v.findViewById(R.id.imageView2);
                Glide.with(getActivity()).load(data.get(position).getImageUrl())
                        // .listener(listener)
                        .into(iv);
            }
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),WebActivity.class);
                    intent.putExtra("url",data.get(position).getRecentUrl());
                    startActivity(intent);
                }
            });
            TextView retitle= (TextView) v.findViewById(R.id.tx_item);
            retitle.setText(data.get(position).getTitle());

            TextView author= (TextView) v.findViewById(R.id.text_author);
            author.setText(data.get(position).getAuthor());

            TextView title= (TextView) v.findViewById(R.id.text_title);
            title.setText(data.get(position).getRecentTitle());
            //TextView textView = (TextView) v.findViewById(R.id.text_item);
            //textView.setText(data.get(position).toString());

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            //protected TextView tv;
            public MyViewHolder(View itemView) {
                super(itemView);
                //tv= (TextView) findViewById(R.id.text_item);
            }
        }
    }



}

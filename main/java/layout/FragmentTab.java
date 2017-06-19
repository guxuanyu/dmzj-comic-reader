package layout;

import android.content.Intent;
import android.graphics.Rect;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.ganger.dmzjapp.News;
import com.example.ganger.dmzjapp.R;
import com.example.ganger.dmzjapp.SpaceItemD;
import com.example.ganger.dmzjapp.WebActivity;
import com.example.ganger.dmzjapp.XmlParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ganger on 2017/6/14.
 */
public class FragmentTab extends Fragment {
    private List datalist;
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
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
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new SpaceItemD(2));
            getNews();


            //getData();
        }
        return view;
    }

    public void getData() {
        datalist=new ArrayList();
        //new GetData().execute();

        for (int i=0;i<100;i++){
            datalist.add("data-----------: "+String.valueOf(i));
            //Log.i("----------------- i : ",String.valueOf(i));
        }
        myAdapter=new MyAdapter(datalist);
        Random random=new Random();
        recyclerView.setAdapter(myAdapter);
    }

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

    RequestListener<String, GlideDrawable> listener = new RequestListener<String, GlideDrawable>(){

        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            Log.i("eeeee",e.toString());
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

        public MyAdapter(List l) {
            data = l;
        }

        public void setData(List l){
            data=l;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            View v = holder.itemView;
//            ImageView iv= (ImageView) v.findViewById(R.id.item_image);
//            Glide.with(getActivity()).load("http://upload-images.jianshu.io/upload_images/6128483-c0ee259c603a9cb1.PNG?imageMogr2/auto-orient/strip%7CimageView2/2")
//                   // .listener(listener)
//                    .into(iv);
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

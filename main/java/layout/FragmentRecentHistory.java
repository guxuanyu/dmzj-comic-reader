package layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ganger.dmzjapp.R;
import com.example.ganger.dmzjapp.SpaceItemD;
import com.example.ganger.dmzjapp.WebActivity;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.List;

import models.ComicCollection;
import models.ComicHistory;
import models.RecentHistory;

/**
 * Created by ganger on 2017/6/17.
 */
public class FragmentRecentHistory extends Fragment{
    private List<RecentHistory> recentHistoryList;
    private RecyclerView recyclerView;
    private View view;
    private MyAdapter myAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_fragmentab, container, false);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new SpaceItemD(2));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getComicHistory();
    }

    public void getComicHistory(){
        recentHistoryList= DataSupport.order("id desc").find(RecentHistory.class);
        if(myAdapter==null){
            myAdapter=new MyAdapter(recentHistoryList);
            recyclerView.setAdapter(myAdapter);
        }
        else {
            myAdapter.setData(recentHistoryList);
            myAdapter.notifyDataSetChanged();
        }
    }

    class MyAdapter extends RecyclerView.Adapter {

        private List<RecentHistory> data;

        public MyAdapter(List l) {
            data = l;
        }

        public void setData(List l) {
            data = l;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_favs_item, parent, false);
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
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", data.get(position).getUrl());
                    startActivity(intent);
                }
            });
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Snackbar.make(view,"您想删除这条历史吗?",Snackbar.LENGTH_LONG).setAction("删除", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getContext(), "删除删除", Toast.LENGTH_SHORT).show();
                            DataSupport.delete(RecentHistory.class,data.get(position).getId());
                            getComicHistory();
                            Snackbar.make(view,"删除成功",Snackbar.LENGTH_SHORT).show();
                        }
                    }).show();
                    return true;
                }
            });
            TextView retitle = (TextView) v.findViewById(R.id.tx_item);
            retitle.setText(data.get(position).getTitle());


            TextView title = (TextView) v.findViewById(R.id.text_title);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time=formatter.format(data.get(position).getDate().getTime());
            title.setText(time);
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

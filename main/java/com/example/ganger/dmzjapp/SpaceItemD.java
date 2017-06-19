package com.example.ganger.dmzjapp;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ganger on 2017/6/17.
 */
public class SpaceItemD extends RecyclerView.ItemDecoration {
    int mSpace;

    public SpaceItemD(int s) {
        mSpace = s;
        //Log.i("2333333","23333333333");
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //outRect.left=mSpace;
        outRect.bottom = mSpace;
        //outRect.right=mSpace;
    }
}

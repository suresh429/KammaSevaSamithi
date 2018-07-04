package com.pivotalsoft.kammasevasamithi.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.pivotalsoft.kammasevasamithi.Items.SliderItem;
import com.pivotalsoft.kammasevasamithi.R;

import java.util.List;

/**
 * Created by Gangadhar on 10/30/2017.
 */

public class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private List<SliderItem> sliderItemList;

    public CustomPagerAdapter(Context mContext, List<SliderItem> sliderItemList) {
        this.mContext = mContext;
        this.sliderItemList = sliderItemList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return sliderItemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        final SliderItem album = sliderItemList.get(position);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        // loading album cover using Glide library
        Glide.with(mContext).load(album.getAlbumimage()).into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}


package com.pivotalsoft.kammasevasamithi.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pivotalsoft.kammasevasamithi.AddImagesActivity;
import com.pivotalsoft.kammasevasamithi.GallerySliderActivity;
import com.pivotalsoft.kammasevasamithi.Items.GalleryItem;
import com.pivotalsoft.kammasevasamithi.R;

import java.util.List;

/**
 * Created by Gangadhar on 10/30/2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private Context mContext;
    private List<GalleryItem> coursesItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtimageCount;
        public ImageView imgGallery;
        public FloatingActionButton fab;
        public LinearLayout batchLayout;


        public MyViewHolder(View view) {
            super(view);
            txtimageCount = (TextView) view.findViewById(R.id.txtCount);
            imgGallery=(ImageView)view.findViewById(R.id.images);
            batchLayout =(LinearLayout)view.findViewById(R.id.openCustomGallery1);
            fab=(FloatingActionButton)view.findViewById(R.id.addFab);
        }
    }


    public GalleryAdapter(Context mContext, List<GalleryItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GalleryItem album = coursesItemList.get(position);
        holder.txtimageCount.setText(album.getTitle());

        // holder.venue.setText(album.getVenue() );
        /*

        holder.discountPrice.setText(album.getDiscountFee() );
        holder.discountPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);*/

       /* // rating bar
        holder.ratingBar.setRating(Float.parseFloat(album.getRatingBar()));
        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);*/


        // loading album cover using Glide library
        Glide.with(mContext).load(album.getCoverimage()).into(holder.imgGallery);


        holder.batchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery =new Intent(mContext,GallerySliderActivity.class);
                gallery.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                gallery.putExtra("albumid",album.getGalleryid());
                mContext.startActivity(gallery);
            }
        });


        // To retrieve value from shared preference in another activity
        SharedPreferences sp = mContext.getSharedPreferences("MyPref", 0); // 0 for private mode
        String role = sp.getString("role", "");

        // validate btw admin and member
        if (role.equals("Admin")){

            holder.fab.setVisibility(View.VISIBLE);

        }
        else {
            holder.fab.setVisibility(View.GONE);
        }


        holder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gallery1 =new Intent(mContext,AddImagesActivity.class);
                gallery1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                gallery1.putExtra("albumid",album.getGalleryid());
                mContext.startActivity(gallery1);
            }
        });


    }


    @Override
    public int getItemCount() {
        return coursesItemList.size();
    }




}

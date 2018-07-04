package com.pivotalsoft.kammasevasamithi.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pivotalsoft.kammasevasamithi.Items.DirectoryItem;
import com.pivotalsoft.kammasevasamithi.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gangadhar on 10/31/2017.
 */

public class AboutUsAdapter extends RecyclerView.Adapter<AboutUsAdapter.MyViewHolder> {

    private Context mContext;
    private List<DirectoryItem> coursesItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtMoble,txtMembershipno,txtEmail;
        public ImageView thumbnail;
        public RelativeLayout parentLayout;


        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtMoble = (TextView) view.findViewById(R.id.txtMobile);
            txtEmail =(TextView)view.findViewById(R.id.txtEmail);
            txtMembershipno = (TextView) view.findViewById(R.id.txtMembershipNo);
            thumbnail = (CircleImageView) view.findViewById(R.id.profileImage);
            parentLayout = (RelativeLayout) view.findViewById(R.id.parentLayout);
        }
    }


    public AboutUsAdapter(Context mContext, List<DirectoryItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public AboutUsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_card, parent, false);

        return new AboutUsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AboutUsAdapter.MyViewHolder holder, final int position) {
        final DirectoryItem album = coursesItemList.get(position);
        holder.txtName.setText(album.getFullname());
        holder.txtMoble.setText(album.getMobile());
        holder.txtEmail.setText(album.getEmail());
        holder.txtMembershipno.setText("Membership No : "+album.getMembershipNo());
        // holder.venue.setText(album.getVenue() );
        /*

        holder.discountPrice.setText(album.getDiscountFee() );
        holder.discountPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);*/

       /* // rating bar
        holder.ratingBar.setRating(Float.parseFloat(album.getRatingBar()));
        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);*/


        // loading album cover using Glide library
          Glide.with(mContext).load(album.getProfilepic()).into(holder.thumbnail);


      /*  holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details =new Intent(mContext,ProfileActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(details);
            }
        });*/


    }


    @Override
    public int getItemCount() {
        return coursesItemList.size();
    }

    public void updateList(List<DirectoryItem> list) {
        coursesItemList = list;
        notifyDataSetChanged();
    }
}
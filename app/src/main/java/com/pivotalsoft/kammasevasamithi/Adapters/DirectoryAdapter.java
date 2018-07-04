package com.pivotalsoft.kammasevasamithi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pivotalsoft.kammasevasamithi.ProfileActivity;
import com.pivotalsoft.kammasevasamithi.Items.DirectoryItem;
import com.pivotalsoft.kammasevasamithi.R;

import java.util.List;

/**
 * Created by Gangadhar on 10/30/2017.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<DirectoryItem> coursesItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName,txtMoble,txtMembershipno,txtProfisson,txtEmail,txtMembershipRole;
        public ImageView thumbnail;
        public LinearLayout parentLayout;



        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtMoble = (TextView) view.findViewById(R.id.txtMobile);
            txtMembershipno = (TextView) view.findViewById(R.id.txtMembershipNo);
           // txtProfisson = (TextView) view.findViewById(R.id.txtProfession);
            txtEmail = (TextView) view.findViewById(R.id.txtEmail);
            txtMembershipRole = (TextView) view.findViewById(R.id.txtMembershipRole);
            thumbnail=(ImageView)view.findViewById(R.id.profileImage);
            parentLayout =(LinearLayout)view.findViewById(R.id.parentLayout);
        }
    }


    public DirectoryAdapter(Context mContext, List<DirectoryItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dirctory_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final DirectoryItem album = coursesItemList.get(position);
        holder.txtName.setText(album.getFullname());
        holder.txtMoble.setText(album.getMobile() );
        holder.txtMembershipno.setText("Membership No : "+album.getMembershipNo());
        Log.e("memeber",""+album.getMembershipNo());

       // holder.txtProfisson.setText("Profession : "+album.getProfission());
        holder.txtEmail.setText(album.getEmail());
        holder.txtMembershipRole.setText(album.getRole() );
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
                details.putExtra("role",album.getRole());
                details.putExtra("fullname",album.getFullname());
                details.putExtra("membershipno",album.getMembershipNo());
                details.putExtra("profilepic",album.getProfilepic());
                mContext.startActivity(details);
            }
        });*/


    }



    @Override
    public int getItemCount() {
        return coursesItemList.size();
    }

    public void updateList(List<DirectoryItem> list){
        coursesItemList = list;
        notifyDataSetChanged();
    }
}
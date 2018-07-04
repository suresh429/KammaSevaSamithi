package com.pivotalsoft.kammasevasamithi.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pivotalsoft.kammasevasamithi.Items.DonationItem;
import com.pivotalsoft.kammasevasamithi.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gangadhar on 10/31/2017.
 */

public class DonationsAdapter extends RecyclerView.Adapter<DonationsAdapter.MyViewHolder> {

    private Context mContext;
    private List<DonationItem> coursesItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName,txtDob,txtAmount,txtMembershipNo,txtProfession,txtDescription,txtPaymentMode;
        public ImageView imgevent;
        ImageView profileImg;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtDob = (TextView) view.findViewById(R.id.txtMobile);
            txtAmount = (TextView) view.findViewById(R.id.txtAmount);
            txtMembershipNo=(TextView)view.findViewById(R.id.txtMembershipNo);
           // txtProfession=(TextView)view.findViewById(R.id.txtProfession);
            txtDescription=(TextView)view.findViewById(R.id.txtDescription);
            txtPaymentMode=(TextView)view.findViewById(R.id.txtPaymentMode);
            imgevent=(ImageView)view.findViewById(R.id.imgEvent);
            profileImg=(ImageView)view.findViewById(R.id.profileImage);


            // batchLayout =(LinearLayout)view.findViewById(R.id.batchLayout);
        }
    }


    public DonationsAdapter(Context mContext, List<DonationItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donation_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final DonationItem album = coursesItemList.get(position);
        holder.txtName.setText(album.getFullname());
        holder.txtDob.setText("Date : "+album.getPaiddate());
        holder.txtAmount.setText(album.getAmount());
        holder.txtMembershipNo.setText(album.getMembershipno());
       // holder.txtProfession.setText("Profession : "+album.getProfission());
        holder.txtPaymentMode.setText("Payment Mode :"+album.getPaymentmode());
        holder.txtDescription.setText(album.getDescription());

        // holder.venue.setText(album.getVenue() );
        /*

        holder.discountPrice.setText(album.getDiscountFee() );
        holder.discountPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);*/

       /* // rating bar
        holder.ratingBar.setRating(Float.parseFloat(album.getRatingBar()));
        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);*/


        // loading album cover using Glide library
          Glide.with(mContext).load(album.getProfilepic()).into(holder.profileImg);


       /* holder.batchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


    }


    @Override
    public int getItemCount() {
        return coursesItemList.size();
    }

}

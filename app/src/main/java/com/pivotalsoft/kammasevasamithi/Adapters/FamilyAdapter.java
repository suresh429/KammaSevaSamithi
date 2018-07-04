package com.pivotalsoft.kammasevasamithi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.pivotalsoft.kammasevasamithi.EditFamilyActivity;
import com.pivotalsoft.kammasevasamithi.Items.FamilyItem;
import com.pivotalsoft.kammasevasamithi.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Gangadhar on 10/31/2017.
 */

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {

    private Context mContext;
    private List<FamilyItem> coursesItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtRelation,txtQualification,txtBloodGroup,txtDay,txtMonth,txtYear;
        public ImageView thumbnail,overflow;
       // public RelativeLayout parentLayout;


        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtRelation = (TextView) view.findViewById(R.id.txtRelation);
            txtBloodGroup = (TextView) view.findViewById(R.id.txtBloodGroup);
            txtQualification = (TextView) view.findViewById(R.id.txtQualification);
            txtDay = (TextView) view.findViewById(R.id.day);
            txtMonth = (TextView) view.findViewById(R.id.month);
            txtYear = (TextView) view.findViewById(R.id.year);
            thumbnail = (ImageView) view.findViewById(R.id.imageView);
            overflow=(ImageView)view.findViewById(R.id.overflow);
           // parentLayout = (RelativeLayout) view.findViewById(R.id.parentLayout);
        }
    }


    public FamilyAdapter(Context mContext, List<FamilyItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.family_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final FamilyItem album = coursesItemList.get(position);
        holder.txtName.setText(album.getMembername());
        holder.txtRelation.setText("Relation : "+album.getRelation());
        holder.txtQualification.setText("Qualification : "+album.getQualification());
        holder.txtBloodGroup.setText("Blood Group : "+album.getBloodgroup() );
        /*

        holder.discountPrice.setText(album.getDiscountFee() );
        holder.discountPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);*/

       /* // rating bar
        holder.ratingBar.setRating(Float.parseFloat(album.getRatingBar()));
        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);*/


        // loading album cover using Glide library
        //  Glide.with(mContext).load(album.getImage()).into(holder.thumbnail);

        String test = album.getMembername();
        String firstLetter=test.substring(0,1);


        String startDate = formateDateFromstring("yyyy-MM-dd", "dd,MMM,yyyy", album.getOccdate());
        Log.e("startDateformat",""+startDate);

        StringTokenizer tokens = new StringTokenizer(startDate, ",");
        String day = tokens.nextToken();
        String month = tokens.nextToken();// this will contain " they taste good"
        String year = tokens.nextToken();// this will contain "Fruit"

        Log.e("startDatetoken",""+day+"\n"+month);

        holder.txtDay.setText(day);
        holder.txtMonth.setText(month);
        holder.txtYear.setText(year);


        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color1 = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(60)  // width in px
                .height(60) // height in px
                .endConfig()
                .buildRoundRect(firstLetter, color1,5);

        holder.thumbnail.setImageDrawable(drawable);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPopupMenu(holder.overflow);

                PopupMenu popup = new PopupMenu(mContext, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_family, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_add_edit:
                                FamilyItem newJobItem =coursesItemList.get(position);
                                Intent details = new Intent(mContext, EditFamilyActivity.class);
                                details.putExtra("memberid", newJobItem.getMemberid());
                                details.putExtra("familyid", newJobItem.getFamilyid());
                                details.putExtra("membername", newJobItem.getMembername());
                                details.putExtra("relation", newJobItem.getRelation());
                                details.putExtra("qualification", newJobItem.getQualification());
                                details.putExtra("bloodgroup", newJobItem.getBloodgroup());
                                details.putExtra("occastiondate", newJobItem.getOccdate());
                                details.putExtra("occassion", newJobItem.getOccassion());
                                details.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(details);
                                return true;


                            default:
                        }

                        return false;
                    }
                });

                popup.show();
            }
        });


      /*  holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*Intent details =new Intent(mContext,ProfileActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(details);*//*
            }
        });*/


    }


    @Override
    public int getItemCount() {
        return coursesItemList.size();
    }


    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            // LOGE(TAG, "ParseException - dateFormat");
        }

        return outputDate;

    }
}
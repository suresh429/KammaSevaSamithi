package com.pivotalsoft.kammasevasamithi.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.pivotalsoft.kammasevasamithi.Items.NewsItem;
import com.pivotalsoft.kammasevasamithi.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Gangadhar on 10/30/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context mContext;
    private List<NewsItem> coursesItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle,txtDescription,txtTime,txtDay,txtMonth;

        public MyViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            txtTime = (TextView) view.findViewById(R.id.txtTime);
            txtDay = (TextView) view.findViewById(R.id.day);
            txtMonth = (TextView) view.findViewById(R.id.month);

            // batchLayout =(LinearLayout)view.findViewById(R.id.batchLayout);
        }
    }


    public NewsAdapter(Context mContext, List<NewsItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final NewsItem album = coursesItemList.get(position);
        holder.txtTitle.setText(album.getTitle());
        holder.txtDescription.setText(album.getDescription());

       // holder.txtTime.setText(album.getTime());

        /*

        holder.discountPrice.setText(album.getDiscountFee() );
        holder.discountPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);*/

       /* // rating bar
        holder.ratingBar.setRating(Float.parseFloat(album.getRatingBar()));
        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);*/


        // loading album cover using Glide library
        //  Glide.with(mContext).load(album.getImage()).into(holder.thumbnail);


       /* holder.batchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/



        String startDate = formateDateFromstring("yyyy-MM-dd", "dd,MMM,yyyy", album.getDate());
        Log.e("startDateformat",""+startDate);

        StringTokenizer tokens = new StringTokenizer(startDate, ",");
        String day = tokens.nextToken();
        String month = tokens.nextToken();// this will contain " they taste good"
        String year = tokens.nextToken();// this will contain "Fruit"

        Log.e("startDatetoken",""+day+"\n"+month);

        holder.txtDay.setText(day);
        holder.txtMonth.setText(month);
        holder.txtTime.setText(year);




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
package com.pivotalsoft.kammasevasamithi.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pivotalsoft.kammasevasamithi.Items.MessageItem;
import com.pivotalsoft.kammasevasamithi.Items.NewsItem;
import com.pivotalsoft.kammasevasamithi.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Gangadhar on 11/20/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private Context mContext;
    private List<MessageItem> coursesItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDescription,txtYear,txtDay,txtMonth;

        public MyViewHolder(View view) {
            super(view);

            txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            txtYear = (TextView) view.findViewById(R.id.txtYear);
            txtDay = (TextView) view.findViewById(R.id.day);
            txtMonth = (TextView) view.findViewById(R.id.month);

            // batchLayout =(LinearLayout)view.findViewById(R.id.batchLayout);
        }
    }


    public MessageAdapter(Context mContext, List<MessageItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MessageItem album = coursesItemList.get(position);
      //  holder.txtTitle.setText(album.getTitle());
        holder.txtDescription.setText(album.getMessage());
        // holder.txtdate.setText(album.getDate());



        String startDate = formateDateFromstring("yyyy-MM-dd HH:mm:ss", "dd,MMM,yyyy,h:mm a", album.getCurrentdate());
        Log.e("startDateformat",""+startDate);

        StringTokenizer tokens = new StringTokenizer(startDate, ",");
        String day = tokens.nextToken();
        String month = tokens.nextToken();// this will contain " they taste good"
        String year = tokens.nextToken();// this will contain "Fruit"
        String time = tokens.nextToken();// this will contain "Fruit"



        Log.e("startDatetoken",""+day+"\n"+month);

        holder.txtDay.setText(day);
        holder.txtMonth.setText(month);
        holder.txtYear.setText(year);




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

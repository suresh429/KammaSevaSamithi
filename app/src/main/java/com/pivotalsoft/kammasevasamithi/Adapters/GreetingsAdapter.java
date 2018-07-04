package com.pivotalsoft.kammasevasamithi.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pivotalsoft.kammasevasamithi.Items.GreetingsItem;
import com.pivotalsoft.kammasevasamithi.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gangadhar on 10/30/2017.
 */

public class GreetingsAdapter extends RecyclerView.Adapter<GreetingsAdapter.MyViewHolder> {

    private Context mContext;
    private List<GreetingsItem> coursesItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName,txtRelation,txtOccation,txtMemberno,txtProfission,txtMembershipRole;
        public ImageView imgevent;
        ImageView profileImg;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtRelation = (TextView) view.findViewById(R.id.txtRelation);
            txtOccation =(TextView)view.findViewById(R.id.txtOccastion);
            txtMemberno =(TextView)view.findViewById(R.id.txtMembershipNo);
           // txtProfission =(TextView)view.findViewById(R.id.txtProfession);
            txtMembershipRole =(TextView)view.findViewById(R.id.txtMembershipRole);
            imgevent=(ImageView)view.findViewById(R.id.imgEvent);
            profileImg=(ImageView)view.findViewById(R.id.profileImage);


            // batchLayout =(LinearLayout)view.findViewById(R.id.batchLayout);
        }
    }


    public GreetingsAdapter(Context mContext, List<GreetingsItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.greeting_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GreetingsItem album = coursesItemList.get(position);

        holder.txtMemberno.setText("Membership No : "+album.getMembershipno());
        holder.txtMembershipRole.setText(album.getRole());


        // loading album cover using Glide library
        Glide.with(mContext).load(album.getProfilepic()).into(holder.profileImg);


        String currentDate = new SimpleDateFormat("MM-dd").format(Calendar.getInstance().getTime());
        Log.e("currentDate",""+currentDate);

        String dateofBirth = formateDateFromstring("yyyy-MM-dd", "MM-dd", album.getDob());
        Log.e("dateofBirth",""+dateofBirth);

        String occdate = formateDateFromstring("yyyy-MM-dd", "MM-dd", album.getOccdate());
        Log.e("occdate",""+occdate);



        if (dateofBirth.equals(currentDate)){

            holder.txtName.setText(album.getFullname());
            holder.txtRelation.setText("Relation : "+ "Self");
            holder.txtOccation.setText("Occasion : "+"BirthDay");
        }


        if (occdate.equals(currentDate)){

            holder.txtName.setText(album.getMembername());
            holder.txtRelation.setText("Relation : "+album.getRelation());
            holder.txtOccation.setText("Occasion : "+album.getOccassion());
        }

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

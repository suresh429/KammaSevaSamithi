package com.pivotalsoft.kammasevasamithi.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.pivotalsoft.kammasevasamithi.EditEventActivity;
import com.pivotalsoft.kammasevasamithi.Items.EventsItem;
import com.pivotalsoft.kammasevasamithi.R;

import java.util.List;

/**
 * Created by Gangadhar on 9/26/2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    private Context mContext;
    private List<EventsItem> coursesItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView courseName,date,venue;
        public ImageView thumbnail,overflow;
        public LinearLayout batchLayout;
        public Button enrollBtn;


        public MyViewHolder(View view) {
            super(view);
            courseName = (TextView) view.findViewById(R.id.courseName);
            date = (TextView) view.findViewById(R.id.date);
            venue = (TextView) view.findViewById(R.id.venue);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            batchLayout =(LinearLayout)view.findViewById(R.id.batchLayout);
        }
    }


    public EventsAdapter(Context mContext, List<EventsItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public EventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);

        return new EventsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EventsAdapter.MyViewHolder holder, final int position) {
        final EventsItem album = coursesItemList.get(position);
        holder.courseName.setText(album.getEventname());
        holder.date.setText(album.getEventdate() );
        holder.venue.setText(album.getVenue() );
        /*

        holder.discountPrice.setText(album.getDiscountFee() );
        holder.discountPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);*/

       /* // rating bar
        holder.ratingBar.setRating(Float.parseFloat(album.getRatingBar()));
        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);*/

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = mContext.getSharedPreferences("MyPref", 0); // 0 for private mode
        String role = sp.getString("role", "");
        // loading album cover using Glide library
//        Glide.with(mContext).load(album.getEventname()).into(holder.thumbnail);

        // validate btw admin and member
        if (role.equals("Admin")){

            holder.overflow.setVisibility(View.VISIBLE);

        }
        else {
            holder.overflow.setVisibility(View.GONE);
        }

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // inflate menu
                PopupMenu popup = new PopupMenu(mContext, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_update, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.action_edit:

                                Intent intent =new Intent(mContext,EditEventActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("eventid",album.getEventid());
                                intent.putExtra("eventname",album.getEventname());
                                intent.putExtra("venue",album.getVenue());
                                intent.putExtra("eventdate",album.getEventdate());
                                intent.putExtra("description",album.getDescription());
                                mContext.startActivity(intent);

                                // Toast.makeText(mContext,"Postion"+position,Toast.LENGTH_LONG).show();
                                return true;


                            default:
                        }


                        return false;
                    }
                });
                popup.show();

            }
        });






        holder.batchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
               // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.events_details_layout);

                // set the custom dialog components - text, image and button
                TextView eventName = (TextView) dialog.findViewById(R.id.eventName);
                eventName.setText(album.getEventname());

                TextView venue = (TextView) dialog.findViewById(R.id.venue);
                venue.setText(album.getVenue());

                TextView date = (TextView) dialog.findViewById(R.id.date);
                date.setText(album.getEventdate());

                TextView description = (TextView) dialog.findViewById(R.id.description);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    description.setText(Html.fromHtml(album.getDescription(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    description.setText(Html.fromHtml(album.getDescription()));
                }

                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.eventgrid_img);

                dialog.show();
            }
        });


    }



    @Override
    public int getItemCount() {
        return coursesItemList.size();
    }
}


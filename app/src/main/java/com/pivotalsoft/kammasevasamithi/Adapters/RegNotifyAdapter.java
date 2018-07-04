package com.pivotalsoft.kammasevasamithi.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pivotalsoft.kammasevasamithi.AcceptActivity;
import com.pivotalsoft.kammasevasamithi.AddDonationActivity;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.Items.DirectoryItem;
import com.pivotalsoft.kammasevasamithi.R;
import com.pivotalsoft.kammasevasamithi.RegNotificationActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gangadhar on 11/20/2017.
 */

public class RegNotifyAdapter extends RecyclerView.Adapter<RegNotifyAdapter.MyViewHolder> {
    private ProgressDialog pDialog;
    private Context mContext;
    private List<DirectoryItem> coursesItemList;
    DirectoryItem album;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName,txtMoble,txtMembershipno,txtProfisson,txtEmail,txtAccept,txtReject;
        public ImageView thumbnail;
        public LinearLayout parentLayout;



        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtMoble = (TextView) view.findViewById(R.id.txtMobile);
            txtMembershipno = (TextView) view.findViewById(R.id.txtMembershipNo);
           // txtProfisson = (TextView) view.findViewById(R.id.txtProfession);
            txtEmail = (TextView) view.findViewById(R.id.txtEmail);
            txtAccept = (TextView) view.findViewById(R.id.txtAccept);
            txtReject = (TextView) view.findViewById(R.id.txtReject);
            thumbnail=(ImageView)view.findViewById(R.id.profileImage);
            parentLayout =(LinearLayout)view.findViewById(R.id.parentLayout);
        }
    }


    public RegNotifyAdapter(Context mContext, List<DirectoryItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.regnotify_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        album = coursesItemList.get(position);
        holder.txtName.setText(album.getFullname());
        holder.txtMoble.setText(album.getMobile() );
        holder.txtMembershipno.setText("Membership No : "+album.getMembershipNo());
        Log.e("memeber",""+album.getMembershipNo());

        //holder.txtProfisson.setText("Profession : "+album.get());
        holder.txtEmail.setText(album.getEmail());
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


        holder.txtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                album = coursesItemList.get(position);
                Intent details =new Intent(mContext,AcceptActivity.class);
                details.putExtra("name",album.getFullname());
                details.putExtra("email",album.getEmail());
                details.putExtra("mobile",album.getMobile());
                details.putExtra("memberid",album.getMemberid());
                details.putExtra("profilepic",album.getProfilepic());
                details.putExtra("fcmkey",album.getFcmkey());
                details.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(details);


            }
        });

        holder.txtReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                album = coursesItemList.get(position);
                rejectDiloge(album.getMemberid(),album.getFcmkey(),album.getFullname());
            }
        });


    }



    @Override
    public int getItemCount() {
        return coursesItemList.size();
    }

   /* public void updateList(List<DirectoryItem> list){
        coursesItemList = list;
        notifyDataSetChanged();
    }*/


    private void rejectDiloge(final String memberid, final String fcmkey, final String fullname){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mContext);
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setTitle("ALERT")
                .setMessage("Are you sure you want to Reject?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        addReject(memberid,fcmkey,fullname);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.ic_warning_black_24dp)
                .show();
    }


    private void addReject(final String memberid, final String fcmkey, final String fullname){
// Progress dialog
        pDialog = new ProgressDialog(mContext);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");
        showDialog();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REJECT_RIGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hideDialog();

                        pushRejectnotification(fcmkey, fullname);

                        Intent pivotal = new Intent(mContext, RegNotificationActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(pivotal);
                        //Toast.makeText(AcceptActivity.this,"Accept Successfully",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RESPONSE_ERROR: ",""+error);
                        hideDialog();
                        // Toast.makeText(AddAddsActivity.this,"Email Already Exist",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("memberid",memberid);

                Log.e("RESPONSE_Parasms: ",""+memberid);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    //push notification
    private void pushRejectnotification(String fcmkey,String fullname){

        Log.e("FCMKEY",fcmkey);
        try{

           /* String fcmArray= Arrays.toString(new ArrayList[]{arrPackage}).replaceAll("\\[|\\]", "");
            Log.e("ARRAY",""+fcmArray);*/

            RequestQueue queue = Volley.newRequestQueue(mContext);

            String url = "https://fcm.googleapis.com/fcm/send";

            String body ="Hi! "+fullname+" your Registration has been Rejected!.";
            Log.e("body",""+body);

            JSONObject data = new JSONObject();
            data.put("title", "Rejected !");
            data.put("body", body);
            JSONObject notification_data = new JSONObject();
            notification_data.put("notification", data);
            notification_data.put("to",fcmkey);

            Log.e("ConsultKey",""+"");

            JsonObjectRequest request = new JsonObjectRequest(url, notification_data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    String api_key_header_value = "Key="+Constants.AUTH_KEY_FCM;
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", api_key_header_value);
                    return headers;
                }
            };

            queue.add(request);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}

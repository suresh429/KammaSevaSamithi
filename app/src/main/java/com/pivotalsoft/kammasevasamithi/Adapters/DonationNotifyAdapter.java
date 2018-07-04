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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pivotalsoft.kammasevasamithi.AcceptActivity;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.DonationNotificationActivity;
import com.pivotalsoft.kammasevasamithi.Items.DonationItem;
import com.pivotalsoft.kammasevasamithi.R;
import com.pivotalsoft.kammasevasamithi.RegNotificationActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gangadhar on 11/20/2017.
 */

public class DonationNotifyAdapter extends RecyclerView.Adapter<DonationNotifyAdapter.MyViewHolder> {
    private ProgressDialog pDialog;
    private Context mContext;
    private List<DonationItem> coursesItemList;
    DonationItem album;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName,txtDob,txtAmount,txtMembershipNo,txtProfession,txtDescription,txtPaymentMode,txtAccept,txtReject;
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
            txtAccept = (TextView) view.findViewById(R.id.txtAccept);
            txtReject = (TextView) view.findViewById(R.id.txtReject);
            imgevent=(ImageView)view.findViewById(R.id.imgEvent);
            profileImg=(ImageView)view.findViewById(R.id.profileImage);


            // batchLayout =(LinearLayout)view.findViewById(R.id.batchLayout);
        }
    }


    public DonationNotifyAdapter(Context mContext, List<DonationItem> coursesItemList) {
        this.mContext = mContext;
        this.coursesItemList = coursesItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donation_notify_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
         album = coursesItemList.get(position);
        holder.txtName.setText(album.getFullname());
        holder.txtDob.setText("Date : "+album.getPaiddate());
        holder.txtAmount.setText(album.getAmount());
        holder.txtMembershipNo.setText(album.getMembershipno());
       // holder.txtProfession.setText("Profession : "+album.getProfission());
        holder.txtPaymentMode.setText("Payment Mode :"+album.getPaymentmode());
        holder.txtDescription.setText(album.getDescription());

        // holder.venue.setText(album.getVenue() );





        // loading album cover using Glide library
        //  Glide.with(mContext).load(album.getImage()).into(holder.thumbnail);


        holder.txtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(mContext);
                } else {
                    builder = new AlertDialog.Builder(mContext);
                }
                builder.setTitle("ALERT")
                        .setMessage("Are you sure you want to Approve?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                album = coursesItemList.get(position);

                               addAccept(album.getDonationid(),album.getFcmkey(),album.getFullname());
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
        });

        holder.txtReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// continue with delete
                album = coursesItemList.get(position);

                rejectDiloge(album.getDonationid(),album.getFcmkey(),album.getFullname());
            }
        });



    }


    @Override
    public int getItemCount() {
        return coursesItemList.size();
    }




    private void rejectDiloge(final String donationid, final String fcmkey, final String fullname){

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
                  addReject(donationid,fcmkey,fullname);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void addReject(final String donationid, final String fcmkey, final String fullname){
// Progress dialog
        pDialog = new ProgressDialog(mContext);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");
        showDialog();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REJECT_DONATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hideDialog();
                        pushRejectnotification(fcmkey,fullname);

                        Intent pivotal = new Intent(mContext, DonationNotificationActivity.class);
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
                params.put("donationid",donationid);

                Log.e("RESPONSE_Parasms: ",""+donationid);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void addAccept(final String donationid, final String fcmkey, final String fullname){
// Progress dialog
        pDialog = new ProgressDialog(mContext);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");
        showDialog();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ACCEPT_DONATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hideDialog();

                        pushAcceptnotification(fcmkey,fullname);

                        Intent pivotal = new Intent(mContext, DonationNotificationActivity.class);
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
                params.put("donationid",donationid);

                Log.e("RESPONSE_Parasms: ",""+donationid);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    //push notification
    private void pushAcceptnotification(String fcmkey,String fullname){

        Log.e("FCMKEY",fcmkey);
        try{

           /* String fcmArray= Arrays.toString(new ArrayList[]{arrPackage}).replaceAll("\\[|\\]", "");
            Log.e("ARRAY",""+fcmArray);*/

            RequestQueue queue = Volley.newRequestQueue(mContext);

            String url = "https://fcm.googleapis.com/fcm/send";

            String body ="Hi! "+fullname+" your Registration Accepted. Welcome to Kamma Seva Samithi!";
            Log.e("body",""+body);

            JSONObject data = new JSONObject();
            data.put("title", "Accepted !");
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

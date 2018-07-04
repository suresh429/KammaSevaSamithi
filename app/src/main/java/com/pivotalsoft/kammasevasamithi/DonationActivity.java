package com.pivotalsoft.kammasevasamithi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pivotalsoft.kammasevasamithi.Adapters.DirectoryAdapter;
import com.pivotalsoft.kammasevasamithi.Adapters.DonationsAdapter;
import com.pivotalsoft.kammasevasamithi.Adapters.GreetingsAdapter;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.Items.DonationItem;
import com.pivotalsoft.kammasevasamithi.Items.GreetingsItem;
import com.pivotalsoft.kammasevasamithi.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DonationActivity extends AppCompatActivity {
    private static final String TAG = DonationActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private RecyclerView recyclerDirectory;
    private DonationsAdapter directoryAdapter;
    private List<DonationItem> directoryItemList= new ArrayList<>();
    TextView txtNoData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Donations");

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();


        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 for private mode
        String role = sp.getString("role", "");

        txtNoData=(TextView)findViewById(R.id.txtNoData);

        FloatingActionButton floatingActionButton =(FloatingActionButton)findViewById(R.id.addFab);

        // validate btw admin and member
        if (role.equals("Admin")){

            floatingActionButton.setVisibility(View.GONE);

        }
        else {
            floatingActionButton.setVisibility(View.VISIBLE);
        }



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent details =new Intent(DonationActivity.this,AddDonationActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(details);
            }
        });

        recyclerDirectory = (RecyclerView) findViewById(R.id.greetingsRecycler);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this, 1);
        recyclerDirectory.setLayoutManager(mLayoutManager1);
        recyclerDirectory.setItemAnimator(new DefaultItemAnimator());

        directoryAdapter=new DonationsAdapter(this,directoryItemList);

        recyclerDirectory.setAdapter(directoryAdapter);

        prepareallDonationsData();
        dialoge();
    }

    private void dialoge(){

        // custom dialog
        final Dialog dialog = new Dialog(DonationActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        //dialog.setTitle("Update UserCode");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.donation_diloge_layout);

        TextView txtAc =(TextView)dialog.findViewById(R.id.textViewAccountNo);
        txtAc.setTextIsSelectable(true);
        TextView txtIfsc =(TextView)dialog.findViewById(R.id.textViewIfsc);
        txtIfsc.setTextIsSelectable(true);

        ImageButton cancel =(ImageButton)dialog.findViewById(R.id.imageButtonCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    private void prepareallDonationsData() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.ALL_DONATION_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.ALL_DONATION_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    directoryItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("alldata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String donationid = jsonObject.getString("donationid");
                        String memberid = jsonObject.getString("memberid");
                        String amount = jsonObject.getString("amount");
                        String paymentmode = jsonObject.getString("paymentmode");
                        String description = jsonObject.getString("description");
                        String paiddate = jsonObject.getString("paiddate");
                        String status = jsonObject.getString("status");
                        String membershipno = jsonObject.getString("membershipno");
                        String fullname = jsonObject.getString("fullname");
                        String role = jsonObject.getString("role");
                        String profilepic = Constants.IMAGE_PROFILEPIC_URL+jsonObject.getString("profilepic");
                        String fcmkey = jsonObject.getString("fcmkey");

                        JSONObject messageObject =new JSONObject(response.toString());
                        String message =messageObject.getString("message");
                        Log.e("msg",""+message);


                        if (message.equals("no data received..")){

                            txtNoData.setVisibility(View.VISIBLE);
                            recyclerDirectory.setVisibility(View.GONE);
                        }
                        else {

                            txtNoData.setVisibility(View.GONE);
                            recyclerDirectory.setVisibility(View.VISIBLE);
                        }


                        directoryItemList.add(new DonationItem(donationid,memberid,amount,paymentmode,description,paiddate,status,membershipno,fullname,role,profilepic,fcmkey));
                        directoryAdapter = new DonationsAdapter(DonationActivity.this, directoryItemList);
                        recyclerDirectory.setAdapter(directoryAdapter);
                        directoryAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DonationActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(OffersActivity.this, "no data Found. check once data is enable or not..", Toast.LENGTH_LONG).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

      /*  //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(OffersActivity.this);

        //Adding request to the queue
        requestQueue.add(jsonObjReq);*/

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }



    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }
}

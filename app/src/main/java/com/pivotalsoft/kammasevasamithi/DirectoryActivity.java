package com.pivotalsoft.kammasevasamithi;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pivotalsoft.kammasevasamithi.Adapters.DirectoryAdapter;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.Items.DirectoryItem;
import com.pivotalsoft.kammasevasamithi.app.AppController;
import com.pivotalsoft.kammasevasamithi.utils.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DirectoryActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    int color;
    LinearLayout linearLayout;
    // Log tag
    private static final String TAG = DirectoryActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private RecyclerView recyclerDirectory;
    private DirectoryAdapter directoryAdapter;
    private List<DirectoryItem> directoryItemList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Directory");

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        recyclerDirectory = (RecyclerView) findViewById(R.id.directoryRecycler);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this, 1);
        recyclerDirectory.setLayoutManager(mLayoutManager1);
        recyclerDirectory.setItemAnimator(new DefaultItemAnimator());

        directoryAdapter=new DirectoryAdapter(this,directoryItemList);

        recyclerDirectory.setAdapter(directoryAdapter);

        SearchView searchView=(SearchView)findViewById(R.id.simpleSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
                return false;
            }
        });


       // check network connection
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (isConnected){

            prepareDirectoryData();
            Log.e("CONNECTIONTRUE",""+isConnected);
        }
        else {
            checkConnection();
            Log.e("CONNECTIONFALSE",""+isConnected);
        }


    }

    private void prepareDirectoryData() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.DIRECTORY_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.DIRECTORY_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    directoryItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("alldata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String memberId = jsonObject.getString("memberid");
                        String fullname = jsonObject.getString("fullname");
                        String fathername = jsonObject.getString("fathername");
                        String dob = jsonObject.getString("dob");
                        String gothram = jsonObject.getString("gothram");
                        String mobile = jsonObject.getString("mobile");
                        String email = jsonObject.getString("email");
                        String bloodgroup = jsonObject.getString("bloodgroup");
                        String referalno = jsonObject.getString("referalno");
                        String presentaddress = jsonObject.getString("presentaddress");
                        String nativeaddress = jsonObject.getString("nativeaddress");
                        String profilepic = Constants.IMAGE_PROFILEPIC_URL+jsonObject.getString("profilepic");
                        String role = jsonObject.getString("role");
                        String status = jsonObject.getString("status");
                        String membershipno = jsonObject.getString("membershipno");
                        String password = jsonObject.getString("password");
                        String regdate = jsonObject.getString("regdate");
                        String fcmkey = jsonObject.getString("fcmkey");

                        directoryItemList.add(new DirectoryItem(memberId,fullname,fathername,dob,gothram,mobile,email,bloodgroup,referalno,presentaddress,nativeaddress,profilepic,status,role,membershipno,password,regdate,fcmkey));
                        directoryAdapter = new DirectoryAdapter(DirectoryActivity.this, directoryItemList);
                        recyclerDirectory.setAdapter(directoryAdapter);
                        directoryAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DirectoryActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    void filter(String text){
        List<DirectoryItem> temp = new ArrayList();
        for(DirectoryItem d: directoryItemList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getFullname().toLowerCase().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        directoryAdapter.updateList(temp);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    // check internet connection

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        Log.e("CONNECTION",""+isConnected);
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;

        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        linearLayout =(LinearLayout) findViewById(R.id.parentLayout);

        setSnackBar(linearLayout,message);
    }

    public void setSnackBar(View coordinatorLayout, String snackTitle) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, snackTitle, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        TextView txtv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setTextColor(color);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        AppController.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
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

package com.pivotalsoft.kammasevasamithi;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pivotalsoft.kammasevasamithi.Adapters.AboutUsAdapter;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.Items.DirectoryItem;
import com.pivotalsoft.kammasevasamithi.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity {
    private static final String TAG = AboutUsActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private RecyclerView recyclerDirectory;
    private AboutUsAdapter directoryAdapter;
    private List<DirectoryItem> directoryItemList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("About Us");

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        TextView textView = (TextView) findViewById(R.id.txtDescription);
        textView.setMovementMethod(new ScrollingMovementMethod());

        recyclerDirectory = (RecyclerView) findViewById(R.id.recyclerProfile);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this, 2);
       // LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerDirectory.setLayoutManager(mLayoutManager1);
        recyclerDirectory.setItemAnimator(new DefaultItemAnimator());

        directoryAdapter=new AboutUsAdapter(this,directoryItemList);

        recyclerDirectory.setAdapter(directoryAdapter);

        prepareProfileData();
    }

   /* private void prepareListData() {

        DirectoryItem eventsItem=new DirectoryItem("suresh","8985018109","1101","Engineer","suresh@gmail.com",R.drawable.avatar_author);
        directoryItemList.add(eventsItem);

        eventsItem=new DirectoryItem("Mahesh","8985018109","1101","Laywer","Mahesh@gmail.com",R.drawable.avatar_author);
        directoryItemList.add(eventsItem);

        eventsItem=new DirectoryItem("Manoj","8985018109","1101","Technician","Manoj@gmail.com",R.drawable.avatar_author);
        directoryItemList.add(eventsItem);

        eventsItem=new DirectoryItem("suresh","8985018109","1101","Engineer","suresh@gmail.com",R.drawable.avatar_author);
        directoryItemList.add(eventsItem);

        directoryAdapter.notifyDataSetChanged();
    }*/

    private void prepareProfileData() {

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
                        Log.e("PIC",""+profilepic);
                        String role = jsonObject.getString("role");
                        String status = jsonObject.getString("status");
                        String membershipno = jsonObject.getString("membershipno");
                        String password = jsonObject.getString("password");
                        String regdate = jsonObject.getString("regdate");
                        String fcmkey = jsonObject.getString("fcmkey");

                        if (role.equals("Commitee")) {

                            directoryItemList.add(new DirectoryItem(memberId, fullname, fathername, dob, gothram, mobile, email, bloodgroup, referalno, presentaddress, nativeaddress, profilepic, status, role, membershipno, password, regdate,fcmkey));
                            directoryAdapter = new AboutUsAdapter(AboutUsActivity.this, directoryItemList);
                            recyclerDirectory.setAdapter(directoryAdapter);
                            directoryAdapter.notifyDataSetChanged();

                        }else {
                           // Toast.makeText(AboutUsActivity.this, "No data found" , Toast.LENGTH_LONG).show();

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AboutUsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(NewsActivity.this, "no data Found. check once data is enable or not..", Toast.LENGTH_LONG).show();
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

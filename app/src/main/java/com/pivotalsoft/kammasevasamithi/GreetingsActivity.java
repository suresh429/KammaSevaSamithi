package com.pivotalsoft.kammasevasamithi;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pivotalsoft.kammasevasamithi.Adapters.GalleryAdapter;
import com.pivotalsoft.kammasevasamithi.Adapters.GreetingsAdapter;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.Items.GreetingsItem;
import com.pivotalsoft.kammasevasamithi.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GreetingsActivity extends AppCompatActivity {
    private static final String TAG = GreetingsActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private RecyclerView recyclerDirectory;
    private GreetingsAdapter directoryAdapter;
    private List<GreetingsItem> directoryItemList= new ArrayList<>();
    TextView txtNoData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greetings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Greetings");

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        txtNoData=(TextView)findViewById(R.id.txtNoData);
        recyclerDirectory = (RecyclerView) findViewById(R.id.greetingsRecycler);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this, 1);
        recyclerDirectory.setLayoutManager(mLayoutManager1);
        recyclerDirectory.setItemAnimator(new DefaultItemAnimator());

        directoryAdapter=new GreetingsAdapter(this,directoryItemList);

        recyclerDirectory.setAdapter(directoryAdapter);

        prepareGreetingsData();
    }


    private void prepareGreetingsData() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.GREETINGS_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.GREETINGS_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    directoryItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("alldata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String memberid = jsonObject.getString("memberid");
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
                        String familyid = jsonObject.getString("familyid");
                        String membername = jsonObject.getString("membername");
                        String relation = jsonObject.getString("relation");
                        String qualification = jsonObject.getString("qualification");
                        String occassion = jsonObject.getString("occassion");
                        String occdate = jsonObject.getString("occdate");

                        JSONObject messageObject =new JSONObject(response.toString());
                        String message1 =messageObject.getString("message");
                        Log.e("msg",""+message1);


                        if (message1.equals("no data received..")){

                            txtNoData.setVisibility(View.VISIBLE);
                            recyclerDirectory.setVisibility(View.GONE);
                        }
                        else {

                            txtNoData.setVisibility(View.GONE);
                            recyclerDirectory.setVisibility(View.VISIBLE);
                        }


                        directoryItemList.add(new GreetingsItem(memberid,fullname,fathername,dob,gothram,mobile,email,bloodgroup,
                                referalno,presentaddress,nativeaddress,profilepic,role,status,membershipno,password,regdate,familyid,
                                membername,relation,qualification,occassion,occdate));
                        directoryAdapter = new GreetingsAdapter(GreetingsActivity.this, directoryItemList);
                        recyclerDirectory.setAdapter(directoryAdapter);
                        directoryAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(GreetingsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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


package com.pivotalsoft.kammasevasamithi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
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
import com.pivotalsoft.kammasevasamithi.Adapters.EventsAdapter;
import com.pivotalsoft.kammasevasamithi.Adapters.NewsAdapter;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.Items.EventsItem;
import com.pivotalsoft.kammasevasamithi.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {
    private static final String TAG = NewsActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private RecyclerView recyclerCurrentEvents;
    private EventsAdapter eventsAdapter;
    private List<EventsItem> eventsItemList= new ArrayList<>();
    TextView txtNoData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Events");

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

            floatingActionButton.setVisibility(View.VISIBLE);

        }
        else {
            floatingActionButton.setVisibility(View.GONE);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent details =new Intent(EventsActivity.this,AddEventsActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(details);
            }
        });

        recyclerCurrentEvents = (RecyclerView) findViewById(R.id.recyclerCourses);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this, 2);
        recyclerCurrentEvents.setLayoutManager(mLayoutManager1);
        recyclerCurrentEvents.setItemAnimator(new DefaultItemAnimator());

        eventsAdapter=new EventsAdapter(this,eventsItemList);

        recyclerCurrentEvents.setAdapter(eventsAdapter);

        prepareEventsData();
    }

    


    private void prepareEventsData() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.EVENTS_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.EVENTS_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    eventsItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("eventsdata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String eventid = jsonObject.getString("eventid");
                        String eventname = jsonObject.getString("eventname");
                        String venue = jsonObject.getString("venue");
                        String description = jsonObject.getString("description");
                        String eventdate = jsonObject.getString("eventdate");

                        JSONObject messageObject =new JSONObject(response.toString());
                        String message1 =messageObject.getString("message");
                        Log.e("msg",""+message1);


                        if (message1.equals("no data received..")){

                            txtNoData.setVisibility(View.VISIBLE);
                            recyclerCurrentEvents.setVisibility(View.GONE);
                        }
                        else {

                            txtNoData.setVisibility(View.GONE);
                            recyclerCurrentEvents.setVisibility(View.VISIBLE);
                        }

                        eventsItemList.add(new EventsItem(eventid,eventname,venue,eventdate,description));
                        eventsAdapter = new EventsAdapter(EventsActivity.this, eventsItemList);
                        recyclerCurrentEvents.setAdapter(eventsAdapter);
                        eventsAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EventsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

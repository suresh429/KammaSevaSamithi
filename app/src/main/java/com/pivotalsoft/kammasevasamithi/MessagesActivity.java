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
import com.pivotalsoft.kammasevasamithi.Adapters.MessageAdapter;
import com.pivotalsoft.kammasevasamithi.Adapters.NewsAdapter;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.Items.MessageItem;
import com.pivotalsoft.kammasevasamithi.Items.NewsItem;
import com.pivotalsoft.kammasevasamithi.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {
    private static final String TAG = MessagesActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private RecyclerView recyclerMessage;
    private MessageAdapter messageAdapter;
    private List<MessageItem> directoryItemList = new ArrayList<>();
    TextView txtNoData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Messages");

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
                Intent details =new Intent(MessagesActivity.this,AddMessagesActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(details);
            }
        });
        recyclerMessage = (RecyclerView) findViewById(R.id.messageRecycler);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this, 1);
        recyclerMessage.setLayoutManager(mLayoutManager1);
        recyclerMessage.setItemAnimator(new DefaultItemAnimator());

        messageAdapter = new MessageAdapter(this, directoryItemList);

        recyclerMessage.setAdapter(messageAdapter);


        prepareMessagesData();
    }



    private void prepareMessagesData() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.MESSAGE_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.MESSAGE_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    directoryItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("messagesdata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String msgid = jsonObject.getString("msgid");
                        String message = jsonObject.getString("message");
                        String sentdate = jsonObject.getString("sentdate");

                        JSONObject messageObject =new JSONObject(response.toString());
                        String message1 =messageObject.getString("message");
                        Log.e("msg",""+message1);


                        if (message1.equals("no data received..")){

                            txtNoData.setVisibility(View.VISIBLE);
                            recyclerMessage.setVisibility(View.GONE);
                        }
                        else {

                            txtNoData.setVisibility(View.GONE);
                            recyclerMessage.setVisibility(View.VISIBLE);
                        }


                        directoryItemList.add(new MessageItem(msgid,message,sentdate));
                        messageAdapter = new MessageAdapter(MessagesActivity.this, directoryItemList);
                        recyclerMessage.setAdapter(messageAdapter);
                        messageAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MessagesActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

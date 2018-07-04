package com.pivotalsoft.kammasevasamithi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pivotalsoft.kammasevasamithi.Adapters.MessageAdapter;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.Items.AllFcmTokenItem;
import com.pivotalsoft.kammasevasamithi.Items.MessageItem;
import com.pivotalsoft.kammasevasamithi.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddMessagesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = AddMessagesActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    ArrayList<String> arrPackage =  new ArrayList<>();
    EditText etMessage;
    String message,currentDate,resStringArray;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmessages);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add Messages");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //getting unique id for device
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("DEVICEID1",""+deviceId);

        etMessage=(EditText)findViewById(R.id.editTextMessage) ;

        currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        submit =(Button)findViewById(R.id.button_submit);
        submit.setOnClickListener(this);

      prepareAllFcmData();
    }
    private void prepareAllFcmData() {

        showDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.ALL_FCM_KEYS_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.ALL_FCM_KEYS_URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                    // directoryItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("fcmdata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String memberid = jsonObject.getString("memberid");
                        String fullname = jsonObject.getString("fullname");
                        String mobile = jsonObject.getString("mobile");
                        String fcmkey = jsonObject.getString("fcmkey");

                        //Add Array
                        arrPackage.add(fcmkey);

                        HashSet<String> hashSet = new HashSet<String>();
                        hashSet.addAll(arrPackage);
                        arrPackage.clear();
                        arrPackage.addAll(hashSet);


                    }

                    String str = "";

                    Iterator<String> it = arrPackage.iterator();
                    while (it.hasNext()){
                        String actual = it.next();
                        str = str + "\"" + actual + "\"";
                        str = str + ",";
                    }
                   String resString = str.substring(0,str.length()-1);
                    Log.e("Resstring",""+"["+resStringArray+"]");

                     resStringArray ="["+resString+"]";
                   // arrPackage.clear();

                    Log.e("arrPackage",""+arrPackage);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddMessagesActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(NewsActivity.this, "no data Found. check once data is enable or not..", Toast.LENGTH_LONG).show();
                // hide the progress dialog
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    @Override
    public void onClick(View view) {
        message = etMessage.getText().toString().trim();

        if (!message.isEmpty()){

            sendMessage();
            pushnotification();
        }
        else {
            Toast.makeText(AddMessagesActivity.this,"Enter Message",Toast.LENGTH_SHORT).show();
        }
    }

    private void pushnotification(){


        try{

           /* String fcmArray= Arrays.toString(new ArrayList[]{arrPackage}).replaceAll("\\[|\\]", "");
            Log.e("ARRAY",""+fcmArray);*/

            RequestQueue queue = Volley.newRequestQueue(AddMessagesActivity.this);

            String url = "https://fcm.googleapis.com/fcm/send";

            String body ="Hi! ";
            Log.e("body",""+message);

            JSONObject data = new JSONObject();
            data.put("title", "Message !");
            data.put("body", message);
            JSONObject notification_data = new JSONObject();
            notification_data.put("notification", data);
            notification_data.put("registration_ids",new JSONArray(resStringArray));

            Log.e("registration_ids",""+new JSONArray(resStringArray));

            JsonObjectRequest request = new JsonObjectRequest(url, notification_data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e("response",""+response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error",""+error);
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

    private void sendMessage(){

        pDialog.setMessage("Loading ...");
        showDialog();
       /* final String paymentMode ="Online";
        final String paymentStatus ="success";
        final String transactionId ="65465464";*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MESSAGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hideDialog();

                        Intent pivotal = new Intent(AddMessagesActivity.this, MessagesActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);

                        Toast.makeText(AddMessagesActivity.this,"Message Add Successfully",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RESPONSE_ERROR: ",""+error);
                        hideDialog();
                        // Toast.makeText(EnrollActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("message",message);
                params.put("sentdate",currentDate);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
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

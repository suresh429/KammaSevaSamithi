package com.pivotalsoft.kammasevasamithi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.app.AppController;
import com.pivotalsoft.kammasevasamithi.utils.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddDonationActivity extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener{
    int color;
    LinearLayout linearLayout;
    private static final String TAG = AddMessagesActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private DatePickerDialog tentDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    final int RADIOBUTTON_ALERTDIALOG = 1;
    final CharSequence[] payment_radio = {"Cash","Cheque","DD"};
    EditText etName,etDate,etPaymentmode,etDescription;
    Spinner spinner;

    String amount,date,description,paymentmode,memberid,adminFcmKey,fullName;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add Donations");

        // Manually checking internet connection
        checkConnection();

// Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 for private mode
        memberid = sp.getString("memberid", "1");
        fullName = sp.getString("fullname", "1");

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        etDate=(EditText)findViewById(R.id.etDate);
        etDate.setInputType(InputType.TYPE_NULL);
        etDate.requestFocus();
        etDate.setOnClickListener(this);

        etName=(EditText)findViewById(R.id.etName);
        etDescription=(EditText)findViewById(R.id.etDescription);

        etPaymentmode=(EditText) findViewById(R.id.etPaymentmode);
        etPaymentmode.setInputType(InputType.TYPE_NULL);
        etPaymentmode.requestFocus();
        etPaymentmode.setOnClickListener(this);

        submit =(Button)findViewById(R.id.button_submit);
        submit.setOnClickListener(this);

        setDateTimeField();
        adminData();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){


            case R.id.button_submit:

                boolean isConnected = ConnectivityReceiver.isConnected();

                if (isConnected){

                    validate();
                    Log.e("CONNECTIONTRUE",""+isConnected);
                }
                else {
                    checkConnection();
                    Log.e("CONNECTIONFALSE",""+isConnected);
                }

                break;

            case R.id.etDate:

                tentDatePickerDialog.show();
                break;

            case R.id.etPaymentmode:

                showDialog(RADIOBUTTON_ALERTDIALOG);
                break;

        }
    }

    /*triggered by showDialog method. onCreateDialog creates a dialog*/
    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {

            case RADIOBUTTON_ALERTDIALOG:

                AlertDialog.Builder builder2 = new AlertDialog.Builder(AddDonationActivity.this)
                        .setTitle("Payment Mode*")
                        .setSingleChoiceItems(payment_radio, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                                //Toast.makeText(getApplicationContext(),"The selected" + day_radio[which], Toast.LENGTH_LONG).show();

                                etPaymentmode.setText(payment_radio[which]);


//dismissing the dialog when the user makes a selection.
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog2 = builder2.create();
                return alertdialog2;


        }
        return null;

    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
// TODO Auto-generated method stub

        switch (id) {

            case RADIOBUTTON_ALERTDIALOG:
                AlertDialog prepare_radio_dialog = (AlertDialog) dialog;
                ListView list_radio = prepare_radio_dialog.getListView();
                for (int i = 0; i < list_radio.getCount(); i++) {
                    list_radio.setItemChecked(i, false);
                }
                break;


        }
    }


    private void setDateTimeField(){

        Calendar newCalendar = Calendar.getInstance();
        tentDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }


    private void validate(){

        amount=etName.getText().toString().trim();
        date=etDate.getText().toString().trim();
        description=etDescription.getText().toString().trim();
        paymentmode=etPaymentmode.getText().toString().trim();

        if (!amount.isEmpty() && !date.isEmpty() && !description.isEmpty() && !paymentmode.isEmpty() ){

            enrollUser();
        }else {

            Toast.makeText(AddDonationActivity.this,"Enter All Fields",Toast.LENGTH_SHORT).show();
        }
    }


    private void enrollUser(){

        pDialog.setMessage("Loading ...");
        showDialog();




        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.DONATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hideDialog();

                        pushnotification();

                        Intent pivotal = new Intent(AddDonationActivity.this, DonationActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);

                        Toast.makeText(AddDonationActivity.this,"Data Sent Successfully",Toast.LENGTH_LONG).show();
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
                params.put("memberid",memberid);
                params.put("description",description);
                params.put("paiddate",date);
                params.put("paymentmode",paymentmode);
                params.put("amount",amount);
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

    private void adminData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.ADMIN_FCM_KEY_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.ADMIN_FCM_KEY_URL);

                try {
                    // Parsing json object response
                    // response will be a json object

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("fcmdata");

                    //Iterate the jsonArray and print the info of JSONObjects
                    // for (int i = 0; i < jsonArray1.length(); i++) {

                    JSONObject jsonObject = jsonArray1.getJSONObject(0);

                    String memberid = jsonObject.getString("memberid");
                    String fullname = jsonObject.getString("fullname");
                    String mobile = jsonObject.getString("mobile");
                    adminFcmKey = jsonObject.getString("fcmkey");


                    //String profilepicurl = Apis.IMAGE_PROFILE_URL + jsonObject.optString("profilepicurl").toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                // Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }
        });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(AddDonationActivity.this);

        //Adding request to the queue
        requestQueue.add(jsonObjReq);

    }

    //push notification
    private void pushnotification(){


        try{

           /* String fcmArray= Arrays.toString(new ArrayList[]{arrPackage}).replaceAll("\\[|\\]", "");
            Log.e("ARRAY",""+fcmArray);*/

            RequestQueue queue = Volley.newRequestQueue(AddDonationActivity.this);

            String url = "https://fcm.googleapis.com/fcm/send";

            String body ="Hi! Admin "+fullName+" is Donated "+amount;
            Log.e("body",""+body);

            JSONObject data = new JSONObject();
            data.put("title", "Applied !");
            data.put("body", body);
            JSONObject notification_data = new JSONObject();
            notification_data.put("notification", data);
            notification_data.put("to",adminFcmKey);

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

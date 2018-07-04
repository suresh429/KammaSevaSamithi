package com.pivotalsoft.kammasevasamithi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.app.AppController;
import com.pivotalsoft.kammasevasamithi.utils.ConnectivityReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditEventActivity extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener{
    int color;
    NestedScrollView nestedScrollView;
    private ProgressDialog pDialog;
    private DatePickerDialog tentDatePickerDialog,startDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    EditText ettentdate,etstartDaet,etEventName,etVenue,etDescription;
    String tentdate,startDaet,eventName,venue,description,eventid;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Update Events");

        // Manually checking internet connection
        checkConnection();

        eventid = getIntent().getExtras().getString("eventid","defaultKey");
        String txteventname = getIntent().getExtras().getString("eventname","defaultKey");
        String txtvenue = getIntent().getExtras().getString("venue","defaultKey");
        String txteventdate = getIntent().getExtras().getString("eventdate","defaultKey");
        String txtdescription = getIntent().getExtras().getString("description","defaultKey");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        etEventName=(EditText) findViewById(R.id.etEventName);
        etEventName.setText(txteventname);

        etVenue=(EditText) findViewById(R.id.etVenue);
        etVenue.setText(txtvenue);

        etDescription=(EditText) findViewById(R.id.etDescription);
        etDescription.setText(txtdescription);



        etstartDaet=(EditText) findViewById(R.id.etStartDate);
        etstartDaet.setText(txteventdate);
        etstartDaet.setInputType(InputType.TYPE_NULL);
        etstartDaet.requestFocus();
        etstartDaet.setOnClickListener(this);



        btnSave =(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        setDateTimeField();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.btnSave:
                boolean isConnected = ConnectivityReceiver.isConnected();

                if (isConnected){

                    saveDate();
                    Log.e("CONNECTIONTRUE",""+isConnected);
                }
                else {
                    checkConnection();
                    Log.e("CONNECTIONFALSE",""+isConnected);
                }

                break;


            case R.id.etStartDate:

                startDatePickerDialog.show();
                break;

        }
    }

    private void setDateTimeField(){

        Calendar newCalendar = Calendar.getInstance();


        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etstartDaet.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void saveDate(){

        eventName =etEventName.getText().toString().trim();

        startDaet =etstartDaet.getText().toString().trim();
        venue =etVenue.getText().toString().trim();
        description =etDescription.getText().toString().trim();


        if (!eventName.isEmpty()  && !venue.isEmpty() && !description.isEmpty() ) {

            addAdds();

        } else {
            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
        }
    }


    private void addAdds(){

        pDialog.setMessage("Loading ...");
        showDialog();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_EVENTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hideDialog();

                        Intent pivotal = new Intent(EditEventActivity.this, EventsActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);
                        Toast.makeText(EditEventActivity.this,"Event Updated Successfully",Toast.LENGTH_LONG).show();
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
                params.put("eventid",eventid);
                params.put("eventname",eventName);
                params.put("venue",venue);
                params.put("eventdate",startDaet);
                params.put("description", description);
                Log.e("RESPONSE_Parasms: ",""+eventName+"\n"+venue+"\n"+startDaet);
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

        nestedScrollView =(NestedScrollView) findViewById(R.id.parentLayout);

        setSnackBar(nestedScrollView,message);
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
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }


}

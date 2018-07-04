package com.pivotalsoft.kammasevasamithi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
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

public class AddNewsActivity extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener{
    int color;
    LinearLayout linearLayout;
    private static final String TAG = AddMessagesActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private DatePickerDialog tentDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    EditText etTitle,etDate,etDescription;

    String title,date,description;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add News");

        // Manually checking internet connection
        checkConnection();

// Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        etDate=(EditText)findViewById(R.id.etDate);
        etDate.setInputType(InputType.TYPE_NULL);
        etDate.requestFocus();
        etDate.setOnClickListener(this);

        etTitle=(EditText)findViewById(R.id.etTitle);
        etDescription=(EditText)findViewById(R.id.etDescription);

        submit =(Button)findViewById(R.id.button_submit);
        submit.setOnClickListener(this);

    setDateTimeField();
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

        title=etTitle.getText().toString().trim();
        date=etDate.getText().toString().trim();
        description=etDescription.getText().toString().trim();


        if (!title.isEmpty() && !date.isEmpty() && !description.isEmpty()){

            enrollUser();
        }else {

            Toast.makeText(AddNewsActivity.this,"Enter All Fields",Toast.LENGTH_SHORT).show();
        }
    }


    private void enrollUser(){

        pDialog.setMessage("Loading ...");
        showDialog();




        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.NEWS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hideDialog();

                        Intent pivotal = new Intent(AddNewsActivity.this, NewsActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);

                        Toast.makeText(AddNewsActivity.this,"Data Sent Successfully",Toast.LENGTH_LONG).show();
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
                params.put("title",title);
                params.put("postedon",date);
                params.put("description",description);
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


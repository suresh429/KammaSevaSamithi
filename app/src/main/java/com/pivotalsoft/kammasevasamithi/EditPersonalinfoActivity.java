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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class EditPersonalinfoActivity extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener{
    final int RADIOBUTTON_ALERTDIALOG1 = 2;
    final CharSequence[] bloodgroup_radio = {"A+","A-","B+","B-","O+","o-","AB+","AB-"};
    int color;
    NestedScrollView nested;
    private ProgressDialog pDialog;
    private DatePickerDialog startDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    EditText etDob,etFullName,etFathername,etMobileno,etEmail,etGothram,etBloodgroup,etPresentAddress,etNativeAddress;
    String dob,fullname,fatherName,mobileno,email,gothram,bloodgroup,presentaddress,nativeaddress,memberid;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personalinfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Update PersonalInfo");

        // Manually checking internet connection
        checkConnection();
        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 for private mode
        memberid = sp.getString("memberid", "1");


        String txtFullname = getIntent().getExtras().getString("fullname","defaultKey");
        String txtFathername = getIntent().getExtras().getString("fathername","defaultKey");
        String txtMobile = getIntent().getExtras().getString("mobile","defaultKey");
        String txtEmail = getIntent().getExtras().getString("email","defaultKey");
        String txtGothram = getIntent().getExtras().getString("gothram","defaultKey");
        String txtBloodgroup = getIntent().getExtras().getString("bloodgroup","defaultKey");
        String txtpresentAddress = getIntent().getExtras().getString("presentaddress","defaultKey");
        String txtnativeAddress = getIntent().getExtras().getString("nativeaddress","defaultKey");
        String txtDob = getIntent().getExtras().getString("dob","defaultKey");
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        etDob=(EditText) findViewById(R.id.etDob);
        etDob.setText(txtDob);
        etDob.setInputType(InputType.TYPE_NULL);
        etDob.requestFocus();
        etDob.setOnClickListener(this);

        etFullName=(EditText) findViewById(R.id.etFullName);
        etFullName.setText(txtFullname);

        etFathername=(EditText) findViewById(R.id.etFatherName);
        etFathername.setText(txtFathername);

        etMobileno=(EditText) findViewById(R.id.etMobile);
        etMobileno.setText(txtMobile);

        etEmail=(EditText) findViewById(R.id.etEmail);
        etEmail.setText(txtEmail);

        etGothram=(EditText) findViewById(R.id.etGothram);
        etGothram.setText(txtGothram);

        etBloodgroup=(EditText) findViewById(R.id.etBloodGroup);
        etBloodgroup.setText(txtBloodgroup);
        etBloodgroup.setInputType(InputType.TYPE_NULL);
        etBloodgroup.requestFocus();
        etBloodgroup.setOnClickListener(this);

        etPresentAddress=(EditText) findViewById(R.id.etPresentAddress);
        etPresentAddress.setText(txtpresentAddress);

        etNativeAddress=(EditText) findViewById(R.id.etNativeAddress);
        etNativeAddress.setText(txtnativeAddress);


        btnSave =(Button)findViewById(R.id.button_submit);
        btnSave.setOnClickListener(this);

        setDateTimeField();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.button_submit:
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


            case R.id.etDob:

                startDatePickerDialog.show();
                break;

            case R.id.etBloodGroup:

                showDialog(RADIOBUTTON_ALERTDIALOG1);
                break;

        }
    }

    /*triggered by showDialog method. onCreateDialog creates a dialog*/
    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {



            case RADIOBUTTON_ALERTDIALOG1:

                AlertDialog.Builder builder21 = new AlertDialog.Builder(EditPersonalinfoActivity.this)
                        .setTitle("BloodGroup Type*")
                        .setSingleChoiceItems(bloodgroup_radio, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                                //Toast.makeText(getApplicationContext(),"The selected" + bloodgroup_radio[which], Toast.LENGTH_LONG).show();

                                etBloodgroup.setText(bloodgroup_radio[which]);


//dismissing the dialog when the user makes a selection.
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog21 = builder21.create();
                return alertdialog21;
        }
        return null;

    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
// TODO Auto-generated method stub

        switch (id) {


            case RADIOBUTTON_ALERTDIALOG1:
                AlertDialog prepare_radio_dialog1 = (AlertDialog) dialog;
                ListView list_radio1 = prepare_radio_dialog1.getListView();
                for (int i = 0; i < list_radio1.getCount(); i++) {
                    list_radio1.setItemChecked(i, false);
                }
                break;

        }
    }

    private void setDateTimeField(){

        Calendar newCalendar = Calendar.getInstance();


        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDob.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void saveDate(){

        fullname =etFullName.getText().toString().trim();
        fatherName =etFathername.getText().toString().trim();
        dob =etDob.getText().toString().trim();
        mobileno =etMobileno.getText().toString().trim();
        email =etEmail.getText().toString().trim();
        gothram =etGothram.getText().toString().trim();
        bloodgroup =etBloodgroup.getText().toString().trim();
        presentaddress =etPresentAddress.getText().toString().trim();
        nativeaddress =etNativeAddress.getText().toString().trim();


        if (!fullname.isEmpty() && !fatherName.isEmpty()  && !dob.isEmpty() && !mobileno.isEmpty() && !email.isEmpty() && !gothram.isEmpty() && !bloodgroup.isEmpty() && !presentaddress.isEmpty() && !nativeaddress.isEmpty()) {

            addAdds();

        } else {
            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
        }
    }

    private void addAdds(){

        pDialog.setMessage("Loading ...");
        showDialog();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_PERSONAL_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hideDialog();
                        int page = 0;
                        Intent pivotal = new Intent(EditPersonalinfoActivity.this, ProfileActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        pivotal.putExtra("One", page);// One is your argument
                        startActivity(pivotal);
                        Toast.makeText(EditPersonalinfoActivity.this,"Personal info Updated Successfully",Toast.LENGTH_LONG).show();
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
                params.put("memberid",memberid);
                params.put("fullname",fullname);
                params.put("fathername",fatherName);
                params.put("mobile",mobileno);
                params.put("email",email);
                params.put("dob",dob);
                params.put("gothram",gothram);
                params.put("bloodgroup",bloodgroup);
                params.put("presentaddress", presentaddress);
                params.put("nativeaddress", nativeaddress);
                Log.e("RESPONSE_Parasms: ",""+params);
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

        nested =(NestedScrollView) findViewById(R.id.parentLayout);

        setSnackBar(nested,message);
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

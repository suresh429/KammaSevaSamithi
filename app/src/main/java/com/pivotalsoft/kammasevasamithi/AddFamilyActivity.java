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

public class AddFamilyActivity extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener{
    int color;
    NestedScrollView nestedScrollView;
    private ProgressDialog pDialog;
    private DatePickerDialog startDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    final int RADIOBUTTON_ALERTDIALOG = 1;
    final int RADIOBUTTON_ALERTDIALOG1 = 2;
    final int RADIOBUTTON_ALERTDIALOG2 = 3;
    final CharSequence[] day_radio = {"Spouse","Brother","Sister","Mother","Father","Son","Daughter","Other"};
    final CharSequence[] bloodgroup_radio = {"A+","A-","B+","B-","O+","o-","AB+","AB-"};
    final CharSequence[] gender_radio = {"male","female","others"};

    EditText etstartDaet,etMemberName,etMobileno,etGender,etRelation,etOccation,etQualification,etBloodGroup;
    String startDaet,memberName,mobileno,gender,relation,occation,memberid,qualification,bloodgroup;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add Family");

        // Manually checking internet connection
        checkConnection();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 for private mode
        memberid = sp.getString("memberid", null);
        Log.e("MEBERID",""+memberid);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        etMemberName=(EditText) findViewById(R.id.etMemberName);

        etRelation=(EditText) findViewById(R.id.etRelation);
        etRelation.setInputType(InputType.TYPE_NULL);
        etRelation.requestFocus();
        etRelation.setOnClickListener(this);

        etQualification=(EditText) findViewById(R.id.etQualification);
        etBloodGroup=(EditText) findViewById(R.id.etBloodGroup);
        etBloodGroup.setInputType(InputType.TYPE_NULL);
        etBloodGroup.requestFocus();
        etBloodGroup.setOnClickListener(this);

        etOccation=(EditText) findViewById(R.id.etOccation);
        etMobileno=(EditText)findViewById(R.id.etMobile);

        etGender=(EditText)findViewById(R.id.etGender);
        etGender.setInputType(InputType.TYPE_NULL);
        etGender.requestFocus();
        etGender.setOnClickListener(this);


        etstartDaet=(EditText) findViewById(R.id.etStartDate);
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

            case R.id.etRelation:

                showDialog(RADIOBUTTON_ALERTDIALOG);
                break;

            case R.id.etBloodGroup:

                showDialog(RADIOBUTTON_ALERTDIALOG1);
                break;

            case R.id.etGender:

                showDialog(RADIOBUTTON_ALERTDIALOG2);
                break;

            case R.id.etStartDate:

                startDatePickerDialog.show();
                break;

        }
    }

    /*triggered by showDialog method. onCreateDialog creates a dialog*/
    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {

            case RADIOBUTTON_ALERTDIALOG:

                AlertDialog.Builder builder2 = new AlertDialog.Builder(AddFamilyActivity.this)
                        .setTitle("Relation Type*")
                        .setSingleChoiceItems(day_radio, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                                //Toast.makeText(getApplicationContext(),"The selected" + day_radio[which], Toast.LENGTH_LONG).show();

                                etRelation.setText(day_radio[which]);


//dismissing the dialog when the user makes a selection.
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog2 = builder2.create();
                return alertdialog2;

            case RADIOBUTTON_ALERTDIALOG1:

                AlertDialog.Builder builder21 = new AlertDialog.Builder(AddFamilyActivity.this)
                        .setTitle("BloodGroup Type*")
                        .setSingleChoiceItems(bloodgroup_radio, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                               //Toast.makeText(getApplicationContext(),"The selected" + bloodgroup_radio[which], Toast.LENGTH_LONG).show();

                                etBloodGroup.setText(bloodgroup_radio[which]);


//dismissing the dialog when the user makes a selection.
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog21 = builder21.create();
                return alertdialog21;

            case RADIOBUTTON_ALERTDIALOG2:

                AlertDialog.Builder builder22 = new AlertDialog.Builder(AddFamilyActivity.this)
                        .setTitle("Gender*")
                        .setSingleChoiceItems(bloodgroup_radio, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                                //Toast.makeText(getApplicationContext(),"The selected" + bloodgroup_radio[which], Toast.LENGTH_LONG).show();

                                etGender.setText(gender_radio[which]);


//dismissing the dialog when the user makes a selection.
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertdialog22 = builder22.create();
                return alertdialog22;
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

            case RADIOBUTTON_ALERTDIALOG1:
                AlertDialog prepare_radio_dialog1 = (AlertDialog) dialog;
                ListView list_radio1 = prepare_radio_dialog1.getListView();
                for (int i = 0; i < list_radio1.getCount(); i++) {
                    list_radio1.setItemChecked(i, false);
                }
                break;

            case RADIOBUTTON_ALERTDIALOG2:
                AlertDialog prepare_radio_dialog2 = (AlertDialog) dialog;
                ListView list_radio2 = prepare_radio_dialog2.getListView();
                for (int i = 0; i < list_radio2.getCount(); i++) {
                    list_radio2.setItemChecked(i, false);
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
                etstartDaet.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void saveDate(){

        memberName =etMemberName.getText().toString().trim();
        startDaet =etstartDaet.getText().toString().trim();
        mobileno=etMobileno.getText().toString().trim();
        gender=etGender.getText().toString().trim();
        relation =etRelation.getText().toString().trim();
        occation =etOccation.getText().toString().trim();
        qualification =etQualification.getText().toString().trim();
        bloodgroup =etBloodGroup.getText().toString().trim();



        if (!memberName.isEmpty() && !startDaet.isEmpty() && !mobileno.isEmpty() && !gender.isEmpty() && !relation.isEmpty() && !occation.isEmpty() && !bloodgroup.isEmpty() && !qualification.isEmpty()) {

            addAdds();

        } else {
            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
        }
    }


    private void addAdds(){

        pDialog.setMessage("Loading ...");
        showDialog();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_FAMILY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hideDialog();

                        int page = 2;
                        Intent pivotal = new Intent(AddFamilyActivity.this, ProfileActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        pivotal.putExtra("One", page);// One is your argument
                        startActivity(pivotal);

                       // onBackPressed();
                        Toast.makeText(AddFamilyActivity.this,"Family Added Successfully",Toast.LENGTH_LONG).show();
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
                params.put("membername",memberName);
                params.put("mobileno",mobileno);
                params.put("gender",gender);
                params.put("relation",relation);
                params.put("qualification",qualification);
                params.put("bloodgroup",bloodgroup);
                params.put("occassion",occation);
                params.put("occdate", startDaet);
               // Log.e("RESPONSE_Parasms: ",""+eventName+"\n"+venue+"\n"+tentdate+"\n"+startDaet);
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

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.app.AppController;
import com.pivotalsoft.kammasevasamithi.utils.ConnectivityReceiver;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptActivity extends AppCompatActivity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener{
    private ProgressDialog pDialog;

    final int RADIOBUTTON_ALERTDIALOG = 1;
    final CharSequence[] day_radio = {"Member","Commitee","VIP"};
    int color;
    NestedScrollView  nestedScrollView;
    EditText etMemberName,etmembershipType,etmembershipno,etmobie,etemail;
    String memberName,membershipType,membershipno,email,mobile,password,memberid,fcmkey,fullname;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Accept");

        // Manually checking internet connection
        checkConnection();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        fullname =getIntent().getStringExtra("name");
        String  mobile =getIntent().getStringExtra("mobile");
        String  email =getIntent().getStringExtra("email");
        memberid =getIntent().getStringExtra("memberid");
        fcmkey =getIntent().getStringExtra("fcmkey");
        String profilepic =getIntent().getStringExtra("profilepic");

        try {
            Glide.with(this).load(profilepic).into((ImageView) findViewById(R.id.imageProfile));
        } catch (Exception e) {
            e.printStackTrace();
        }

        etMemberName=(EditText) findViewById(R.id.etMemberName);
        etMemberName.setText(fullname);
        etMemberName.setEnabled(false);

        etmembershipType=(EditText) findViewById(R.id.etmembershipType);
        etmembershipType.setInputType(InputType.TYPE_NULL);
        etmembershipType.requestFocus();
        etmembershipType.setOnClickListener(this);

        etmembershipno=(EditText) findViewById(R.id.etMembershipNo);

        etemail=(EditText) findViewById(R.id.etEmail);
        etemail.setText(email);
        etemail.setEnabled(false);

        etmobie=(EditText) findViewById(R.id.etMobile);
        etmobie.setText(mobile);
        etmobie.setEnabled(false);

        btnSave =(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);




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

            case R.id.etmembershipType:

                showDialog(RADIOBUTTON_ALERTDIALOG);
                break;



        }
    }

    /*triggered by showDialog method. onCreateDialog creates a dialog*/
    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {

            case RADIOBUTTON_ALERTDIALOG:

                AlertDialog.Builder builder2 = new AlertDialog.Builder(AcceptActivity.this)
                        .setTitle("Membership Type*")
                        .setSingleChoiceItems(day_radio, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                                Toast.makeText(getApplicationContext(),"The selected" + day_radio[which], Toast.LENGTH_LONG).show();

                                etmembershipType.setText(day_radio[which]);


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


    private void saveDate(){

        memberName =etMemberName.getText().toString().trim();
        mobile =etmobie.getText().toString().trim();
        email =etemail.getText().toString().trim();
        membershipno =etmembershipno.getText().toString().trim();
        membershipType =etmembershipType.getText().toString().trim();
        Toast.makeText(AcceptActivity.this,""+membershipType,Toast.LENGTH_LONG).show();
       // password =etMemberName.getText().toString().trim().toLowerCase().substring(0,4)+"@123";

        if ( !membershipno.isEmpty()&& !membershipType.isEmpty() ) {

            addAdds();

        } else {
            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
        }
    }


    private void addAdds(){

        pDialog.setMessage("Loading ...");
        showDialog();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ACCEPT_RIGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hideDialog();

                        pushnotification();

                        Intent pivotal = new Intent(AcceptActivity.this, RegNotificationActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);
                        Toast.makeText(AcceptActivity.this,"Accept Successfully",Toast.LENGTH_LONG).show();
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
                params.put("membershipno",membershipno);
                params.put("role",membershipType);
               // params.put("password",password);

                Log.e("RESPONSE_Parasms: ",""+memberid+"\n"+memberName+"\n"+membershipno+"\n"+membershipType);
//                Toast.makeText(AcceptActivity.this,""+memberid+"\n"+memberName+"\n"+membershipno+"\n"+membershipType,Toast.LENGTH_LONG).show();
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


    //push notification
    private void pushnotification(){

        Log.e("FCMKEY",fcmkey);
        try{

           /* String fcmArray= Arrays.toString(new ArrayList[]{arrPackage}).replaceAll("\\[|\\]", "");
            Log.e("ARRAY",""+fcmArray);*/

            RequestQueue queue = Volley.newRequestQueue(AcceptActivity.this);

            String url = "https://fcm.googleapis.com/fcm/send";

            String body ="Hi! "+fullname+" your Registration Accepted. Welcome to Kamma Seva Samithi!";
            Log.e("body",""+body);

            JSONObject data = new JSONObject();
            data.put("title", "Applied !");
            data.put("body", body);
            JSONObject notification_data = new JSONObject();
            notification_data.put("notification", data);
            notification_data.put("to",fcmkey);

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
package com.pivotalsoft.kammasevasamithi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.app.AppController;
import com.pivotalsoft.kammasevasamithi.utils.ConnectivityReceiver;
import com.pivotalsoft.kammasevasamithi.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity implements View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener{
    private Session session;
    // Progress dialog
    private ProgressDialog pDialog;

    private static String TAG = LoginActivity.class.getSimpleName();

    private String URL_LOGIN;
    private String fcmtkn;
    int color;
    LinearLayout linearLayout;
    TextView txtRegister;
    EditText etMobile,etPassword;

    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Manually checking internet connection
        checkConnection();

        session = new Session(this);

        if(session.loggedin()){
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }

        fcmtkn = FirebaseInstanceId.getInstance().getToken();
        // Toast.makeText(getApplicationContext(), "Current token ["+fcmtkn+"]", Toast.LENGTH_LONG).show();
        Log.e("FcmKEY", "Token ["+fcmtkn+"]");

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
       // pDialog.show();


        etMobile=(EditText)findViewById(R.id.etMobile);
        etPassword=(EditText)findViewById(R.id.etPassword);

        btnLogin=(Button)findViewById(R.id.Login);
        btnLogin.setOnClickListener(this);

        txtRegister=(TextView)findViewById(R.id.txtRegister);
        txtRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.Login:

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

            case R.id.txtRegister:
                Intent reg =new Intent(LoginActivity.this,RegisterActivity.class);
                reg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(reg);
                break;

        }

    }

    private void validate(){

        final String mobile = etMobile.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        URL_LOGIN = Constants.LOGIN_URL+"/"+mobile+"/"+password;

        Log.e("loginurl",""+URL_LOGIN);


        if (!mobile.isEmpty()  && !password.isEmpty()) {

            loginUser();

        } else {
            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
        }
    }

    private void loginUser() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL_LOGIN, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+URL_LOGIN);

                try {
                    // Parsing json object response
                    // response will be a json object

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("userdata");

                    //Iterate the jsonArray and print the info of JSONObjects
                    // for (int i = 0; i < jsonArray1.length(); i++) {

                    JSONObject jsonObject = jsonArray1.getJSONObject(0);

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
                    //String profilepicurl = Apis.IMAGE_PROFILE_URL + jsonObject.optString("profilepicurl").toString();

                    JSONObject messageObject =new JSONObject(response.toString());
                    String message =messageObject.getString("message");
                    Log.e("msg",""+message);

                    if (message.equals("login successfully..")){

                        Intent pivotal = new Intent(LoginActivity.this, HomeActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);
                        Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();


                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("memberid", memberid);  // Saving string
                        editor.putString("fullname", fullname);  // Saving string
                        editor.putString("fathername", fathername);  // Saving string
                        editor.putString("mobile", mobile);  // Saving string
                        editor.putString("email", email);  // Saving string
                        editor.putString("gothram", gothram);  // Saving string
                        editor.putString("dob", dob);  // Saving string
                        editor.putString("bloodgroup", bloodgroup);  // Saving string
                        editor.putString("address", nativeaddress);  // Saving string
                        editor.putString("membershipNo", membershipno);  // Saving string
                        editor.putString("profilepic", profilepic);  // Saving string
                        editor.putString("role", role);  // Saving string

                        // editor.putString("profilepicurl", profilepicurl);  // Saving string
                        // Save the changes in SharedPreferences
                        editor.commit(); // commit changes

                        session.setLoggedin(true);

                        updateUser();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(LoginActivity.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        //Adding request to the queue
        requestQueue.add(jsonObjReq);

    }

    private void updateUser(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_FCM_KEY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);


                      /*  Intent pivotal = new Intent(AddSkillsActivity.this, EventsActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);
                        Toast.makeText(AddSkillsActivity.this,"Event Added Successfully",Toast.LENGTH_LONG).show();*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RESPONSE_ERROR: ",""+error);
                        //hideDialog();
                        // Toast.makeText(AddAddsActivity.this,"Email Already Exist",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("mobile",etMobile.getText().toString());
                params.put("fcmkey",fcmtkn);

                Log.e("RESPONSE_Parasms: ",""+params);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
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

        linearLayout =(LinearLayout)findViewById(R.id.parentLayout);

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

}

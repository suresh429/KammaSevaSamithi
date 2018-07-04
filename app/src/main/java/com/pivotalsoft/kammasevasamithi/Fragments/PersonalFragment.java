package com.pivotalsoft.kammasevasamithi.Fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.EditPersonalinfoActivity;
import com.pivotalsoft.kammasevasamithi.EventsActivity;
import com.pivotalsoft.kammasevasamithi.ProfileActivity;
import com.pivotalsoft.kammasevasamithi.R;
import com.pivotalsoft.kammasevasamithi.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {
    Dialog dialog;
    // Progress dialog
    private ProgressDialog pDialog;
    TextView txtFullName,txtfatherName,txtNativeaddress,txtmobile,txtemail,txtgothram,txtbloodgroup,txtPresentAddress,txtdob;
    String memberid;
    FloatingActionButton floatingActionButton;
    View rootview;
    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_personal, container, false);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getActivity().getSharedPreferences("MyPref", 0); // 0 for private mode
        memberid = sp.getString("memberid", "1");

        txtFullName=(TextView)rootview.findViewById(R.id.txtFullName);
        txtfatherName=(TextView)rootview.findViewById(R.id.txtFather);
        txtPresentAddress=(TextView)rootview.findViewById(R.id.txtPresentAddress);
        txtNativeaddress=(TextView)rootview.findViewById(R.id.txtNativeAddress);
        txtmobile=(TextView)rootview.findViewById(R.id.txtMobile);
        txtemail=(TextView)rootview.findViewById(R.id.txtEmail);
        txtgothram=(TextView)rootview.findViewById(R.id.txtGothram);
        txtbloodgroup=(TextView)rootview.findViewById(R.id.txtBloodgroup);
        txtdob=(TextView)rootview.findViewById(R.id.txtDob);


        floatingActionButton=(FloatingActionButton)rootview.findViewById(R.id.addFab);

        prepareProfileData();

        return rootview;
    }


    private void prepareProfileData() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.PROFILE_INFO_URL+memberid, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d("", response.toString());

                Log.e("URL",""+Constants.PROFILE_INFO_URL+memberid);

                try {
                    // Parsing json object response
                    // response will be a json object
                    // directoryItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("profdata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String memberid = jsonObject.getString("memberid");
                        final String fullname = jsonObject.getString("fullname");
                        final String fathername = jsonObject.getString("fathername");
                        final String dob = jsonObject.getString("dob");
                        final String gothram = jsonObject.getString("gothram");
                        final String mobile = jsonObject.getString("mobile");
                        final String email = jsonObject.getString("email");
                        final String bloodgroup = jsonObject.getString("bloodgroup");
                        String referalno = jsonObject.getString("referalno");
                        final String presentaddress = jsonObject.getString("presentaddress");
                        final String nativeaddress = jsonObject.getString("nativeaddress");
                        String profilepic = Constants.IMAGE_PROFILEPIC_URL+jsonObject.getString("profilepic");
                        String role = jsonObject.getString("role");
                        String status = jsonObject.getString("status");
                        String membershipno = jsonObject.getString("membershipno");
                        String password = jsonObject.getString("password");
                        String regdate = jsonObject.getString("regdate");
                        String fcmkey = jsonObject.getString("fcmkey");

                        JSONObject messageObject =new JSONObject(response.toString());
                        String message1 =messageObject.getString("message");
                        Log.e("msg",""+message1);

                        txtFullName.setText(fullname);
                        txtfatherName.setText(fathername);
                        txtPresentAddress.setText(presentaddress);
                        txtNativeaddress.setText(nativeaddress);
                        txtmobile.setText(mobile);
                        txtemail.setText(email);
                        txtgothram.setText(gothram);
                        txtbloodgroup.setText(bloodgroup);
                        txtdob.setText(dob);


                        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent pivotal = new Intent(getContext(), EditPersonalinfoActivity.class);
                                pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                pivotal.putExtra("fullname",fullname);
                                pivotal.putExtra("fathername",fathername);
                                pivotal.putExtra("presentaddress",presentaddress);
                                pivotal.putExtra("nativeaddress",nativeaddress);
                                pivotal.putExtra("dob",dob);
                                pivotal.putExtra("mobile",mobile);
                                pivotal.putExtra("email",email);
                                pivotal.putExtra("gothram",gothram);
                                pivotal.putExtra("bloodgroup",bloodgroup);
                                startActivity(pivotal);
                            }
                        });


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                // Toast.makeText(OffersActivity.this, "no data Found. check once data is enable or not..", Toast.LENGTH_LONG).show();
                // hide the progress dialog
                //hidepDialog();
            }
        });


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
}

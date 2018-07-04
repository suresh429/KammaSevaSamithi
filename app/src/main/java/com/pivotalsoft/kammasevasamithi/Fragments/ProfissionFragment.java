package com.pivotalsoft.kammasevasamithi.Fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.pivotalsoft.kammasevasamithi.Adapters.DirectoryAdapter;
import com.pivotalsoft.kammasevasamithi.AddFamilyActivity;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.DirectoryActivity;
import com.pivotalsoft.kammasevasamithi.DonationActivity;
import com.pivotalsoft.kammasevasamithi.EditEventActivity;
import com.pivotalsoft.kammasevasamithi.Items.DirectoryItem;
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
public class ProfissionFragment extends Fragment {
    // Log tag
    private static final String TAG = ProfissionFragment.class.getSimpleName();

    TextView txtqualification,txtprofission,txtdesignation,txtcompany;
    String qualification,profision,designation,company,memberid;
    String Qualification,Profession,Designation,Company;
    String URL;
    Dialog dialog;
    // Progress dialog
    private ProgressDialog pDialog;
    View rootview;
    public ProfissionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_profission, container, false);

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getActivity().getSharedPreferences("MyPref", 0); // 0 for private mode
        memberid = sp.getString("memberid", "1");


        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        URL=Constants.PROFISSIONAL_INFO_URL+memberid;

        txtqualification=(TextView)rootview.findViewById(R.id.txtEducationalQul);
       // qualification.setText("Qualification : B.Tech (IT)");

        txtprofission=(TextView)rootview.findViewById(R.id.txtProffission);
       // profission.setText("Proffesion : Job");

        txtdesignation=(TextView)rootview.findViewById(R.id.txtDesignation);
       // designation.setText("Designation : G.A.Tester");

        txtcompany=(TextView)rootview.findViewById(R.id.txtCompany);


        FloatingActionButton floatingActionButton=(FloatingActionButton)rootview.findViewById(R.id.addFab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDilaogue();
            }
        });

        prepareUpdatData();
        return rootview;
    }

    private void prepareUpdatData() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+URL);

                try {
                    // Parsing json object response
                    // response will be a json object
                   // directoryItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("profdata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String profid = jsonObject.getString("profid");
                         Qualification = jsonObject.getString("qualification");
                         Profession = jsonObject.getString("profession");
                         Designation = jsonObject.getString("designation");
                         Company = jsonObject.getString("company");
                        String memberid = jsonObject.getString("memberid");

                        txtqualification.setText(Qualification);
                        txtprofission.setText(Profession);
                        txtdesignation.setText(Designation);
                        txtcompany.setText(Company);



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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(OffersActivity.this, "no data Found. check once data is enable or not..", Toast.LENGTH_LONG).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

             // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void updateDilaogue(){

        // custom dialog
        dialog = new Dialog(getContext(),android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        dialog.setTitle("Update Info");
        /*dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));*/
        dialog.setContentView(R.layout.update_profision_layout);

        // set the custom dialog components - text, image and button
        final EditText etQulaification = (EditText) dialog.findViewById(R.id.etQualification);
        etQulaification.setText(Qualification);
        Log.e("qualification",""+Qualification);

        final EditText etProfision = (EditText) dialog.findViewById(R.id.etProfission);
        etProfision.setText(Profession);

        final EditText etDesignation = (EditText) dialog.findViewById(R.id.etDesignation);
        etDesignation.setText(Designation);

        final EditText etCompany = (EditText) dialog.findViewById(R.id.etCompany);
        etCompany.setText(Company);

        Button btn =(Button)dialog.findViewById(R.id.button_submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 qualification=etQulaification.getText().toString().trim();
                 profision=etProfision.getText().toString().trim();
                 designation=etDesignation.getText().toString().trim();
                 company=etCompany.getText().toString().trim();

                if (!qualification.isEmpty() && !profision.isEmpty() && !designation.isEmpty() && !company.isEmpty()){

                    updateinfo();


                }
                else {

                    Toast.makeText(getContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }

            }
        });

        dialog.show();
    }

    private void updateinfo(){



        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_PROF_INFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hidepDialog();

                        dialog.dismiss();
                        int page = 1;
                        Intent pivotal = new Intent(getContext(), ProfileActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        pivotal.putExtra("One", page);// One is your argument
                        startActivity(pivotal);
                       // Toast.makeText(EditEventActivity.this,"Event Updated Successfully",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RESPONSE_ERROR: ",""+error);
                        hidepDialog();
                        // Toast.makeText(AddAddsActivity.this,"Email Already Exist",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put("memberid",memberid);
                params.put("qualification",qualification);
                params.put("profession", profision);
                params.put("designation",designation);
                params.put("company",company);
                Log.e("RESPONSE_Parasms: ",""+qualification+"\n"+profision+"\n"+designation+"\n"+company);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
}

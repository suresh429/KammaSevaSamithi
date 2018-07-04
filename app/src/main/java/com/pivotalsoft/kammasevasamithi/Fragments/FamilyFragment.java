package com.pivotalsoft.kammasevasamithi.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pivotalsoft.kammasevasamithi.Adapters.FamilyAdapter;
import com.pivotalsoft.kammasevasamithi.AddFamilyActivity;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.Items.FamilyItem;
import com.pivotalsoft.kammasevasamithi.R;
import com.pivotalsoft.kammasevasamithi.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {
    private static final String TAG = FamilyFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    private RecyclerView recyclerDirectory;
    private FamilyAdapter directoryAdapter;
    private List<FamilyItem> directoryItemList= new ArrayList<>();
    String memberid;
    String URL;
    View rootview;
    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_family, container, false);


        pDialog = new ProgressDialog(getContext());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getActivity().getSharedPreferences("MyPref", 0); // 0 for private mode
        memberid = sp.getString("memberid", null);

        URL =Constants.FAMILY_URL+memberid;


        recyclerDirectory = (RecyclerView) rootview.findViewById(R.id.recyclerFamily);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(getContext(), 1);
        recyclerDirectory.setLayoutManager(mLayoutManager1);
        recyclerDirectory.setItemAnimator(new DefaultItemAnimator());
        directoryAdapter=new FamilyAdapter(getContext(),directoryItemList);
        recyclerDirectory.setAdapter(directoryAdapter);

        FloatingActionButton floatingActionButton=(FloatingActionButton)rootview.findViewById(R.id.addFab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent details =new Intent(getContext(),AddFamilyActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(details);
            }
        });

        prepareOffersData();
        return rootview;

    }



    private void prepareOffersData() {

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
                    directoryItemList.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("familydata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String familyid = jsonObject.getString("familyid");
                        String membername = jsonObject.getString("membername");
                        String relation = jsonObject.getString("relation");
                        String qualification = jsonObject.getString("qualification");
                        String bloodgroup = jsonObject.getString("bloodgroup");
                        String occassion = jsonObject.getString("occassion");
                        String occdate = jsonObject.getString("occdate");
                        String memberid = jsonObject.getString("memberid");


                        directoryItemList.add(new FamilyItem(familyid,membername,relation,qualification,bloodgroup,occassion,occdate,memberid));
                        directoryAdapter = new FamilyAdapter(getContext(), directoryItemList);
                        recyclerDirectory.setAdapter(directoryAdapter);
                        directoryAdapter.notifyDataSetChanged();

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
                // Toast.makeText(NewsActivity.this, "no data Found. check once data is enable or not..", Toast.LENGTH_LONG).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

      /*  //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(OffersActivity.this);

        //Adding request to the queue
        requestQueue.add(jsonObjReq);*/

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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        directoryItemList.clear();
    }
}

package com.pivotalsoft.kammasevasamithi;

import android.app.ProgressDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pivotalsoft.kammasevasamithi.Adapters.CustomPagerAdapter;
import com.pivotalsoft.kammasevasamithi.Adapters.GalleryAdapter;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.Items.SliderItem;
import com.pivotalsoft.kammasevasamithi.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GallerySliderActivity extends AppCompatActivity {
    private static final String TAG = GalleryActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private CustomPagerAdapter mCustomPagerAdapter;
    private ArrayList<SliderItem> sliderItems = new ArrayList<>();
    ViewPager mViewPager;
    String albumid;
    String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_slider);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Images");

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        albumid =getIntent().getStringExtra("albumid");

        URL=Constants.GALLERY_SLIDES_URL+albumid;
        Log.e("URL",""+URL);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mCustomPagerAdapter=new CustomPagerAdapter(GallerySliderActivity.this,sliderItems);
        mViewPager.setAdapter(mCustomPagerAdapter);

        prepareSlideData();
    }



    private void prepareSlideData() {

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
                    sliderItems.clear();

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("albumdata");


                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        String picid = jsonObject.getString("picid");
                        String galleryid = jsonObject.getString("galleryid");
                        String albumimage = Constants.IMAGE_ALBUMPICS_URL+jsonObject.getString("albumimage");




                        sliderItems.add(new SliderItem(picid,galleryid,albumimage));
                        mCustomPagerAdapter = new CustomPagerAdapter(GallerySliderActivity.this, sliderItems);
                        mViewPager.setAdapter(mCustomPagerAdapter);
                        mCustomPagerAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(GallerySliderActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

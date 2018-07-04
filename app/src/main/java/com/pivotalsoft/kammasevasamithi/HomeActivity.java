package com.pivotalsoft.kammasevasamithi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.utils.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.views.BannerSlider;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    // Progress dialog
    private ProgressDialog pDialog;
    Dialog dialog;
    private Session session;
    String password,memberid;
    SharedPreferences sp;
LinearLayout profile,directory,news,events,message,donation,gallery,greetings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        session = new Session(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        // To retrieve value from shared preference in another activity
         sp = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 for private mode
        String role = sp.getString("role", "");
        memberid = sp.getString("memberid", null);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BannerSlider bannerSlider = (BannerSlider) findViewById(R.id.banner_slider1);
        List<Banner> banners=new ArrayList<>();
        //add banner using image url
       // banners.add(new RemoteBanner("Put banner image url here ..."));
        //add banner using resource drawable
        banners.add(new DrawableBanner(R.drawable.offers_img));
        bannerSlider.setBanners(banners);

        profile=(LinearLayout)findViewById(R.id.profileLayout);
        directory=(LinearLayout)findViewById(R.id.directoryLayout);
        news=(LinearLayout)findViewById(R.id.newsLayout);
        events=(LinearLayout)findViewById(R.id.eventsLayout);
        message=(LinearLayout)findViewById(R.id.messagesLayout);
        donation=(LinearLayout)findViewById(R.id.donationLayout);
        gallery=(LinearLayout)findViewById(R.id.galleryLayout);
        greetings=(LinearLayout)findViewById(R.id.greetingsLayout);

        profile.setOnClickListener(this);
        directory.setOnClickListener(this);
        news.setOnClickListener(this);
        events.setOnClickListener(this);
        message.setOnClickListener(this);
        donation.setOnClickListener(this);
        gallery.setOnClickListener(this);
        greetings.setOnClickListener(this);


        Menu menuNav=navigationView.getMenu();
        MenuItem nav_item1 = menuNav.findItem(R.id.nav_registration);
        MenuItem nav_item2 = menuNav.findItem(R.id.nav_donation);

        if (role.equals("Admin")){

            nav_item1.setVisible(true);
            nav_item2.setVisible(true);
        }
        else {
            nav_item1.setVisible(false);
            nav_item2.setVisible(false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(HomeActivity.this);
            } else {
                builder = new AlertDialog.Builder(HomeActivity.this);
            }
            builder.setTitle("Confirm Exit ")
                    .setMessage("Do you want to exit app?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {

            Intent profile =new Intent(this,ProfileActivity.class);
            profile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(profile);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("CommitPrefEdits")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_password) {
            // Handle the camera action

          updateDilaogue();

        } else if (id == R.id.nav_share) {

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.pivotalsoft.kammasevasamithi&hl=en");

            startActivity(Intent.createChooser(share, "Share Kamma Seva Samithi app"));

        } else if (id == R.id.nav_send) {

            Intent contact =new Intent(this,ContactUsActivity.class);
            contact.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(contact);

        }
        else if (id == R.id.nav_registration) {

            Intent registration =new Intent(this,RegNotificationActivity.class);
            registration.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(registration);

        }
        else if (id == R.id.nav_donation) {

            Intent donation =new Intent(this,DonationNotificationActivity.class);
            donation.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(donation);

        }
        else if (id == R.id.nav_logout) {

                session.setLoggedin(false);
                Intent logout =new Intent(this,LoginActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logout);

                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.profileLayout:
                Intent profile =new Intent(this,AboutUsActivity.class);
                profile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(profile);

                break;

            case R.id.directoryLayout:
                Intent directory =new Intent(this,DirectoryActivity.class);
                directory.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(directory);

                break;


            case R.id.newsLayout:
                Intent news =new Intent(this,NewsActivity.class);
                news.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(news);

                break;

            case R.id.eventsLayout:
                Intent events =new Intent(this,EventsActivity.class);
                events.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(events);

                break;


            case R.id.messagesLayout:
                Intent messages =new Intent(this,MessagesActivity.class);
                messages.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(messages);

                break;

            case R.id.donationLayout:
                Intent donation =new Intent(this,DonationActivity.class);
                donation.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(donation);

                break;

            case R.id.galleryLayout:
                Intent gallery =new Intent(this,GalleryActivity.class);
                gallery.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(gallery);

                break;


            case R.id.greetingsLayout:
                Intent greetings =new Intent(this,GreetingsActivity.class);
                greetings.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(greetings);

                break;


        }

    }


    private void updateDilaogue(){

        // custom dialog
        dialog = new Dialog(HomeActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        dialog.setTitle("Change Password ?");
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.password_layout);



        final EditText etpassword = (EditText) dialog.findViewById(R.id.etPassword);
        /*etCompany.setText(Company);*/

        Button btn =(Button)dialog.findViewById(R.id.button_submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password=etpassword.getText().toString().trim();

                if (!password.isEmpty()){

                    updatePassword();

                }
                else {

                    Toast.makeText(HomeActivity.this, "Please enter your password!", Toast.LENGTH_LONG).show();
                }

            }
        });

        dialog.show();
    }


    private void updatePassword(){


        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RESPONSE : ",""+response);
                        hidepDialog();

                        dialog.dismiss();

                       /* Intent pivotal = new Intent(EditEventActivity.this, EventsActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);
                        Toast.makeText(EditEventActivity.this,"Event Updated Successfully",Toast.LENGTH_LONG).show();*/
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
                params.put("password",password);
                Log.e("RESPONSE_Parasms: ",""+params);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
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

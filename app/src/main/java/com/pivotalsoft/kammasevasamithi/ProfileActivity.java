package com.pivotalsoft.kammasevasamithi;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.pivotalsoft.kammasevasamithi.Adapters.GreetingsAdapter;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.Fragments.FamilyFragment;
import com.pivotalsoft.kammasevasamithi.Fragments.PersonalFragment;
import com.pivotalsoft.kammasevasamithi.Fragments.ProfissionFragment;
import com.pivotalsoft.kammasevasamithi.Items.GreetingsItem;
import com.pivotalsoft.kammasevasamithi.app.AppController;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    CircleImageView  coverIamge;

    //Image request code
    private int PICK_IMAGE_REQUEST1 = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath1;

    private ProgressDialog pDialog;
    String memberid;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Profile");

        requestStoragePermission();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        int defaultValue = 0;
        int page = getIntent().getIntExtra("One", defaultValue);


        // To retrieve value from shared preference in another activity
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 for private mode
        String fullname = sp.getString("fullname", "");
        String membershipNo = sp.getString("membershipNo", "");
        String role = sp.getString("role", "");
        memberid = sp.getString("memberid", "");


       /* TextView txtFullName =(TextView)findViewById(R.id.txtFullname);
        txtFullName.setText(fullname);*/

        TextView txtMembershipno =(TextView)findViewById(R.id.txtMembershipNo);
        txtMembershipno.setText("Membership No : "+membershipNo);

        TextView txtRole =(TextView)findViewById(R.id.txtMembershipRole);
        txtRole.setText(role);



        coverIamge=(CircleImageView) findViewById(R.id.profileImage);
        coverIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser1(PICK_IMAGE_REQUEST1);
            }
        });





        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(page);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        prepareProfileData();
       // uploadMultipart();
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PersonalFragment(), "PERSONAL INFO");
        adapter.addFragment(new ProfissionFragment(), "PROFESSIONAL INFO");
        adapter.addFragment(new FamilyFragment(), "FAMILY MEMBERS");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    private void prepareProfileData() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.PROFILE_INFO_URL+memberid, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

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
                        Log.e("PIC",""+profilepic);
                        String role = jsonObject.getString("role");
                        String status = jsonObject.getString("status");
                        String membershipno = jsonObject.getString("membershipno");
                        String password = jsonObject.getString("password");
                        String regdate = jsonObject.getString("regdate");
                        String fcmkey = jsonObject.getString("fcmkey");

                        JSONObject messageObject =new JSONObject(response.toString());
                        String message1 =messageObject.getString("message");
                        Log.e("msg",""+message1);

                        try {

                            if (profilepic.equals("http://vizagnext.com/pivotalkss/uploads/profilepics/")){

                                Glide.with(ProfileActivity.this).load(R.drawable.icprofile_orange).into(coverIamge);
                            }else {
                                Glide.with(ProfileActivity.this).load(profilepic).into(coverIamge);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // Toast.makeText(OffersActivity.this, "no data Found. check once data is enable or not..", Toast.LENGTH_LONG).show();
                // hide the progress dialog
                //hidepDialog();
            }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    /*
   * This is the method responsible for image upload
   * We need the full image path and the name for the image in this method
   * */
    public void uploadMultipart() {

          pDialog.setMessage("Uploading...");
        showpDialog();
        //getting the actual path of the image
        String path1 = getPath(filePath1);


        Log.e("Path",""+path1);


        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Constants.UPDATE_PROFILE_PIC_URL)
                    .addFileToUpload(path1, "imagePath") //Adding file
                    .addParameter("memberid", memberid) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(ProfileActivity.this,"Profile updated Succussfully",Toast.LENGTH_SHORT).show();
                    Intent gallery =new Intent(ProfileActivity.this,ProfileActivity.class);
                    gallery.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(gallery);
                    hidepDialog();
                }
            }, 5000);



        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    //method to show file chooser
    private void showFileChooser1(int req_code){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select file to upload "), req_code);
    }


    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath1 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                int size = imageInByte.length/1024;
                Log.e("LENTGH",""+size);

                if (size<200){

                    coverIamge.setImageBitmap(bitmap);
                    uploadMultipart();
                }
                else {

                    Toast.makeText(ProfileActivity.this, "please select Image size must be 200 kb  or below.", Toast.LENGTH_LONG).show();
                }




            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    //method to get the file path from uri
    public String getPath(Uri uri) {

        Cursor cursor = ProfileActivity.this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = ProfileActivity.this.getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;

       /* String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);*/
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission


        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(ProfileActivity.this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(ProfileActivity.this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
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

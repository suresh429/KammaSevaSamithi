package com.pivotalsoft.kammasevasamithi;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.app.AppController;
import com.pivotalsoft.kammasevasamithi.utils.ConnectivityReceiver;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    int color;
    NestedScrollView  nestedScrollView;
    private static final String TAG = "RegisterActivity";
    final int RADIOBUTTON_ALERTDIALOG = 1;
    final CharSequence[] bloodgroup_radio = {"A+","A-","B+","B-","O+","o-","AB+","AB-"};
    ImageView imageview;
    EditText etFullName,etFatherName,etAadharno,etDob,etPassword,etAddress,etMobile,etGothram,etEmail,etNAtiveAddress,etBloodGroup,etReffredby;
    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;

    private ProgressDialog pDialog;
    private DatePickerDialog tentDatePickerDialog,startDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    String fullName,fatherName,aadharno,dob,password,address,mobile,gothram,email,nativePlace,bloodGroup,referId ,fcmtkn,adminFcmKey;
    Button buttonSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Register");

        // Manually checking internet connection
        checkConnection();

        fcmtkn = FirebaseInstanceId.getInstance().getToken();
        // Toast.makeText(getApplicationContext(), "Current token ["+fcmtkn+"]", Toast.LENGTH_LONG).show();
        Log.e("FcmKEY", "Token ["+fcmtkn+"]");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        requestStoragePermission();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        imageview=(ImageView)findViewById(R.id.uploadImageView);
        imageview.setOnClickListener(this);

        etFullName=(EditText)findViewById(R.id.etFullName);
        etFatherName=(EditText)findViewById(R.id.etFatherName);
        etAadharno=(EditText)findViewById(R.id.etAdharno);

        etDob=(EditText)findViewById(R.id.etDob);
        etDob.setInputType(InputType.TYPE_NULL);
        etDob.requestFocus();
        etDob.setOnClickListener(this);

       // etProfession=(EditText)findViewById(R.id.etProfission);
        etAddress=(EditText)findViewById(R.id.etAddress);
        etMobile=(EditText)findViewById(R.id.etMobile);
        etGothram=(EditText)findViewById(R.id.etGothram);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etNAtiveAddress=(EditText)findViewById(R.id.etNativePlace);
        etBloodGroup=(EditText)findViewById(R.id.etBloodGroup);
        etBloodGroup.setInputType(InputType.TYPE_NULL);
        etBloodGroup.requestFocus();
        etBloodGroup.setOnClickListener(this);

        etReffredby=(EditText)findViewById(R.id.etRefferedBy);
        etPassword=(EditText)findViewById(R.id.etPassword);

        buttonSubmit=(Button)findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(this);

        setDateTimeField();
        adminData();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.uploadImageView:

                showFileChooser();

                break;

            case R.id.button_submit:

                boolean isConnected = ConnectivityReceiver.isConnected();

                if (isConnected){

                    validateForm();
                    Log.e("CONNECTIONTRUE",""+isConnected);
                }
                else {
                    checkConnection();
                    Log.e("CONNECTIONFALSE",""+isConnected);
                }

                break;

            case R.id.etDob:

                tentDatePickerDialog.show();


                break;

            case R.id.etBloodGroup:

                showDialog(RADIOBUTTON_ALERTDIALOG);
                break;

        }
    }

    /*triggered by showDialog method. onCreateDialog creates a dialog*/
    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {



            case RADIOBUTTON_ALERTDIALOG:

                AlertDialog.Builder builder21 = new AlertDialog.Builder(RegisterActivity.this)
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
        }
        return null;

    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
// TODO Auto-generated method stub

        switch (id) {

            case RADIOBUTTON_ALERTDIALOG:
                AlertDialog prepare_radio_dialog1 = (AlertDialog) dialog;
                ListView list_radio1 = prepare_radio_dialog1.getListView();
                for (int i = 0; i < list_radio1.getCount(); i++) {
                    list_radio1.setItemChecked(i, false);
                }
                break;

        }
    }

    private void setDateTimeField(){

        final Calendar newCalendar = Calendar.getInstance();
        tentDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar dob = Calendar.getInstance();
                Calendar today = Calendar.getInstance();
                dob.set(year, monthOfYear, dayOfMonth);

                 // age calculation
                int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                    age--;
                }

                Integer ageInt = Integer.valueOf(age);
                String ageS = ageInt.toString();
                Log.e("AGE",""+ageS);

                if (ageInt<18){
                    // etDob.setFocusable(true);
                    // etDob.setError("Age Should be 18 Years or Above!");
                    Toast.makeText(getApplicationContext(),"Age Should be 18 Years or Above!",Toast.LENGTH_LONG).show();

                }else {
                    etDob.setText(dateFormatter.format(dob.getTime()));
                    Log.e("DATE::", "" + dateFormatter.format(dob.getTime()));
                }

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    private void validateForm(){

        //getting name for the image
        fullName = etFullName.getText().toString().trim();
        fatherName = etFatherName.getText().toString().trim();
        aadharno = etAadharno.getText().toString().trim();
        dob = etDob.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        address = etAddress.getText().toString().trim();
        mobile = etMobile.getText().toString().trim();
        gothram = etGothram.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        nativePlace = etNAtiveAddress.getText().toString().trim();
        bloodGroup = etBloodGroup.getText().toString().trim();
        referId = etReffredby.getText().toString().trim();

      //  password =etFullName.getText().toString().trim().toLowerCase().substring(0,4)+"@123";

        if (referId.equals("")){

            referId ="NA";
        }


        Log.e("referid",""+referId);

        if (!fullName.isEmpty()&& !fatherName.isEmpty() && !aadharno.isEmpty() && !dob.isEmpty()  && !password.isEmpty() && !address.isEmpty() && !mobile.isEmpty() &&
                !gothram.isEmpty() && !bloodGroup.isEmpty() ) {

            uploadMultipart();

        }else {

            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
        }


    }

    /*
    * This is the method responsible for image upload
    * We need the full image path and the name for the image in this method
    * */
    public void uploadMultipart() {

        pDialog.setMessage("Loading ...");
        showDialog();

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        if (filePath != null) {
            //getting the actual path of the image
            String path = getPath(filePath);


            Log.e("Path", "" + path + "\n" + path);
        // formattedDate have current date/time
        //getting the actual path of the image


      //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, Constants.RIGISTER_URL)
                        .addFileToUpload(path, "imagePath") //Adding file
                        .addParameter("fullname", fullName) //Adding text parameter to the request
                        .addParameter("fathername", fatherName)
                        .addParameter("aadharno", aadharno)
                        .addParameter("dob", dob)
                        .addParameter("presentaddress", address)
                        .addParameter("mobile", mobile)
                        .addParameter("gothram", gothram)
                        .addParameter("email", email)
                        .addParameter("nativeaddress", nativePlace)
                        .addParameter("bloodgroup", bloodGroup)
                        .addParameter("referalno", referId)
                        .addParameter("regdate", formattedDate)
                        .addParameter("fcmkey", fcmtkn)
                        .addParameter("password", password)

                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent pivotal = new Intent(RegisterActivity.this, LoginActivity.class);
                        pivotal.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(pivotal);
                       // Toast.makeText(RegisterActivity.this,"Document Updated Successfully",Toast.LENGTH_LONG).show();

                        hideDialog();
                    }
                }, 5000);

                pushnotification();


            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();

                hideDialog();
            }

        }else {
            Toast.makeText(RegisterActivity.this, "Please Select image", Toast.LENGTH_SHORT).show();

        }


    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                int size = imageInByte.length/1024;
                Log.e("LENTGH",""+size);

                if (size<200){

                    imageview.setImageBitmap(bitmap);

                }
                else {

                    Toast.makeText(RegisterActivity.this, "please select Image size must be 200 kb  or below.", Toast.LENGTH_LONG).show();
                }





            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void adminData() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.ADMIN_FCM_KEY_URL, null, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Log.e("URL",""+Constants.ADMIN_FCM_KEY_URL);

                try {
                    // Parsing json object response
                    // response will be a json object

                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray1 = response.optJSONArray("fcmdata");

                    //Iterate the jsonArray and print the info of JSONObjects
                     for (int i = 0; i < jsonArray1.length(); i++) {

                         JSONObject jsonObject = jsonArray1.getJSONObject(0);

                         String memberid = jsonObject.getString("memberid");
                         String fullname = jsonObject.getString("fullname");
                         String mobile = jsonObject.getString("mobile");
                         adminFcmKey = jsonObject.getString("fcmkey");

                         //String profilepicurl = Apis.IMAGE_PROFILE_URL + jsonObject.optString("profilepicurl").toString();

                     }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
               // Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }
        });

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

        //Adding request to the queue
        requestQueue.add(jsonObjReq);

    }

    //push notification
    private void pushnotification(){


        try{

           /* String fcmArray= Arrays.toString(new ArrayList[]{arrPackage}).replaceAll("\\[|\\]", "");
            Log.e("ARRAY",""+fcmArray);*/

            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

            String url = "https://fcm.googleapis.com/fcm/send";

            String body ="Hi! Admin "+fullName+" is Registered now! Please click here for approval";
            Log.e("body",""+body);

            JSONObject data = new JSONObject();
            data.put("title", "Applied !");
            data.put("body", body);
            JSONObject notification_data = new JSONObject();
            notification_data.put("notification", data);
            notification_data.put("to",adminFcmKey);

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



    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
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
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }
}

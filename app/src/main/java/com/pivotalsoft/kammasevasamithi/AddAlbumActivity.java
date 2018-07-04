package com.pivotalsoft.kammasevasamithi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pivotalsoft.kammasevasamithi.Api.Constants;
import com.pivotalsoft.kammasevasamithi.app.AppController;
import com.pivotalsoft.kammasevasamithi.utils.ConnectivityReceiver;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

public class AddAlbumActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener{
    int color;
    NestedScrollView nestedScrollView;

    ImageView coverIamge;

    EditText etTitle;
    String title;
    //Image request code
    private int PICK_IMAGE_REQUEST1 = 1;



    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath1;

    private ProgressDialog pDialog;

    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add Album");

        // Manually checking internet connection
        checkConnection();

        requestStoragePermission();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        etTitle=(EditText)findViewById(R.id.etTitle);

        coverIamge=(ImageView)findViewById(R.id.coverImage);
        coverIamge.setOnClickListener(this);


        submit=(Button)findViewById(R.id.button_submit);
        submit.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.coverImage:

                showFileChooser1(PICK_IMAGE_REQUEST1);

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



        }
    }

    private void validateForm(){

        //getting name for the image
        title = etTitle.getText().toString().trim();


        if (!title.isEmpty()) {

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

        pDialog.setMessage("Uploading...");
        showpDialog();

        if (filePath1 != null) {
            //getting the actual path of the image
            String path1 = getPath(filePath1);

            Log.e("Path", "" + path1);


            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();


                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, Constants.ALBUM_URL)
                        .addFileToUpload(path1, "imagePath") //Adding file
                        .addParameter("title", title) //Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(AddAlbumActivity.this, "Album Added Succussfully", Toast.LENGTH_SHORT).show();
                        Intent gallery = new Intent(AddAlbumActivity.this, GalleryActivity.class);
                        gallery.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(gallery);
                        hidepDialog();
                    }
                }, 5000);



            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(AddAlbumActivity.this, "Please Select image", Toast.LENGTH_SHORT).show();

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


        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {

            if (resultCode == RESULT_OK) {

                if (requestCode == PICK_IMAGE_REQUEST1 && data != null && data.getData() != null) {
                    filePath1 = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                        coverIamge.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



            }

        }else {

            if (resultCode == RESULT_OK) {


                if (requestCode == PICK_IMAGE_REQUEST1 && data != null && data.getData() != null) {
                    filePath1 = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                        coverIamge.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }




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

        cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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

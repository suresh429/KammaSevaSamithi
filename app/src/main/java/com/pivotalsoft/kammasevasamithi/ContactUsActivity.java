package com.pivotalsoft.kammasevasamithi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ContactUsActivity extends AppCompatActivity {
    private static int REQUEST_CALL_PHONE =1;
Button callButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Contact Us");

        callButton=(Button)findViewById(R.id.callbutton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkPermission = ContextCompat.checkSelfPermission(ContactUsActivity.this, Manifest.permission.CALL_PHONE);
                if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ContactUsActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_CALL_PHONE);
                } else {

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:+919491349494"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                }
            }
        });
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

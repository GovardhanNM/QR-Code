package com.example.csb;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton scanqr,about;
    private static final int requestCode = 100;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );

        scanqr = (ImageButton) findViewById( R.id.scan_qr );
        scanqr.setOnClickListener( this );

        about = (ImageButton) findViewById( R.id.about );
        about.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scan_qr:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[] {Manifest.permission.CAMERA}, requestCode);

                else
                    startActivity( new Intent( HomeActivity.this,ScanActivity.class ) );
                break;

            case R.id.about:
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference();

                myRef.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("about").exists()) {
                            startActivity( new Intent( HomeActivity.this,InformationActivity.class ) );

                        }
                        else{
                            Toast.makeText( HomeActivity.this,"No value to show yet", Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //
                    }


                });
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == requestCode) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startActivity( new Intent( HomeActivity.this,ScanActivity.class ) );

            } else {

                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();

            }

        }}

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

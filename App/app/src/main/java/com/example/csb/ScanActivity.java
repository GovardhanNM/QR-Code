package com.example.csb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ScanActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView Link;
    private TextView Name;
    private ImageButton back;
    byte[] byteArray;
    /* access modifiers changed from: private */
    public TextView details;
    /* access modifiers changed from: private */
    public ImageView imageView;
    private String link,uid;
    private String name;
    DatabaseReference rootRef,childRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_scan );
        IntentIntegrator qrScan = new IntentIntegrator( this );
        qrScan.setPrompt("Place the QR Code inside the rectangle");
        qrScan.setCameraId(0);  // Use a specific camera of the device
        qrScan.setOrientationLocked(true);
        qrScan.setBeepEnabled(true);
        qrScan.setCaptureActivity(CaptureActivityPortrait.class);

        qrScan.initiateScan();

        Name = (TextView) findViewById( R.id.name );
        details = (TextView) findViewById( R.id.details );

        Link = (TextView) findViewById( R.id.link );
        Link.setOnClickListener( this );
        imageView = (ImageView) findViewById( R.id.image_show );
        imageView.setOnClickListener(this);
        back = (ImageButton) findViewById( R.id.back );
        back.setOnClickListener( this );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result == null) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (result.getContents() == null) {
            startActivity(new Intent(this, HomeActivity.class));
            Toast.makeText(this, "Result Not Found", Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject obj = new JSONObject(result.getContents());
                this.name = obj.getString("name");
                this.uid = obj.getString("uid");
                this.Name.setText(this.name);
                Name.setText(name);
                downloadDetails();
            } catch (JSONException e) {
                e.printStackTrace();
                startActivity(new Intent(this, HomeActivity.class));
                Toast.makeText(this, "Please Sacn a Valid QR Code", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void downloadDetails() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        childRef = rootRef.child("users/"+uid+"/"+name);
        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                   String data = dataSnapshot.child("data").getValue(String.class);
                    details.setText(data);
                    link = dataSnapshot.child("link").getValue(String.class);
                    String imagePath = dataSnapshot.child("name").getValue(String.class);
                    downloadImage(imagePath);
            }

            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    private void downloadImage(String imagePath) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        storageReference.child(uid+"/"+imagePath).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                byteArray = bytes;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back:
                startActivity( new Intent( ScanActivity.this,HomeActivity.class ) );
                break;

            case R.id.link:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);
                break;

            case R.id.image_show:
                Intent image_intent = new Intent(ScanActivity.this,FullScreenImageActivity.class);
                image_intent.putExtra("image",byteArray);
                image_intent.putExtra("name",name);
                startActivity(image_intent);
                break;
        }

    }
}

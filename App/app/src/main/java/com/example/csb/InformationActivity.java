package com.example.csb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
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

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back;
    private ImageView imageView;
    private TextView details;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_information );

        back = (ImageButton) findViewById( R.id.back );
        back.setOnClickListener( this );

        imageView = (ImageView) findViewById( R.id.image_show );

        details = (TextView) findViewById( R.id.details );

        downloadDetails();
        downloadImage();

    }

    private void downloadDetails() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("about");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                details.setText( text );
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(InformationActivity.this,"No information to show yet",Toast.LENGTH_LONG).show();
                startActivity( new Intent(InformationActivity.this,HomeActivity.class) );
            }
        });
    }

    private void downloadImage() {

        storage = FirebaseStorage.getInstance();

        storageReference = storage.getReference().child( "images/about.jpg" );
        final long TWO_MEGABYTE = 2*1024 * 1024;
        storageReference.getBytes(TWO_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );
                imageView.setImageBitmap( bitmap );
            }
        }).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("W/","Image Not fetched");
            }
        } );

    }

    @Override
    public void onClick(View v) {
        startActivity( new Intent(InformationActivity.this,HomeActivity.class) );
    }
}

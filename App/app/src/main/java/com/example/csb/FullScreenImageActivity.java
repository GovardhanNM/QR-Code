package com.example.csb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FullScreenImageActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private ImageView imageView;
    private ImageButton back;
    private TextView textView;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        imageView = (ImageView) findViewById(R.id.imageView);
        back = (ImageButton) findViewById(R.id.back);
        textView = (TextView) findViewById(R.id.textView);
        back.setOnClickListener(this);
        Bitmap bmp;

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        name = getIntent().getStringExtra("name");

        imageView.setImageBitmap(bmp);
        textView.setText(name);
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}

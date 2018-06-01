package com.faruksahin.blogapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class detailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = this.getIntent();
        ImageView photo = findViewById(R.id.detailPhoto);
        Picasso.with(getApplicationContext()).load(intent.getStringExtra("photoUrl")).into(photo);
    }
}

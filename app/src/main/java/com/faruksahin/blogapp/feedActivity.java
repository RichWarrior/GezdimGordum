package com.faruksahin.blogapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class feedActivity extends AppCompatActivity {

    private ListView blogList;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> desc = new ArrayList<>();
    private ArrayList<String> photo = new ArrayList<>();
    private customAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        blogList = findViewById(R.id.blogList);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Posts");
        adapter = new customAdapter(feedActivity.this,title,desc,photo);
        blogList.setAdapter(adapter);
        blogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(feedActivity.this,detailActivity.class);
                intent.putExtra("photoUrl",photo.get(position));
                startActivity(intent);
            }
        });
        refreshData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.feed_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add:
                startActivity(new Intent(feedActivity.this,postActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void refreshData()
    {
        try
        {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        title.add(ds.child("Title").getValue().toString());
                        desc.add(ds.child("Description").getValue().toString());
                        photo.add(ds.child("Photo").getValue().toString());
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception ex)
        {
            Toast.makeText(this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

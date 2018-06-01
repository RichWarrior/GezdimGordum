package com.faruksahin.blogapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class customAdapter extends ArrayAdapter<String>
{
    private Activity context;
    private ArrayList<String> titleFromDatabase,descFromDatabase,photoFromDatabase;
    public customAdapter(Activity context,ArrayList<String> title,ArrayList<String> desc,ArrayList<String>photo)
    {
        super(context,R.layout.listrows,title);
        this.context = context;
        this.titleFromDatabase = title;
        this.descFromDatabase = desc;
        this.photoFromDatabase = photo;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.listrows, null, false);
        ImageView imageView = view.findViewById(R.id.rowsPhoto);
        TextView txtTitle = view.findViewById(R.id.rowsTitle);
        TextView txtDesc = view.findViewById(R.id.rowsDesc);
        Picasso.with(context).load(photoFromDatabase.get(position)).into(imageView);
        txtTitle.setText(titleFromDatabase.get(position));
        txtDesc.setText(descFromDatabase.get(position));
        return view;
    }
}

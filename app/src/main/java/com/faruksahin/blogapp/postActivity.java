package com.faruksahin.blogapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class postActivity extends AppCompatActivity {

    private ImageView photo;
    private Uri photoUri = null;
    private EditText txtTitle,txtDesc;
    private Button shareButton;
    private ProgressDialog progress;
    private StorageReference mStorageRef;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private UUID uuıd;
    private String key = "";
    private Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        photo = findViewById(R.id.postPhoto);
        txtTitle = findViewById(R.id.txtTitle);
        txtDesc = findViewById(R.id.txtDesc);
        shareButton = findViewById(R.id.shareButton);
        progress = new ProgressDialog(postActivity.this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Posts");

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(ActivityCompat.checkSelfPermission(postActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),1);
                }else
                {
                    ActivityCompat.requestPermissions(postActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(txtTitle.getText().toString().equals("")||txtDesc.getText().toString().equals("")||photoUri.equals(""))
                {
                    snackMessage("Lütfen İlgili Yerleri Doldurunuz !");
                }else
                {
                    try
                    {
                        progress.setMessage("Sunucuya Yükleniyor...");
                        progress.show();
                        uuıd = UUID.randomUUID();
                        key = uuıd.toString();
                        StorageReference ref = mStorageRef.child("Images/"+key+".jpg");
                        ref.putFile(photoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                            {
                                photoUri = taskSnapshot.getDownloadUrl();
                                myRef.child(key).child("Title").setValue(txtTitle.getText().toString());
                                myRef.child(key).child("Description").setValue(txtDesc.getText().toString());
                                myRef.child(key).child("Photo").setValue(photoUri.toString());
                                myRef.push();
                                new CountDownTimer(1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish()
                                    {
                                        startActivity(new Intent(postActivity.this,feedActivity.class));
                                        postActivity.this.finish();
                                    }
                                }.start();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                progress.dismiss();
                                snackMessage(e.getLocalizedMessage());
                            }
                        });
                    }catch (Exception e)
                    {
                        progress.dismiss();
                        snackMessage(e.getLocalizedMessage());
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            if(data!=null)
            {
                photoUri = data.getData();
                photo.setImageURI(photoUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 2)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),1);
            }
        }
    }

    private void snackMessage(String message)
    {
        ConstraintLayout layout = findViewById(R.id.postLayout);
        snackbar.make(layout,message,Snackbar.LENGTH_SHORT).show();
    }
}

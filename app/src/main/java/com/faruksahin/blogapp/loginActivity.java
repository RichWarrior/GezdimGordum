package com.faruksahin.blogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {


    private EditText txtMail,txtPassword;
    private Button signIn,signUp;
    private ProgressDialog progress;
    private Snackbar snackar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtMail = findViewById(R.id.txtMail);
        txtPassword = findViewById(R.id.txtPassword);
        txtMail.setText("faruk_thecno@hotmail.com");
        txtPassword.setText("03102593");
        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);
        progress = new ProgressDialog(loginActivity.this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
            }
        };
        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(txtMail.getText().toString().equals("")||txtPassword.getText().toString().equals(""))
                {
                    snackMessage("Kullanıcı Adı veya Şifre Eksik !");
                }else
                {
                    try
                    {
                        progress.setMessage("Kayıt Olunuyor...");
                        progress.show();
                        mAuth.createUserWithEmailAndPassword(txtMail.getText().toString(),txtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    progress.dismiss();
                                    snackMessage("Başarıyla Kayıt Olundu !");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                progress.dismiss();
                                snackMessage(e.getLocalizedMessage());
                            }
                        });
                    }catch (Exception ex)
                    {
                        progress.dismiss();
                        snackMessage(ex.getLocalizedMessage());
                    }
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(txtMail.getText().toString().equals("")||txtPassword.getText().toString().equals(""))
                {
                    snackMessage("Kullanıcı Adı veya Şifre Eksik !");
                }else
                {
                    try
                    {
                        progress.setMessage("Giriş Yapılıyor...");
                        progress.show();
                        mAuth.signInWithEmailAndPassword(txtMail.getText().toString(),txtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    startActivity(new Intent(loginActivity.this,feedActivity.class));
                                    loginActivity.this.finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                progress.dismiss();
                                snackMessage(e.getLocalizedMessage());
                            }
                        });
                    }catch (Exception ex)
                    {
                        progress.dismiss();
                        snackMessage(ex.getLocalizedMessage());
                    }
                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(mAuthListener!=null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void snackMessage(String message)
    {
        ConstraintLayout layout = findViewById(R.id.loginLayout);
        snackar.make(layout,message,Snackbar.LENGTH_SHORT).show();
    }
}

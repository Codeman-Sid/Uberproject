package com.example.uberproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLoginActivity extends AppCompatActivity {
    private EditText memail,mpassword;
    private Button mLogin,mRegistration;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseauthlistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        mAuth= FirebaseAuth.getInstance();
        firebaseauthlistener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null)
                {
                    Intent intent =  new Intent(DriverLoginActivity.this,MapActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mLogin=findViewById(R.id.login);
        mpassword=findViewById(R.id.password);
        mRegistration=findViewById(R.id.registraion);
        memail=findViewById(R.id.email);
        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = memail.getText().toString();
                final String password = mpassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Toast.makeText(DriverLoginActivity.this, "sign up error", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            String user_id= mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user_id);
                            current_user_db.setValue(true);
                        }
                    }
                });
            }
        });
    mLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String email = memail.getText().toString();
            final String password = mpassword.getText().toString();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful())
                    {
                        Toast.makeText(DriverLoginActivity.this, "sign up error", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseauthlistener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseauthlistener);
    }
}
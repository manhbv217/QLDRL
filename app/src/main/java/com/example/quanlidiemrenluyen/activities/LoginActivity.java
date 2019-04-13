package com.example.quanlidiemrenluyen.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlidiemrenluyen.Models.User;
import com.example.quanlidiemrenluyen.R;
import com.example.quanlidiemrenluyen.helper.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    SessionManager sessionManager;
    Button btnLogin;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtPassword = findViewById(R.id.edtPassword);
        edtUsername = findViewById(R.id.edtUserName);
        btnLogin = findViewById(R.id.btnLogin);
       // sessionManager = new SessionManager(getApplication());

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                if(TextUtils.isEmpty(password) || TextUtils.isEmpty(username)){
                    Toast.makeText(LoginActivity.this,"do not empty",Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final FirebaseUser firebaseUser = auth.getCurrentUser();
                                final String userid = firebaseUser.getUid();

                                reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.child(userid).exists()){
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap.put("id", userid);
                                            hashMap.put("username", edtUsername.getText().toString());
                                            hashMap.put("imageURL", "default");

                                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.e("no c","vd");
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            });
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

//                                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
//                                    HashMap<String, String> hashMap = new HashMap<>();
//                                    hashMap.put("id", userid);
//                                    hashMap.put("username", edtUsername.getText().toString());
//                                    hashMap.put("imageURL", "default");
//                                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                startActivity(intent);
//                                                finish();
//                                            }
//                                        }
//                                    });

                            }else {
                                Log.e("LOGIN Error","dont understand");
                                Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });



    }


}

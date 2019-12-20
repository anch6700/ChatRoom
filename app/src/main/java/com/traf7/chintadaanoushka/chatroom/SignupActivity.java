package com.traf7.chintadaanoushka.chatroom;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    EditText edtFirstName, edtLastName, edtUsername, edtEmail, edtpassword;
    Button btnSubmit;
    RadioGroup rdbGroup;
    ProgressDialog progressDialog;
//    android.support.v7.app.ActionBar actionBar;

    String firstName, lastName, username, email, gender, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnSubmit = findViewById(R.id.button);
        edtEmail = findViewById(R.id.email);
        edtpassword = findViewById(R.id.password);
        edtUsername = findViewById(R.id.username);
        edtFirstName = findViewById(R.id.firstname);
        edtLastName = findViewById(R.id.lastname);

    }

        private void signupUser() {
            progressDialog.setMessage("Registering. Please Wait...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //set username as user's display name
                                user = firebaseAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();
                                user.updateProfile(profileUpdate);
                                addUserToDatabase(); //store user's data in cloud firestore
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Registration successful.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Registration error. Check your details", Toast.LENGTH_SHORT);
                            }
                        }
                    });
        }

        private void addUserToDatabase(){
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            String key = user.getUid();

            HashMap<String, String> userData = new HashMap<>();
            userData.put("a1_firstName", firstName);
            userData.put("a2_lastname", lastName);
            userData.put("a3_username", username);
            userData.put("a4_email", email);
            userData.put("a5_gender", gender);
            userData.put("a6_imageUrl", "none");

            Map<String, Object> update = new HashMap<>();
            update.put(key, userData);

            database.collection("users").add(update);
        }
    }

//    });
//
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
//
//}

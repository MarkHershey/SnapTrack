package com.example.snaptrack.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snaptrack.R;
import com.example.snaptrack.data.UserInfo;
import com.example.snaptrack.data.DataUtils;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

// code here largely stolen from https://github.com/firebase/snippets-android/blob/bcab807dd4924a253117fcc10a3f5dcd9198381e/auth/app/src/main/java/com/google/firebase/quickstart/auth/FirebaseUIActivity.java

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private TextView loginConfirmedTextView;
    private Button signOutButton;

    public void createSignInIntent(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            //IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                mAuth = FirebaseAuth.getInstance();

                FirebaseUser user = mAuth.getCurrentUser();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                if (user != null) {
                    Log.e(TAG, "User Exists, uid = "+user.getUid());
                    DatabaseReference dbRef = db.getReference().child("users").child(user.getUid());
                    ValueEventListener vel = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserInfo info = snapshot.getValue(UserInfo.class);
                            if(info == null){
                                // TODO this is a shitty initialisation, put your sign up interfaces here
                                Toast.makeText(LoginActivity.this, "Setting up data...",Toast.LENGTH_LONG).show();
                                DataUtils.createExampleUser();
                            } else {
                                if(info.getDisplay_name() != null)
                                    loginConfirmedTextView.setText(String.format("Hi %s!",info.getDisplay_name()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    dbRef.addValueEventListener(vel);
                    signOutButton.setOnClickListener(v -> {
                        dbRef.removeEventListener(vel);
                        signOut();
                    });
                    signOutButton.setEnabled(true);
                } else {
                    Toast.makeText(LoginActivity.this, "Somehow sign in worked but user doesn't exist?", Toast.LENGTH_LONG).show();
                }
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                signOutButton.setEnabled(false);
                Toast.makeText(LoginActivity.this, "Sign in failed", Toast.LENGTH_LONG).show();
                createSignInIntent();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createSignInIntent();

        signOutButton = findViewById(R.id.signOutButton);
        loginConfirmedTextView = findViewById(R.id.loginConfirmedTextView);
    }
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        loginConfirmedTextView.setText("Signed Out");

                    }
                });
    }
}
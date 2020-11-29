package com.example.snaptrackapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.snaptrackapp.data.UserInfo;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Log in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) createSignInIntent();


        // Bottom Nav Bar
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_1, // Today
                R.id.navigation_2, // Activities
                R.id.navigation_3, // Analytics
                R.id.navigation_4  // Me
        ).build();
        // Set up Navigation Controller
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }
//    Fragment fragment;
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        // Check if the fragment is an instance of the right fragment
//        if (fragment instanceof com.example.snaptrackapp.ui.create_activity.create_activity) {
//            com.example.snaptrackapp.ui.create_activity.create_activity my = (com.example.snaptrackapp.ui.create_activity.create_activity) fragment;
//            // Pass intent or its data to the fragment's method
//            my.processNFC(intent.getStringExtra());
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) { // logged in successfully

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase db = FirebaseDatabase.getInstance();

                if (user != null) {
                    String authID = user.getUid();
                    String userName = user.getDisplayName();
                    // generate UID for new user
                    UserInfo.createUIDIfNotExist(userName, authID);
                    Toast.makeText(MainActivity.this, "Howdy " + userName + "!", Toast.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, "Unexpected: Somehow sign in worked but user doesn't exist.");
                    Toast.makeText(MainActivity.this, "Somehow sign in worked but user doesn't exist?", Toast.LENGTH_LONG).show();
                }
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.

                Toast.makeText(MainActivity.this, "Sign in failed", Toast.LENGTH_LONG).show();
                createSignInIntent();
            }
        }
    }




}
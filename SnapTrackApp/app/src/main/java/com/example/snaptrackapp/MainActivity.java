package com.example.snaptrackapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snaptrackapp.data.DataUtils;
import com.example.snaptrackapp.data.Listener;
import com.example.snaptrackapp.data.UserActivityInfo;
import com.example.snaptrackapp.data.UserInfo;
import com.example.snaptrackapp.record.NdefMessageParser;
import com.example.snaptrackapp.record.ParsedNdefRecord;
import com.example.snaptrackapp.ui.today.TrackingActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 123;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Log in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) createSignInIntent();

        //Start NFC capturing event
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null){
            Toast.makeText(this,"NO NFC Capabilities",Toast.LENGTH_SHORT).show();
            finish();
        }

        //Create a PendingIntent object so the Android system can populate it with the details of the tag when it is scanned.
        pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);

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

    @Override
    protected void onResume() {
        super.onResume();
        //Enables foreground dispatch which handles NFC intents (waiting for NFC card to be tapped)
        //The foreground dispatch system allows an activity to intercept an intent and
        // claim priority over other activities that handle the same intent.
        assert nfcAdapter != null;
        nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);
    }

    //New NFC Intent (NFC Card detected)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.v(TAG,getClass().getName());
        try {
            resolveIntent(intent);
        } catch (Exception e) {
            Log.v(TAG,"unable to resolveIntent");
            e.printStackTrace();
        }
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                assert tag != null;
                startTimer(tag);
            }

        }

    private void startTimer(Tag tag) {
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareUltralight.class.getName())) {
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                readTag(mifareUlTag);
            }
        }
    }

    public void readTag(MifareUltralight mifareUlTag) {
                String signature = null;
                String userID = null;
                String AID = null;
                try {
                    mifareUlTag.connect();
                    //Each read page reads the first 16 characters (16 bytes), with each subsequent read with a 4 char offset
                    //Page 4-7: Signature
                    //Page 8-11: userID
                    //Page 12-15: AID
                    byte[] payload_signature = mifareUlTag.readPages(4);
                    byte[] payload_userID = mifareUlTag.readPages(8);
                    byte[] payload_AID = mifareUlTag.readPages(12);

                    signature = new String(payload_signature, Charset.forName("US-ASCII")).trim();
                    userID = new String(payload_userID, Charset.forName("US-ASCII")).trim();
                    AID = new String(payload_AID, Charset.forName("US-ASCII")).trim();


                    Log.v("payload_signature", signature);
                    Log.v("payload_userID", userID);
                    Log.v("payload_AID", AID);

                    //Check if NFC contain our unique signature
                    if (signature.equals("HHCCJRDLZY2020ST")){
                        // Toast.makeText(this,"Bring User to timer",Toast.LENGTH_LONG).show();
                        // Get the activity from Firebase
                        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                                .getReference(UserInfo.ALL_USER_PARENT)
                                .child(DataUtils.getCurrentUserAuthID())
                                .child(UserActivityInfo.ALL_USER_ACTIVITY_PARENT)
                                .child(AID);

                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    UserActivityInfo thisActivity = snapshot.getValue(UserActivityInfo.class);
                                    // Create Intent to start Tracking!
                                    Intent startTrackingIntent = new Intent(MainActivity.this, TrackingActivity.class);
                                    startTrackingIntent.putExtra("AID", thisActivity.getAID());
                                    startTrackingIntent.putExtra("activityName", thisActivity.getActivityName());
                                    startTrackingIntent.putExtra("color", String.valueOf(thisActivity.getColor()));
                                    startActivity(startTrackingIntent);

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, error.toString());
                            }
                        });

                    }
                } catch (IOException e) {
                    Log.e(TAG, "IOException while reading MifareUltralight message...", e);
                } finally {
                    if (mifareUlTag != null) {
                        try {
                            mifareUlTag.close();
                        } catch (IOException e) {
                            Log.e(TAG, "Error closing tag...", e);
                        }
                    }
                }
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
                    UserInfo.addUserInfoIfNotExist(userName, authID);
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
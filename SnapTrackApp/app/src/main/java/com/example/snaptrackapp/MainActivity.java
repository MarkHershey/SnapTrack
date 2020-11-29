package com.example.snaptrackapp;

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

import com.example.snaptrackapp.data.UserInfo;
import com.example.snaptrackapp.record.NdefMessageParser;
import com.example.snaptrackapp.record.ParsedNdefRecord;
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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    //dummy check
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //dummy check
        text = findViewById(R.id.text);

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
        if (nfcAdapter != null){
            if (!nfcAdapter.isEnabled()){
                showBluetoothSettings();
            }
        }
        //Enables foreground dispatch which handles NFC intents (waiting for NFC card to be tapped)
        //The foreground dispatch system allows an activity to intercept an intent and
        // claim priority over other activities that handle the same intent.
        assert nfcAdapter != null;
        nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);
    }

    private void showBluetoothSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected void onPause() {

        super.onPause();
        //On pause stop listening
//        if (nfcAdapter != null) {
//            nfcAdapter.disableForegroundDispatch(this);
//        }
    }

    //New NFC Intent (NFC Card detected)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.v("logcat",getClass().getName());
        try {
            //Popup Window must exist before user can scan nfc tag and write, else nothing will happen
            resolveIntent(intent);
        } catch (Exception e) {
            Log.v("logcat","info has not been keyed in to scan");
            e.printStackTrace();
        }
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;


            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            } else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                assert tag != null;
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }

            displayMsgs(msgs);
            Toast.makeText(this,"Successfully Paired NFC",Toast.LENGTH_LONG).show();
        }
    }

    private void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();

        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String str = record.str();
            builder.append(str).append("\n");
        }
        text.setText(builder.toString());
    }

    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("NFC ID (dec): ").append(toDec(id)).append('\n');
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareUltralight.class.getName())) {
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String payload_UUID = null;
                String payload_AID = null;
                try {
                    mifareUlTag.connect();
                    //Each read page reads the first 16 characters (16 bytes), with each subsequent read with a 4 char offset
                    byte[] payload = mifareUlTag.readPages(4);
                    byte[] payload2 = mifareUlTag.readPages(10);
                    payload_UUID = new String(payload, Charset.forName("US-ASCII"));
                    payload_AID = new String(payload2, Charset.forName("US-ASCII"));

                    sb.append(payload_UUID);
                    sb.append('\n');
                    sb.append(payload_AID);
                    Log.v("Final_payload_check", payload_UUID);
                    Log.v("sb_check", sb.toString());
                    // Add user id together with this to make it so that only that user can use this tag
//                    if (UUID_existence == 1){
//
//                    }
                } catch (IOException e) {
                    Log.e("logcat", "IOException while reading MifareUltralight message...", e);
                } finally {
                    if (mifareUlTag != null) {
                        try {
                            mifareUlTag.close();
                        } catch (IOException e) {
                            Log.e("logcat", "Error closing tag...", e);
                        }
                    }
                }

                try {
                    mifareUlTag.connect();
                    //Write 1 page (4 bytes)
                    //Storing Convention
                    //Page 4-7: Signature
                    //Page 8-11: userID
                    //Page 12-15: AID
                    mifareUlTag.writePage(4, "UUID".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(5, ":453".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(6, "ijkd".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(7, "asdf".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(8, "    ".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(9, "    ".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(10, "AID:".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(11, "dfas".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(12, "A342".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(13, "sdfs".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(14, "    ".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(15, "    ".getBytes(Charset.forName("US-ASCII")));
                } catch (IOException e) {
                    Log.e("logcat", "IOException while writing MifareUltralight...", e);
                } finally {
                    try {
                        mifareUlTag.close();
                    } catch (IOException e) {
                        Log.e("logcat", "IOException while closing MifareUltralight...", e);
                    }
                }
            }
        }

        return sb.toString();
    }

    public String readTag(Tag tag) {
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareUltralight.class.getName())) {
                MifareUltralight mifare = MifareUltralight.get(tag);
                try {
                    mifare.connect();
                    byte[] payload = mifare.readPages(4);
                    return new String(payload, Charset.forName("US-ASCII"));
                } catch (IOException e) {
                    Log.e("logcat", "IOException while reading MifareUltralight message...", e);
                } finally {
                    if (mifare != null) {
                        try {
                            mifare.close();
                        } catch (IOException e) {
                            Log.e("logcat", "Error closing tag...", e);
                        }
                    }
                }
            }
            return null;
        }
        return null;
    }

    public String WriteTag(Tag tag) {
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareUltralight.class.getName())) {
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                try {
                    mifareUlTag.connect();
                    //Write 1 page (4 bytes)
                    // UUID: (16 bytes, page 4 to page 7) AID :(16 bytes, page 10 to page 13)
                    mifareUlTag.writePage(4, "UUID".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(5, ":453".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(6, "ijkd".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(7, "asdf".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(8, "    ".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(9, "    ".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(10, "AID:".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(11, "dfas".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(12, "A342".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(13, "sdfs".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(14, "    ".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(15, "    ".getBytes(Charset.forName("US-ASCII")));
                } catch (IOException e) {
                    Log.e("logcat", "IOException while writing MifareUltralight...", e);
                } finally {
                    try {
                        mifareUlTag.close();
                    } catch (IOException e) {
                        Log.e("logcat", "IOException while closing MifareUltralight...", e);
                    }
                }
            }
            return null;
        }
        return null;
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private ArrayList stringToPage(String input){
        ArrayList<String> arr = new ArrayList();
        int remainder = input.length() % 4;
        for (int i = 0; i < remainder ;i++){
            arr.add(input.substring(i*4,(i+4)*4));
        }
        if (remainder != 0){
            arr.add(input.substring(remainder*4));
        }
        return arr;
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
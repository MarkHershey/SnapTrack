package com.example.snaptrackapp.ui.today;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snaptrackapp.MainActivity;
import com.example.snaptrackapp.R;
import com.example.snaptrackapp.data.DataUtils;
import com.example.snaptrackapp.data.EventInfo;
import com.example.snaptrackapp.data.UserActivityInfo;
import com.example.snaptrackapp.data.UserInfo;
import com.example.snaptrackapp.ui.activities.EditUserActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.nio.charset.Charset;

public class TrackingActivity extends AppCompatActivity {

    String activityName;
    String AID;
    int color;

    long timeStartMillis;
    long timeEndMillis;
    int trackedTimeSeconds;
    EventInfo thisEvent;

    TextView activityNameTextView;
    Chronometer theChronometer;
    Button stopButton;
    View backgroundView;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    @Override
    public void onBackPressed() {
        Toast.makeText(TrackingActivity.this, "Please stop current tracking before leaving.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);



        // get view object references
        activityNameTextView = findViewById(R.id.currentTrackingActivity);
        theChronometer = findViewById(R.id.chronometer);
        stopButton = findViewById(R.id.stopTrackingButton);
        backgroundView = (View) activityNameTextView.getParent();

        // start timer
        theChronometer.start();
        timeStartMillis = System.currentTimeMillis();
        // Store data
        // SharedPreferences sharedPref = TrackingActivity.this.getPreferences(Context.MODE_PRIVATE);
        // SharedPreferences.Editor editor = sharedPref.edit();
        // editor.putLong("timeStart", timeStartMillis);
        // editor.apply();

        // get data from intent
        Intent intent = getIntent();
        activityName = intent.getStringExtra("activityName");
        AID = intent.getStringExtra("AID");
        color = Integer.parseInt(intent.getStringExtra("color"));


        // set views
        activityNameTextView.setText(activityName);
        activityNameTextView.setTextColor(color);
        // backgroundView.setBackgroundColor(color);

        // set OnClickListener for stop button
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTracking();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        assert nfcAdapter != null;
        nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);
    }

    //New NFC Intent (NFC Card detected)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.v("logcat",getClass().getName());
        try {
            resolveIntent(intent);
        } catch (Exception e) {
            Log.v("logcat","unable to resolveIntent");
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
        String NFC_signature = null;
        String NFC_userID = null;
        String NFC_AID = null;
        try {
            mifareUlTag.connect();
            //Each read page reads the first 16 characters (16 bytes), with each subsequent read with a 4 char offset
            //Page 4-7: Signature
            //Page 8-11: userID
            //Page 12-15: AID
            byte[] payload_signature = mifareUlTag.readPages(4);
            byte[] payload_userID = mifareUlTag.readPages(8);
            byte[] payload_AID = mifareUlTag.readPages(12);

            NFC_signature = new String(payload_signature, Charset.forName("US-ASCII")).trim();
            NFC_userID = new String(payload_userID, Charset.forName("US-ASCII")).trim();
            NFC_AID = new String(payload_AID, Charset.forName("US-ASCII")).trim();

            Log.v("payload_NFC_signature", NFC_signature);
            Log.v("payload_NFC_userID", NFC_userID);
            Log.v("payload_NFC_AID", NFC_AID);

            //Check if NFC contain our unique signature
            if (NFC_signature.equals("HHCCJRDLZY2020ST") && NFC_AID.equals(AID)){
                stopTracking();
                }
            if (NFC_signature.equals("HHCCJRDLZY2020ST") && !NFC_AID.equals(AID)){
                Toast.makeText(TrackingActivity.this, "Incorrect NFC scanned, please scan the right NFC", Toast.LENGTH_LONG).show();
            }
            if (!NFC_signature.equals("HHCCJRDLZY2020ST")){
                Toast.makeText(TrackingActivity.this, "NFC has not been paired", Toast.LENGTH_LONG).show();
            }

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
    }

    public void stopTracking(){
        theChronometer.stop();
        timeEndMillis = System.currentTimeMillis();
        trackedTimeSeconds = (int) (timeEndMillis - timeStartMillis) / 1000;
        // Toast.makeText(TrackingActivity.this, String.valueOf(trackedTimeSeconds)+"s", Toast.LENGTH_LONG).show();
        // create new Event Object
        thisEvent = new EventInfo(AID, timeStartMillis, timeEndMillis);
        // Submit Event to Firebase
        EventInfo.add(thisEvent);
        // dismiss this activity
        finish();
    }





}
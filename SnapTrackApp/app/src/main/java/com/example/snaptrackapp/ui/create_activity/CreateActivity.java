package com.example.snaptrackapp.ui.create_activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.nfc.FormatException;
import android.nfc.tech.Ndef;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import com.example.snaptrackapp.R;
import com.example.snaptrackapp.data.DataUtils;
import com.example.snaptrackapp.data.Listener;
import com.example.snaptrackapp.data.UserInfo;
import com.example.snaptrackapp.record.NdefMessageParser;
import com.example.snaptrackapp.record.ParsedNdefRecord;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CreateActivity extends AppCompatActivity{
    Button buttonPairAnNFC;
    Button buttonEraseNFCData;

    //Load Edittext to CreateActivity
    EditText editNameOfActivity;
    EditText editShortName;
    EditText editLabels;
    EditText editColour;

    PopupWindow popupWindow;
    PopupWindow confirmPopupWindow;
    //PopupButton
    Button buttonYes;
    Button buttonNo;


    TextView text;

    // Create NfcAdapter
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    private String journey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e("AddChangeTag","Start AddChangeTag process");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activity);
        //Initiate all findable ids

        text = findViewById(R.id.text);
        //Initiate buttons
        buttonPairAnNFC = findViewById(R.id.buttonPairAnNFC);
        buttonEraseNFCData = findViewById(R.id.buttonEraseNFCData);

        //Initiate EditText
        editNameOfActivity = findViewById(R.id.editNameOfActivity);
        editShortName = findViewById(R.id.editShortName);
        editLabels = findViewById(R.id.editLabels);
        editColour = findViewById(R.id.editColour);

        //Create Adapter to check if NFC is on
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null){
            Toast.makeText(this,"NO NFC Capabilities",Toast.LENGTH_SHORT).show();
            finish();
        }
        //Create a PendingIntent object so the Android system can populate it with the details of the tag when it is scanned.
        pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);


        //Button to press to start listening to NFC scan
        buttonPairAnNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if the compulsory fields are completed (To add if the activity is unique
                if (!editNameOfActivity.getText().toString().isEmpty() && !editColour.getText().toString().isEmpty() ) {
                    Log.v("logcat","compulsary completed");
                    Log.v("logcat", "Start to show popup");
                    journey = "Add";
                    onButtonShowPopupWindowClick(v);
                } else {
                    Toast.makeText(com.example.snaptrackapp.ui.create_activity.CreateActivity.this,"Please specify the 'Name of the Activity' and a unique 'Colour' of the activity",Toast.LENGTH_LONG).show();
                }
            }
        });
        buttonEraseNFCData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowQuestionPopupWindowClick(v);
            }
        });

    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.create_activity_scan_nfc_pop_up, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(false);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

//         dismiss the popup window when Trigger
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void onButtonShowQuestionPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.question_want_to_erase, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false; // lets taps outside the popup also dismiss it
        confirmPopupWindow = new PopupWindow(popupView, width, height, focusable);
        confirmPopupWindow.setOutsideTouchable(false);
        confirmPopupWindow.setTouchable(false);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        confirmPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        buttonYes = popupView.findViewById(R.id.yes);
        buttonNo = popupView.findViewById(R.id.no);

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                journey = "Erase";
                onButtonShowPopupWindowClick(v);
                Log.v("logcat","hi");
            }
        });
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPopupWindow.dismiss();
            }
        });

        //Handler for clicking on the inactive zone of the window

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null){
            if (!nfcAdapter.isEnabled()){
                showWirelessSettings();
            }
        }
        //Enables foreground dispatch which handles NFC intents (waiting for NFC card to be tapped)
        //The foreground dispatch system allows an activity to intercept an intent and
        // claim priority over other activities that handle the same intent.
        assert nfcAdapter != null;
        nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);
    }

    private void showWirelessSettings() {
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
            popupWindow.dismiss();
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
            Log.v("logcat",journey);
            if (journey.equals("Add")) {
                NdefMessage[] msgs;
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                assert tag != null;
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
                displayMsgs(msgs);
                Toast.makeText(this, "Successfully Paired NFC", Toast.LENGTH_LONG).show();
            }
            Log.v("logcat",journey);
            if (journey.equals("Erase")) {
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                for (String tech : tag.getTechList()) {
                    if (tech.equals(MifareUltralight.class.getName())) {
                        MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                        eraseTagData(mifareUlTag);
                        Toast.makeText(this, "Successfully Erased NFC Data", Toast.LENGTH_LONG).show();
                    }
                }
            }
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
                readTag(mifareUlTag, sb);
                writeTag(mifareUlTag);
            }
        }
        return sb.toString();
    }

    public StringBuilder readTag(MifareUltralight mifareUlTag, StringBuilder sb) {
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

            //Gets the payload with empty trailing and leading spaces removed
            signature = new String(payload_signature, Charset.forName("US-ASCII")).trim();
            userID = new String(payload_userID, Charset.forName("US-ASCII")).trim();
            AID = new String(payload_AID, Charset.forName("US-ASCII")).trim();

            Log.v("payload_userID", signature);
            Log.v("payload_userID", userID);
            Log.v("payload_userID", AID);
            sb.append("signature: "+signature);
            sb.append('\n');
            sb.append("userID: "+userID);
            sb.append('\n');
            sb.append("AID: "+AID);

            // Add user id together with this to make it so that only that user can use this tag
            if (signature.equals("HHCCJRDLZY2020ST")){
                //TO-DO
//                Toast.makeText(this,"Bring up, pop up to confirm changes",Toast.LENGTH_LONG).show();
                onButtonShowQuestionPopupWindowClick(this.getCurrentFocus());
            }
            else {
                for (int i = 8;i < 36; i+=4) {
                    //If not empty, illegal NFC  pages 4-7 and pages 36-39 are skipped to ensure no jibberish values
                    Log.v("Tag_data",new String(mifareUlTag.readPages(i), Charset.forName("US-ASCII")).trim() + i);
                    if (!new String(mifareUlTag.readPages(i), Charset.forName("US-ASCII")).trim().isEmpty()) {
                        Toast.makeText(this, "Non-snapTrack NFCs are not allowed, please reformat your NFC tag", Toast.LENGTH_LONG).show();
                    }
                }
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
        return sb;
    }

    public void writeTag(MifareUltralight mifareUlTag) {
        final String[] id = {null};
        DataUtils.fetchUserInfoSingle(new Listener<UserInfo>() {
            public void update(UserInfo info) {
                if (info != null) {
                    id[0] = info.getUserID();
                    Log.v("getUSERID", id[0]);
                    Log.v("getUSERID_array", id[0].substring(0,4));

                    try {
                        mifareUlTag.connect();
                        //Write 1 page (4 bytes)
                        //Page 4-7: Signature
                        //Page 8-11: userID
                        //Page 12-15: AID
                        //TO DO check if the id is correct and what's its length
                        if (id[0].length() != 16) {
                            throw new NullPointerException("id not found");
                        }
                        //write Signature
                        mifareUlTag.writePage(4, "HHCC".getBytes(Charset.forName("US-ASCII")));
                        mifareUlTag.writePage(5, "JRDL".getBytes(Charset.forName("US-ASCII")));
                        mifareUlTag.writePage(6, "ZY20".getBytes(Charset.forName("US-ASCII")));
                        mifareUlTag.writePage(7, "20ST".getBytes(Charset.forName("US-ASCII")));
                        //write userID
                        mifareUlTag.writePage(8, id[0].substring(0, 4).getBytes(Charset.forName("US-ASCII")));
                        mifareUlTag.writePage(9, id[0].substring(4, 8).getBytes(Charset.forName("US-ASCII")));
                        mifareUlTag.writePage(10, id[0].substring(8, 12).getBytes(Charset.forName("US-ASCII")));
                        mifareUlTag.writePage(11, id[0].substring(12, 16).getBytes(Charset.forName("US-ASCII")));
                        //write AID
                        mifareUlTag.writePage(12, "A342".getBytes(Charset.forName("US-ASCII")));
                        mifareUlTag.writePage(13, "sdfs".getBytes(Charset.forName("US-ASCII")));
                        mifareUlTag.writePage(14, "dsds".getBytes(Charset.forName("US-ASCII")));
                        mifareUlTag.writePage(15, "rqwe".getBytes(Charset.forName("US-ASCII")));
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
        });
    }

    public void eraseTagData(MifareUltralight mifareUlTag) {
        try {
            mifareUlTag.connect();
            //Write 1 page (4 bytes)
            for (int i = 4; i<40;i++) {
                mifareUlTag.writePage(i, "".getBytes(Charset.forName("US-ASCII")));
            }
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

}
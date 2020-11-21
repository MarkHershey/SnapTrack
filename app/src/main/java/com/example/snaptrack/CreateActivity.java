package com.example.snaptrack;

import android.app.PendingIntent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snaptrack.record.NdefMessageParser;
import com.example.snaptrack.record.ParsedNdefRecord;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    //Load buttons to leave CreateActivity screen
    Button buttonDataAnalytics_CA;
    Button buttonToday_CA;
    Button buttonPairAnNFC;
    Button buttonMe_CA;

    //Load Edittext to CreateActivity
    EditText editNameOfActivity;
    EditText editShortName;
    EditText editLabels;
    EditText editColour;

    //UserID uses intent to pass this data here, to be used to check if Activity matches user
    String UserID;

    // Create NfcAdapter
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    //Create Popup
    PopupWindow popupWindow;

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e("AddChangeTag","Start AddChangeTag process");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activity_sub_2);
        //Initiate all findable ids

        text = findViewById(R.id.text);
        //Initiate buttons
        buttonPairAnNFC = findViewById(R.id.buttonPairAnNFC);
        buttonMe_CA = findViewById(R.id.buttonMe_CA);
        buttonToday_CA = findViewById(R.id.buttonToday_CA);
        buttonDataAnalytics_CA = findViewById(R.id.buttonDataAnalytics_CA);

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

        Log.e("logcat","compulsary completed");
        buttonPairAnNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editNameOfActivity.getText().toString().equals("") && !editColour.getText().toString().equals("")) {
                    Log.e("logcat", "Start to show popup");
                    onButtonShowPopupWindowClick(v);
                } else {
                    Toast.makeText(CreateActivity.this,"Please specify the 'Name of the Activity' and a unique 'Colour' of the activity",Toast.LENGTH_LONG).show();
                }
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
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(false);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        // dismiss the popup window when touched

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled()) {
                try {
                    nfcAdapter.disableForegroundDispatch(this);
                }
                catch(Exception IllegalStateException) {
                    Log.e("NFC", "Error disabling NFC foreground dispatch");
                }
            }
        }
    }

    //New NFC Intent (NFC Card detected)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            popupWindow.dismiss();
            setIntent(intent);
            resolveIntent(intent);
        } catch (Exception e) {
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
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String payload_complete = "";
                try {
                    mifareUlTag.connect();
                    for (int i=4;i<40;i++) {
                        byte[] payload = mifareUlTag.readPages(i);
                        sb.append(new String(payload, Charset.forName("US-ASCII")));
                        Log.e("logcat", new String(payload, Charset.forName("US-ASCII")));
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

                try {
                    mifareUlTag.connect();
                    mifareUlTag.writePage(4, "UUID".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(5, ":453".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(6, "ijkl".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(7, "AID:".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(8, "1234".getBytes(Charset.forName("US-ASCII")));
                    mifareUlTag.writePage(9, "4312".getBytes(Charset.forName("US-ASCII")));
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
//            if (tech.equals(MifareClassic.class.getName())) {
//                sb.append('\n');
//                String type = "Unknown";
//
//                try {
//                    MifareClassic mifareTag = MifareClassic.get(tag);
//
//                    switch (mifareTag.getType()) {
//                        case MifareClassic.TYPE_CLASSIC:
//                            type = "Classic";
//                            break;
//                        case MifareClassic.TYPE_PLUS:
//                            type = "Plus";
//                            break;
//                        case MifareClassic.TYPE_PRO:
//                            type = "Pro";
//                            break;
//                    }
//                    sb.append("Mifare Classic type: ");
//                    sb.append(type);
//                    sb.append('\n');
//
//                    sb.append("Mifare size: ");
//                    sb.append(mifareTag.getSize() + " bytes");
//                    sb.append('\n');
//
//                    sb.append("Mifare sectors: ");
//                    sb.append(mifareTag.getSectorCount());
//                    sb.append('\n');
//
//                    sb.append("Mifare blocks: ");
//                    sb.append(mifareTag.getBlockCount());
//                } catch (Exception e) {
//                    sb.append("Mifare classic error: " + e.getMessage());
//                }
//            }


        }

        return sb.toString();
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

package com.example.snaptrackapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snaptrackapp.MainActivity;
import com.example.snaptrackapp.R;
import com.example.snaptrackapp.data.DataUtils;
import com.example.snaptrackapp.data.Listener;
import com.example.snaptrackapp.data.UserInfo;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;

public class EditUserActivity extends AppCompatActivity {

    private static final String TAG = "EditUserActivity";
    EditText nameEditView;
    EditText categoryEditView;
    SeekBar aBar;
    SeekBar rBar;
    SeekBar gBar;
    SeekBar bBar;
    TextView colorDisplay;
    TextView aidView;
    TextView text;
    Button pairNfcTagButton;
    Button cancelButton;
    int intColor;
    int intA;
    int intR;
    int intG;
    int intB;

    // Create NfcAdapter
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    //Create popupWindows and popupButtons
    PopupWindow confirmPopupWindow;
    //PopupButton
    Button cancelButton_pop;
    Button overwriteButton;
    //PopupText
    TextView popup_text;
    TextView aidLabel_pop;

    //Activity Information
    String AID = null;

    //NFC information
    String NFC_signature = null;
    String NFC_userID = null;
    String NFC_AID = null;

    //Track progression
    String journey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_activity);

        nameEditView = findViewById(R.id.nameEdit);
        categoryEditView = findViewById(R.id.categoryEdit);
        aBar = findViewById(R.id.aBar);
        rBar = findViewById(R.id.rBar);
        gBar = findViewById(R.id.gBar);
        bBar = findViewById(R.id.bBar);
        colorDisplay = findViewById(R.id.colorDisplay);
        aidView = findViewById(R.id.aidLabel);
        text = findViewById(R.id.text);
        cancelButton = findViewById(R.id.cancelButton);
        pairNfcTagButton = findViewById(R.id.pairNfcTagButton);

        //Create Adapter to check if NFC is on
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null){
            Toast.makeText(this,"NO NFC Capabilities",Toast.LENGTH_SHORT).show();
            finish();
        }
        //Create a PendingIntent object so the Android system can populate it with the details of the tag when it is scanned.
        pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);

        Intent intent = getIntent();
        String activityName = intent.getStringExtra("activityName");
        String category = intent.getStringExtra("category");
        String color = intent.getStringExtra("color");
        AID = intent.getStringExtra("AID");

        intColor = Integer.parseInt(color);
        colorDisplay.setBackgroundColor(intColor);

        intA = (intColor >> 24) & 0xff;
        intR = (intColor >> 16) & 0xff;
        intG = (intColor >>  8) & 0xff;
        intB = (intColor      ) & 0xff;

        nameEditView.setText(activityName);
        categoryEditView.setText(category);

        aBar.setProgress(intA);
        rBar.setProgress(intR);
        gBar.setProgress(intG);
        bBar.setProgress(intB);

        aidView.setText("Activity ID: " + AID);

        aBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >=0 && progress <= 255){
                    intA = progress;
                }
                updateColorDisplay();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        rBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >=0 && progress <= 255){
                    intR = progress;
                }
                updateColorDisplay();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        gBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >=0 && progress <= 255){
                    intG = progress;
                }
                updateColorDisplay();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        bBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >=0 && progress <= 255){
                    intB = progress;
                }
                updateColorDisplay();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Button to press to start listening to NFC scan
        pairNfcTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if the compulsory fields are completed (To add if the activity is unique
                if (!nameEditView.getText().toString().isEmpty() && !categoryEditView.getText().toString().isEmpty()) {
                    onScanShowExistingDataPopUp(v);
                } else {
                    //TO-DO add check if the activity name is unique and category is unique
                    Toast.makeText(EditUserActivity.this,"Please specify a unique 'Activity Name' and a 'Category' for activity",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void updateColorDisplay() {
        intColor = (intA & 0xff) << 24 | (intR & 0xff) << 16 | (intG & 0xff) << 8 | (intB & 0xff);
        colorDisplay.setBackgroundColor(intColor);
    }

    public void onScanShowExistingDataPopUp(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_exisiting_user_activity, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false; // lets taps outside the popup also dismiss it
        confirmPopupWindow = new PopupWindow(popupView, width, height, focusable);
        confirmPopupWindow.setOutsideTouchable(false);
        //Set touchable so that we can click our buttons
        confirmPopupWindow.setTouchable(true);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        confirmPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        //TODO: show information from NFC_AID
        aidLabel_pop = popupView.findViewById(R.id.aidLabel_pop);
        popup_text = popupView.findViewById(R.id.popup_text);
        overwriteButton = popupView.findViewById(R.id.overwriteButton);
        cancelButton_pop = popupView.findViewById(R.id.cancelButton_pop);

       //TODO: Get Activity name, category name and colour from NFC_AID

        overwriteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                journey = "Write";
                overwriteButton.setVisibility(View.INVISIBLE);
                cancelButton_pop.setVisibility(View.INVISIBLE);
                aidLabel_pop.setVisibility(View.INVISIBLE);
                popup_text.setVisibility(View.VISIBLE);
                popup_text.setText("Please Tap NFC Again");
            }
        });
        cancelButton_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get out of all popups
                confirmPopupWindow.dismiss();
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
    protected void onPause() {

        super.onPause();
        //On pause stop listening
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.v(TAG,getClass().getName());
        try {
            //Popup Window must exist before user can scan nfc tag and write, else nothing will happen
            overwriteButton.setVisibility(View.VISIBLE);
            cancelButton_pop.setVisibility(View.VISIBLE);
            aidLabel_pop.setVisibility(View.VISIBLE);
            resolveIntent(intent);
        } catch (Exception e) {
            Log.v(TAG,"info has not been keyed in to scan");
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
                byte[] payload = locateTagData(tag).getBytes();

            }
    }

    private String locateTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("NFC ID (dec): ").append(toDec(id)).append('\n');
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareUltralight.class.getName())) {
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                if (journey == null) {
                    readTag(mifareUlTag, sb);
                    if (NFC_AID.isEmpty()){
                        aidLabel_pop.setText("Activity ID: Empty Tag");
                    }
                    else {
                        aidLabel_pop.setText("Activity ID: " + NFC_AID);
                    }
                }
                else{
                    writeTag(mifareUlTag);
                }
            }
        }
        return sb.toString();
    }

    public StringBuilder readTag(MifareUltralight mifareUlTag, StringBuilder sb) {
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
            NFC_signature = new String(payload_signature, Charset.forName("US-ASCII")).trim();
            NFC_userID = new String(payload_userID, Charset.forName("US-ASCII")).trim();
            NFC_AID = new String(payload_AID, Charset.forName("US-ASCII")).trim();

            Log.v("payload_NFC_signature", NFC_signature);
            Log.v("payload_NFC_userID", NFC_userID);
            Log.v("payload_NFC_AID", NFC_AID);
            sb.append("signature: "+NFC_signature);
            sb.append('\n');
            sb.append("userID: "+NFC_userID);
            sb.append('\n');
            sb.append("AID: "+NFC_AID);

            // Add user id together with this to make it so that only that user can use this tag
            if (NFC_signature.equals("HHCCJRDLZY2020ST")){
                popup_text.setText("Are you sure you want to overwrite existing data?");
//                onScanShowExistingDataPopUp(popupWindow.getContentView(),mifareUlTag);
//                Toast.makeText(this, , Toast.LENGTH_SHORT).show();

            }
            else{
                popup_text.setText("Important data inside NFC may be overwritten, overwrite?");
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
        return sb;
    }

    public void writeTag(MifareUltralight mifareUlTag) {
        final String[] id = {null};
        DataUtils.fetchUserInfoSingle(new Listener<UserInfo>() {
            public void update(UserInfo info) {
                if (info != null) {
                    id[0] = info.getUserID();
                    Log.v("getUSERID", id[0]);
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
                        mifareUlTag.writePage(12, AID.substring(0, 4).getBytes(Charset.forName("US-ASCII")));
                        mifareUlTag.writePage(13, AID.substring(4, 8).getBytes(Charset.forName("US-ASCII")));
                        mifareUlTag.writePage(14, AID.substring(8, 12).getBytes(Charset.forName("US-ASCII")));
                        mifareUlTag.writePage(15, AID.substring(12, 16).getBytes(Charset.forName("US-ASCII")));
                    } catch (IOException e) {
                        Log.e(TAG, "IOException while writing MifareUltralight...", e);
                    } finally {
                        try {
                            mifareUlTag.close();
                            try {
                                confirmPopupWindow.dismiss();
                                journey = null;
                                Toast.makeText(EditUserActivity.this, "Successfully Overwritten NFC", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(EditUserActivity.this , MainActivity.class);
                                startActivity(intent);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "IOException while closing MifareUltralight...", e);
                        }
                    }
                }
            }
        });
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
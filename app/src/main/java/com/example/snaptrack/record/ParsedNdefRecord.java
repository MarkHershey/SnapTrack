package com.example.snaptrack.record;

import android.app.Activity;
import android.net.Uri;
import android.nfc.NdefRecord;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.util.Preconditions;

import java.nio.charset.Charset;
import java.util.Arrays;

public interface ParsedNdefRecord {

    String str();
}
package com.sarkarinaukri;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomTabsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String url = intent.getDataString();

        //Toast.makeText(context, "Copy link pressed. URL = " + url, Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(url);
            Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Clip", url);
            Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show();
            clipboard.setPrimaryClip(clip);
        }
    }
}
package com.sarkarinaukri.helperClass;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.sarkarinaukri.CustomTabsBroadcastReceiver;
import com.sarkarinaukri.R;
import com.sarkarinaukri.SarkariExam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelperClass {
    public static ProgressDialog mProgressDialog;

    public static void showProgressDialog(Context context, String message) {
        mProgressDialog = new ProgressDialog(context);
        if (message == null || message.equalsIgnoreCase("")) {
            mProgressDialog.setMessage("Please wait while loading...");
        } else {

            mProgressDialog.setMessage(message);
        }
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public static void dismissProgressDialog() {
        mProgressDialog.dismiss();
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) SarkariExam.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static int displayWidth(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return width;
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            String packageName = context.getApplicationContext().getPackageName();
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Log.e("Package Name=", context.getApplicationContext().getPackageName());
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        return key;
    }

    public static void sendMail(Context context) {
        Intent mailer = new Intent(Intent.ACTION_SEND);
        mailer.setType("text/plain");
        mailer.putExtra(Intent.EXTRA_EMAIL, new String[]{"arpit29@gmail.com"});
        mailer.putExtra(Intent.EXTRA_SUBJECT, "");
        mailer.putExtra(Intent.EXTRA_TEXT, "");
        mailer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(mailer, "Send email..."));
    }

    public static void rateUs(Context mContext) {
        final String appPackageName = mContext.getPackageName();
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static void shareApp(Context context) {
        String title = "Now get all sarkari naukri in your phone, just download our app:";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, title + "\n\n " + "https://play.google.com/store/apps/details?id=" + context.getPackageName());
        context.startActivity(Intent.createChooser(shareIntent, "Share Apps..."));
    }


    public static void showSnackBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void openOrSaveUrlInChromeCustomTab(Context context, String title, String url) {
        try {
            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
            // Set toolbar(tab) color of your chrome browser
            intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
            intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));

            final Bitmap backButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_arrow_back_black_24dp);
            intentBuilder.setCloseButtonIcon(backButton);
            intentBuilder.enableUrlBarHiding();

            intentBuilder.setActionButton(BitmapFactory.decodeResource(context.getResources(), R.drawable.bookmark), "Share", getItem(context, title, url), true);

            Intent intent = new Intent(context, CustomTabsBroadcastReceiver.class);
            String label = "Copy link";
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            CustomTabsIntent customTabsIntent = intentBuilder.addMenuItem(label, pendingIntent).build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://" + context.getPackageName()));
            intentBuilder.addDefaultShareMenuItem();
            customTabsIntent.launchUrl(context, Uri.parse(url));
        } catch (Exception e) {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);
            } catch (NullPointerException e1) {

            }
        }
    }

    public static PendingIntent getItem(Context context, String title, String url) {
        Intent intent = new Intent(context, PostSaverActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void openUrlInChromeCustomTab(Context context, String url) {
        try {
            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
            // Set toolbar(tab) color of your chrome browser
            intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
            intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));

            final Bitmap backButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_arrow_back_black_24dp);
            intentBuilder.setCloseButtonIcon(backButton);
            intentBuilder.enableUrlBarHiding();

            //builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);

            intentBuilder.setActionButton(BitmapFactory.decodeResource(context.getResources(), R.drawable.share_icon), "Share", getItem(context, url), true);

            Intent intent = new Intent(context, CustomTabsBroadcastReceiver.class);
            String label = "Copy link";
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            CustomTabsIntent customTabsIntent = intentBuilder.addMenuItem(label, pendingIntent).build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://" + context.getPackageName()));
            intentBuilder.addDefaultShareMenuItem();
            customTabsIntent.launchUrl(context, Uri.parse(url));







        } catch (Exception e) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        }
    }

    public static PendingIntent getItem(Context context, String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        return PendingIntent.getActivity(context, 0, shareIntent, 0);
    }

    public static void openAppInPlayStore(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    public static String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        //String outputPattern = "dd-MMM-yyyy h:mm a";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void hideKeyboard(Context context) {
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyBoard(Context context, EditText editText) {
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(editText, 0);
        }
    }


}

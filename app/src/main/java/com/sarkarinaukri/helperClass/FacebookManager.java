package com.sarkarinaukri.helperClass;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiClientForQna;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.RetroFit.ApiclientArchi;
import com.sarkarinaukri.model.Newlogin;
import com.sarkarinaukri.model.ResultEntity;
import com.sarkarinaukri.qnaModule.LoginActivity;
import com.sarkarinaukri.qnaModule.NewLoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacebookManager {
    private CallbackManager callbackManager;
    private Context context;
    private CoordinatorLayout coordinatorLayout;


    public FacebookManager(final CallbackManager callbackManager, CoordinatorLayout coordinatorLayout, final Context context) {
        this.callbackManager = callbackManager;
        this.context = context;
        this.coordinatorLayout = coordinatorLayout;
    }

    public void doFaceBookLogin() {
        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult result) {
                HelperClass.showProgressDialog(context, null);
                final AccessToken accessToken = result.getAccessToken();

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,picture.type(large)");

                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                        HelperClass.dismissProgressDialog();
                        String emailID = user.optString("email");
                        final String name = user.optString("name");
                        final String facebookID = user.optString("id");
                        String gender = user.optString("gender");
                        String profilePicUrl = null;
                        if (user.has("picture")) {
                            try {
                                profilePicUrl = user.getJSONObject("picture").getJSONObject("data").getString("url");
                                Log.e("ProfilePicURL", profilePicUrl);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (TextUtils.isEmpty(emailID)) {
                            askForEmailDialog(name, facebookID);
                        } else {
                            signUpFromOurServer(facebookID,emailID,"fb",name);
                        }
                    }
                });

                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onError(FacebookException error) {
                if (error.getMessage() != null) {
                }
            }

            @Override
            public void onCancel() {

            }

        });
    }

//    private void signUpFromOurServer(final String emailID, final String name, final String facebookID, final String profilePicUrl) {
//        HelperClass.showProgressDialog(context, "");
//        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
//        Call<ResultEntity> call = apiInterface.socialLogin(facebookID, name, "fb", "2", FirebaseInstanceId.getInstance().getToken(), profilePicUrl, emailID);
//        call.enqueue(new Callback<ResultEntity>() {
//            @Override
//            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
//                HelperClass.dismissProgressDialog();
//                if (response.code() == 200) {
//                    ResultEntity resultEntity = response.body();
//
//                    if (resultEntity.getCode().equalsIgnoreCase("200") && ResultEntity.status.success.name().equalsIgnoreCase(resultEntity.getStatus())) {
//                        //PreferenceHelper.saveProfileData(context, resultEntity.getUserData());
//                        Intent intent = new Intent();
//                        ((Activity) context).setResult(((Activity) context).RESULT_OK, intent);
//                        ((Activity) context).finish();
//                    } else {
//                        Snackbar snackbar = Snackbar.make(coordinatorLayout, resultEntity.getMessage(), Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                signUpFromOurServer(emailID, name, facebookID, profilePicUrl);
//                            }
//                        });
//                        snackbar.setActionTextColor(Color.RED);
//                        snackbar.show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResultEntity> call, Throwable t) {
//                Log.e("Error", "Error");
//            }
//        });
//    }

    private void signUpFromOurServer(final String facebookID,final String emailID,String type,final String name) {
        HelperClass.showProgressDialog(context, "");
        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
        Call<Newlogin> call = apiInterface.socialsLogin(facebookID,emailID,type,name);
        call.enqueue(new Callback<Newlogin>() {
            @Override
            public void onResponse(Call<Newlogin> call, Response<Newlogin> response) {
                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    Newlogin resultEntity = response.body();

                    if (resultEntity.status.equalsIgnoreCase("true")) {
                        //PreferenceHelper.saveProfileData(context, resultEntity.getUserData());
                        LoginManager.getInstance().logOut();
                        PreferenceHelper.WriteSharePrefrence(context,Constant.USER_ID,resultEntity.data.id);
                        Intent intent = new Intent();
                        ((Activity) context).setResult(((Activity) context).RESULT_OK, intent);
                        ((Activity) context).finish();
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, resultEntity.status, Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                signUpFromOurServer(facebookID,emailID,"fb",name);
                            }
                        });
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Newlogin> call, Throwable t) {
                Log.e("Error", "Error");
            }
        });
    }

    private void askForEmailDialog(final String name, final String facebookID) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_ask_email_address);

        final EditText etEmail = dialog.findViewById(R.id.etEmail);
        TextView tvSubmit = dialog.findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailId = etEmail.getText().toString().trim();
                if (TextUtils.isEmpty(emailId)) {
                    Toast.makeText(context, "Enter Email Address", Toast.LENGTH_LONG).show();
                } else {
                    signUpFromOurServer(facebookID,emailId,"fb",name);
                }
            }
        });
        dialog.show();
    }

}

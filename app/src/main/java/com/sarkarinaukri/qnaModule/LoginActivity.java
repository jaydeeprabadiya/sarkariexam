package com.sarkarinaukri.qnaModule;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.facebook.login.Login;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiClientForQna;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.FacebookManager;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.helperClass.ValidationHelperClass;
import com.sarkarinaukri.model.ResultEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 007;
    private LinearLayout llSignUp;
    private TextView tvLogin;
    private TextView tvForgotPassword;
    private EditText etEmailId;
    private EditText etPassword;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayout llFacebookLogin;
    private LinearLayout llGmailLogin;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callBackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void initFacebook() {
        HelperClass.printKeyHash(LoginActivity.this);
        callBackManager = CallbackManager.Factory.create();
    }

    private void initGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void init() {
        initFacebook();
        initGoogle();

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        llSignUp = findViewById(R.id.llSignUp);
        tvLogin = findViewById(R.id.tvLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        etEmailId = findViewById(R.id.etEmailId);
        etPassword = findViewById(R.id.etPassword);

        llFacebookLogin = findViewById(R.id.llFacebookLogin);
        llGmailLogin = findViewById(R.id.llGmailLogin);

        clickListener();
    }

    private void clickListener() {
        tvForgotPassword.setOnClickListener(this);
        llSignUp.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        llFacebookLogin.setOnClickListener(this);
        llGmailLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.llSignUp:
                intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.tvLogin:
                HelperClass.hideKeyboard(LoginActivity.this);
                authenticateUser();
                break;
            case R.id.tvForgotPassword:
                openForgetPasswordDialog();
                break;
            case R.id.llFacebookLogin:
                new FacebookManager(callBackManager, coordinatorLayout, LoginActivity.this).doFaceBookLogin();
                break;
            case R.id.llGmailLogin:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
        }
    }

    private void authenticateUser() {
        String email = etEmailId.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (!ValidationHelperClass.isValidEmail(email)) {
            HelperClass.showSnackBar(coordinatorLayout, "Please enter your valid email id");
        } else if (TextUtils.isEmpty(password)) {
            HelperClass.showSnackBar(coordinatorLayout, "Please enter password");
        } else {
            HelperClass.showProgressDialog(LoginActivity.this, "");
            ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
            Call<ResultEntity> call = apiInterface.login(email, password, FirebaseInstanceId.getInstance().getToken(), "2");
            call.enqueue(new Callback<ResultEntity>() {
                @Override
                public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                    //HelperClass.displayRequestAndResponse(response, null);

                    HelperClass.dismissProgressDialog();
                    if (response.code() == 200) {
                        ResultEntity resultEntity = response.body();

                        if (ResultEntity.status.success.name().equalsIgnoreCase(resultEntity.getStatus())) {
                            PreferenceHelper.saveProfileData(LoginActivity.this, resultEntity.getUserData());
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            HelperClass.showSnackBar(coordinatorLayout, resultEntity.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultEntity> call, Throwable t) {
                    HelperClass.dismissProgressDialog();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callBackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("GOOGLE", "onConnectionFailed:" + connectionResult);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String email = acct.getEmail();
            String id = acct.getId();

            signUpFromOurServer(email, personName, id);

        }
    }

    private void signUpFromOurServer(final String emailID, final String name, final String socialId) {
        HelperClass.showProgressDialog(LoginActivity.this, "");
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<ResultEntity> call = apiInterface.socialLogin(socialId, name, "gmail", "2", FirebaseInstanceId.getInstance().getToken(), "", emailID);
        call.enqueue(new Callback<ResultEntity>() {
            @Override
            public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    ResultEntity resultEntity = response.body();

                    if (ResultEntity.status.success.name().equalsIgnoreCase(resultEntity.getStatus())) {

                      // PreferenceHelper.WriteSharePrefrence(LoginActivity.this, Constant.USER_ID,resultEntity.data.id);

                        PreferenceHelper.saveProfileData(LoginActivity.this, resultEntity.getUserData());
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, resultEntity.getMessage(), Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                signUpFromOurServer(emailID, name, socialId);
                            }
                        });
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultEntity> call, Throwable t) {

            }
        });
    }

    private void openForgetPasswordDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_forgot_password);

        final EditText etEmail = (EditText) dialog.findViewById(R.id.etEmail);
        TextView tvSubmit = (TextView) dialog.findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailId = etEmail.getText().toString().trim();
                if (TextUtils.isEmpty(emailId)) {
                    Toast.makeText(LoginActivity.this, "Enter Email Address", Toast.LENGTH_LONG).show();
                } else {
                    HelperClass.showProgressDialog(LoginActivity.this, "Password is sending to you email.");
                    ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
                    Call<ResultEntity> call = apiInterface.forgotPassword(emailId);
                    call.enqueue(new Callback<ResultEntity>() {
                        @Override
                        public void onResponse(Call<ResultEntity> call, Response<ResultEntity> response) {
                            HelperClass.dismissProgressDialog();
                            //HelperClass.displayRequestAndResponse(response, null);

                            if (response.code() == 200) {
                                ResultEntity resultEntity = response.body();
                                Snackbar snackbar = Snackbar.make(coordinatorLayout, resultEntity.getMessage(), Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });
                                snackbar.setActionTextColor(Color.RED);
                                snackbar.show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultEntity> call, Throwable t) {
                            HelperClass.dismissProgressDialog();
                        }
                    });
                }
            }
        });
        dialog.show();
    }
}

package com.sarkarinaukri.qnaModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sarkarinaukri.R;
import com.sarkarinaukri.RetroFit.ApiClientForQna;
import com.sarkarinaukri.RetroFit.ApiInterface;
import com.sarkarinaukri.RetroFit.ApiclientArchi;
import com.sarkarinaukri.helperClass.Constant;
import com.sarkarinaukri.helperClass.FacebookManager;
import com.sarkarinaukri.helperClass.HelperClass;
import com.sarkarinaukri.helperClass.PreferenceHelper;
import com.sarkarinaukri.helperClass.ValidationHelperClass;
import com.sarkarinaukri.model.Newlogin;
import com.sarkarinaukri.model.ResultEntity;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewSignupActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 007;
    private CoordinatorLayout coordinatorLayout;
    private ImageButton ivBackButton;
    private TextView tvSignUp;
    private EditText etName;
    private EditText etEmailId;
    private EditText etMobile;
    private EditText etPassword;
    private EditText etCPassword;
    private LinearLayout llLogin;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callBackManager;
    private LinearLayout llFacebookLogin;
    private LinearLayout llGmailLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_new_signup);

        init();
    }

    private void initFacebook() {
        HelperClass.printKeyHash(NewSignupActivity.this);
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
        ivBackButton = findViewById(R.id.ivBackButton);

        etName = findViewById(R.id.etName);
        etEmailId = findViewById(R.id.etEmailId);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        etCPassword = findViewById(R.id.etCPassword);
        tvSignUp = findViewById(R.id.tvSignUp);

        llLogin = findViewById(R.id.llLogin);
        llFacebookLogin = findViewById(R.id.llFacebookLogin);
        llGmailLogin = findViewById(R.id.llGmailLogin);

        clickListener();
    }

    private void clickListener() {
        ivBackButton.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        llLogin.setOnClickListener(this);
        llFacebookLogin.setOnClickListener(this);
        llGmailLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBackButton:
                finish();
                break;
            case R.id.tvSignUp:
                HelperClass.hideKeyboard(NewSignupActivity.this);
                registerUser();
                break;
            case R.id.llLogin:
                Intent intent = new Intent(NewSignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.llFacebookLogin:
                new FacebookManager(callBackManager, coordinatorLayout, NewSignupActivity.this).doFaceBookLogin();
                break;
            case R.id.llGmailLogin:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
        }
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmailId.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String password = etPassword.getText().toString();
        String cPassword = etCPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            HelperClass.showSnackBar(coordinatorLayout, "Please enter your name");
        } else if (!ValidationHelperClass.isValidEmail(email)) {
            HelperClass.showSnackBar(coordinatorLayout, "Please enter your valid email id");
        } else if (!ValidationHelperClass.isValidPhoneNumber(mobile)) {
            HelperClass.showSnackBar(coordinatorLayout, "Please enter your valid mobile number");
        } else if (TextUtils.isEmpty(password)) {
            HelperClass.showSnackBar(coordinatorLayout, "Please enter password");
        } else if (!password.equals(cPassword)) {
            HelperClass.showSnackBar(coordinatorLayout, "Password not matching");
        } else {
            HelperClass.showProgressDialog(NewSignupActivity.this, "");
            ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
            Call<Newlogin> call = apiInterface.NewRegistration(name, email, mobile, password);
            call.enqueue(new Callback<Newlogin>() {
                @Override
                public void onResponse(Call<Newlogin> call, Response<Newlogin> response) {
                    //HelperClass.displayRequestAndResponse(response, null);
                    HelperClass.dismissProgressDialog();
                    if (response.code() == 200) {
                        Newlogin resultEntity = response.body();

                        if (resultEntity.status.equalsIgnoreCase("true")) {
                            //PreferenceHelper.saveProfileData(NewSignupActivity.this, resultEntity.getUserData());
                            PreferenceHelper.WriteSharePrefrence(NewSignupActivity.this, Constant.USER_ID,resultEntity.data.id);
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            HelperClass.showSnackBar(coordinatorLayout, resultEntity.msg);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Newlogin> call, Throwable t) {
                    HelperClass.dismissProgressDialog();
                }
            });


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
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

            signUpFromOurServer(id,email,"email",personName);

        }
    }

    private void signUpFromOurServer(final String socialId,final String emailID,String type,final String name) {
        ApiInterface apiInterface = ApiClientForQna.getClient().create(ApiInterface.class);
        Call<Newlogin> call = apiInterface.socialsLogin(socialId,emailID,type,name);
        call.enqueue(new Callback<Newlogin>() {
            @Override
            public void onResponse(Call<Newlogin> call, Response<Newlogin> response) {
                //HelperClass.displayRequestAndResponse(response, null);

                if (response.code() == 200) {
                    Newlogin resultEntity = response.body();

                    if (resultEntity.status.equalsIgnoreCase("true")) {
                        PreferenceHelper.WriteSharePrefrence(NewSignupActivity.this, Constant.USER_ID,resultEntity.data.id);
                        //PreferenceHelper.saveProfileData(NewSignupActivity.this, resultEntity.getUserData());
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, resultEntity.msg, Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                signUpFromOurServer(socialId,emailID,"email",name);
                            }
                        });
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Newlogin> call, Throwable t) {

            }
        });
    }


}

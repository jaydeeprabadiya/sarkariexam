package com.sarkarinaukri.qnaModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sarkarinaukri.LanguageChooserActivity;
import com.sarkarinaukri.MainActivity;
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
import com.sarkarinaukri.model.QuestionList;
import com.sarkarinaukri.model.ResultEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewLoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,NavigationView.OnNavigationItemSelectedListener {

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

    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        init();
    }

    private void initFacebook() {
        HelperClass.printKeyHash(NewLoginActivity.this);
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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();
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
                intent = new Intent(NewLoginActivity.this, NewSignupActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.tvLogin:
                HelperClass.hideKeyboard(NewLoginActivity.this);
                authenticateUser();
                break;
            case R.id.tvForgotPassword:
                openForgetPasswordDialog();
                break;
            case R.id.llFacebookLogin:
                new FacebookManager(callBackManager, coordinatorLayout, NewLoginActivity.this).doFaceBookLogin();
                break;
            case R.id.llGmailLogin:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void authenticateUser() {
        String email = etEmailId.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (!ValidationHelperClass.isValidEmail(email)) {
            HelperClass.showSnackBar(coordinatorLayout, "Please enter your valid email id");
        } else if (TextUtils.isEmpty(password)) {
            HelperClass.showSnackBar(coordinatorLayout, "Please enter password");
        } else {
            HelperClass.showProgressDialog(NewLoginActivity.this, "");
            ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
            Call<Newlogin> call = apiInterface.Newlogin(email, password);
            call.enqueue(new Callback<Newlogin>() {
                @Override
                public void onResponse(Call<Newlogin> call, Response<Newlogin> response) {
                    //HelperClass.displayRequestAndResponse(response, null);

                    HelperClass.dismissProgressDialog();
                    if (response.code() == 200) {
                        Newlogin resultEntity = response.body();

                        if (resultEntity.status.equalsIgnoreCase("true")) {
                            PreferenceHelper.WriteSharePrefrence(NewLoginActivity.this, Constant.USER_ID,resultEntity.data.id);
                            PreferenceHelper.savequestionid(NewLoginActivity.this, resultEntity.data.id);
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

       private void ifAppIsLogedIn() {
            MenuItem changePassword = menu.findItem(R.id.nav_change_password);
        MenuItem logout = menu.findItem(R.id.nav_logout);
//
//        if (PreferenceHelper.getSignUpMedium(MainActivity.this).equalsIgnoreCase(UserProfile.SignUpMedium.email.name())) {
//            changePassword.setVisible(true);
//        } else {
//            changePassword.setVisible(false);
//        }

        if (!PreferenceHelper.ReadSharePrefrence(NewLoginActivity.this, Constant.USER_ID).equals("")) {
            changePassword.setVisible(true);
            logout.setVisible(true);
        } else {
            changePassword.setVisible(false);
            logout.setVisible(false);
        }

//        if (TextUtils.isEmpty(PreferenceHelper.getToken(this))) {
//            logout.setVisible(false);
//        } else {
//            logout.setVisible(true);
//        }

        //  logout.setVisible(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            ifAppIsLogedIn();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else if (requestCode == RC_SIGN_IN) {
            ifAppIsLogedIn();
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

    private void signUpFromOurServer(String socialId,final String emailID, String type,final String name) {
        HelperClass.showProgressDialog(NewLoginActivity.this, "");
        ApiInterface apiInterface = ApiclientArchi.getClient().create(ApiInterface.class);
        Call<Newlogin> call = apiInterface.socialsLogin(socialId,emailID,type,name);
        call.enqueue(new Callback<Newlogin>() {
            @Override
            public void onResponse(Call<Newlogin> call, Response<Newlogin> response) {
                HelperClass.dismissProgressDialog();
                if (response.code() == 200) {
                    Newlogin resultEntity = response.body();

                    if (resultEntity.status.equalsIgnoreCase("true")) {
                        //PreferenceHelper.saveProfileData(NewLoginActivity.this, resultEntity.getUserData());
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
//                            updateUI(false);
                                    }
                                });

                        ifAppIsLogedIn();
                        PreferenceHelper.WriteSharePrefrence(NewLoginActivity.this,Constant.USER_ID,resultEntity.data.id);
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
                    Toast.makeText(NewLoginActivity.this, "Enter Email Address", Toast.LENGTH_LONG).show();
                } else {
                    HelperClass.showProgressDialog(NewLoginActivity.this, "Password is sending to you email.");
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
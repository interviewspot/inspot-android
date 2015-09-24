package com.sunrise.interview.interviewspot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.sunrise.interview.interviewspot.R;
import com.sunrise.interview.interviewspot.util.CGlobal;
import com.sunrise.interview.interviewspot.util.socialNetwork.GooglePlusProcessor;

/**
 * Created by jerry on 7/6/2015.
 */
public class Login extends FragmentActivity {
    private String info = "";
    private LoginButton btnFacebook;
    private Button btnGoogle;
    private Button btnLinkedin;
    private TextView tvConnecting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        initUI();
        initialize();
    }

    private void initUI() {
        initGooglePlus_Login();
        initFacebook_Login();
        btnLinkedin = (Button) findViewById(R.id.btn_linkedin_login);
        btnLinkedin.setOnClickListener(new BtnLinkedinOnClickListener());
        tvConnecting = (TextView) findViewById(R.id.tv_connecting_login);
    }

    private void updateButton(boolean isVisible) {
        if (!isVisible) {
            btnFacebook.setVisibility(View.GONE);
            btnGoogle.setVisibility(View.GONE);
            btnLinkedin.setVisibility(View.GONE);
            tvConnecting.setVisibility(View.VISIBLE);
        } else {
            btnFacebook.setVisibility(View.VISIBLE);
            btnGoogle.setVisibility(View.VISIBLE);
            btnLinkedin.setVisibility(View.VISIBLE);
            tvConnecting.setVisibility(View.GONE);
        }
    }

    private void initialize(){
        updateButton(true);
        if(isFacebookLoggedIn()){
            startActivity(new Intent(getApplication(), Home.class));
            finish();
            CGlobal.LOG_ON = CGlobal.LOG_ON_FACEBOOK;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (googlePlusProcessor.onActivityResult(requestCode, resultCode, data)) {
            updateButton(true);
            return;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private class BtnLinkedinOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplication(), Home.class));
            finish();
        }
    }

    //Google+ Login
    private GooglePlusProcessor googlePlusProcessor;

    private void initGooglePlus_Login() {
        googlePlusProcessor = new GooglePlusProcessor(this) {
            @Override
            public void updateUI(boolean isUserSignedIn) {
                if (isUserSignedIn) {
                    CGlobal.LOG_ON = CGlobal.LOG_ON_GOOLE_PLUS;
                    Toast.makeText(getApplicationContext(), "User is login!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplication(), Home.class));
                    finish();
                } else {
                    CGlobal.LOG_ON = CGlobal.LOG_OUT;
                }
            }
        };
        btnGoogle = (Button) findViewById(R.id.btn_google_login);
        btnGoogle.setOnClickListener(new BtnGoogleOnClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googlePlusProcessor.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googlePlusProcessor.onStop();
    }

    private class BtnGoogleOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            googlePlusProcessor.onClick(GooglePlusProcessor.ACTION_SIGN_IN);
            updateButton(false);
        }
    }

    //Facebook Login
    private CallbackManager callbackManager;

    private void initFacebook_Login() {
        callbackManager = CallbackManager.Factory.create();
        btnFacebook = (LoginButton) findViewById(R.id.btn_facebook_login);
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info =
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken();
                Log.d("Facebook", info);
                startActivity(new Intent(getApplication(), Home.class));
                finish();
                CGlobal.LOG_ON = CGlobal.LOG_ON_FACEBOOK;
            }

            @Override
            public void onCancel() {
                info = "Login attempt canceled.";
                Log.d("Facebook", info);
                CGlobal.LOG_ON = CGlobal.LOG_OUT;
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                info = "Login attempt failed.";
                Toast.makeText(getApplicationContext(), info, Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean isFacebookLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}

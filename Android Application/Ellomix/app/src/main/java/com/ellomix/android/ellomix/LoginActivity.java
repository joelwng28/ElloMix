package com.ellomix.android.ellomix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ellomix.android.ellomix.Activities.SignedInActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class LoginActivity extends AppCompatActivity {

    private LoginButton FbLoginButton;
    private CallbackManager callbackManager;
    private Button myLoginButton;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        FbLoginButton = (LoginButton) findViewById(R.id.login_button);
        FbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                goMainScreen();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });


        myLoginButton = (Button) findViewById(R.id.appSignUpButton);

        myLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSignUpScreen();
            }
        });

    }

    private void goMainScreen() {
        Intent i = new Intent(this, SignedInActivity.class);
        startActivity(i);

    }

    protected void goSignUpScreen() {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

}

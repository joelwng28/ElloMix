package com.ellomix.android.ellomix;

        import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

public class SignedInActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if(AccessToken.getCurrentAccessToken() == null)
        {
            goLoginScreen();

        }

    }

    protected void goLoginScreen(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);

    }

    public void logout(View view){
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
}





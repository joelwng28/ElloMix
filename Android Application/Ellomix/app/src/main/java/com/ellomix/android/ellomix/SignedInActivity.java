package com.ellomix.android.ellomix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

public class SignedInActivity extends AppCompatActivity{

    private Button myTimelineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);
        OnClickButtonListener();
    }

       public void OnClickButtonListener(){
        myTimelineButton = (Button) findViewById(R.id.toTimelineButton);
        myTimelineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), TimelineActivity.class);
                startActivity(i);
            }
        });



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





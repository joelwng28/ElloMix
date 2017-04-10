package com.ellomix.android.ellomix.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ellomix.android.ellomix.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignedInActivity extends AppCompatActivity{

    private TextView textViewUserName;
    private Button myTimelineButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);
        OnClickButtonListener();

        textViewUserName = (TextView) findViewById(R.id.textViewUserName);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = mAuth.getCurrentUser();

       // textViewUserName = (TextView) findViewById(R.id.textViewUserName);

        textViewUserName.setText("Welcome " + user.getDisplayName());


    }

    public void OnClickButtonListener(){
        myTimelineButton = (Button) findViewById(R.id.toTimelineButton);
        myTimelineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignedInActivity.this, ScreenSlidePagerActivity.class);
                startActivity(i);
            }
        });


    }

    protected void goLoginScreen(){
        Intent i = new Intent(this, LoginActivity.class);
        finish();
        startActivity(i);
    }

    public void logout(View view){
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        goLoginScreen();
    }
}





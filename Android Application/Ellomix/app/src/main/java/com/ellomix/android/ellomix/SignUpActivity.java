package com.ellomix.android.ellomix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity {

    private Button mySignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mySignUpButton = (Button) findViewById(R.id.appSignUpButton);

        mySignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goConnectSocialMediaScreen();
            }
        });
    }

    protected void goConnectSocialMediaScreen() {
        Intent i = new Intent(this, ConnectSocialMediaActivity.class);
        startActivity(i);
    }
}

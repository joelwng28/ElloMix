package com.ellomix.android.ellomix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SignUpActivity extends AppCompatActivity {

    private Button mySignUpButton;
    private ImageButton myAddPhotoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mySignUpButton = (Button) findViewById(R.id.appSignUpButton);
        myAddPhotoButton = (ImageButton) findViewById(R.id.AddPhotoButton);

        mySignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goConnectSocialMediaScreen();
            }
        });

        myAddPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAddPhotoScreen();
            }
        });

    }

    protected void goConnectSocialMediaScreen() {
        Intent i = new Intent(this, ConnectSocialMediaActivity.class);
        startActivity(i);
    }


    protected void goAddPhotoScreen() {
        Intent i = new Intent(this, AddPhotoActivity.class);
        startActivity(i);
    }
}

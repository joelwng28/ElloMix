package com.ellomix.android.ellomix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ConnectSocialMediaActivity extends AppCompatActivity {

    private Button myContinueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_social_media);

        myContinueButton = (Button) findViewById(R.id.appContinueButton);

        myContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPopulateActivityScreen();
            }
        });

    }



    protected void goToPopulateActivityScreen() {
        Intent i = new Intent(this, PopulateTimelineActivity.class);
        startActivity(i);
    }

}

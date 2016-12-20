package com.ellomix.android.ellomix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PopulateTimelineActivity extends AppCompatActivity {

    private Button myFinishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_populate_timeline);


        myFinishButton = (Button) findViewById(R.id.finishButton);

        myFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignedInScreen();
            }
        });
    }

    protected void goToSignedInScreen() {
        Intent i = new Intent(this, SignedInActivity.class);
        startActivity(i);
    }
}

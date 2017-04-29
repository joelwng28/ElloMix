package com.ellomix.android.ellomix.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ellomix.android.ellomix.FirebaseAPI.FirebaseService;
import com.ellomix.android.ellomix.Model.User;
import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.Services.PlayerLab;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "sign in activity";
    private static final String PREFS = "PrefFile";
    private static final String FIRST_TIME = "firstTime";

    private SharedPreferences preferences;
    private boolean isFirstTime = true;
    //firebase auth stuff
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // TODO: Fix bug where user has friends and is re-installing app
    // TODO: Delete friends from database if user logs out.
    private void prepareNewUser() {

        //If users first time using the app ever, add him to the database
        FirebaseUser firebaseUser = FirebaseService.getFirebaseUser();
        if (firebaseUser != null) {
            String firebaseUid = firebaseUser.getUid();
            String firebaseName = firebaseUser.getDisplayName();
            String name = (firebaseName != null) ? firebaseName
                    : editTextFirstName.getText().toString().trim() + " " +
                    editTextLastName.getText().toString().trim();
            Uri firebasePicture = firebaseUser.getPhotoUrl();
            String photoUrl = (firebasePicture != null)
                    ? firebasePicture.toString()
                    : null;
            User user = new User(firebaseUid,
                    name,
                    photoUrl);
            FirebaseService.pushNewUser(user);
            UserProfileChangeRequest profileUpdates =
                    new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();

            firebaseUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                                goToFriendSearchActivity();
                            }
                        }
                    });
        }

        progressDialog.hide();

    }



    private void goToFriendSearchActivity(){
        Intent i = FriendSearchActivity.newIntent(this, true);
        startActivity(i);
        finish();
    }


    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        //progressDialog.hide();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if(task.isSuccessful()) {
                            prepareNewUser();
                        }
                        else {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                                        Toast.makeText(SignUpActivity.this, R.string.auth_failed,
                                        Toast.LENGTH_SHORT).show();
                        }
                    }


                });
    }
}



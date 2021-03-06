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
import com.ellomix.android.ellomix.Messaging.Chat;
import com.ellomix.android.ellomix.Messaging.Chats;
import com.ellomix.android.ellomix.Model.ChatLab;
import com.ellomix.android.ellomix.Model.FriendLab;
import com.ellomix.android.ellomix.Model.User;
import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.Services.PlayerLab;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    private static final String TAG = "LoginActivity";

    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    private String userId;
    private SharedPreferences preferences;
    private static final String setup = "Setup";
    private static final String returningUser = "ReturningUser";
    private boolean isFirstTime = false;

    //firebase auth stuff
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private ChildEventListener chatIdsEventListener;
    private ChildEventListener chatEventListener;
    private DatabaseReference mDatabase;

    private ChatLab chatLab;
    private HashSet<String> mChatIds;
    private Button buttonRegister;
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;
    private PlayerLab mPlayerLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        mPlayerLab = (PlayerLab) getApplicationContext();
        preferences = getSharedPreferences(setup, 0);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUpScreen();
            }
        });

        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(this);
        //editTextEmail.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail.setOnClickListener(this);
        editTextPassword.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(
                    @NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                   // goMainScreen();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
       // mAuth.addAuthStateListener(mAuthListener);

        callbackManager = CallbackManager.Factory.create();

        facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions("email", "public_profile");
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, error.toString());
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

        if(mAuth.getCurrentUser() != null){
            //profile activity
            // start the profile activity
            //startActivity(new Intent(getApplicationContext(),GenreActivity.class));
            goToHomeScreen();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignIn){
            LogInTheUser();
        }
        if(view == editTextEmail){
            editTextEmail.setText("");
        }
        if(view == editTextPassword){
            editTextPassword.setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //facebook callback

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        progressDialog.setMessage("Logging in Please Wait...");
        progressDialog.show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        progressDialog.setMessage("Logging in...");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            progressDialog.hide();
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        else {

                            //If first time login in with facebook
                            FirebaseUser firebaseUser = FirebaseService.getFirebaseUser();
                            FirebaseService.getUserQuery(firebaseUser.getUid()).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User currentUser = (User) dataSnapshot.getValue(User.class);
                                            // If first time user then add to firebase user database
                                            if (currentUser == null) {
                                                Log.d(TAG, "New Facebook user");
                                                FirebaseUser firebaseUser = FirebaseService.getFirebaseUser();
                                                Uri photoUri = firebaseUser.getPhotoUrl();
                                                User user = new User(firebaseUser.getUid(),
                                                        firebaseUser.getDisplayName(),
                                                        (photoUri != null)
                                                                ? photoUri.toString()
                                                                : null
                                                );
                                                FirebaseService.pushNewUser(user);
                                                progressDialog.dismiss();
                                                goToFriendSearchActivity();
                                            }
                                            else {
                                                Log.d(TAG, "Returning Facebook user");
                                                mPlayerLab.returningUserSetup(new PlayerLab.FirebasePresenter() {
                                                    @Override
                                                    public void finishLoading() {
                                                        progressDialog.dismiss();
                                                        goToHomeScreen();
                                                    }
                                                });
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );
                        }
                    }
                });
    }

    // TODO: Fix bug where user has friends and is re-installing app
    // TODO: Delete friends from database if user logs out.
    private void prepareUser() {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(returningUser, false);
        editor.apply();
    }


    public void toastRegistrationError() {
        Toast.makeText(this, "Log In Error", Toast.LENGTH_SHORT).show();

    }

    private void LogInTheUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;

        }

        progressDialog.setMessage("Signing in Please Wait...");
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "Successful Sign In");
                            mPlayerLab.returningUserSetup(new PlayerLab.FirebasePresenter() {
                                @Override
                                public void finishLoading() {
                                    progressDialog.dismiss();
                                    goToHomeScreen();
                                }
                            });
                        }
                        else {
                            toastRegistrationError();
                            progressDialog.hide();
                        }
                    }
                });
    }


    private void goToSignUpScreen(){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
        finish();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        mAuthListener = null;

    }

    private void goToHomeScreen(){
        //send to the Home Screen
        prepareUser();
//        isFirstTime = preferences.getBoolean(returningUser, true);
//        progressDialog.dismiss();
//        if (isFirstTime) {
//
//        }
        Intent i = new Intent(this, ScreenSlidePagerActivity.class);
        startActivity(i);
        finish();
    }

    private void goToFriendSearchActivity(){
        Intent i = new Intent(this, FriendSearchActivity.class);
        startActivity(i);
        finish();
    }



}

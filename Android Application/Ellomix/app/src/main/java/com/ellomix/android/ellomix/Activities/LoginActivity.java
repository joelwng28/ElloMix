package com.ellomix.android.ellomix.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ellomix.android.ellomix.FirebaseAPI.FirebaseService;
import com.ellomix.android.ellomix.Model.FriendLab;
import com.ellomix.android.ellomix.Model.User;
import com.ellomix.android.ellomix.R;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String PREFS = "PrefFile";
    private static final String FIRST_TIME = "firstTime";

    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    private String userId;
    private SharedPreferences preferences;
    private boolean isFirstTime = false;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(
                    @NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    goMainScreen();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            FirebaseUser firebaseUser = FirebaseService.getFirebaseUser();
                            FirebaseService.getUserQuery(firebaseUser.getUid()).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User currentUser = (User) dataSnapshot.getValue(User.class);
                                            // If first time user then add to firebase user database
                                            if (currentUser == null) {
                                                FirebaseUser firebaseUser = FirebaseService.getFirebaseUser();
                                                User user = new User(firebaseUser.getUid(),
                                                        firebaseUser.getDisplayName(),
                                                        firebaseUser.getPhotoUrl().toString());
                                                FirebaseService.pushNewUser(user);
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
    private void prepareUser() {
        // Need to check if this is the first time they open the app, if so download all the friends into the phone
        preferences = getSharedPreferences(PREFS, 0);
        isFirstTime = preferences.getBoolean(FIRST_TIME, true);

        if (isFirstTime) {
            //Download friends from firebase
            FirebaseService.getMainUserFollowingQuery().addValueEventListener(
                    new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                        // Get the userId and with that search for the user in firebase
                        String friendId = child.getKey();

                        FirebaseService.getUserQuery(friendId).addValueEventListener(
                                new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User friend = (User) dataSnapshot.getValue(User.class);
                                if (friend != null) {
                                    FriendLab friendLab = FriendLab.get(getApplicationContext());
                                    friendLab.addFriend(friend);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void goMainScreen(){
        Intent i;
        prepareUser();
        preferences = getSharedPreferences(PREFS, 0);
        isFirstTime = preferences.getBoolean(FIRST_TIME, true);
        if (isFirstTime) {
            i = new Intent(this, FriendSearchActivity.class);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(FIRST_TIME, false);
            editor.apply();
        }
        else {
            i = new Intent(this, ScreenSlidePagerActivity.class);
        }

        startActivity(i);
        finish();
    }

}

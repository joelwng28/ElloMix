package com.ellomix.android.ellomix.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ellomix.android.ellomix.Activities.LoginActivity;
import com.ellomix.android.ellomix.Activities.NewMessageActivity;
import com.ellomix.android.ellomix.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by abetorres on 12/17/16.
 */

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private Button logOutButton;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        logOutButton = (Button) v.findViewById(R.id.log_out_button);
        logOutButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginManager.getInstance().logOut();
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        getActivity().finish();
                        startActivity(i);
                    }
                }
        );
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.chat_feed_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_compose:
                Intent intent = new Intent(getContext(), NewMessageActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

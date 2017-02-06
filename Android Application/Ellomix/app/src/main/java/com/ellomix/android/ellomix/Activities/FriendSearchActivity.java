package com.ellomix.android.ellomix.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ellomix.android.ellomix.FirebaseAPI.FirebaseService;
import com.ellomix.android.ellomix.Model.FriendLab;
import com.ellomix.android.ellomix.Model.User;
import com.ellomix.android.ellomix.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendSearchActivity extends AppCompatActivity {

    private RecyclerView mFriendListRecyclerView;
    private TextView mFriendAddedTextView;
    private String userId;
    private List<User> mUserList;
    private Set<User> mFriendSelectedSet;
    private FriendLab friendLab;
    private FriendAdapter mAdapter;
    private static final String TAG = "FriendSearchActivity";
    private StringBuffer mFriendsSelectedStringBuffer;

    //Firebase instance variable
    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);
        mFriendListRecyclerView = (RecyclerView) findViewById(R.id.friend_list_recycler_view);
        mFriendListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFriendAddedTextView = (TextView) findViewById(R.id.friend_added_text_view);
        FirebaseUser mUserProfile = FirebaseService.getFirebaseUser();
        userId = mUserProfile.getUid();
        mUserList = new ArrayList<>();
        friendLab = FriendLab.get(this);
        mFriendsSelectedStringBuffer = new StringBuffer();
        mFriendSelectedSet = new HashSet<>();

        // Add friends to the data base

        // New child entries
        mFirebaseDatabaseReference = FirebaseService.getFirebaseDatabase();
        mFirebaseDatabaseReference.child("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // for each child of the users
                        for (DataSnapshot friends : dataSnapshot.getChildren()) {
                            User friend = (User) friends.getValue(User.class);
                            if (!friend.getId().equals(userId) && friendLab.getFriend(friend.getId()) == null) {
                                mUserList.add(friend);
                            }
                        }
                        updateUI();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        // Set up adapter
        if (mAdapter == null) {
            mAdapter = new FriendAdapter(mUserList);
            mFriendListRecyclerView.setAdapter(mAdapter);
        }
        else {
            Log.d(TAG, "update list");
            mAdapter.setFriends(mUserList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_friends_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_friends:
                Iterator it = mFriendSelectedSet.iterator();

                while (it.hasNext()) {
                    User friend = (User) it.next();
                    friendLab.addFriend(friend);
                    FirebaseService.addNewFriend(userId, friend);
                }

                Intent intent = new Intent(this, ScreenSlidePagerActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {

        private List<User> mFriends;

        public FriendAdapter(List<User> friends) {
            mFriends = friends;
        }

        @Override
        public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(FriendSearchActivity.this);
            View view = inflater.inflate(R.layout.friend_item, parent, false);


            return new FriendViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FriendViewHolder holder, int position) {
            holder.bindItem(FriendSearchActivity.this, mFriends.get(position));
        }

        public void setFriends(List<User> friends) {
            mFriends = friends;
        }

        @Override
        public int getItemCount() {
            return mFriends.size();
        }

    }

    public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private User mFriend;
        private TextView mNameTextView;
        private CircleImageView mFriendImageView;
        private boolean isSelected;
        private int mStartPos;
        private int mEndPos;

        public FriendViewHolder(View v) {
            super(v);
            mNameTextView = (TextView) itemView.findViewById(R.id.friend_text_view);
            mFriendImageView = (CircleImageView) itemView.findViewById(R.id.friend_image_view);
            isSelected = false;
            itemView.setOnClickListener(this);
        }

        public void bindItem(Context context, User friend) {
            mFriend = friend;
            mNameTextView.setText(mFriend.getName());
            if (mFriend.getPhotoUrl() == null) {
                mFriendImageView
                        .setImageDrawable(ContextCompat
                                .getDrawable(context,
                                        R.drawable.ic_account_circle_black_36dp));
            } else {
                Glide.with(context)
                        .load(mFriend.getPhotoUrl())
                        .into(mFriendImageView);
            }
        }

        @Override
        public void onClick(View v) {

            if(!isSelected) {
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                mStartPos = mFriendsSelectedStringBuffer.length();
                if (mStartPos == 0) {
                    mFriendsSelectedStringBuffer.append(mFriend.getName());
                }
                else {
                    mFriendsSelectedStringBuffer.append(", " + mFriend.getName());
                }

                mFriendAddedTextView.setText(mFriendsSelectedStringBuffer.toString());
                mFriendSelectedSet.add(mFriend);
            }
            else {
                v.setBackgroundColor(Color.TRANSPARENT);
                mFriendSelectedSet.remove(mFriend);

                int bufferSize = mFriendsSelectedStringBuffer.length();
                int nameSize = mFriend.getName().length();

                mFriendsSelectedStringBuffer.delete(mStartPos,  mStartPos + nameSize + 2);

                if (mFriendSelectedSet.size() == 0) {
                    mFriendsSelectedStringBuffer.delete(0, bufferSize);
                }

                mFriendAddedTextView.setText(mFriendsSelectedStringBuffer.toString());

            }
            isSelected = !isSelected;
        }
    }
}

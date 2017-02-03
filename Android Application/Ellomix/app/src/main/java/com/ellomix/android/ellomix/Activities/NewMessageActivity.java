package com.ellomix.android.ellomix.Activities;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ellomix.android.ellomix.FirebaseAPI.FirebaseService;
import com.ellomix.android.ellomix.Fragments.ChatListFragment;
import com.ellomix.android.ellomix.Messaging.Chat;
import com.ellomix.android.ellomix.Model.FriendLab;
import com.ellomix.android.ellomix.Model.User;
import com.ellomix.android.ellomix.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewMessageActivity extends AppCompatActivity  {

    private static final String TAG = "NewMessageActivity";
    private List<User> mFollowingUsers;
    private List<User> mGroupMembersList;
    private RecyclerView mFollowingRecyclerView;
    private TextView mGroupMembersTextView;
    private StringBuffer mGroupMembersStrBuffer;
    private LinearLayoutManager mLinearLayoutManager;
    private FollowingAdapter mAdapter;

    // Firebase Instance Variable
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<User, FollowingHolder>
            mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseService.getFirebaseDatabase();
        mFollowingUsers = new ArrayList<>();
        mGroupMembersList = new ArrayList<>();
        mGroupMembersStrBuffer = new StringBuffer();

        mGroupMembersTextView = (TextView) findViewById(R.id.group_member_text_view);
        mFollowingRecyclerView = (RecyclerView) findViewById(R.id.following_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mFollowingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();

//        mFirebaseAdapter = new FirebaseRecyclerAdapter<User, FollowingHolder>(
//                User.class,
//                R.layout.friend_item,
//                FollowingHolder.class,
//                mDatabase.child("Users")) {
//
//            @Override
//            protected void populateViewHolder(FollowingHolder viewHolder, final User model, int position) {
//
//                viewHolder.bindItem(model);
//                if (model.getPhotoUrl() != null && !model.getPhotoUrl().equals("")) {
//                    Picasso.with(NewMessageActivity.this).load(model.getPhotoUrl())
//                            .placeholder(R.drawable.ic_profile)
//                            .into(viewHolder.mFriendImageView);
//                }
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//                    }
//                });
//            }
//        };
//
//        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                int chatCount = mFirebaseAdapter.getItemCount();
//                int lastVisiblePosition =
//                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
//                // If the recycler view is initially being loaded or the
//                // user is at the bottom of the list, scroll to the bottom
//                // of the list to show the newly added message.
//                if (lastVisiblePosition == -1 ||
//                        (positionStart >= (chatCount - 1) &&
//                                lastVisiblePosition == (positionStart - 1))) {
//                    mFollowingRecyclerView.scrollToPosition(positionStart);
//                }
//            }
//        });

        //mFollowingRecyclerView.setAdapter(mFirebaseAdapter);
    }

    private void updateUI() {
        FriendLab friendLab = FriendLab.get(this);
        List<User> friendsList = friendLab.getFriends();

        if (mAdapter == null) {
            mAdapter = new FollowingAdapter(friendsList);
            mFollowingRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setUsers(friendsList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_message_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_members:
                if (mGroupMembersList.size() == 0) {
                    Toast.makeText(this, "No members have been added", Toast.LENGTH_LONG).show();
                }
                else {
                    // Add listener to add chat into other group member chat list
                    Chat chat = new Chat();

                    //Need to find out here if its a one-to-one or group chat
                    int size = mGroupMembersList.size();
                    if (size == 1) {
                        chat.setFromRecipient(mGroupMembersList.get(0).getName());
                    }
                    else {
                        //TODO: Let user decide on a group name
                        //TODO: Else just show first 2 names and then + "how many remaining"
                    }
                    String chatId = FirebaseService.pushNewChat(chat);
                    chat.setId(chatId);

                    for (int i = 0; i < size; i++) {
                        User groupMember = mGroupMembersList.get(i);
                        FirebaseService.addChatIdToUser(groupMember, chat);
                        groupMember.addChat(chatId);
                    }
                    // chat id to the main user
                    FirebaseUser firebaseUser = FirebaseService.getFirebaseUser();
                    FirebaseService.addChatIdToUser(firebaseUser.getUid().toString(), chat);
                    // start chat activity
                    Intent intent = ChatActivity.newIntent(this, chatId);
                    startActivity(intent);
                    finish();

                }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    // return - true if the user already exist in the list
    // false otherwise
    public boolean checkForRepetition(User user, List<User> groupMembers) {
        if (groupMembers == null || groupMembers.size() == 0 || groupMembers.size() == mFollowingUsers.size()) {
            //Nothing to check
            return true;
        }
        else if (user == null) {
            return true;
        }
        else {
            int size = groupMembers.size();
            for (int i = 0; i < size; i++) {
                if (groupMembers.get(i).getId().equals(user.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void generateModel() {


//        });
//        Log.d(TAG, "generate timeline");
//        String[] usersDemo = {"Akshay Vyas"};
//        String[] messagesDemo = {
//                "DOPE",
//                "Some sick beats, everyone check it ok",
//                "New favourite song",
//                "So mellow",
//                "Chillax"
//        };
//
//        // Testing Akshay user
//        User testSubject = new User("Akshay Vyas");
//        testSubject.setId("W2uNXKfzxkY7pyBOsJix25OtE0m2");
//        testSubject.setPhotoUrl("https://scontent.xx.fbcdn.net/v/t1.0-1/p100x100/13307453_10208087943429112_3240435931001222578_n.jpg?oh=00f650c9ea57d38bcacabd5d7dc1bacd&oe=591438C7");
//        mFollowingUsers.add(testSubject);
//
//
//
////        for (int i = 0; i < usersDemo.length; i++) {
////            User user = new User(usersDemo[i]);
////            user.setId();
////            mFollowingUsers.add(user);
////        }
//
//
//        // Test saving my data
//        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
//        User myProfile = new User(
//                firebaseUser.getDisplayName(),
//                messagesDemo[0],
//                0,
//                0,
//                firebaseUser.getPhotoUrl().toString());
//        myProfile.setId(firebaseUser.getUid());
//
//        FirebaseService.pushNewUser(myProfile);
    }

    public class FollowingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public User mUser;
        public CircleImageView mFriendImageView;
        public TextView mFriendNameTextView;

        public FollowingHolder(View itemView) {
            super(itemView);
            mFriendImageView = (CircleImageView) itemView.findViewById(R.id.friend_image_view);
            mFriendNameTextView = (TextView) itemView.findViewById(R.id.friend_text_view);
            itemView.setOnClickListener(this);
        }

        public void bindItem(User user) {
            mUser = user;
            mFriendNameTextView.setText(mUser.getName());
            if (mUser.getPhotoUrl() == null) {
                mFriendImageView
                        .setImageDrawable(ContextCompat
                                .getDrawable(NewMessageActivity.this,
                                        R.drawable.ic_account_circle_black_36dp));
            } else {
                Glide.with(NewMessageActivity.this)
                        .load(mUser.getPhotoUrl())
                        .into(mFriendImageView);
            }
        }

        @Override
        public void onClick(View v) {
            if (mGroupMembersList.size() == 0) {
                //Toast.makeText(NewMessageActivity.this, "Friend Added: " + model.getName() + ", id: " + mUser.getId(), Toast.LENGTH_SHORT).show();
                mGroupMembersList.add(mUser);
                mGroupMembersStrBuffer.append(mUser.getName());
                mGroupMembersTextView.setText(mGroupMembersStrBuffer.toString());
            }

            else if (!checkForRepetition(mUser, mGroupMembersList)) {
                //Toast.makeText(NewMessageActivity.this, "Friend Added: " + mUser.getName() + ", id: " + mUser.getId(), Toast.LENGTH_SHORT).show();
                mGroupMembersList.add(mUser);
                mGroupMembersStrBuffer.append("; " + mUser.getName());
                mGroupMembersTextView.setText(mGroupMembersStrBuffer.toString());
            }
        }
    }

    private class FollowingAdapter extends RecyclerView.Adapter<FollowingHolder> {

        private List<User> mUsers;

        public FollowingAdapter(List<User> users) {
            mUsers = users;
        }

        @Override
        public FollowingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(NewMessageActivity.this);
            View view = inflater.inflate(R.layout.friend_item, parent, false);

            return new FollowingHolder(view);
        }

        @Override
        public void onBindViewHolder(FollowingHolder holder, int position) {
            holder.bindItem(mUsers.get(position));
        }

        public void setUsers(List<User> users) {
            mUsers = users;
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }
    }

}

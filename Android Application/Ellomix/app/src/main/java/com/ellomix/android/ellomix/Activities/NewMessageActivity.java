package com.ellomix.android.ellomix.Activities;

import android.content.Intent;
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

import com.ellomix.android.ellomix.FirebaseAPI.FirebaseService;
import com.ellomix.android.ellomix.Messaging.Chat;
import com.ellomix.android.ellomix.Model.User;
import com.ellomix.android.ellomix.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

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

    // Firebase Instance Variable
    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFollowingUsers = new ArrayList<>();
        mGroupMembersList = new ArrayList<>();
        mGroupMembersStrBuffer = new StringBuffer();

        mGroupMembersTextView = (TextView) findViewById(R.id.group_member_text_view);
        mFollowingRecyclerView = (RecyclerView) findViewById(R.id.following_recycler_view);
        mFollowingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        generateModel();
        mFollowingRecyclerView.setAdapter(new FollowingAdapter(mFollowingUsers));
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
                    // TODO: Push chat Id into group member chat Id list
                    // Add listener to add chat into other group member chat list

                    Chat chat = new Chat();
                    String chatId = FirebaseService.pushNewChat(chat);
                    chat.setId(chatId);

                    int size = mGroupMembersList.size();
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

    public void generateModel() {
        Log.d(TAG, "generate timeline");
        String[] usersDemo = {"Akshay Vyas"};
        String[] messagesDemo = {
                "DOPE",
                "Some sick beats, everyone check it ok",
                "New favourite song",
                "So mellow",
                "Chillax"
        };

        // Testing Akshay user
        User testSubject = new User("Akshay Vyas");
        testSubject.setId("W2uNXKfzxkY7pyBOsJix25OtE0m2");
        testSubject.setPhotoUrl("https://scontent.xx.fbcdn.net/v/t1.0-1/p100x100/13307453_10208087943429112_3240435931001222578_n.jpg?oh=00f650c9ea57d38bcacabd5d7dc1bacd&oe=591438C7");
        mFollowingUsers.add(testSubject);

//        for (int i = 0; i < usersDemo.length; i++) {
//            User user = new User(usersDemo[i]);
//            user.setId();
//            mFollowingUsers.add(user);
//        }


        // Test saving my data
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        User myProfile = new User(
                firebaseUser.getDisplayName(),
                messagesDemo[0],
                0,
                0,
                firebaseUser.getPhotoUrl().toString());
        myProfile.setId(firebaseUser.getUid());

        FirebaseService.pushNewUser(myProfile);
    }

    private class FollowingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private User mUser;
        private CircleImageView mFriendImageView;
        private TextView mFriendNameTextView;

        public FollowingHolder(View itemView) {
            super(itemView);
            mFriendImageView = (CircleImageView) itemView.findViewById(R.id.friend_image_view);
            mFriendNameTextView = (TextView) itemView.findViewById(R.id.friend_text_view);
            itemView.setOnClickListener(this);
        }

        public void bindItem(User user) {
            mUser = user;
//            Picasso.with(getApplicationContext()).load(mUser.getPhotoUrl())
//                    .placeholder(R.drawable.ic_profile)
//                    .into(mFriendImageView);
            mFriendNameTextView.setText(mUser.getName());
        }

        // return - true if the user already exist in the list
        // false otherwise
        public boolean checkForRepetition(List<User> groupMembers) {
            if (groupMembers == null || groupMembers.size() == 0 || groupMembers.size() == mFollowingUsers.size()) {
                //Nothing to check
                return true;
            }
            else if (mUser == null) {
                return true;
            }
            else {
                int size = groupMembers.size();
                for (int i = 0; i < size; i++) {
                    if (groupMembers.get(i).getId().equals(mUser.getId())) {
                        return true;
                    }
                }
            }
            return false;
        }


        @Override
        public void onClick(View v) {
            // Add the name to the list
            //TODO: Push member bubble on "To:" list

            if (mGroupMembersList.size() == 0) {
                //Toast.makeText(NewMessageActivity.this, "Friend Added: " + mUser.getName() + ", id: " + mUser.getId(), Toast.LENGTH_SHORT).show();
                mGroupMembersList.add(mUser);
                mGroupMembersStrBuffer.append(mUser.getName());
                mGroupMembersTextView.setText(mGroupMembersStrBuffer.toString());
            }

            else if (!checkForRepetition(mGroupMembersList)) {
                //Toast.makeText(NewMessageActivity.this, "Friend Added: " + mUser.getName() + ", id: " + mUser.getId(), Toast.LENGTH_SHORT).show();
                mGroupMembersList.add(mUser);
                mGroupMembersStrBuffer.append("; " + mUser.getName());
                mGroupMembersTextView.setText(mGroupMembersStrBuffer.toString());
            }

            Log.d(TAG, mGroupMembersList.toString());

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

        @Override
        public int getItemCount() {
            return mUsers.size();
        }
    }

}

package com.ellomix.android.ellomix.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ellomix.android.ellomix.Model.User;
import com.ellomix.android.ellomix.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewMessageActivity extends AppCompatActivity  {

    private static final String TAG = "NewMessageActivity";
    private List<User> mFollowingUsers;
    private List<User> mGroupMembersList;
    private RecyclerView mFollowingRecyclerView;
    private TextView mGroupMembersTextView;
    private StringBuffer mGroupMembersStrBuffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        mFollowingUsers = new ArrayList<>();
        mGroupMembersList = new ArrayList<>();
        mGroupMembersStrBuffer = new StringBuffer();

        mGroupMembersTextView = (TextView) findViewById(R.id.group_member_text_view);
        mFollowingRecyclerView = (RecyclerView) findViewById(R.id.following_recycler_view);
        mFollowingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        generateModel();
        mFollowingRecyclerView.setAdapter(new FollowingAdapter(mFollowingUsers));

    }

    public void generateModel() {
        Log.d(TAG, "generate timeline");
        String[] usersDemo = {"Abe Torres", "Neil Tanner", "Akshay", "Elena Carrasco", "Micah Peoples"};
        String[] messagesDemo = {
                "DOPE",
                "Some sick beats, everyone check it ok",
                "New favourite song",
                "So mellow",
                "Chillax"
        };

        for (int i = 0; i < 5; i++) {
            User user = new User(usersDemo[i]);
            user.setId("" + i);
            mFollowingUsers.add(user);
        }


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
                Toast.makeText(NewMessageActivity.this, "Friend Added: " + mUser.getName() + ", id: " + mUser.getId(), Toast.LENGTH_SHORT).show();

                mGroupMembersList.add(mUser);
                mGroupMembersStrBuffer.append(mUser.getName());
                mGroupMembersTextView.setText(mGroupMembersStrBuffer.toString());
            }

            else if (!checkForRepetition(mGroupMembersList)) {
                Toast.makeText(NewMessageActivity.this, "Friend Added: " + mUser.getName() + ", id: " + mUser.getId(), Toast.LENGTH_SHORT).show();
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

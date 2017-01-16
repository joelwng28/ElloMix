package com.ellomix.android.ellomix.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ellomix.android.ellomix.Activities.ChatActivity;
import com.ellomix.android.ellomix.Messaging.Chat;
import com.ellomix.android.ellomix.Messaging.Chats;
import com.ellomix.android.ellomix.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by abetorres on 12/2/16.
 */

public class ChatListFragment extends Fragment {

    private static final String TAG = "ChatListFragment";
    private RecyclerView mChatFeedRecyclerView;
    private CircleImageView mGroupChatImageView;
    private TextView mGroupChatName;
    private TextView mGroupRecentMessage;
    private Chats mChats;
    private ChatAdapter mAdapter;

    // Firebase Instance variable
    private DatabaseReference mDatabase;

    public static ChatListFragment newInstance() {
        return new ChatListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChats = new Chats("1", "Abe");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Chats").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // A new chat has been added, add it to the displayed list
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // Got a new response, update it on the displayed list
                Chat chat = dataSnapshot.getValue(Chat.class);
                String chatKey = dataSnapshot.getKey();
                mChats.updateChat(chatKey, chat);
                updateUI();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Chat was deleted
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_feed, container, false);

        mChatFeedRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_chat_feed_recycler_view);
        mChatFeedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGroupChatImageView = (CircleImageView) v.findViewById(R.id.receipt_image_view);
        mGroupChatName = (TextView) v.findViewById(R.id.from_text_view);
        mGroupRecentMessage = (TextView) v.findViewById(R.id.recent_message_text_view);

        generateGroupChat();
        generateModel();

        updateUI();

        return v;
    }

    private void generateGroupChat() {
        mGroupChatName.setText("Ellomix group");
        mGroupRecentMessage.setText("Day 1 World Music - Sweden");
    }

    public void generateModel() {

        for (int i = 0; i < 2 ; i++) {
            Chat chat = new Chat("" + i);
            mDatabase.child("Chats").child("" + i).setValue(chat);
            mChats.addChat(chat);
        }

        Log.d(TAG, "current chats: " + mChats.getChats().size());
        updateUI();
    }

    public void updateUI() {

        List<Chat> chats = mChats.getChats();
        if (mAdapter == null) {
            mAdapter = new ChatAdapter(chats);
            mChatFeedRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setChats(chats);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class ChatHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{

        private Chat mChat;
        private TextView mFromTextView;
        private TextView mRecentMessageTextView;

        public ChatHolder(View view) {
            super(view);

            mFromTextView = (TextView) view.findViewById(R.id.from_text_view);
            mRecentMessageTextView = (TextView) view.findViewById(R.id.recent_message_text_view);
            view.setOnClickListener(this);
        }

        public void bindChat(Chat chat) {
            mChat = chat;
            mFromTextView.setText(mChat.getFromRecipient());
            mRecentMessageTextView.setText(mChat.getMostRecentMessage());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
            Intent i = ChatActivity.newIntent(getContext(), mChat.getId());
            startActivity(i);
        }
    }

    private class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {

        private List<Chat> mChats;

        public ChatAdapter(List<Chat> chats) {
            this.mChats = chats;
        }

        @Override
        public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.chat_feed_item, parent, false);
            return new ChatHolder(view);
        }

        @Override
        public void onBindViewHolder(ChatHolder holder, int position) {
            Chat chat = this.mChats.get(position);
            holder.bindChat(chat);
        }

        @Override
        public int getItemCount() {
            return this.mChats.size();
        }

        public void setChats(List<Chat> chats) {
            this.mChats = chats;
        }
    }
}

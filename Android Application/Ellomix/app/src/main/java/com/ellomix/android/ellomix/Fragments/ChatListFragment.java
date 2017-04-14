package com.ellomix.android.ellomix.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.ellomix.android.ellomix.Activities.ChatActivity;
import com.ellomix.android.ellomix.Activities.NewMessageActivity;
import com.ellomix.android.ellomix.FirebaseAPI.FirebaseService;
import com.ellomix.android.ellomix.Messaging.Chat;
import com.ellomix.android.ellomix.Messaging.Chats;
import com.ellomix.android.ellomix.Model.ChatLab;
import com.ellomix.android.ellomix.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by abetorres on 12/2/16.
 */

public class ChatListFragment extends Fragment {

    private static final String TAG = "ChatListFragment";
    private RecyclerView mChatFeedRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private static final String CHATS = "Chats";
    private CircleImageView mGroupChatImageView;
    private TextView mGroupChatName;
    private TextView mGroupRecentMessage;
    private Chats mChats;
    private List<String> mChatIds;
    private ChatAdapter mAdapter;

    // Firebase Instance variable
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Chat, ChatHolder>
            mFirebaseAdapter;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private ChildEventListener chatIdsEventListener;
    private ChildEventListener chatEventListener;
    private ChatLab chatLab;

    public static ChatListFragment newInstance() {
        return new ChatListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChats = new Chats();
        mChatIds = new ArrayList<>();

        if (getActivity() == null) {
            Log.e(TAG, "context is null");
        }
        else {
            chatLab = ChatLab.get(getActivity());
        }


        mFirebaseUser = FirebaseService.getFirebaseUser();
        mDatabase = FirebaseService.getFirebaseDatabase();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_feed, container, false);

        mChatFeedRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_chat_feed_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mChatFeedRecyclerView.setLayoutManager(mLinearLayoutManager);
        mGroupChatImageView = (CircleImageView) v.findViewById(R.id.receipt_image_view);
        mGroupChatName = (TextView) v.findViewById(R.id.from_text_view);
        mGroupRecentMessage = (TextView) v.findViewById(R.id.recent_message_text_view);

        generateGroupChat();

        chatIdsEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Add new chat to database
                String chatId = dataSnapshot.getKey();
                Chat chat = chatLab.getChat(chatId);

                // if the chat does not exist in the database, add it
                if (chat == null) {
                    Log.d(TAG, "adding new chat id");
                    chat = new Chat(chatId);
                    chatLab.addChat(chat);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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
        };

        if (mFirebaseUser != null) {
            // Checks for new chats
            mDatabase.child("Users")
                    .child(mFirebaseUser.getUid())
                    .child("chatIds")
                    .addChildEventListener(chatIdsEventListener);
        }

        /*TODO: From recipient can be either one of 3 cases
        case 1: If no group name, then every else in the group
        case 2: Else group name
        */
        chatEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String chatId = (String) dataSnapshot.getKey();
                Chat firebaseChat = (Chat) dataSnapshot.getValue(Chat.class);
                Chat chat = chatLab.getChat(firebaseChat.getId());

                // if the chat does exist, it means we update it in the database
                if (chat != null) {
                    Log.d(TAG, "adding to chat feed");
                    chatLab.updateChat(firebaseChat);
                    updateUI();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String chatId = (String) dataSnapshot.getKey();
                Chat firebaseChat = (Chat) dataSnapshot.getValue(Chat.class);
                Chat chat = chatLab.getChat(firebaseChat.getId());

                // if the chat does exist, it means we update it in the database
                if (chat != null) {
                    Log.d(TAG, "updating chat feed");
                    chatLab.updateChat(firebaseChat);
                    updateUI();
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Do nothing
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // Do nothing
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        // Check for changes in current chats
        mDatabase.child("Chats")
                .addChildEventListener(chatEventListener);

//        mFirebaseAdapter = new FirebaseRecyclerAdapter<Chat, ChatHolder>(
//                Chat.class,
//                R.layout.chat_feed_item,
//                ChatHolder.class,
//                mDatabase.child(CHATS)) {
//            @Override
//            protected void populateViewHolder(ChatHolder viewHolder, final Chat model, int position) {
//                //viewHolder.mFromTextView.setText(model.getFromRecipient());
//                //viewHolder.mRecentMessageTextView.setText(model.getMostRecentMessage());
//                viewHolder.bindChat(model);
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //Toast.makeText(getActivity(), "chat Id: " + model.getId(), Toast.LENGTH_SHORT).show();
//                        Intent i = ChatActivity.newIntent(getContext(), model.getId());
//                        startActivity(i);
//                    }
//                });
//            }
//
//            @Override
//            protected Chat parseSnapshot(DataSnapshot snapshot) {
//                Chat chat = super.parseSnapshot(snapshot);
//                if (chat != null) {
//                    chat.setId(snapshot.getKey());
//                }
//
//                return chat;
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
//                    mChatFeedRecyclerView.scrollToPosition(positionStart);
//                }
//            }
//        });
//
//        mChatFeedRecyclerView.setAdapter(mFirebaseAdapter);
        //generateModel();

        updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (mFirebaseUser != null) {
            mDatabase.child("Users")
                    .child(mFirebaseUser.getUid())
                    .child("chatIds")
                    .removeEventListener(chatIdsEventListener);
            mDatabase.child("Chats")
                    .removeEventListener(chatEventListener);
        }
    }

    private void updateUI() {
        ChatLab chatLab = ChatLab.get(getActivity());
        List<Chat> chats = chatLab.getChats();

        // Set up adapter
        if (mAdapter == null) {
            mAdapter = new ChatAdapter(chats);
            mChatFeedRecyclerView.setAdapter(mAdapter);
        }
        else {
            Log.d(TAG, "update list");
            mAdapter.setChats(chats);
            mAdapter.notifyDataSetChanged();
        }

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

    private void generateGroupChat() {
        mGroupChatName.setText("Ellomix group");
        mGroupRecentMessage.setText("Day 1 World Music - Sweden");
    }

    public void generateModel() {

        for (int i = 0; i < 2 ; i++) {
            String id = UUID.randomUUID().toString();
            Chat chat = new Chat(id);
            mDatabase.child("Chats").child(id).setValue(chat);
            mChats.addChat(chat);
        }

        Log.d(TAG, "current chats: " + mChats.getChats().size());
    }

    public class ChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mFromTextView;
        public TextView mRecentMessageTextView;
        private Chat mChat;

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
            Intent i = ChatActivity.newIntent(getActivity(), mChat.getId());
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

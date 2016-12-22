package com.ellomix.android.ellomix.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ellomix.android.ellomix.Messaging.Chat;
import com.ellomix.android.ellomix.R;

import java.util.List;

/**
 * Created by abetorres on 12/2/16.
 */

public class ChatListFragment extends Fragment {

    private static final String TAG = "ChatListFragment";
    private RecyclerView mChatFeedRecyclerView;

    public static ChatListFragment newInstance() {
        return new ChatListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_feed, container, false);

        mChatFeedRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_chat_feed_recycler_view);
        mChatFeedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    private class ChatHolder extends RecyclerView.ViewHolder {

        private Chat mChat;
        private TextView mFromTextView;
        private TextView mRecentMessageTextView;

        public ChatHolder(View view) {
            super(view);

            mFromTextView = (TextView) view.findViewById(R.id.from_text_view);
            mRecentMessageTextView = (TextView) view.findViewById(R.id.recentMessageTextView);
        }

        public void bindChat(Chat chat) {
            mChat = chat;
            mFromTextView.setText(mChat.getFromRecipient());
            mRecentMessageTextView.setText(mChat.getMostRecentMessage());
        }
    }

    private class ChatAdapter extends RecyclerView.Adapter<ChatHolder> {

        private List<Chat> mChats;

        public ChatAdapter(List<Chat> chats) {
            mChats = chats;
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
            Chat chat = mChats.get(position);
            holder.bindChat(chat);
        }

        @Override
        public int getItemCount() {
            return mChats.size();
        }
    }
}

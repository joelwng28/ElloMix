package com.ellomix.android.ellomix.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.ellomix.android.ellomix.Fragments.ChatFragment;

import java.util.UUID;

public class ChatActivity extends SingleFragmentActivity {

    private static final String EXTRA_CHAT_ID =
            "com.ellomix.android.ellomix.chat_id";
    private static final String TAG = "ChatActivity";
    private String chatId;


    public static Intent newIntent(Context packageContext, UUID chatId) {
        Intent intent = new Intent(packageContext, ChatActivity.class);
        intent.putExtra(EXTRA_CHAT_ID, chatId);
        return intent;
    }

    public static Intent newIntent(Context packageContext, String chatId) {
        Intent intent = new Intent(packageContext, ChatActivity.class);
        intent.putExtra(EXTRA_CHAT_ID, chatId);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        chatId = (String) getIntent().getStringExtra(EXTRA_CHAT_ID);
    }

    @Override
    protected Fragment createFragment() {
        chatId = (String) getIntent().getStringExtra(EXTRA_CHAT_ID);
        if (chatId != null) {
            Log.e(TAG, "Chat Id: " + chatId);
            return ChatFragment.newInstance(chatId);
        }
        else {
            Log.e(TAG, "Chat not passing");
            return ChatFragment.newInstance();
        }
    }
}

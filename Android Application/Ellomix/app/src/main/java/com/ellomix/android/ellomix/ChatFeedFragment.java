package com.ellomix.android.ellomix;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by abetorres on 12/2/16.
 */

public class ChatFeedFragment extends Fragment {

    private static final String TAG = "ChatFeedFragment";
    private RecyclerView chatFeedRecyclerView;

    public static ChatFeedFragment newInstance() {
        return new ChatFeedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_feed, container, false);

        chatFeedRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_time_line_recycler_view);

        return v;
    }
}

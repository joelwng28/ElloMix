package com.ellomix.android.ellomix.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ellomix.android.ellomix.Model.Comment;
import com.ellomix.android.ellomix.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ATH-AJT2437 on 1/12/2017.
 */

public class CommentsFragment extends Fragment {

    private static final String TAG = "CommentsFragment";
    private RecyclerView commentRecyclerView;
    private List<Comment> mCommentList;

    // TODO: Figure how to transfer comment list to comment fragment
    public static CommentsFragment newInstance() {
        return new CommentsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);

        commentRecyclerView = (RecyclerView) view.findViewById(R.id.comments_recycler_view);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();
        return view;
    }

    public void setupAdapter() {
        commentRecyclerView.setAdapter(new CommentAdapter(mCommentList));
    }

    private class CommentHolder extends RecyclerView.ViewHolder {

        Comment mComment;
        private CircleImageView mProfileImageView;
        private TextView mCommentTextView;

        public CommentHolder(View itemView) {
            super(itemView);

            mProfileImageView = (CircleImageView) itemView.findViewById(R.id.comment_profile_image_view);
            mCommentTextView = (TextView) itemView.findViewById(R.id.comment_text_view);
        }

        public void bindItem(final Comment comment) {
            mComment = comment;
            int start = 0;
            int end = comment.getUser().getName().length();
            String commentText = comment.getUser().getName() + " " + comment.getText();

            mCommentTextView.setMovementMethod(LinkMovementMethod.getInstance());
            mCommentTextView.setText(commentText, TextView.BufferType.SPANNABLE);
            Spannable mySpannable = (Spannable) mCommentTextView.getText();
            ClickableSpan myClickableSpan = new ClickableSpan()
            {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(getActivity(), "pop comment", Toast.LENGTH_SHORT).show();
                }

            };
            mySpannable.setSpan(myClickableSpan, start, start + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mySpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlue)), start, start + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

        private List<Comment> mComments;

        public CommentAdapter(List<Comment> comments) {
            mComments = comments;
        }

        @Override
        public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.comment_fragment_item, parent, false);
            return new CommentHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentHolder holder, int position) {
            Comment comment = mComments.get(position);
            holder.bindItem(comment);
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }
    }

}

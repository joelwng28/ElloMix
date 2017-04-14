package com.ellomix.android.ellomix.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ellomix.android.ellomix.FirebaseAPI.FirebaseService;
import com.ellomix.android.ellomix.Model.Comment;
import com.ellomix.android.ellomix.Model.User;
import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.Style.NoUnderlineSpan;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentsActivity extends AppCompatActivity {

    private static final String TAG = "CommentsActivity";
    public static final String EXTRA_COMMENT = "Comments";

    private RecyclerView mCommentRecyclerView;
    private ArrayList<Comment> mCommentList;
    private CommentAdapter mAdapter;
    private EditText mCommentEditText;
    private Button mSubmitButton;

    //Firebase instance variable
    User currentUser;

    public static Intent newIntent(Context context, ArrayList<Comment> comments) {
        Intent i = new Intent(context, CommentsActivity.class);
        // Add any extras
        i.putParcelableArrayListExtra(EXTRA_COMMENT, comments);
        return i;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        FirebaseUser firebaseUser = FirebaseService.getFirebaseUser();
        if (firebaseUser != null) {
            currentUser = new User(firebaseUser.getUid(),
                    firebaseUser.getDisplayName(),
                    firebaseUser.getPhotoUrl().toString());
        }

        mCommentRecyclerView = (RecyclerView) findViewById(R.id.comments_recycler_view);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCommentEditText = (EditText) findViewById(R.id.comment_edit_text);
        mSubmitButton = (Button) findViewById(R.id.submit_button);

        mCommentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    mSubmitButton.setEnabled(true);
                }
                else {
                    mSubmitButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "before: " + mCommentList.size());
                Comment newComment =
                        new Comment(currentUser,
                                mCommentEditText.getText().toString());
                mCommentList.add(newComment);
                mCommentEditText.setText("");
                Log.i(TAG, "new size: " + mCommentList.size());
                updateUI();

            }
        });

        downloadContent();
        updateUI();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent i = new Intent();
        i.putParcelableArrayListExtra(EXTRA_COMMENT, mCommentList);
        setResult(RESULT_OK, i);
    }

    public void downloadContent() {
        Intent i = getIntent();
        if (i != null) {
            mCommentList = i.getParcelableArrayListExtra(EXTRA_COMMENT);
        }
    }

    public void updateUI() {
        if (mAdapter == null) {
            mAdapter = new CommentAdapter(mCommentList);
            mCommentRecyclerView.setAdapter(mAdapter);
        }
        else {
            Log.i(TAG, "Adapter updated");
            mAdapter.setComments(mCommentList);
            mAdapter.notifyDataSetChanged();
        }
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

            User user = mComment.getCommenter();
            if (user.getPhotoUrl() != null && !user.getPhotoUrl().equals("")) {
                Picasso.with(CommentsActivity.this)
                        .load(user.getPhotoUrl())
                        .into(mProfileImageView);
            }

            int start = 0;
            int end = user.getName().length();
            String commentText = user.getName() + " " + mComment.getText();

            mCommentTextView.setMovementMethod(LinkMovementMethod.getInstance());
            mCommentTextView.setText(commentText, TextView.BufferType.SPANNABLE);
            Spannable mySpannable = (Spannable) mCommentTextView.getText();
            ClickableSpan myClickableSpan = new ClickableSpan()
            {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(CommentsActivity.this, "pop user profile", Toast.LENGTH_SHORT).show();
                }

            };
            mySpannable.setSpan(myClickableSpan, start, start + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mySpannable.setSpan(new NoUnderlineSpan(), start, start + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
            LayoutInflater inflater = LayoutInflater.from(CommentsActivity.this);
            View view = inflater.inflate(R.layout.comment_activity_item, parent, false);
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

        public void setComments(List<Comment> comments) {
            mComments = comments;
        }
    }
}

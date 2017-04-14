package com.ellomix.android.ellomix.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ellomix.android.ellomix.Activities.CommentsActivity;
import com.ellomix.android.ellomix.Model.Comment;
import com.ellomix.android.ellomix.Model.TimelinePost;
import com.ellomix.android.ellomix.Model.User;
import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.SoundCloudAPI.SCMusicService;
import com.ellomix.android.ellomix.SoundCloudDataModel.SCTrack;
import com.ellomix.android.ellomix.SoundCloudAPI.SCService;
import com.ellomix.android.ellomix.SoundCloudAPI.SoundCloudAPI;
import com.ellomix.android.ellomix.Style.NoUnderlineSpan;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by abetorres on 11/16/16.
 */

public class
TimelineFragment extends Fragment {

    private static final String TAG = "TimelineFragment";
    static final int COMMENT_REQUEST = 1;

    private List<SCTrack> mListItems = new ArrayList<>();
    private ArrayList<TimelinePost> mPostList = new ArrayList<>();
    private RecyclerView mTimelineRecyclerView;
    private TimelineAdapter mAdapter;
    private int lastIndexPressed = 0;

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup list
        loadRecentTracks();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_time_line, container, false);

        mTimelineRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_time_line_recycler_view);
        mTimelineRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    //TODO: Create UpdateUI method to account for refreshing for more posts
    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new TimelineAdapter(mPostList);
            mTimelineRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setPosts(mPostList);
            mAdapter.notifyDataSetChanged();
        }
    }

    // Testing SC
    private int randomNumberGenerator() {
        Random rand = new Random();
        int randomNum = rand.nextInt(4 + 1);
        return randomNum;
    }

    //Example
    public void generateModel(List<SCTrack> tracks) {
        Log.d(TAG, "generate timeline");
        String[] usersDemo = {"Abe Torres", "Neil Tanner", "Akshay", "Elena Carrasco", "Micah Peoples"};
        String[] messagesDemo = {
                "DOPE",
                "Some sick beats, everyone check it ok",
                "New favourite song",
                "So mellow",
                "Chillax"
        };

        for (SCTrack track: tracks) {
            User poster = new User(usersDemo[randomNumberGenerator()]);
            User commenter = new User(usersDemo[randomNumberGenerator()]);
            TimelinePost post = new TimelinePost(poster, track, messagesDemo[randomNumberGenerator()]);
            post.addComment(new Comment(commenter, messagesDemo[randomNumberGenerator()]));
            mPostList.add(post);
        }

        updateUI();
    }

    private void loadRecentTracks() {
        SCService scService = SoundCloudAPI.getService();
        Call<List<SCTrack>> call = scService.getRecentTracks(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US).format(new Date()));

        call.enqueue(new Callback<List<SCTrack>>() {
            @Override
            public void onResponse(Response<List<SCTrack>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<SCTrack> listItems = response.body();
                    generateModel(listItems);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "Error: " + t.toString());
            }
        });
    }
    // Testing ends

    private class TimelineHolder extends RecyclerView.ViewHolder {

        private TimelinePost mPost;
        private int mIndex;
        private RelativeLayout mContainerView;
        private CircleImageView mProfileImageView;
        private TextView mUploaderTextView;
        private TextView mLongAgoTextView;
        private ImageView mTrackArtworkImageView;
        private TextView mArtistTextView;
        private TextView mTitleTextView;
        private TextView mMessageTextView;
        private ImageButton mLikeButton;
        private ImageButton mCommentButton;
        private ImageButton mRepostButton;
        private ImageButton mShareButton;
        private ListView mCommentListView;
        private BaseAdapter mAdapter;
        private TextView mShowAllComments;

        public TimelineHolder(View itemView) {
            super(itemView);

            //bind resources
            mContainerView = (RelativeLayout) itemView.findViewById(R.id.item_container);
            mProfileImageView = (CircleImageView) itemView.findViewById(R.id.post_profile_image_view);
            mUploaderTextView = (TextView) itemView.findViewById(R.id.uploader_text_view);
            mLongAgoTextView = (TextView) itemView.findViewById(R.id.long_ago_text_view);
            mTrackArtworkImageView = (ImageView) itemView.findViewById(R.id.track_artwork_image_view);
            //mArtistTextView = (TextView) itemView.findViewById(R.id.artist_text_view);
            //mTitleTextView = (TextView) itemView.findViewById(R.id.song_title_text_view);
            mMessageTextView = (TextView) itemView.findViewById(R.id.message_text_view);
            mLikeButton = (ImageButton) itemView.findViewById(R.id.like);
            mCommentButton = (ImageButton) itemView.findViewById(R.id.comment);
            mRepostButton = (ImageButton) itemView.findViewById(R.id.repost);
            mShareButton = (ImageButton) itemView.findViewById(R.id.share);
            mCommentListView = (ListView) itemView.findViewById(R.id.comment_timeline_list_view);
            mShowAllComments = (TextView) itemView.findViewById(R.id.view_comments_button);
        }

        public void bindItem(final TimelinePost post, int index) {
            mPost = post;
            mIndex = index;

            User poster = mPost.getUser();
            if (poster.getPhotoUrl() !=  null && !poster.getPhotoUrl().equals("")) {
                Picasso.with(getActivity())
                        .load(poster.getPhotoUrl())
                        .into(mProfileImageView);
            }
            mProfileImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), TimelineHolder.this.mPost.getUser().getName(), Toast.LENGTH_SHORT).show();
                }
            });
            mUploaderTextView.setText(mPost.getUser().getName());

            try {
                Log.d(TAG, "date of post: " + mPost.getSinceCreated());
                Log.d(TAG, "long time ago: " + mPost.getSinceCreated());
                mLongAgoTextView.setText(mPost.getSinceCreated());
            }

            catch (ParseException pe) {
                Log.e(TAG, pe.getLocalizedMessage());
            }

            //TODO: Reasearch for better source of album artwork
            Picasso.with(getActivity())
                    .load(mPost.getTrack().getArtworkURL())
                    .into(mTrackArtworkImageView);
            mTrackArtworkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = SCMusicService.newIntent(getActivity(), TimelineHolder.this.mPost.getTrack().getStreamURL(), TimelineHolder.this.mPost.getTrack().getTitle());
                    getActivity().startService(intent);
                }
            });
            //mArtistTextView.setText(mPost.getTrack().getArtist());
            //mTitleTextView.setText(mPost.getTrack().getTitle());
            mMessageTextView.setText(this.mPost.getDescription());
            mCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastIndexPressed = mIndex;
                    Intent i = CommentsActivity.newIntent(getActivity(), mPost.getCommentList());
                    startActivityForResult(i, COMMENT_REQUEST);
                }
            });
            mCommentListView.setAdapter(new BaseAdapter() {
                ArrayList<Comment> comments = mPost.getCommentList();
                @Override
                public int getCount() {
                    //limit to 5 comments displayed on timeline post
                    int size = comments.size();
                    if (size <= 5) {
                        mContainerView.removeView(mShowAllComments);
                        return size;
                    }
                    else {
                        mShowAllComments.setMovementMethod(LinkMovementMethod.getInstance());
                        Spannable mySpannable = (Spannable) mShowAllComments.getText();
                        ClickableSpan myClickableSpan = new ClickableSpan()
                        {
                            @Override
                            public void onClick(View widget) {
                                Toast.makeText(getActivity(), "show all commments", Toast.LENGTH_SHORT).show();
                            }

                        };
                        int start = 0;
                        int end = 17;

                        mySpannable.setSpan(myClickableSpan, start, start + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mySpannable.setSpan(new NoUnderlineSpan(), start, start + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mySpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), start, start + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        return 5;
                    }
                }

                @Override
                public Object getItem(int position) {
                    return comments.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    ViewHolder holder;
                    if (convertView == null) {
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.comment_timeline_item, null);
                        holder = new ViewHolder();
                        holder.userAndText = (TextView) convertView.findViewById(R.id.timeline_comment_text_view);
                        convertView.setTag(holder);
                    }
                    else {
                        holder = (ViewHolder) convertView.getTag();
                    }

                    Comment comment = comments.get(position);
                    int start = 0;
                    int end = comment.getCommenter().getName().length();
                    String commentText = comment.getCommenter().getName() + " " + comment.getText();

                    holder.userAndText.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.userAndText.setText(commentText, TextView.BufferType.SPANNABLE);
                    Spannable mySpannable = (Spannable)holder.userAndText.getText();
                    ClickableSpan myClickableSpan = new ClickableSpan()
                    {
                        @Override
                        public void onClick(View widget) {
                            Toast.makeText(getActivity(), "pop users profile", Toast.LENGTH_SHORT).show();
                        }

                    };

                    mySpannable.setSpan(myClickableSpan, start, start + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mySpannable.setSpan(new NoUnderlineSpan(), start, start + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mySpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorBlue)), start, start + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    return convertView;
                }

                class ViewHolder {
                    TextView userAndText;
                }
            });
        }
    }

    private class TimelineAdapter extends RecyclerView.Adapter<TimelineHolder> {

        private ArrayList<TimelinePost> mPosts;

        public TimelineAdapter(ArrayList<TimelinePost> posts) {
            mPosts = posts;
        }

        @Override
        public TimelineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.time_line_item, parent, false);
            return new TimelineHolder(view);
        }

        @Override
        public void onBindViewHolder(TimelineHolder holder, int position) {
            TimelinePost post = mPosts.get(position);
            holder.bindItem(post, position);
        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }

        public void setPosts(ArrayList<TimelinePost> posts) {
            mPosts = posts;
        }
    }

}

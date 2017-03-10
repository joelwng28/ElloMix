package com.ellomix.android.ellomix.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ellomix.android.ellomix.Model.Comment;
import com.ellomix.android.ellomix.Model.TimelinePost;
import com.ellomix.android.ellomix.Model.User;
import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.SoundCloudAPI.SCMusicService;
import com.ellomix.android.ellomix.SoundCloudDataModel.SCTrack;
import com.ellomix.android.ellomix.SoundCloudAPI.SCService;
import com.ellomix.android.ellomix.SoundCloudAPI.SoundCloud;
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
    private List<SCTrack> mListItems = new ArrayList<>();
    private List<TimelinePost> mPostList = new ArrayList<>();
    private RecyclerView mTimelineRecyclerView;

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

        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        mTimelineRecyclerView.setAdapter(new TimelineAdapter(mPostList));
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

        setupAdapter();
    }

    private void loadRecentTracks() {
        SCService scService = SoundCloud.getService();
        Call<List<SCTrack>> call = scService.getRecentTracks(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US).format(new Date()));

        call.enqueue(new Callback<List<SCTrack>>() {
            @Override
            public void onResponse(Response<List<SCTrack>> response, Retrofit retrofit) {
                List<SCTrack> listItems = response.body();
                Log.e(TAG, "date created: " + listItems.get(0).getCreatedAt().substring(0, 19));
                generateModel(listItems);
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

        public TimelineHolder(View itemView) {
            super(itemView);

            //bind resources
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
        }

        public void bindItem(final TimelinePost post) {
            mPost = post;

            mUploaderTextView.setText(post.getUser().getName());

            try {
                Log.d(TAG, "date of post: " + post.getSinceCreated());
                Log.d(TAG, "long time ago: " + post.getSinceCreated());
                mLongAgoTextView.setText(post.getSinceCreated());
            }

            catch (ParseException pe) {
                Log.e(TAG, pe.getLocalizedMessage());
            }
            Picasso.with(getActivity())
                    .load(post.getTrack().getArtworkURL())
                    //.placeholder(R.drawable.art_work_placeholder)
                    .into(mTrackArtworkImageView);
            mTrackArtworkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = SCMusicService.newIntent(getActivity(), mPost.getTrack().getStreamURL(), mPost.getTrack().getTitle());
                    getActivity().startService(intent);
                }
            });
            //mArtistTextView.setText(mPost.getTrack().getArtist());
            //mTitleTextView.setText(mPost.getTrack().getTitle());
            mMessageTextView.setText(mPost.getDescription());
            mCommentListView.setAdapter(new BaseAdapter() {
                List<Comment> comments = post.getCommentList();
                @Override
                public int getCount() {
                    //limit to 5 comments displayed on timeline post
                    int size = comments.size();
                    if (size <= 5) {
                        return size;
                    }
                    else {
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
                    int end = comment.getUser().getName().length();
                    String commentText = comment.getUser().getName() + " " + comment.getText();

                    holder.userAndText.setMovementMethod(LinkMovementMethod.getInstance());
                    holder.userAndText.setText(commentText, TextView.BufferType.SPANNABLE);
                    Spannable mySpannable = (Spannable)holder.userAndText.getText();
                    ClickableSpan myClickableSpan = new ClickableSpan()
                    {
                        @Override
                        public void onClick(View widget) {
                            Toast.makeText(getActivity(), "pop comment", Toast.LENGTH_SHORT).show();
                        }

                    };
                    mySpannable.setSpan(myClickableSpan, start, start + end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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

        private List<TimelinePost> mPosts;

        public TimelineAdapter(List<TimelinePost> posts) {
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
            holder.bindItem(post);
        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }
    }

}

package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailsBinding;
import com.codepath.apps.restclienttemplate.models.JsonDate;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetDetailsActivity extends AppCompatActivity {

    private static final String TAG = "TweetDetailsActivity";

    Tweet mTweet;

    ImageView profileImageView;
    ImageView mediaImageView;

    TextView bodyTextView;
    TextView nameTextView;
    TextView screenNameTextView;
    TextView createdTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTweetDetailsBinding binding = ActivityTweetDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        mTweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        profileImageView = binding.profileImageView;
        mediaImageView = binding.mediaImageView;
        bodyTextView = binding.tweetBodyTextView;
        nameTextView = binding.nameTextView;
        screenNameTextView = binding.screenNameTextView;
        createdTimeTextView = binding.createdTimeTextView;

        bind(mTweet);
    }

    public void bind(Tweet tweet) {
        bodyTextView.setText(tweet.body);
        nameTextView.setText(tweet.user.name);

        String userHandle = "@" + tweet.user.screenName;
        screenNameTextView.setText(userHandle);

        String createdTime = JsonDate.getTimeDetails(tweet.createdAt);
        createdTimeTextView.setText(createdTime);

        String profileImageUrl = tweet.user.profileImageUrl;
        profileImageUrl = profileImageUrl.replace("_normal", "");

        Log.i(TAG, "User profile image url: " + profileImageUrl);

        Glide.with(this).load(profileImageUrl).transform(new CircleCrop()).into(profileImageView);

        if (tweet.mediaUrl == null) {
            mediaImageView.setVisibility(View.GONE);
        } else {
            mediaImageView.setVisibility(View.VISIBLE);
            Glide.with(this).load(tweet.mediaUrl).transform(new RoundedCornersTransformation(40, 10)).into(mediaImageView);
        }
    }
}
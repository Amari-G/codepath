package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.models.JsonDate;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private static final String TAG = "TweetsAdapter";

    Context mContext;
    List<Tweet> mTweets;

    // Pass in the context and list of tweets


    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.mContext = context;
        this.mTweets = tweets;
    }

    // For each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tweet, parent, false);

        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = mTweets.get(position);

        // Bind the tweet with the view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // Define a ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImageView;
        ImageView mediaImageView;

        TextView bodyTextView;
        TextView nameTextView;
        TextView screenNameTextView;
        TextView elapsedTimeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            mediaImageView = itemView.findViewById(R.id.mediaImageView);
            bodyTextView = itemView.findViewById(R.id.tweetBodyTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            screenNameTextView = itemView.findViewById(R.id.screenNameTextView);
            elapsedTimeTextView = itemView.findViewById(R.id.elapsedTimeTextView);

        }

        public void bind(Tweet tweet) {
            bodyTextView.setText(tweet.body);
            nameTextView.setText(tweet.user.name);

            String userHandle = "@" + tweet.user.screenName;
            screenNameTextView.setText(userHandle);

            String elapsedTime = " · " + JsonDate.getElapsedTime(tweet.createdAt);
            elapsedTimeTextView.setText(elapsedTime);

            String profileImageUrl = tweet.user.profileImageUrl;
            profileImageUrl = profileImageUrl.replace("_normal", "");

//            StringBuffer stringBuffer = new StringBuffer(tweet.user.profileImageUrl);
//            stringBuffer.insert(profileImageUrl.length() - 4, "_mini");
//
//            profileImageUrl = stringBuffer.toString();
            Log.i(TAG, "User profile image url: " + profileImageUrl);

            Glide.with(mContext).load(profileImageUrl).transform(new CircleCrop()).into(profileImageView);

            if (tweet.mediaUrl == null) {
                mediaImageView.setVisibility(View.GONE);
            } else {
                mediaImageView.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(tweet.mediaUrl).fitCenter().transform(new RoundedCornersTransformation(40, 10)).into(mediaImageView);
            }
        }
    }

    // Clear all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Tweet> tweets) {
        mTweets.addAll(tweets);
    }
}

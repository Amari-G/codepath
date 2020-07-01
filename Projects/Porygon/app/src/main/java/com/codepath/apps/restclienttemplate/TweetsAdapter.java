package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

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
        TextView screenNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            mediaImageView = itemView.findViewById(R.id.mediaImageView);
            bodyTextView = itemView.findViewById(R.id.tweetBodyTextView);
            screenNameTextView = itemView.findViewById(R.id.screenNameTextView);
        }

        public void bind(Tweet tweet) {
            bodyTextView.setText(tweet.body);
            screenNameTextView.setText(tweet.user.screenName);
            Glide.with(mContext).load(tweet.user.profileImageUrl).into(profileImageView);

            if (tweet.mediaUrl == null) {
                mediaImageView.setVisibility(View.GONE);
            } else {
                mediaImageView.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(tweet.mediaUrl).fitCenter().into(mediaImageView);
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

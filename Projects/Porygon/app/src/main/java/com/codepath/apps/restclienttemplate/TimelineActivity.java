package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetDao;
import com.codepath.apps.restclienttemplate.models.TweetWithUser;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

import static androidx.recyclerview.widget.DividerItemDecoration.HORIZONTAL;
import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class TimelineActivity extends AppCompatActivity {

    private static final String TAG = "TimelineActivity";
    private final int REQUEST_CODE = 20;

    TwitterClient mClient;
    TweetDao mTweetDao;
    RecyclerView mRecyclerViewTweets;
    List<Tweet> mTweets;
    TweetsAdapter mAdapter;
    SwipeRefreshLayout mSwipeContainer;
    EndlessRecyclerViewScrollListener mScrollListener;
    FloatingActionButton mTweetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mClient = TwitterApp.getRestClient(this);
        mTweetDao = ((TwitterApp) getApplicationContext()).getMyDatabase().tweetDao();


        mSwipeContainer = findViewById(R.id.swipeContainer);
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "Fetching new data");
                populateHomeTimeline();
            }
        });

        // Find the recycler view
        mRecyclerViewTweets = findViewById(R.id.tweetsRecyclerView);
        // Initialize the list of tweets and adapter
        mTweets = new ArrayList<>();
        mAdapter = new TweetsAdapter(this, mTweets);
        // RecyclerView setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewTweets.setLayoutManager(layoutManager);
        mRecyclerViewTweets.setAdapter(mAdapter);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, VERTICAL);
        itemDecor.setDrawable(getResources().getDrawable(R.drawable.timeline_tweet_divider));
        mRecyclerViewTweets.addItemDecoration(itemDecor);


        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);
                loadMoreData();

            }
        };

        // Adds this scroll listener to RecyclerView
        mRecyclerViewTweets.addOnScrollListener(mScrollListener);

        mTweetButton = findViewById(R.id.composeTweetButton);
        mTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TimelineActivity.this, "Compose new tweet", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TimelineActivity.this, ComposeTweetActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // Query for existing tweets in the database
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Showing data from database");
                List<TweetWithUser> tweetWithUsers = mTweetDao.recentItems();
                List<Tweet> tweetsFromDB = TweetWithUser.getTweetList(tweetWithUsers);
                mAdapter.clear();
                mAdapter.addAll(tweetsFromDB);
            }
        });

        populateHomeTimeline();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check if request code matches and if result is okay
        Log.i(TAG, "onActivityResult: ");

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Get data from intent sent from ComposeTweetActivity
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));

            // Update the recycler view with the tweet
            //Modify data source
            mTweets.add(0, tweet);
            // Update the adapter
            mAdapter.notifyItemInserted(0);
            mRecyclerViewTweets.smoothScrollToPosition(0);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadMoreData() {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        mClient.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess for loadMoreData");
                //  --> Deserialize and construct new model objects from the API response
                JSONArray jsonArray = json.jsonArray;
                try {
                    List<Tweet> tweets = Tweet.fromJsonArray(jsonArray);
                    //  --> Append the new data objects to the existing set of items inside the array of items
                    mAdapter.addAll(tweets);
                    //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure for loadMoreData", throwable);
            }
        }, mTweets.get(mTweets.size() - 1).id);

    }

    private void populateHomeTimeline() {
        mClient.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess!" + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    final List<Tweet> tweetsFromNetwork = Tweet.fromJsonArray(jsonArray);

                    mAdapter.clear();
                    mAdapter.addAll(tweetsFromNetwork);
                    // Now we call setRefreshing(false) to signal refresh has finished
                    mSwipeContainer.setRefreshing(false);

                    // Insert tweets into local database
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "Saving data into the database");
                            // insert users first
                            List<User> usersFromNetwork = User.fromJsonTweetArray(tweetsFromNetwork);
                            mTweetDao.insertModel(usersFromNetwork.toArray(new User[0]));

                            // insert users first
                            mTweetDao.insertModel(tweetsFromNetwork.toArray(new Tweet[0]));
                        }
                    });

                } catch (JSONException e) {
                    Log.e(TAG, "JSON exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure:" + response, throwable);
                if (mSwipeContainer.isRefreshing()) {
                    mSwipeContainer.setRefreshing(false);
                }
            }
        });
    }
}
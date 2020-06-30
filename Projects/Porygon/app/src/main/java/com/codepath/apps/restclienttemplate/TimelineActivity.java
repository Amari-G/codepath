package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    private static final String TAG = "TimelineActivity";

    TwitterClient mClient;
    RecyclerView mRecyclerViewTweets;
    List<Tweet> mTweets;
    TweetsAdapter mAdapter;
    SwipeRefreshLayout mSwipeContainer;
    EndlessRecyclerViewScrollListener mScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mClient = TwitterApp.getRestClient(this);

        mSwipeContainer = findViewById(R.id.swipeContainer);
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "Fetching new data");
                populateHomeTimeline();
            }
        });

        // Find the recycler view
        mRecyclerViewTweets = findViewById(R.id.rvTweets);
        // Initialize the list of tweets and adapter
        mTweets = new ArrayList<>();
        mAdapter = new TweetsAdapter(this, mTweets);
        // RecyclerView setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewTweets.setLayoutManager(layoutManager);
        mRecyclerViewTweets.setAdapter(mAdapter);

        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);
                loadMoreData();

            }
        };

        // Adds ths scroll listener to RecyclerView
        mRecyclerViewTweets.addOnScrollListener(mScrollListener);


        populateHomeTimeline();
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
                    mAdapter.clear();
                    mAdapter.addAll(Tweet.fromJsonArray(jsonArray));
                    // Now we call setRefreshing(false) to signal refresh has finished
                    mSwipeContainer.setRefreshing(false);
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
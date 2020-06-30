package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {

    private static final String TAG = "Tweet";

    public long id;
    public String body;
    public String createdAt;
    public User user;
    public String imageUrl;

    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        // Parse data from JSONObject
        Tweet tweet = new Tweet();
        tweet.id = jsonObject.getLong("id");
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        // Check if tweet contains an image
        // Get entities, then check entities for a media array
        Log.i(TAG, jsonObject.getJSONObject("entities").toString());
        Log.i(TAG, "fromJson: Tweet has media: " + jsonObject.getJSONObject("entities").has("media"));

        // If tweet contains an image, get image url
        if (jsonObject.getJSONObject("entities").has("media")) {
            // Retrieve the media object from the Tweet JSON
            // NOTE: this only takes the first object in the array
            JSONObject mediaObject = jsonObject.getJSONObject("entities")
                    .getJSONArray("media").getJSONObject(0);
            tweet.imageUrl = mediaObject.getString("media_url_https");
        }

        return tweet;
    }

    // Generate a list of Tweets from a JSONArray
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }

        return tweets;
    }
}

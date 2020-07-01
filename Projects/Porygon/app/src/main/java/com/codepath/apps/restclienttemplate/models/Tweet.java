package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity=User.class, parentColumns="id", childColumns="userId"))
public class Tweet {

    private static final String TAG = "Tweet";

    @ColumnInfo
    @PrimaryKey
    public long id;

    @ColumnInfo
    public String body;

    @ColumnInfo
    public String createdAt;

    @ColumnInfo
    public String mediaUrl;

    @ColumnInfo
    public long userId;

    @Ignore
    public User user;


    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        // Parse data from JSONObject
        Tweet tweet = new Tweet();
        tweet.id = jsonObject.getLong("id");
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        User user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.user = user;
        tweet.userId = user.id;

        // Check if tweet contains an image
        // Get entities, then check entities for a media array
        Log.i(TAG, jsonObject.getJSONObject("entities").toString());
        checkForPhoto(jsonObject, tweet);

        return tweet;
    }

    // If tweet contains an image, set media url
    private static void checkForPhoto(JSONObject jsonObject, Tweet tweet) throws JSONException {
        if (jsonObject.getJSONObject("entities").has("media")) {
            // Retrieve the media object from the Tweet JSON
            // NOTE: this only takes the first image
            JSONObject mediaObject = jsonObject.getJSONObject("entities")
                    .getJSONArray("media").getJSONObject(0);

            if (mediaObject.getString("type").equals("photo")) {
                tweet.mediaUrl = mediaObject.getString("media_url_https");
                tweet.mediaUrl = tweet.getFormattedUrl(tweet.mediaUrl, "thumb");
                Log.i(TAG, "fromJson: Tweet has media: " + tweet.mediaUrl);
            }
        }
    }

    // Generate a list of Tweets from a JSONArray
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }

        return tweets;
    }

    private String getFormattedUrl (String mediaUrl, String name) {
        String baseUrl = mediaUrl.substring(0, mediaUrl.length()-4);
        String format = mediaUrl.substring(mediaUrl.length() - 3, mediaUrl.length());

        return String.format("%s?format=%s&name=%s", baseUrl, format, name);
    }
}

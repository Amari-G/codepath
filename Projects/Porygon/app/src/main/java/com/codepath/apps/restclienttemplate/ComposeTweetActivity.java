package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;

public class ComposeTweetActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 280;

    EditText mComposeTweetEditText;
    Button mTweetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComposeBinding binding = ActivityComposeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mComposeTweetEditText = binding.composeTweetEditText;

        mTweetButton = binding.tweetButton;
        mTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = mComposeTweetEditText.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeTweetActivity.this, "Sorry, your tweet cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Toast.makeText(ComposeTweetActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(ComposeTweetActivity.this, tweetContent, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
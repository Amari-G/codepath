package com.codepath.android.booksearch.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codepath.android.booksearch.GlideApp;
import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.models.Book;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BookDetailsActivity extends AppCompatActivity {
    private ImageView mBookImageView;
    private TextView mTitleTextView;
    private TextView mAuthorTextView;

    private ShareActionProvider miShareAction;

    private Intent shareIntent;

    Book mBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Fetch views
        mBookImageView = (ImageView) findViewById(R.id.bookImageView);
        mTitleTextView = (TextView) findViewById(R.id.titleTextView);
        mAuthorTextView = (TextView) findViewById(R.id.authorTextView);


        // Extract book object from intent extras
        mBook = (Book) Parcels.unwrap(getIntent().getParcelableExtra(Book.class.getSimpleName()));
        // Use book object to populate data into views
        getSupportActionBar().setTitle(mBook.getTitle());
        mTitleTextView.setText(mBook.getTitle());
        mAuthorTextView.setText(mBook.getAuthor());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_detail, menu);
        MenuItem item = menu.findItem(R.id.shareMenuItem);

        // return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.shareMenuItem) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.codepath.android.booksearch.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.android.booksearch.R;
import com.codepath.android.booksearch.adapters.BookAdapter;
import com.codepath.android.booksearch.models.Book;
import com.codepath.android.booksearch.net.BookClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.Headers;


public class BookListActivity extends AppCompatActivity {
    private RecyclerView mBooksRecyclerView;
    private BookAdapter mBookAdapter;
    private BookClient mClient;
    private ArrayList<Book> mBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBooksRecyclerView = findViewById(R.id.recyclerViewBooks);
        mBooks = new ArrayList<>();

        // Initialize the adapter
        mBookAdapter = new BookAdapter(this, mBooks);
        mBookAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Toast.makeText(
                        BookListActivity.this,
                        "An item at position " + position + " clicked!",
                        Toast.LENGTH_SHORT).show();

                // Handle item click here:
                // Create Intent to start BookDetailActivity
                // Get Book at the given position
                // Pass the book into details activity using extras
            }
        });

        // Attach the adapter to the RecyclerView
        mBooksRecyclerView.setAdapter(mBookAdapter);

        // Set layout manager to position the items
        mBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch the data remotely
//        fetchBooks("Oscar Wilde");
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchBooks(String query) {
        mClient = new BookClient();
        mClient.getBooks(query, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Headers headers, JSON response) {
                try {
                    JSONArray docs;
                    if (response != null) {
                        // Get the docs json array
                        docs = response.jsonObject.getJSONArray("docs");
                        // Parse json array into array of model objects
                        final ArrayList<Book> books = Book.fromJson(docs);
                        // Remove all books from the adapter
                        mBooks.clear();
                        // Load model objects into the adapter
                        for (Book book : books) {
                            mBooks.add(book); // add book through the adapter
                        }
                        mBookAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String responseString, Throwable throwable) {
                // Handle failed request here
                Log.e(BookListActivity.class.getSimpleName(),
                        "Request failed with code " + statusCode + ". Response message: " + responseString);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.miSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                fetchBooks(query);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

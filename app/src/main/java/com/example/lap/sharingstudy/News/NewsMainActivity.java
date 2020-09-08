package com.example.lap.sharingstudy.News;

import androidx.loader.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import androidx.loader.content.Loader;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lap.sharingstudy.R;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("deprecation")
public class NewsMainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String NEWS_API_URL = "http://content.guardianapis.com/search?q=android,iOS,startup,recent&show-tags=contributor&api-key=test";

    private static final int NEWS_LOADER_ID = 1;

    private NewsAdapter mAdapter;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        ListView newsListView = findViewById(R.id.news_list);

        mEmptyStateTextView = findViewById(R.id.news_empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new NewsAdapter(this, new ArrayList<>());
        newsListView.setAdapter(mAdapter);

        newsListView.setOnItemClickListener((adapterView, view, position, l) -> {
            News currentNews = mAdapter.getItem(position);
            Uri newsUri = Uri.parse(currentNews.getUrl());
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
            startActivity(websiteIntent);
        });
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        connMgr.registerNetworkCallback(
                builder.build(),
                new ConnectivityManager.NetworkCallback() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onAvailable(Network network) {
                        LoaderManager loaderManager =LoaderManager.getInstance(NewsMainActivity.this);

                        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                        // because this activity implements the LoaderCallbacks interface).

                    }
                    @Override
                    public void onLost(Network network) { // Otherwise, display error
                        // Otherwise, display error
                        // First, hide loading indicator so error message will be visible
                        View loadingIndicator = findViewById(R.id.news_loading_indicator);
                        loadingIndicator.setVisibility(View.GONE);

                        // Update empty state with no connection error message
                        mEmptyStateTextView.setText(R.string.no_internet_connection);
                    }});
        runnableCode.run();

    }

    Handler handler = new Handler();
    private Runnable runnableCode = new Runnable() {
        @SuppressWarnings("deprecation")
        @Override
        public void run() {
            // Do something here on the main thread
            LoaderManager loaderManager = LoaderManager.getInstance(NewsMainActivity.this);
            loaderManager.initLoader(NEWS_LOADER_ID, null, NewsMainActivity.this);
            // Run the above code block on the main thread after 30 seconds
            handler.postDelayed(this, 3000);
            Log.d("Handlers", "Called on main thread");
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, NEWS_API_URL);
    }
    @SuppressWarnings("deprecation")
    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.news_loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No News found."
        mEmptyStateTextView.setText(R.string.empty_news);

        // Clear the adapter of previous news data
        mAdapter.clear();
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}

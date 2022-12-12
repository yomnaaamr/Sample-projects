package com.example.githupreposearch;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import com.example.githupreposearch.utilities.NetworkUtils;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private final static String search_query = "query";
    private final static int GITHUB_SEARCH_LOADER = 1;
    private EditText mSearchBoxEditText;
    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if(savedInstanceState!=null){
            String query_uri = savedInstanceState.getString(search_query);
            mUrlDisplayTextView.setText(query_uri);

        }

        getSupportLoaderManager().initLoader(GITHUB_SEARCH_LOADER,null,this);
    }


    private void makeGithubSearchQuery() {
        String githubQuery = mSearchBoxEditText.getText().toString();

        if(TextUtils.isEmpty(githubQuery)){
            mUrlDisplayTextView.setText("No query entered, nothing to search for.");
            return;
        }
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());
//        new GithubQueryTask().execute(githubSearchUrl);

        Bundle queryBundle = new Bundle();
        queryBundle.putString(search_query,githubSearchUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> githubSearchLoader = loaderManager.getLoader(GITHUB_SEARCH_LOADER);
        if(githubSearchLoader==null){
            loaderManager.initLoader(GITHUB_SEARCH_LOADER,queryBundle,this);
        }else {
            loaderManager.restartLoader(GITHUB_SEARCH_LOADER,queryBundle,this);
        }
    }

    private void showJsonDataView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // Then, make sure the JSON data is visible
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage() {
        // First, hide the currently visible data
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String mGithubJson;

            @Override
            public void deliverResult(@Nullable String data) {
                mGithubJson = data;
                super.deliverResult(data);
            }

            @Nullable
            @Override
            public String loadInBackground() {
                String searchString = args.getString(search_query);
                if(TextUtils.isEmpty(searchString) || searchString ==null){
                    return null;
                }
                try {
                    URL githubUrl = new URL(searchString);
                  String  githubSearchResults = NetworkUtils.getResponseFromHttpUrl(githubUrl);
                  return githubSearchResults;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }


            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args ==null){
                    return;
                }
                if(mGithubJson!=null){
                   deliverResult(mGithubJson);
                }else{
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }

            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data == null) {
           showErrorMessage();
        } else {
          mSearchResultsTextView.setText(data);
          showJsonDataView();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

//    instead we use the loader

//    public class GithubQueryTask extends AsyncTask<URL, Void, String> {
//
//        // COMPLETED (26) Override onPreExecute to set the loading indicator to visible
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mLoadingIndicator.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String doInBackground(URL... params) {
//            URL searchUrl = params[0];
//            String githubSearchResults = null;
//            try {
//                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return githubSearchResults;
//        }
//
//        @Override
//        protected void onPostExecute(String githubSearchResults) {
//            // COMPLETED (27) As soon as the loading is complete, hide the loading indicator
//            mLoadingIndicator.setVisibility(View.INVISIBLE);
//            if (githubSearchResults != null && !githubSearchResults.equals("")) {
//                // COMPLETED (17) Call showJsonDataView if we have valid, non-null results
//                showJsonDataView();
//                mSearchResultsTextView.setText(githubSearchResults);
//            } else {
//                // COMPLETED (16) Call showErrorMessage if the result is null in onPostExecute
//                showErrorMessage();
//            }
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        String query = mUrlDisplayTextView.getText().toString();
        outState.putString(search_query,query);

    }
}
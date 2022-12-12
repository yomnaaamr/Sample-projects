package com.example.contentproviderexample;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Gets the data from the ContentProvider and shows a series of flash cards.
 */

public class MainActivity extends AppCompatActivity {

    // The current state of the app
    private int mCurrentState;
     Cursor mData;
    private Uri uri;
    private int mDefCol, mWordCol;
    private TextView mWordTextView, mDefinitionTextView;
    private Button mButton;

    // This state is when the word definition is hidden and clicking the button will therefore
    // show the definition
    private final int STATE_HIDDEN = 0;

    // This state is when the word definition is shown and clicking the button will therefore
    // advance the app to the next word
    private final int STATE_SHOWN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the views
        mWordTextView = (TextView) findViewById(R.id.text_view_word);
        mDefinitionTextView = (TextView) findViewById(R.id.text_view_definition);
        mButton = (Button) findViewById(R.id.button_next);

        new wordFetchTask().execute();
    }

    /**
     * This is called from the layout when the button is clicked and switches between the
     * two app states.
     * @param view The view that was clicked
     */
    public void onButtonClick(View view) {

        // Either show the definition of the current word, or if the definition is currently
        // showing, move to the next word.
        switch (mCurrentState) {
            case STATE_HIDDEN:
                 showDefinition();
                break;
            case STATE_SHOWN:
                nextWord();
                break;
        }
    }

    public void nextWord() {

        if (mData != null) {
            // Move to the next position in the cursor, if there isn't one, move to the first
            if (!mData.moveToNext()) {
                mData.moveToFirst();
            }
            // Hide the definition TextView
            mDefinitionTextView.setVisibility(View.INVISIBLE);

            // Change button text
            mButton.setText(getString(R.string.show_definition));

//             Get the next word
            mWordTextView.setText(mData.getString(0));
            mDefinitionTextView.setText(mData.getString(1));

            mCurrentState = STATE_HIDDEN;

        }
    }

    public void showDefinition() {

            if (mData != null) {
                // Show the definition TextView
                mDefinitionTextView.setVisibility(View.VISIBLE);

                // Change button text
                mButton.setText(getString(R.string.next_word));

                mCurrentState = STATE_SHOWN;

            }
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mData.close();
    }

    public class wordFetchTask extends AsyncTask<Void,Void, Cursor>{

        @Override
        protected Cursor doInBackground(Void... voids) {

            uri = Uri.parse("content://com.example.bookdatabase.data.bookProvider/books");

            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(uri,new String[]{"title","author"},null,null,null);

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            mData = cursor;
//            mDefCol = mData.getColumnIndex(String.valueOf(0));
//            mWordCol = mData.getColumnIndex(String.valueOf(1));

            nextWord();
        }
    }

}
package com.example.bookdatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import com.example.bookdatabase.data.bookContract.bookEntry;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int existing_book_loader = 0;
    private Uri mCurrentBookUri;
    private EditText mTitleEditText;
    private EditText mAuthorEditText;
    private EditText mpageEditText;
    private EditText mStartDateEditText;
    private EditText mEndDateEditText;
    private boolean bookHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            bookHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();
        if (mCurrentBookUri == null) {
            setTitle(getString(R.string.add_book));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_book));
            LoaderManager.getInstance(this).initLoader(existing_book_loader, null, this);
        }

        mTitleEditText = findViewById(R.id.title);
        mAuthorEditText = findViewById(R.id.author);
        mpageEditText = findViewById(R.id.page);
        mStartDateEditText = findViewById(R.id.start_time);
        mEndDateEditText = findViewById(R.id.end_time);

        mTitleEditText.setOnTouchListener(mTouchListener);
        mAuthorEditText.setOnTouchListener(mTouchListener);
        mpageEditText.setOnTouchListener(mTouchListener);
        mStartDateEditText.setOnTouchListener(mTouchListener);
        mEndDateEditText.setOnTouchListener(mTouchListener);
    }

    private void saveBook() {

        String titleString = mTitleEditText.getText().toString().trim();
        String authorString = mAuthorEditText.getText().toString().trim();
        String pageString = mpageEditText.getText().toString().trim();
        String startDateString = mStartDateEditText.getText().toString().trim();
        String endDateString = mEndDateEditText.getText().toString().trim();

        if (mCurrentBookUri == null && TextUtils.isEmpty(titleString) && TextUtils.isEmpty(authorString)
                && TextUtils.isEmpty(pageString) && TextUtils.isEmpty(startDateString) && TextUtils.isEmpty(endDateString)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(bookEntry.col_book_title, titleString);
        values.put(bookEntry.col_book_author, authorString);
        values.put(bookEntry.col_book_page, pageString);
        values.put(bookEntry.col_book_start_date, startDateString);
        values.put(bookEntry.col_book_end_date, endDateString);

        if (mCurrentBookUri == null) {
            Uri newUri = getContentResolver().insert(bookEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insertfailed), Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.insertsuccess), Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.updatefailed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.updatesuccess), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveBook();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!bookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!bookHasChanged){
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard,discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface !=null){
                    dialogInterface.dismiss();
                }
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteBook() {

        if(mCurrentBookUri!=null){
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri,null,null);
            if(rowsDeleted==0){
                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {bookEntry._ID,
        bookEntry.col_book_title,bookEntry.col_book_author,bookEntry.col_book_page
        ,bookEntry.col_book_start_date,bookEntry.col_book_end_date};

        return new CursorLoader(this,mCurrentBookUri,projection,null,null,null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        if(data==null || data.getCount()<1){
            return;
        }
        if(data.moveToFirst()){
            int titleColumnIndex = data.getColumnIndex(bookEntry.col_book_title);
            int authorColumnIndex = data.getColumnIndex(bookEntry.col_book_author);
            int pageColumnIndex = data.getColumnIndex(bookEntry.col_book_page);
            int startDateColumnIndex = data.getColumnIndex(bookEntry.col_book_start_date);
            int endDateColumnIndex = data.getColumnIndex(bookEntry.col_book_end_date);

            String title = data.getString(titleColumnIndex);
            String author = data.getString(authorColumnIndex);
            String page = data.getString(pageColumnIndex);
            String start = data.getString(startDateColumnIndex);
            String end = data.getString(endDateColumnIndex);

            mTitleEditText.setText(title);
            mAuthorEditText.setText(author);
            mpageEditText.setText(page);
            mStartDateEditText.setText(start);
            mEndDateEditText.setText(end);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        mTitleEditText.setText("");
        mAuthorEditText.setText("");
        mpageEditText.setText("");
        mStartDateEditText.setText("");
        mEndDateEditText.setText("");

    }

}
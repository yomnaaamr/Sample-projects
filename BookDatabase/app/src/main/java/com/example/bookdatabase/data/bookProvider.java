package com.example.bookdatabase.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.bookdatabase.data.bookContract.bookEntry;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class bookProvider extends ContentProvider {

    public static final String LOG_TAG = bookProvider.class.getSimpleName();
    private static final int books = 100;
    private static final int book_id = 101;
    private bookDbHelper dbHelper;

    private static final UriMatcher sUriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(bookContract.CONTENT_AUTHORITY,bookContract.Path_books,books);
        sUriMatcher.addURI(bookContract.CONTENT_AUTHORITY,bookContract.Path_books+"/#",book_id);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new bookDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match){
            case books:
                cursor = database.query(bookEntry.table_name,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case book_id:
                selection = bookEntry._ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(bookEntry.table_name,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can not query unknown uri"+uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match){
            case books:
                return bookEntry.content_list_type;
            case book_id:
                return bookEntry.content_item_type;
            default:
                throw new IllegalArgumentException("Unknown uri"+uri+"with match"+match);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);

        switch (match){
            case books:
                return insertBook(uri,contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for"+uri);
        }
    }

    private Uri insertBook(Uri uri, ContentValues values) {

        String title = values.getAsString(bookEntry.col_book_title);
        if(title==null){
            throw new IllegalArgumentException("book requires a title");
        }

        String author = values.getAsString(bookEntry.col_book_author);
        if(author==null){
            throw new IllegalArgumentException("book requires an author name");
        }

        Integer page = values.getAsInteger(bookEntry.col_book_page);
        if(page==null){
            throw new IllegalArgumentException("please enter the number of pages");
        }

        String start_date = values.getAsString(bookEntry.col_book_start_date);
        if(start_date==null){
            throw new IllegalArgumentException("please enter the start date");
        }

        String end_date = values.getAsString(bookEntry.col_book_end_date);
        if(end_date==null){
            throw new IllegalArgumentException("please enter the end date");
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id = database.insert(bookEntry.table_name,null,values);
        if(id==-1){
            Log.e(LOG_TAG,"failed to insert row for"+uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsDeleted ;
        final int match = sUriMatcher.match(uri);
        switch (match){
            case books:
                rowsDeleted = database.delete(bookEntry.table_name,selection,selectionArgs);
                break;
            case book_id:
                selection = bookEntry._ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(bookEntry.table_name,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for"+uri);
        }

        if(rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match){
            case books:
                return updateBook(uri,contentValues,selection,selectionArgs);
            case book_id:
                selection = bookEntry._ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri,contentValues,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for"+uri);
        }
    }

    private int updateBook(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if(contentValues.containsKey(bookEntry.col_book_title)){
            String title = contentValues.getAsString(bookEntry.col_book_title);
            if(title==null){
                throw new IllegalArgumentException("book requires a title");
            }
        }
        if(contentValues.containsKey(bookEntry.col_book_author)) {
            String author = contentValues.getAsString(bookEntry.col_book_author);
            if (author == null) {
                throw new IllegalArgumentException("book requires an author name");
            }
        }
        if(contentValues.containsKey(bookEntry.col_book_page)){
            Integer page = contentValues.getAsInteger(bookEntry.col_book_page);
            if(page==null){
                throw new IllegalArgumentException("please enter the number of pages");
            }
        }
        if(contentValues.containsKey(bookEntry.col_book_start_date)){
            String start_date = contentValues.getAsString(bookEntry.col_book_start_date);
            if(start_date==null){
                throw new IllegalArgumentException("please enter the start date");
            }
        }
        if (contentValues.containsKey(bookEntry.col_book_end_date)){
            String end_date = contentValues.getAsString(bookEntry.col_book_end_date);
            if(end_date==null){
                throw new IllegalArgumentException("please enter the end date");
            }
        }
        if(contentValues.size()==0){
            return 0;
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsUpdated = database.update(bookEntry.table_name,contentValues,selection,selectionArgs);
        if(rowsUpdated !=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
    }
}

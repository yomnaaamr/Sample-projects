package com.example.bookdatabase.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.bookdatabase.data.bookContract.bookEntry;

public class bookDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = bookDbHelper.class.getSimpleName();
    private static final String database_file_name = "books.db";
    private static final int database_version = 1;

    public bookDbHelper(@Nullable Context context) {
        super(context, database_file_name, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_books_TABLE =  "CREATE TABLE " + bookEntry.table_name + " ("
                + bookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + bookEntry.col_book_title + " TEXT NOT NULL, "
                + bookEntry.col_book_author + " TEXT, "
                + bookEntry.col_book_page + " INTEGER NOT NULL, "
                + bookEntry.col_book_start_date + " TEXT,"
                + bookEntry.col_book_end_date + " TEXT);";

        db.execSQL(SQL_CREATE_books_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

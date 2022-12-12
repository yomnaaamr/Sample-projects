package com.example.bookdatabase.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class bookContract {

    public bookContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.bookdatabase.data.bookProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String Path_books = "books";

    public static final class bookEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,Path_books);

        public static final String content_list_type = ContentResolver.CURSOR_DIR_BASE_TYPE +"/"
                +CONTENT_AUTHORITY +"/"+Path_books;
        public static final String content_item_type = ContentResolver.CURSOR_ITEM_BASE_TYPE +"/"
                +CONTENT_AUTHORITY +"/"+Path_books;
        public final static String table_name = "books";
        public final static String _ID = BaseColumns._ID;
        public final static String col_book_title = "title";
        public final static String col_book_author = "author";
        public final static String col_book_page = "pages";
        public final static String col_book_start_date = "starts";
        public final static String col_book_end_date = "ends";
    }

}

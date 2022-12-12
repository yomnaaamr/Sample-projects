package com.example.bookdatabase;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.bookdatabase.data.bookContract.bookEntry;

public class bookCursorAdapter extends CursorAdapter {

    public bookCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_layout,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView author = (TextView) view.findViewById(R.id.author);

        int titleColumnIndex = cursor.getColumnIndex(bookEntry.col_book_title);
        int authorColumnIndex = cursor.getColumnIndex(bookEntry.col_book_author);

        String bookName = cursor.getString(titleColumnIndex);
        String bookAuthor = cursor.getString(authorColumnIndex);

        if(TextUtils.isEmpty(bookAuthor)){
            bookAuthor = context.getString(R.string.unknown_author);
        }

        title.setText(bookName);
        author.setText(bookAuthor);

    }
}

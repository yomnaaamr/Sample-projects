package com.example.news;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class dataAdapter extends ArrayAdapter<data> {

    private static final String LOCATION_SEPARATOR = "T";


    public dataAdapter(Activity context, ArrayList<data> news) {
        super(context,0,news);
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        data current = getItem(position);
        String main = current.getName();
        String time = "";
        String date ="";



        View rootView = convertView;
        if(rootView==null){
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_items,parent,false);
        }


        if (main.contains(LOCATION_SEPARATOR)) {
            String[] parts = main.split(LOCATION_SEPARATOR);
            date = parts[0];
            time = parts[1];
        }



        ImageView imageView = rootView.findViewById(R.id.image);
        Picasso.get().load(current.getImage()).into(imageView);

        TextView textView = rootView.findViewById(R.id.time);
        textView.setText(date);


        TextView textView1 = rootView.findViewById(R.id.title);
        textView1.setText(current.getTitle());

        TextView textView2 = rootView.findViewById(R.id.content);
        textView2.setText(current.getContent());

        TextView textView3 = rootView.findViewById(R.id.author);
        textView3.setText(current.getAuthor());

        return rootView;
    }


//    /**
//     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
//     */
//    private String formatDate(Date dateObject) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
//        return dateFormat.format(dateObject);
//    }


    private String formatTime(Date dateObject) {
         SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}

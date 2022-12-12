package com.example.android.miwok.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.miwok.R;
import com.example.android.miwok.models.Word;

import java.util.ArrayList;

/**
 * Created by MFQ on 11/13/16.
 */

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ItemViewHolder> {


    Context mContext;
    private ArrayList<Word> mWords;


    /**
     * Resource ID for the background color for this list of words
     */
    private int mBackgroudColor;


    /**
     * Create a new {@link WordsAdapter} object.
     *
     * @param context         is the current context (i.e. Activity) that the adapter is being created in.
     * @param words           is the list of {@link Word}s to be displayed.
     * @param colorResourceId is the resource ID for the background color for this list of words
     */

    public WordsAdapter(Context context, ArrayList<Word> words, int colorResourceId) {
        mContext = context;
        mWords = words;
        mBackgroudColor = colorResourceId;
    }


    /**
     * create the view Holder to hold the Views in the Adapter
     */

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);

        return itemViewHolder;
    }

    /**
     * How Many Item we want to display ? return the number
     */

    @Override
    public int getItemCount() {
        return mWords.size();
    }

    /**
     * Bind the View from ViewHolder to display the real DATA
     */
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        holder.textViewEnglish.setText(mWords.get(position).getDefaultTranslationId());
        holder.textViewMiwok.setText(mWords.get(position).getMiwokTranslationId());

        // if there is no image ths value for the object mImageResourceId is 0 as we fill the array before in PhrasesFragment
        if (mWords.get(position).getImageResourceId() != 0)
            holder.imageView.setImageResource(mWords.get(position).getImageResourceId());
        else
            holder.imageView.setVisibility(View.GONE);


    }


    class ItemViewHolder extends RecyclerView.ViewHolder {


        private final TextView textViewMiwok, textViewEnglish;
        private final ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            textViewMiwok = (TextView) itemView.findViewById(R.id.miwok_text_view);
            textViewEnglish = (TextView) itemView.findViewById(R.id.default_text_view);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            itemView.setBackgroundColor(ContextCompat.getColor(mContext, mBackgroudColor));

            /**
             * Handle clicks on Items
             */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final MediaPlayer mediaPlayer = MediaPlayer.create(mContext, mWords.get(getLayoutPosition()).getAudioResourceId());

                    mediaPlayer.start();
                    // delay method 2 seconds to waite for the media player to finish talking and release it in order to save resources .
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer.release();
                        }
                    }, 2000);

                }
            });
        }
    }
}

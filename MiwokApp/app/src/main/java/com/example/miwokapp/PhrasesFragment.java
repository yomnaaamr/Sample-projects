package com.example.miwokapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.miwokapp.R;
import com.example.miwokapp.Word;
import com.example.miwokapp.WordsAdapter;

import java.util.ArrayList;

/**
 * {@link Fragment} that displays a list of phrases.
 */
public class PhrasesFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);


        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        // 0 here meaning no image to display you will handle this falg in the adapter in order not to display an Img for this Item
        words.add(new Word(R.string.phrase_where_are_you_going,
                R.string.miwok_phrase_where_are_you_going, 0,R.raw.phrase_where_are_you_going));
        words.add(new Word(R.string.phrase_what_is_your_name,
                R.string.miwok_phrase_what_is_your_name,0, R.raw.phrase_what_is_your_name));
        words.add(new Word(R.string.phrase_my_name_is,
                R.string.miwok_phrase_my_name_is, 0,R.raw.phrase_my_name_is));
        words.add(new Word(R.string.phrase_how_are_you_feeling,
                R.string.miwok_phrase_how_are_you_feeling,0, R.raw.phrase_how_are_you_feeling));
        words.add(new Word(R.string.phrase_im_feeling_good,
                R.string.miwok_phrase_im_feeling_good,0, R.raw.phrase_im_feeling_good));
        words.add(new Word(R.string.phrase_are_you_coming,
                R.string.miwok_phrase_are_you_coming,0, R.raw.phrase_are_you_coming));
        words.add(new Word(R.string.phrase_yes_im_coming,
                R.string.miwok_phrase_yes_im_coming,0, R.raw.phrase_yes_im_coming));
        words.add(new Word(R.string.phrase_im_coming,
                R.string.miwok_phrase_im_coming,0, R.raw.phrase_im_coming));
        words.add(new Word(R.string.phrase_lets_go,
                R.string.miwok_phrase_lets_go,0, R.raw.phrase_lets_go));
        words.add(new Word(R.string.phrase_come_here,
                R.string.miwok_phrase_come_here,0, R.raw.phrase_come_here));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordsAdapter adapter = new WordsAdapter(getActivity(), words, R.color.category_phrases);
        // Find the {@link RecyclerView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link RecyclerView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list);

        // Make the {@link RecyclerView} use the {@link WordAdapter} we created above, so that the
        // {@link RecyclerView} will display list items for each {@link Word} in the list. using a layout manger to display items in vertical lines
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);


        return rootView;
    }
}
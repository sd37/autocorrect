package Algorithms;

import Dictionary.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spandan on 9/2/14.
 */
public class NoSuggestion implements Suggestion {

    private Dictionary dict = null;

    public NoSuggestion(Dictionary dict) {
        this.dict = dict;
        assert (this.dict != null);
    }

    @Override
    public List<String> getSuggestions(String word) {
        List<String> sug_list = new ArrayList<>();

        if (this.dict.search(word))
            sug_list.add(word);

        return sug_list;
    }
}

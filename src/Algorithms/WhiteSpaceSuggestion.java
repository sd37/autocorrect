package Algorithms;

import Dictionary.Dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spandan on 8/31/14.
 */
public class WhiteSpaceSuggestion implements Suggestion {

    private Dictionary dict = null;

    public WhiteSpaceSuggestion(Dictionary dict) {
        this.dict = dict;
        assert (this.dict != null);
    }

    @Override
    public List<String> getSuggestions(String word) {

        List<String> sug = new ArrayList<>();

        for (int i = 1; i < word.length() - 1; i++) {
            String first = word.substring(0, i);
            String second = word.substring(i);

            if (this.dict.search(first.trim().toLowerCase()) &&
                    this.dict.search(second.trim().toLowerCase()))
                sug.add(first + " " + second);
        }

        return sug;
    }
}

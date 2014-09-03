package Algorithms;

import Dictionary.Dictionary;
import Dictionary.TrieNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spandan on 8/30/14.
 */
public class PrefixSuggestion implements Suggestion {

    private Dictionary dict = null;

    public PrefixSuggestion(Dictionary dict) {
        this.dict = dict;
        assert (this.dict != null);
    }

    private void dfs(TrieNode root, String root_ch, String prev_str, List<String> sug_list) {

        prev_str += root_ch;

        if (root.getFreq() != 0)
            sug_list.add(prev_str.trim());

        for (Character k : root.getChildren().keySet()) {
            dfs(root.getChild(k), "" + k, prev_str, sug_list);
        }
    }

    @Override
    public List<String> getSuggestions(String word) {

        String tmp_word = "";

        TrieNode pcrawl = this.dict.getRoot();
        assert (pcrawl != null);
        Character ch = null;

        int i;
        for (i = 0; i < word.length(); i++) {

            ch = word.charAt(i);
            tmp_word += ch;

            if (pcrawl.getChild(ch) == null) {
                break;
            }

            pcrawl = pcrawl.getChild(ch);
        }

        assert (pcrawl != null);
        assert (ch != null);

        List<String> sug_list = new ArrayList<>();

        if (i != word.length())
            return sug_list;

        // get all the strings with this common prefix.

        dfs(pcrawl, "", tmp_word.substring(0, i), sug_list);

        return sug_list;
    }
}

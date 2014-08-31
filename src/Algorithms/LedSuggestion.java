package Algorithms;

import Dictionary.Dictionary;
import Dictionary.TrieNode;
import com.BoxOfC.LevenshteinAutomaton.LevenshteinAutomaton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spandan on 8/31/14.
 */
public class LedSuggestion implements Suggestion {

    Dictionary dict = null;
    int ed = 0;

    public LedSuggestion(Dictionary dict, int ed) {
        this.dict = dict;
        assert(this.dict != null);
        this.ed = ed;
    }

    private void dfs(TrieNode root, String root_ch, String prev_str, List<String> sug_list,String word) {

        prev_str += root_ch;

        int myeditd = LevenshteinAutomaton.computeEditDistance(prev_str,word);

        if (root.getFreq() != 0 && myeditd <= this.ed) {

            sug_list.add(prev_str.trim());
        }

        for (Character k : root.getChildren().keySet()) {
            dfs(root.getChild(k), "" + k, prev_str, sug_list,word);
        }
    }

    @Override
    public List<String> getSuggestions(String word) {
        List<String> sug_list = new ArrayList<>();
        dfs(this.dict.getRoot(), "", "", sug_list, word);
        return sug_list;
    }
}

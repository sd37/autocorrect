package Algorithms;

import Dictionary.Dictionary;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by spandan on 8/31/14.
 */
public class SmartRanking implements Ranking {
    private Dictionary dict = null;
    private Map<String, Integer> bigram_freq = null;

    public SmartRanking(Dictionary dict, Map<String, Integer> bigram_freq) {
        this.dict = dict;
        assert (this.dict != null);
        this.bigram_freq = bigram_freq;
        assert(bigram_freq != null);
    }

    @Override
    public List<String> getRanking(String line, String word, Set<String> sug) {
        return null;
    }
}

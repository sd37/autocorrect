package Algorithms;

import Dictionary.Dictionary;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by spandan on 8/31/14.
 */
public class SmartRanking implements Ranking {
    private Dictionary dict = null;
    private Map<String, Integer> bigram_freq = null;
    private Ranking rank = null;

    public SmartRanking(Dictionary dict, Map<String, Integer> bigram_freq) {
        this.rank = new DefaultRanking(dict, bigram_freq);
        assert(this.rank != null);
    }

    @Override
    public List<String> getRanking(String line, String word, Set<String> sug) {

        List<String> rank_sug = rank.getRanking(line, word, sug);

        return rank_sug;
    }
}

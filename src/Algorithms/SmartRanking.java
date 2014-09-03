package Algorithms;

import Dictionary.Dictionary;

import java.util.*;

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
        this.bigram_freq = bigram_freq;
        assert (this.bigram_freq != null);
    }

    @Override
    public List<String> getRanking(String line, String word, Set<String> sug) {
        List<String> rank_sug = rank.getRanking(line, word, sug);
        List<String> new_rank_sug = new ArrayList<>();

        /* sort the results according to the len of the words so that
           the user has to type less.
        */


        for (int i = 0; i < rank_sug.size() && i < 5; i++)
            new_rank_sug.add(rank_sug.get(i));

        if (new_rank_sug.size() < 2)
            return new_rank_sug;

        Collections.sort(new_rank_sug.subList(1,new_rank_sug.size()), new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() < o2.length())
                    return -1;
                else if(o1.length() > o2.length())
                    return 1;
                else
                    return o1.compareTo(o2);
            }
        });

        return new_rank_sug;
    }
}

package Algorithms;

import Dictionary.Dictionary;

import java.util.*;

/**
 * Created by spandan on 8/31/14.
 */
public class DefaultRanking implements Ranking {
    private Dictionary dict = null;
    private Map<String, Integer> bigram_freq = null;

    public DefaultRanking(Dictionary dict, Map<String, Integer> bigram_freq) {
        this.dict = dict;
        assert (this.dict != null);
        this.bigram_freq = bigram_freq;
        assert (this.bigram_freq != null);
    }

    @Override
    public List<String> getRanking(String line, String word, Set<String> sug) {

        List<String> ranked_sug = new ArrayList<>();
        int num_words = UserInputParseUtil.numOfWords(line);

        assert (num_words != 0);

        if (sug.contains(word)) {
            ranked_sug.add(word);
            sug.remove(word);
        }

        TreeMap<Integer, List<String>> sug_order = new TreeMap<>();

        if (num_words == 1) {

            // use unigram frequency.

            for (String s : sug) {

                int value = -1;

                if (UserInputParseUtil.numOfWords(s) > 1) {
                    String[] parts = s.trim().split("\\s+");
                    value = this.dict.searchAndValue(parts[0]);
                } else
                    value = this.dict.searchAndValue(s);

                if (!sug_order.containsKey(value)) {
                    List<String> tmp_list = new ArrayList<>();
                    tmp_list.add(s);
                    sug_order.put(value, tmp_list);
                } else {
                    sug_order.get(value).add(s);
                }
            }

            for (Integer k : sug_order.keySet()) {
                Collections.sort(sug_order.get(k));
            }

        } else {

            assert (num_words > 1);

            String sec_last_word = UserInputParseUtil.SecondLastWord(line);

            // use bigram frequency.

            for (String s : sug) {
                String tmp_string = sec_last_word + " " + s;
                Integer value = null;

                if (UserInputParseUtil.numOfWords(s) > 1) {
                    String[] parts = s.trim().split("\\s+");
                    value = this.bigram_freq.get(sec_last_word + " " + parts[0]);
                } else
                    value = this.bigram_freq.get(tmp_string);

                Integer tmp_value = value == null ? 0 : value;

                if (!sug_order.containsKey(tmp_value)) {
                    List<String> tmp_list = new ArrayList<>();
                    tmp_list.add(s);
                    sug_order.put(tmp_value, tmp_list);
                } else {
                    sug_order.get(tmp_value).add(s);
                }
            }

            for (Integer k : sug_order.keySet()) {
                List<String> k_list = sug_order.get(k);

                Collections.sort(k_list, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        int o1_value = -1;
                        int o2_value = -1;

                        if (UserInputParseUtil.numOfWords(o1) > 1) {
                            String[] parts = o1.trim().split("\\s+");
                            o1_value = dict.searchAndValue(parts[0]);
                        } else
                            o1_value = dict.searchAndValue(o1);

                        if (UserInputParseUtil.numOfWords(o2) > 1) {
                            String[] parts = o2.trim().split("\\s+");
                            o2_value = dict.searchAndValue(parts[0]);
                        } else
                            o2_value = dict.searchAndValue(o2);

                        assert (o1_value != 0 && o2_value != 0);

                        if (o1_value < o2_value)
                            return 1;
                        else if (o1_value > o2_value)
                            return -1;
                        else
                            return o1.compareTo(o2);
                    }
                });
            }
        }

        for (Integer k : sug_order.descendingKeySet()) {
            List<String> word_list = sug_order.get(k);
            for (String s : word_list) {
                ranked_sug.add(s);
            }
        }

        return ranked_sug;
    }
}

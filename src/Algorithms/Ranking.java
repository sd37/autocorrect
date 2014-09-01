package Algorithms;

import java.util.List;
import java.util.Set;

/**
 * Created by spandan on 8/30/14.
 */
public interface Ranking {
    List<String> getRanking(String line, String word, Set<String> sug);
}

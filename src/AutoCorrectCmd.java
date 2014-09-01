import Algorithms.Ranking;
import Algorithms.Suggestion;
import Algorithms.UserInputParseUtil;
import Dictionary.Dictionary;

import java.util.*;

/**
 * Created by spandan on 8/30/14.
 */
public class AutoCorrectCmd {
    public AutoCorrectCmd(Dictionary dict, List<Suggestion> sug, Ranking rank) {

        System.out.println("Ready");

        Scanner in = new Scanner(System.in);

        while (true) {

            String line = in.nextLine();
            if (line == null)
                System.exit(0);

            String new_line = line.replaceAll("[^a-zA-Z]", " ").toLowerCase().trim();
            String word = UserInputParseUtil.LastWord(new_line);

            Set<String> overall_sug = new HashSet<>();

            for (Suggestion s : sug) {
                List<String> sug_list = s.getSuggestions(word);
                overall_sug.addAll(sug_list);
            }

            List<String> ranked_overall_sug = rank.getRanking(new_line, word, overall_sug);
            //List<String> ranked_overall_sug = new ArrayList<>(overall_sug);

            for (String w : ranked_overall_sug) {
                System.out.println(w);
            }

            System.out.println("");
        }
    }
}

import Algorithms.Ranking;
import Algorithms.Suggestion;
import Algorithms.UserInputParseUtil;
import Dictionary.Dictionary;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by spandan on 8/30/14.
 */
public class AutoCorrectCmd {
    public AutoCorrectCmd(Dictionary dict, List<Suggestion> sug, Ranking rank) {

        if (dict.numOfKeys() != 0)
            System.out.println("Ready");
        else
            System.out.println("ERROR:");

        Scanner in = new Scanner(System.in);

        while (true) {

            String line = in.nextLine();
            assert(line == null);

            String tmp_line = line.replaceAll("[^a-zA-Z]", " ").toLowerCase();

            if (tmp_line.length() != 0 && tmp_line.charAt(tmp_line.length() - 1) == ' ') {
                System.out.println("");
                continue;
            }

            String new_line = tmp_line.trim().replaceAll(" +", " ");

            String word = UserInputParseUtil.LastWord(new_line);
            int word_index = new_line.lastIndexOf(word);

            Set<String> overall_sug = new HashSet<>();

            for (Suggestion s : sug) {
                List<String> sug_list = s.getSuggestions(word);
                overall_sug.addAll(sug_list);
            }

            List<String> ranked_overall_sug = rank.getRanking(new_line, word, overall_sug);
            //List<String> ranked_overall_sug = new ArrayList<>(overall_sug);

            for (int i = 0; i < ranked_overall_sug.size() && i < 5; i++) {
                String w = ranked_overall_sug.get(i);
                String sug_str = new_line.substring(0, word_index) + w;
                System.out.println(sug_str.trim().replaceAll(" +", " "));
            }

            System.out.println("");
        }
    }
}

import Algorithms.Suggestion;
import Dictionary.Dictionary;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by spandan on 8/30/14.
 */
public class AutoCorrectCmd {
    public AutoCorrectCmd(Dictionary dict, List<Suggestion> sug) {

        System.out.println("Ready");

        Scanner in = new Scanner(System.in);

        while(true){

            String line = in.nextLine();
            if(line == null)
                System.exit(0);

            String new_line = line.replaceAll("[^a-zA-Z]", " ").toLowerCase().trim();
            String word = UserInputParseUtil.LastWord(new_line);

            Set<String> overall_sug = new HashSet<>();

            for (Suggestion s : sug){
                List<String> sug_list = s.getSuggestions(word);
                overall_sug.addAll(sug_list);
            }

            for (String w : overall_sug){
                System.out.println(w);
            }

            System.out.println("");
        }
    }
}

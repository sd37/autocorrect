import Algorithms.*;
import Dictionary.DictTrieImpl;
import Dictionary.Dictionary;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by spandan on 8/27/14.
 */
public class AutoCorrectApp extends Application {

    static boolean gui_enabled = false;
    static List<String> dict_paths = new ArrayList<>();
    static Dictionary dict = null;
    static List<Suggestion> sug = new ArrayList<>();
    static Ranking rank = null;
    static Map<String, Integer> bigram_freq = null;

    public static void main(String[] args) {

        dict = new DictTrieImpl();

        boolean is_smart = false;

        for (int i = 0; i < args.length; i++) {

            switch (args[i]) {

                case "--led":
                    int edit_distance = Integer.parseInt(args[++i]);
                    sug.add(SuggestionFactory.createLedSuggestion(dict, edit_distance));
                    break;

                case "--prefix":
                    sug.add(SuggestionFactory.createPrefixSuggestion(dict));
                    break;

                case "--whitespace":
                    sug.add(SuggestionFactory.createWhiteSpaceSuggestion(dict));
                    break;

                case "--smart":
                    is_smart = true;
                    break;

                case "--gui":
                    gui_enabled = true;
                    break;

                default:
                    // read the dictionary paths.
                    dict_paths.add(args[i]);
            }
        }

        if (sug.size() == 0)
            sug.add(SuggestionFactory.createNoSuggestion(dict));

        bigram_freq = ComputeProb.computeBigramFreq(dict_paths);
        rank = is_smart ? new SmartRanking(dict, bigram_freq) : new DefaultRanking(dict, bigram_freq);

        // construct the dictionary

        for (int i = 0; i < dict_paths.size(); i++) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(dict_paths.get(i)));
            } catch (FileNotFoundException e) {
                System.out.println("ERROR:");
                continue;
            }

            assert br != null;
            String line = null;

            try {
                while ((line = br.readLine()) != null) {
                    String new_line = line.replaceAll("[^a-zA-Z]", " ").toLowerCase().trim();

                    if (new_line.equals(""))
                        continue;

                    String[] parts = new_line.split("\\s+");

                    for (int j = 0; j < parts.length; j++) {
                        dict.add(parts[j].trim());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }

        if (gui_enabled)
            launch(args);
        else
            new AutoCorrectCmd(dict, sug, rank);
    }

    @Override
    public void start(Stage stage) throws Exception {
        assert stage != null;
        new AutoCorrectGui(dict, sug);
    }
}
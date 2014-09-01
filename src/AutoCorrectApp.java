import Algorithms.*;
import Dictionary.DictTrieImpl;
import Dictionary.Dictionary;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedReader;
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
    static Map<String,Integer> bigram_freq = null;

    public static void main(String[] args) throws IOException {

        dict = new DictTrieImpl();
        System.out.println("Construction Bigram Model");

        bigram_freq = ComputeProb.computeBigramFreq();

        rank = new DefaultRanking(dict,bigram_freq);

        System.out.println("Finished Construction Bigram Model");

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
                    rank = new SmartRanking(dict,bigram_freq);
                    break;

                case "--gui":
                    gui_enabled = true;
                    break;

                default:
                    // read the dictionary paths.
                    dict_paths.add(args[i]);
            }
        }

        // construct the dictionary

        System.out.println("Constructing dictionary ..");

        for (int i = 0; i < dict_paths.size(); i++) {
            BufferedReader br = new BufferedReader(new FileReader(dict_paths.get(i)));
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
                br.close();
            }
        }

        System.out.println("Finished Constructing dictionary.");

        if(gui_enabled)
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
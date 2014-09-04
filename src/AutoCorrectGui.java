import Algorithms.Ranking;
import Algorithms.Suggestion;
import Dictionary.Dictionary;
import javafx.application.Platform;

import java.util.List;

/**
 * Created by spandan on 8/30/14.
 */
public class AutoCorrectGui {
    public AutoCorrectGui(Dictionary dict, List<Suggestion> sug, Ranking rank) {
        System.out.println("GUI MODE ..");
        Platform.runLater(new User(dict, sug, rank));
    }
}

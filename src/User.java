import Algorithms.Ranking;
import Algorithms.Suggestion;
import Algorithms.UserInputParseUtil;
import Dictionary.Dictionary;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.border.Border;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by spandan on 9/3/14.
 */
public class User implements Runnable {

    Dictionary dict = null;
    List<Suggestion> sug = null;
    Ranking rank = null;

    public User(Dictionary dict, List<Suggestion> sug_list, Ranking rank) {
        this.dict = dict;
        this.sug = sug_list;
        this.rank = rank;
    }

    @Override
    public void run() {

        String status = "";

        if (dict.numOfKeys() != 0)
            status = "Ready";
        else
            status = "ERROR: No dictionary loaded";

        Stage stage = new Stage();

        VBox vbox = new VBox();

        final TextField field = new TextField();
        final TextArea  area  = new TextArea(status);
        area.setStyle("-fx-background-color: DARKGRAY;"
                + "-fx-text-fill: BLACK;"
                + "-fx-font-size: 14pt;");

        field.setStyle("-fx-background-color: WHEAT;"
                + "-fx-text-fill: BLACK;"
                + "-fx-font-size: 14pt;");

        area.setEditable(false);

        vbox.getChildren().addAll(field,area);

        Scene scene = new Scene(vbox);

        stage.setScene(scene);
        stage.setTitle("AutoComplete");
        stage.show();

        field.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                final String line = field.getText() + keyEvent.getCharacter();

                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        String tmp_line = line.replaceAll("[^a-zA-Z]", " ").toLowerCase();

                        if (tmp_line.length() != 0 && tmp_line.charAt(tmp_line.length() - 1) == ' ') {
                            Platform.runLater(new TextAreaUpdate(area, "no suggestion"));
                            return null;
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
                        String area_str = "";

                        for (int i = 0; i < ranked_overall_sug.size() && i < 5; i++) {
                            String w = ranked_overall_sug.get(i);
                            String sug_str = new_line.substring(0, word_index) + w;
                            area_str += sug_str + "\n";
                        }

                        Platform.runLater(new TextAreaUpdate(area, area_str));
                        return null;
                    }
                };

                Thread bg_thrd = new Thread(task);
                bg_thrd.setDaemon(true);
                bg_thrd.start();

            }
        });
    }
}

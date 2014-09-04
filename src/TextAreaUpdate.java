import javafx.scene.control.TextArea;

/**
 * Created by spandan on 9/3/14.
 */
public class TextAreaUpdate implements Runnable {

    private String area_string = null;
    private TextArea area = null;

    public TextAreaUpdate(TextArea area, String s) {
        this.area = area;
        assert (this.area != null);
        this.area_string = s;
        assert (this.area_string != null);
    }

    @Override
    public void run() {
        this.area.clear();
        this.area.setText(this.area_string);
    }
}

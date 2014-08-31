/**
 * Created by spandan on 8/31/14.
 */
public class UserInputParseUtil {
    public static String LastWord(String s) {
        if(s == null)
            return "";

        String[] parts = s.trim().split("\\s+");
        return parts[parts.length - 1];
    }
}

package Algorithms;

/**
 * Created by spandan on 8/31/14.
 */
public class UserInputParseUtil {
    public static String LastWord(String s) {
        if (s == null)
            return "";

        String[] parts = s.trim().split("\\s+");
        return parts[parts.length - 1];
    }

    public static String SecondLastWord(String s) {
        if(s == null || numOfWords(s) < 2)
            return "";

        String[] parts = s.trim().split("\\s+");
        return parts[parts.length - 2];
    }

    public static Integer numOfWords(String s) {
        if (s == null)
            return 0;

        String new_s = s.trim();

        if (new_s.equals(""))
            return 0;

        String[] parts = new_s.split("\\s+");

        return parts.length;
    }
}

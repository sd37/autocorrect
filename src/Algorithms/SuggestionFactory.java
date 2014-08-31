package Algorithms;

import Dictionary.Dictionary;

/**
 * Created by spandan on 8/30/14.
 */
public class SuggestionFactory {
    public static Suggestion createPrefixSuggestion(Dictionary dict) {
        return new PrefixSuggestion(dict);
    }

    public static Suggestion createLedSuggestion(Dictionary dict, int ed){
        return new LedSuggestion(dict, ed);
    }

    public static Suggestion createWhiteSpaceSuggestion(Dictionary dict){
        return new WhiteSpaceSuggestion(dict);
    }
}
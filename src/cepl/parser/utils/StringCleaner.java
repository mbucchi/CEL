package cepl.parser.utils;

import java.util.Set;

public class StringCleaner {

    private static final Set<Character> validQuotes = Set.of('\'', '"', '`');

    public static String removeQuotes(String string) {

        char quote = string.charAt(0);
        if (!validQuotes.contains(quote)) {
            throw new Error("Badly formatted quoted string: <" + quote + "> is not a valid quote character.");
        }
        if (string.charAt(string.length()) != quote) {
            throw new Error("Badly formatted quoted string: quote characters not present at both ends.");
        }
        return string.substring(1, string.length() - 1);
    }

    public static boolean hasQuotes(String string) {
        char quote = string.charAt(0);
        return validQuotes.contains(quote) && string.charAt(string.length() - 1) == quote;
    }
}
package org.jabref.logic.formatter.bibtexfields;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jabref.logic.l10n.Localization;
import org.jabref.model.cleanup.Formatter;

public class RegexFormatter implements Formatter {

    private static final Pattern PATTERN_ESCAPED_OPENING_CURLY_BRACE = Pattern.compile("\\\\\\{");

    private static final Pattern PATTERN_ESCAPED_CLOSING_CURLY_BRACE = Pattern.compile("\\\\\\}");

    // RegEx to match {...}
    // \\ is required to have the { interpreted as character
    // ? is required to disable the aggressive match
    private static final Pattern PATTERN_ENCLOSED_IN_CURLY_BRACES = Pattern.compile("(\\{.*?})");

    // Magic arbitrary unicode char, which will never appear in bibtex files
    private static final String PLACEHOLDER_FOR_PROTECTED_GROUP = Character.toString('\u0A14');

    private static final String PLACEHOLDER_FOR_OPENING_CURLY_BRACE = Character.toString('\u0A15');

    private static final String PLACEHOLDER_FOR_CLOSING_CURLY_BRACE = Character.toString('\u0A16');

    // stores the regex set by setRegex
    private static String[] regex;

    @Override
    public String getName() {
        return Localization.lang("Regex");
    }

    @Override
    public String getKey() {
        return "regex";
    }

    private String replaceHonoringProtectedGroups(final String input) {
        Matcher matcher = PATTERN_ENCLOSED_IN_CURLY_BRACES.matcher(input);

        List<String> replaced = new ArrayList<>();
        while (matcher.find()) {
            replaced.add(matcher.group(1));
        }
        String workingString = matcher.replaceAll(PLACEHOLDER_FOR_PROTECTED_GROUP);
        workingString = workingString.replaceAll(RegexFormatter.regex[0], RegexFormatter.regex[1]);

        for (String r : replaced) {
            workingString = workingString.replaceFirst(PLACEHOLDER_FOR_PROTECTED_GROUP, r);
        }
        return workingString;
    }

    @Override
    public String format(final String input) {
        if (regex == null) {
            return input;
        }

        Matcher matcherOpeningCurlyBrace = PATTERN_ESCAPED_OPENING_CURLY_BRACE.matcher(input);
        final String openingCurlyBraceReplaced = matcherOpeningCurlyBrace.replaceAll(PLACEHOLDER_FOR_OPENING_CURLY_BRACE);

        Matcher matcherClosingCurlyBrace = PATTERN_ESCAPED_CLOSING_CURLY_BRACE.matcher(openingCurlyBraceReplaced);
        final String closingCurlyBraceReplaced = matcherClosingCurlyBrace.replaceAll(PLACEHOLDER_FOR_CLOSING_CURLY_BRACE);

        final String regexApplied = replaceHonoringProtectedGroups(closingCurlyBraceReplaced);

        return regexApplied
                .replaceAll(PLACEHOLDER_FOR_OPENING_CURLY_BRACE, "\\\\{")
                .replaceAll(PLACEHOLDER_FOR_CLOSING_CURLY_BRACE, "\\\\}");
    }

    @Override
    public String getDescription() {
        return Localization.lang("Add a regular expression for the key pattern.");
    }

    @Override
    public String getExampleInput() {
        return "Please replace the spaces";
    }

    public static void setRegex(String rex) {
        // formatting is like ("exp1","exp2"), we want to remove (" and ")
        rex = rex.substring(2, rex.length() - 2);
        String[] parts = rex.split("\",\"");
        regex = parts;
    }

}

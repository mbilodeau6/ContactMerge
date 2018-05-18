package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class PropertyMatchingHelpers {
    private static final int MIN_PART_LENGTH_TO_MAKE_YES_MATCH = 3;

    public static Collection<String> splitPropertyString(String propertyValue) {
        assert(propertyValue != null);

        return Arrays.asList(propertyValue.split("[^a-zA-Z]")).stream()
                .filter(x -> !x.isEmpty()).map(x -> x.toLowerCase()).collect(Collectors.toList());
    }

    public static AnswerType doPropertyPartsMatch(Collection<String> sourceCollection, Collection<String> targetCollection) {
        assert(sourceCollection != null);
        assert(targetCollection != null);

        int maxFoundLength = 0;

        for(String part: sourceCollection) {
            if (targetCollection.contains(part)) {
                maxFoundLength = Math.max(maxFoundLength, part.length());
            }
        }

        return maxFoundLength == 0 ? AnswerType.no :
                (maxFoundLength > MIN_PART_LENGTH_TO_MAKE_YES_MATCH ? AnswerType.yes : AnswerType.maybe);
    }

    public static AnswerType doPropertyPartsMatch(String sourceString, String targetString) {
        assert(sourceString != null);
        assert(targetString!= null);

        return sourceString.equalsIgnoreCase(targetString) ? AnswerType.yes : AnswerType.no;
    }

    public static boolean doesInitialMatchWithName(char initial, String name) {
        assert(name != null);

        return name.length() > 0 && name.toLowerCase().charAt(0) == Character.toLowerCase(initial);
    }
}

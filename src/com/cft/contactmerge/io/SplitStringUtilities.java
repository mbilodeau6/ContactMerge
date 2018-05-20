package com.cft.contactmerge.io;

import java.util.*;
import java.util.stream.Collectors;

public class SplitStringUtilities {
    public static List<String> splitTsvString(String input)
    {
        List<String> parts = Arrays.asList(input.split("\t", -1));

        return parts.stream().map(x -> x.trim()).collect(Collectors.toList());
    }

    public static List<String> splitCsvString(String input)
    {
        // from https://stackoverflow.com/questions/15738918/splitting-a-csv-file-with-quotes-as-text-delimiter-using-string-split
        List<String> parts = Arrays.asList(input.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1))
                .stream().map(x -> x.trim()).collect(Collectors.toList());

        List<String> newParts = new ArrayList<String>();

        // TODO: Feels like I should be able to do this with a lambda function but have failed to find one that works.
        for (String part: parts) {
            if (part.startsWith("\"") && part.endsWith("\"")) {
                // adapted from https://stackoverflow.com/questions/2088037/trim-characters-in-java
                newParts.add(part.replaceAll("\"$|^\"", "").trim());
            }
            else {
                newParts.add(part.trim());
            }
        }

        return newParts;
    }
}

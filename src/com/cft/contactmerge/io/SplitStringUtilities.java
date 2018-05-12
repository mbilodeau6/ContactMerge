package com.cft.contactmerge.io;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.*;

public class SplitStringUtilities {
    public static List<String> splitTsvString(String input)
    {
        List<String> x = new ArrayList<String>();

        String testString = input;
        String _regex = "[\t]";

        String[] _split = testString.split(_regex);

        for(int i = 0;i< _split.length;i++){
            x.add(_split[i]);
        }




        return x;
    }

    public static List<String> splitCsvString(String input)
    {
        List<String> x = new ArrayList<String>();

        String testString = input;
        String _regex = "[,]";

        String[] _split = testString.split(_regex);

        for(int i = 0;i< _split.length;i++){
            x.add(_split[i]);
        }

        return x;

    }
}

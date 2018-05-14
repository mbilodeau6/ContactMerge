package com.cft.contactmerge.io;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import java.util.*;

public class SplitStringUtilities {
    public static List<String> splitTsvString(String input) {
        List<String> x = new ArrayList<String>();

        String testString = input;

        String _string = new String("");
        for(int i = 0;i< testString.length();i++){
            if(input.charAt(i)=='\t'){
                x.add(_string);
                _string = "";
            }
            else {
                _string= _string+input.charAt(i);
            }
        }
        x.add(_string);
        //test
        //////////Remove white space before any character, and after the last character in the strings.

        List<String> y = new ArrayList<String>();

        for(ListIterator itb = x.listIterator();itb.hasNext();){
            String strToCheck = itb.next().toString();

            for(int j = 0;j<2;j++) {
                String wsf = "";
                boolean iwsf = true;


                for (int i = 0; i < strToCheck.length(); i++) {
                    if (strToCheck.charAt(i) == ' ') {

                    } else {
                        iwsf = false;
                    }

                    if (iwsf == false) {
                        wsf += strToCheck.charAt(i);
                    }
                }
                strToCheck = wsf;
                StringBuilder strb = new StringBuilder(strToCheck);

                strToCheck = strb.reverse().toString();
            }
            y.add(strToCheck);
        }

        return y;
    }

    public static List<String> splitCsvString(String input) {
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

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



        boolean isQuote = false;
        boolean isComma = false;
        boolean isTextBefore = false;
        boolean isTextAfter = false;
        boolean isSpace = false;
        int index = 0;
        String part = new String("");

        if(index<testString.length()) {
            while (index < testString.length()) {

                switch (testString.charAt(index)) {
                    case '\"'://then we are encapsulated
                        isQuote = true;
                        part = "";
                        break;
                    case ',':
                        isComma = true;
                        break;
                    case ' ':
                        isSpace = true;
                        break;
                }
                if ((isComma == false) && (isQuote == false)) {
                    isTextBefore = true;
                }

                if (isQuote) {
                    index++;
                    for (; (index < testString.length()) && (isQuote == true); index++) {
                        if (testString.charAt(index) == '\"') {
                            isQuote = false;//break the quotations
                            x.add(part);
                            part = "";

                        } else {
                            part = part + testString.charAt(index);
                        }
                    }
                    isTextAfter = true;
                    for (; index < testString.length() && isTextAfter; index++) { //eat up text after end quote
                        if (testString.charAt(index) == ',') {
                            isTextAfter = false;
                        }
                    }
                }
                if (isTextBefore) {
                    isComma = true;
                    for (; (index < testString.length()) && (isComma == true); index++) {
                        if (testString.charAt(index) == ',') {
                            isComma = false;//break the quotations


                        } else {
                            part = part + testString.charAt(index);
                        }
                    }
                    x.add(part);
                    part = "";
                }

                if(isTextBefore){

                }
                else
                {
                    index++;
                }

              isQuote = false;
              isComma = false;
              isTextBefore = false;
              isTextAfter = false;
              isSpace = false;


            }

        }else{
            x.add(part);
        }


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
}

package com.cft.contactmerge.contact.tests;

import com.cft.contactmerge.AnswerType;
import com.cft.contactmerge.contact.PropertyMatchingHelpers;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyMatchingHelpersTest {
    // from https://stackoverflow.com/questions/4930939/preferred-idiom-for-joining-a-collection-of-strings-in-java
    private String joinCollectionOfStrings(Collection<String> collection, String delimeter)
    {
        Iterator<String> iterator = collection.iterator();
        StringBuilder sb = new StringBuilder();

        if (iterator.hasNext()) {
            sb.append(iterator.next());

            while (iterator.hasNext()) {
                sb.append(delimeter).append(iterator.next());
            }
        }

        return sb.toString();
    }

    @Test
    void splitPropertyString_EmptyString() {
        Collection<String> parts = PropertyMatchingHelpers.splitPropertyString("");

        assertEquals(0, parts.size());
    }

    @Test
    void splitPropertyString_SinglePart() {
        Collection<String> parts = PropertyMatchingHelpers.splitPropertyString("Red");

        assertEquals(1, parts.size(), "Verify parts count");
        assertEquals("red", parts.iterator().next(), "Verify parts[0] value");
    }

    private void testSplitWithSpecificDelimeter(String delimeter) {
        Collection<String> expectedParts = Arrays.asList("Red", "Green", "Blue");
        String testString = joinCollectionOfStrings(expectedParts, delimeter);

        Collection<String> parts = PropertyMatchingHelpers.splitPropertyString(testString);

        assertEquals(expectedParts.size(), parts.size(), "Verify part count");

        int i = 0;
        for (String part: parts) {
            assertEquals(((List<String>) expectedParts).get(i).toLowerCase(), part, String.format("Verify parts[%d] value", i));
            i++;
        }
    }

    @Test
    void splitPropertyString_MultipleParts_Spaces() {
        testSplitWithSpecificDelimeter(" ");
    }

    @Test
    void splitPropertyString_MultipleParts_MultipleSpaces() {
        testSplitWithSpecificDelimeter("    ");
    }

    @Test
    void splitPropertyString_MultipleParts_Tabs() {
        testSplitWithSpecificDelimeter("\t");
    }

    @Test
    void splitPropertyString_MultipleParts_Hyphen() {
        testSplitWithSpecificDelimeter("-");
    }

    @Test
    void splitPropertyString_MultipleParts_Period() {
        testSplitWithSpecificDelimeter(".");
    }

    @Test
    void splitPropertyString_MultipleParts_Comma() {
        testSplitWithSpecificDelimeter(",");
    }

    @Test
    void splitPropertyString_MultipleParts_Numeral() {
        testSplitWithSpecificDelimeter("2");
    }

    @Test
    void splitPropertyString_MultipleParts_Apostrophe() {
        testSplitWithSpecificDelimeter("'");
    }

    @Test
    void splitPropertyString_MultipleParts_MultipleDelimeters() {
        Collection<String> parts = PropertyMatchingHelpers.splitPropertyString("--Red100Green   Blue:");

        assertEquals(3, parts.size(), "Verify parts count");
        assertEquals("red", parts.iterator().next(), "Verify parts[0] value");
    }

    @Test
    void doPropertyPartsMatch_Multiple_No() {
        Collection<String> collection1 = Arrays.asList("Red", "Green", "Blue");
        Collection<String> collection2 = Arrays.asList("Yellow", "Orange");

        assertEquals(AnswerType.no, PropertyMatchingHelpers.doPropertyPartsMatch(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatch_Multiple_No_EmptySourceCollection() {
        Collection<String> collection1 = new ArrayList<String>();
        Collection<String> collection2 = Arrays.asList("Yellow", "Orange");

        assertEquals(AnswerType.no, PropertyMatchingHelpers.doPropertyPartsMatch(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatch_Multiple_No_EmptyTargetCollection() {
        Collection<String> collection1 = Arrays.asList("Red", "Green", "Blue");
        Collection<String> collection2 = new ArrayList<String>();

        assertEquals(AnswerType.no, PropertyMatchingHelpers.doPropertyPartsMatch(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatch_Multiple_Yes() {
        Collection<String> collection1 = Arrays.asList("Red", "Green", "Blue");
        Collection<String> collection2 = Arrays.asList("Yellow", "Green");

        assertEquals(AnswerType.yes, PropertyMatchingHelpers.doPropertyPartsMatch(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatch_Multiple_Maybe() {
        Collection<String> collection1 = Arrays.asList("Red", "Green", "Blue");
        Collection<String> collection2 = Arrays.asList("Yellow", "Orange", "Purple", "Red");

        assertEquals(AnswerType.maybe, PropertyMatchingHelpers.doPropertyPartsMatch(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatch_Single_No() {
        assertEquals(AnswerType.no, PropertyMatchingHelpers.doPropertyPartsMatch("Do", "Doe"));
    }

    @Test
    void doPropertyPartsMatch_Single_Yes() {
        assertEquals(AnswerType.yes, PropertyMatchingHelpers.doPropertyPartsMatch("doe", "DOE"));
    }

    @Test
    void doesInitialMatchWithName_True() {
        assertTrue(PropertyMatchingHelpers.doesInitialMatchWithName('d', "Doe"));
    }

    @Test
    void doesInitialMatchWithName_False() {
        assertFalse(PropertyMatchingHelpers.doesInitialMatchWithName('o', "Doe"));
    }

    @Test
    void doNamePartsMatch_Yes_Equal() {
        assertEquals(AnswerType.yes, PropertyMatchingHelpers.doNamePartsMatch("Adams", "ADAMS"));
    }

    @Test
    void doNamePartsMatch_isMatch_Yes_LongPartMatch() {
        assertEquals(AnswerType.yes, PropertyMatchingHelpers.doNamePartsMatch("D'Onofio", "D Onofio"));
    }

    @Test
    void doNamePartsMatch_isMatch_Maybe_ShortPartMatch() {
        assertEquals(AnswerType.maybe, PropertyMatchingHelpers.doNamePartsMatch("JOE", "Mary Joe"));
    }

    @Test
    void doNamePartsMatch_isMatch_No() {
        assertEquals(AnswerType.no, PropertyMatchingHelpers.doNamePartsMatch("Lee", "Kathleen"));
    }



}
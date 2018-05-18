package com.cft.contactmerge.contact.tests;

import com.cft.contactmerge.AnswerType;
import com.cft.contactmerge.contact.PropertyMatchingHelpers;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyMatchingHelpersTest {

    /* --------------------------------------------------------------------------------------------
     * Test helpers for splitPropertyString tests
     * --------------------------------------------------------------------------------------------
     */

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

    interface SplitOperation {
        Collection<String> operation(String testString);
    }

    private void testSplitWithSpecificDelimeter(String delimeter, SplitOperation splitOperation) {
        Collection<String> expectedParts = Arrays.asList("Red", "Green", "Blue");
        String testString = joinCollectionOfStrings(expectedParts, delimeter);

        Collection<String> parts = splitOperation.operation(testString);

        assertEquals(expectedParts.size(), parts.size(), "Verify part count");

        int i = 0;
        for (String part: parts) {
            assertEquals(((List<String>) expectedParts).get(i).toLowerCase(), part, String.format("Verify parts[%d] value", i));
            i++;
        }
    }

    /* --------------------------------------------------------------------------------------------
     * splitPropertyStringOnNonAlpha tests
     * --------------------------------------------------------------------------------------------
     */

    @Test
    void splitPropertyStringOnNonAlpha_EmptyString() {
        Collection<String> parts = PropertyMatchingHelpers.splitPropertyStringOnNonAlpha("");

        assertEquals(0, parts.size());
    }

    @Test
    void splitPropertyStringOnNonAlpha_SinglePart() {
        Collection<String> parts = PropertyMatchingHelpers.splitPropertyStringOnNonAlpha("Red");

        assertEquals(1, parts.size(), "Verify parts count");
        assertEquals("red", parts.iterator().next(), "Verify parts[0] value");
    }


    @Test
    void splitPropertyStringOnNonAlpha_MultipleParts_Spaces() {
        testSplitWithSpecificDelimeter(" ", PropertyMatchingHelpers::splitPropertyStringOnNonAlpha);
    }

    @Test
    void splitPropertyStringOnNonAlpha_MultipleParts_MultipleSpaces() {
        testSplitWithSpecificDelimeter("    ", PropertyMatchingHelpers::splitPropertyStringOnNonAlpha);
    }

    @Test
    void splitPropertyStringOnNonAlpha_MultipleParts_Tabs() {
        testSplitWithSpecificDelimeter("\t", PropertyMatchingHelpers::splitPropertyStringOnNonAlpha);
    }

    @Test
    void splitPropertyStringOnNonAlpha_MultipleParts_Hyphen() {
        testSplitWithSpecificDelimeter("-", PropertyMatchingHelpers::splitPropertyStringOnNonAlpha);
    }

    @Test
    void splitPropertyStringOnNonAlpha_MultipleParts_Period() {
        testSplitWithSpecificDelimeter(".", PropertyMatchingHelpers::splitPropertyStringOnNonAlpha);
    }

    @Test
    void splitPropertyStringOnNonAlpha_MultipleParts_Comma() {
        testSplitWithSpecificDelimeter(",", PropertyMatchingHelpers::splitPropertyStringOnNonAlpha);
    }

    @Test
    void splitPropertyStringOnNonAlpha_MultipleParts_Numeral() {
        testSplitWithSpecificDelimeter("2", PropertyMatchingHelpers::splitPropertyStringOnNonAlpha);
    }

    @Test
    void splitPropertyStringOnNonAlpha_MultipleParts_Apostrophe() {
        testSplitWithSpecificDelimeter("'", PropertyMatchingHelpers::splitPropertyStringOnNonAlpha);
    }

    @Test
    void splitPropertyStringOnNonAlpha_MultipleParts_MultipleDelimeters() {
        Collection<String> parts = PropertyMatchingHelpers.splitPropertyStringOnNonAlpha("--Red100Green   Blue:");

        assertEquals(3, parts.size(), "Verify parts count");
        assertEquals("red", parts.iterator().next(), "Verify parts[0] value");
    }

    /* --------------------------------------------------------------------------------------------
     * splitPropertyStringOnNonAlphaNumeric tests
     * --------------------------------------------------------------------------------------------
     */
    @Test
    void splitPropertyStringOnNonAlphaNumeric_MultipleParts_Spaces() {
        testSplitWithSpecificDelimeter(" ", PropertyMatchingHelpers::splitPropertyStringOnNonAlphaNumeric);
    }

    @Test
    void splitPropertyStringOnNonAlphaNumeric_MultipleParts_Comma() {
        testSplitWithSpecificDelimeter(",", PropertyMatchingHelpers::splitPropertyStringOnNonAlphaNumeric);
    }

    @Test
    void splitPropertyStringOnNonAlphaNumeric_DoesNotSplitNumeral() {
        Collection<String> parts = PropertyMatchingHelpers.splitPropertyStringOnNonAlphaNumeric("123 Main St");

        assertEquals(3, parts.size(), "Verify parts count");
        assertEquals("123", parts.iterator().next(), "Verify parts[0] value");
    }

    /* --------------------------------------------------------------------------------------------
     * doPropertyPartsMatchOrderDoesNotMatter tests
     * --------------------------------------------------------------------------------------------
     */

    @Test
    void doPropertyPartsMatchOrderDoesNotMatter_Multiple_No() {
        Collection<String> collection1 = Arrays.asList("Red", "Green", "Blue");
        Collection<String> collection2 = Arrays.asList("Yellow", "Orange");

        assertEquals(AnswerType.no, PropertyMatchingHelpers.doPropertyPartsMatchOrderDoesNotMatter(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatchOrderDoesNotMatter_Multiple_No_EmptySourceCollection() {
        Collection<String> collection1 = new ArrayList<String>();
        Collection<String> collection2 = Arrays.asList("Yellow", "Orange");

        assertEquals(AnswerType.no, PropertyMatchingHelpers.doPropertyPartsMatchOrderDoesNotMatter(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatchOrderDoesNotMatter_Multiple_No_EmptyTargetCollection() {
        Collection<String> collection1 = Arrays.asList("Red", "Green", "Blue");
        Collection<String> collection2 = new ArrayList<String>();

        assertEquals(AnswerType.no, PropertyMatchingHelpers.doPropertyPartsMatchOrderDoesNotMatter(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatchOrderDoesNotMatter_Multiple_Yes() {
        Collection<String> collection1 = Arrays.asList("Red", "Green", "Blue");
        Collection<String> collection2 = Arrays.asList("Yellow", "Green");

        assertEquals(AnswerType.yes, PropertyMatchingHelpers.doPropertyPartsMatchOrderDoesNotMatter(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatchOrderDoesNotMatter_Multiple_Maybe() {
        Collection<String> collection1 = Arrays.asList("Red", "Green", "Blue");
        Collection<String> collection2 = Arrays.asList("Yellow", "Orange", "Purple", "Red");

        assertEquals(AnswerType.maybe, PropertyMatchingHelpers.doPropertyPartsMatchOrderDoesNotMatter(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatchOrderDoesNotMatter_Single_No() {
        assertEquals(AnswerType.no, PropertyMatchingHelpers.doPropertyPartsMatch("Do", "Doe"));
    }

    @Test
    void doPropertyPartsMatchOrderDoesNotMatter_Single_Yes() {
        assertEquals(AnswerType.yes, PropertyMatchingHelpers.doPropertyPartsMatch("doe", "DOE"));
    }

    /* --------------------------------------------------------------------------------------------
     * doPropertyPartsMatchOrderDoesNotMatter tests
     * --------------------------------------------------------------------------------------------
     */
    @Test
    void doPropertyPartsMatchOrderDoesMatter_No_DifferentOrder() {
        Collection<String> collection1 = Arrays.asList("Red", "Green", "Blue");
        Collection<String> collection2 = Arrays.asList("Red", "Blue", "Green");

        assertEquals(AnswerType.no, PropertyMatchingHelpers.doPropertyPartsMatchOrderDoesMatter(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatchOrderDoesMatter_No_SourceSizeGreater() {
        Collection<String> collection1 = Arrays.asList("Red", "Green", "Blue");
        Collection<String> collection2 = Arrays.asList("Red", "Green");

        assertEquals(AnswerType.no, PropertyMatchingHelpers.doPropertyPartsMatchOrderDoesMatter(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatchOrderDoesMatter_No_TargetSizeGreater() {
        Collection<String> collection1 = Arrays.asList("Red", "Green");
        Collection<String> collection2 = Arrays.asList("Red", "Green", "Blue");

        assertEquals(AnswerType.no, PropertyMatchingHelpers.doPropertyPartsMatchOrderDoesMatter(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatchOrderDoesMatter_No_EmptyIsNotMatching() {
        Collection<String> collection1 = new ArrayList<String>();
        Collection<String> collection2 = new ArrayList<String>();

        assertEquals(AnswerType.no, PropertyMatchingHelpers.doPropertyPartsMatchOrderDoesMatter(collection1, collection2));
    }

    @Test
    void doPropertyPartsMatchOrderDoesMatter_Yes() {
        Collection<String> collection1 = Arrays.asList("Red", "Green", "Blue");
        Collection<String> collection2 = Arrays.asList("Red", "Green", "Blue");

        assertEquals(AnswerType.yes, PropertyMatchingHelpers.doPropertyPartsMatchOrderDoesMatter(collection1, collection2));
    }

    /* --------------------------------------------------------------------------------------------
     * doesInitialMatch tests
     * --------------------------------------------------------------------------------------------
     */

    @Test
    void doesInitialMatchWithName_True() {
        assertTrue(PropertyMatchingHelpers.doesInitialMatchWithName('d', "Doe"));
    }

    @Test
    void doesInitialMatchWithName_False() {
        assertFalse(PropertyMatchingHelpers.doesInitialMatchWithName('o', "Doe"));
    }

    /* --------------------------------------------------------------------------------------------
     * doNamePartsMatch tests
     * --------------------------------------------------------------------------------------------
     */

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
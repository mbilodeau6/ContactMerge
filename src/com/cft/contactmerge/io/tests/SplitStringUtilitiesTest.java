package com.cft.contactmerge.io.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.cft.contactmerge.io.*;
import java.util.*;

class SplitStringUtilitiesTest {

    private String createVerificationMessage(String itemName)
    {
        return String.format("Verify %s", itemName);
    }

    /* ----------------------------------------------------------
     * TSV Tests
     * ---------------------------------------------------------- */
    @Test
    void splitTsvString_EmptyString() {
        List<String> parts = SplitStringUtilities.splitTsvString("");

        assertEquals(1, parts.size(), createVerificationMessage("result size"));
        assertEquals( "", parts.get(0), createVerificationMessage("first result"));
    }

    @Test
    void splitTsvString_SingleValue() {
        List<String> parts = SplitStringUtilities.splitTsvString("Joe");

        assertEquals(1, parts.size(), createVerificationMessage("result size"));
        assertEquals( "Joe", parts.get(0), "first result");
    }

    @Test
    void splitTsvString_MultipleValues() {
        List<String> expectedParts = Arrays.asList("Joe", "Smith", "123 Main St");
        String testString = String.join("\t", expectedParts);

        List<String> actualParts = SplitStringUtilities.splitTsvString(testString);

        assertEquals(3, actualParts.size(), createVerificationMessage("result size"));

        for(int i=0; i < actualParts.size(); i++) {
            assertEquals(expectedParts.get(i), actualParts.get(i), String.format("result[%d]", i));
        }
    }

    @Test
    void splitTsvString_MissingValues() {
        List<String> expectedParts = Arrays.asList("", "Smith", "", "");
        String testString = String.join("\t", expectedParts);

        List<String> actualParts = SplitStringUtilities.splitTsvString(testString);

        assertEquals(4, actualParts.size(), createVerificationMessage("result size"));

        for(int i=0; i < actualParts.size(); i++) {
            assertEquals(expectedParts.get(i), actualParts.get(i), String.format("result[%d]", i));
        }
    }

    @Test
    void splitTsvString_LeadingTrailingSpacesTrimmed() {
        List<String> expectedParts = Arrays.asList("   ", " Smith  ");
        String testString = String.join("\t", expectedParts);

        List<String> actualParts = SplitStringUtilities.splitTsvString(testString);

        assertEquals(2, actualParts.size(), createVerificationMessage("result size"));

        assertEquals("", actualParts.get(0), "result[0]");
        assertEquals("Smith", actualParts.get(1), "result[1]");
    }

    /* ----------------------------------------------------------
     * CSV Tests
     * ---------------------------------------------------------- */
    @Test
    void splitCsvString_EmptyString() {
        List<String> parts = SplitStringUtilities.splitCsvString("");

        assertEquals(1, parts.size(), createVerificationMessage("result size"));
        assertEquals( "", parts.get(0), createVerificationMessage("first result"));
    }

    @Test
    void splitCsvString_SingleValue() {
        List<String> parts = SplitStringUtilities.splitCsvString("Joe");

        assertEquals(1, parts.size(), createVerificationMessage("result size"));
        assertEquals( "Joe", parts.get(0), "first result");
    }

    @Test
    void splitCsvString_MultipleValues() {
        List<String> expectedParts = Arrays.asList("Joe", "Smith", "123 Main St");
        String testString = String.join(",", expectedParts);

        List<String> actualParts = SplitStringUtilities.splitCsvString(testString);

        assertEquals(3, actualParts.size(), createVerificationMessage("result size"));

        for(int i=0; i < actualParts.size(); i++) {
            assertEquals(expectedParts.get(i), actualParts.get(i), String.format("result[%d]", i));
        }
    }

    @Test
    void splitCsvString_MissingValues() {
        List<String> expectedParts = Arrays.asList("", "Smith", "", "");
        String testString = String.join(",", expectedParts);

        List<String> actualParts = SplitStringUtilities.splitCsvString(testString);

        assertEquals(4, actualParts.size(), createVerificationMessage("result size"));

        for(int i=0; i < actualParts.size(); i++) {
            assertEquals(expectedParts.get(i), actualParts.get(i), String.format("result[%d]", i));
        }
    }

    @Test
    void splitCsvString_LeadingTrailingSpacesTrimmed() {
        List<String> expectedParts = Arrays.asList("   ", " Smith  ");
        String testString = String.join(",", expectedParts);

        List<String> actualParts = SplitStringUtilities.splitCsvString(testString);

        assertEquals(2, actualParts.size(), createVerificationMessage("result size"));
        assertEquals("", actualParts.get(0), "result[0]");
        assertEquals("Smith", actualParts.get(1), "result[1]");
    }

    @Test
    void splitCsvString_SingleQuotedValue() {
        List<String> parts = SplitStringUtilities.splitCsvString("\"Smith, Joe\"");

        assertEquals(1, parts.size(), createVerificationMessage("result size"));
        assertEquals( "Smith, Joe", parts.get(0), "first result");
    }

    @Test
    void splitCsvString_MixQuotedNonQuoted() {
        List<String> parts = SplitStringUtilities.splitCsvString("\"Joe\", Smith, \"123 Main St, Apt 10\"");

        assertEquals(3, parts.size(), createVerificationMessage("result size"));
        assertEquals( "Joe", parts.get(0), "result[0]");
        assertEquals( "Smith", parts.get(1), "result[1]");
        assertEquals( "123 Main St, Apt 10", parts.get(2), "result[2]");
    }

    @Test
    void splitCsvString_MixQuotedNonQuotedWithBlanks() {
        List<String> parts = SplitStringUtilities.splitCsvString(",\"\",\"Joe\", Smith,");

        assertEquals(5, parts.size(), createVerificationMessage("result size"));
        assertEquals( "Joe", parts.get(2), "result[2]");
        assertEquals( "Smith", parts.get(3), "result[3]");
    }

    @Test
    void splitCsvString_TextAfterQuoted() {
        List<String> parts = SplitStringUtilities.splitCsvString("\"Joe\", \"Adam\"son, \"123 Main St\"");

        assertEquals(3, parts.size(), createVerificationMessage("result size"));
        assertEquals( "Joe", parts.get(0), "result[0]");
        assertEquals( "\"Adam\"son", parts.get(1), "result[1]");
        assertEquals( "123 Main St", parts.get(2), "result[2]");
    }

    @Test
    void splitCsvString_TextAfterLastQuoted() {
        List<String> parts = SplitStringUtilities.splitCsvString("\"Joe\", \"Adam\"son, \"123 Main St\"reet");

        assertEquals(3, parts.size(), createVerificationMessage("result size"));
        assertEquals( "Joe", parts.get(0), "result[0]");
        assertEquals( "\"Adam\"son", parts.get(1), "result[1]");
        assertEquals( "\"123 Main St\"reet", parts.get(2), "result[2]");
    }

    @Test
    void splitCsvString_TextBeforeQuoted() {
        List<String> parts = SplitStringUtilities.splitCsvString("\"Joe\", Adam\"son\", \"123 Main St\"");

        assertEquals(3, parts.size(), createVerificationMessage("result size"));
        assertEquals( "Joe", parts.get(0), "result[0]");
        assertEquals( "Adam\"son\"", parts.get(1), "result[1]");
        assertEquals( "123 Main St", parts.get(2), "result[2]");
    }

    @Test
    void splitCsvString_TextBeforeQuotedWithComma() {
        List<String> parts = SplitStringUtilities.splitCsvString("\"Joe\", Adam\"s,on\", \"123 Main St\"");

        assertEquals(3, parts.size(), createVerificationMessage("result size"));
        assertEquals( "Joe", parts.get(0), "result[0]");
        assertEquals( "Adam\"s,on\"", parts.get(1), "result[1]");
        assertEquals( "123 Main St", parts.get(2), "result[2]");
    }
}
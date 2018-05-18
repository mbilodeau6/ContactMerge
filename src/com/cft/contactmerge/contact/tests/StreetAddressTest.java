package com.cft.contactmerge.contact.tests;

import com.cft.contactmerge.AnswerType;
import com.cft.contactmerge.contact.StreetAddress;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreetAddressTest {

    @Test
    void Constructor()
    {
        StreetAddress property = new StreetAddress("123 Main St");

        assertNotNull(property);
    }

    @Test
    void Constructor_NullValue()
    {
        assertThrows(IllegalArgumentException.class, () -> new StreetAddress(null));
    }

    @Test
    void Constructor_EmptyValue()
    {
        assertThrows(IllegalArgumentException.class, () -> new StreetAddress(""));
    }

    @Test
    void StreetAddress_isMatch_Yes_Equal() {
        StreetAddress source = new StreetAddress("123 Main St");
        StreetAddress target = new StreetAddress("123 Main St");

        assertEquals(AnswerType.yes, source.isMatch(target));
    }

    @Test
    void StreetAddress_isMatch_Maybe_MissingStreet() {
        StreetAddress source = new StreetAddress("123 Main");
        StreetAddress target = new StreetAddress("123 Main St");

        assertEquals(AnswerType.maybe, source.isMatch(target));
    }

    // TODO: Test all other street types

    @Test
    void StreetAddress_isMatch_Maybe_MissingDirectionW() {
        StreetAddress source = new StreetAddress("123 W Main St");
        StreetAddress target = new StreetAddress("123 Main St");

        assertEquals(AnswerType.maybe, source.isMatch(target));
    }

    // TODO: Test all other directions

    @Test
    void StreetAddress_isMatch_No() {
        StreetAddress source = new StreetAddress("123 Main St");
        StreetAddress target = new StreetAddress("132 Main St");

        assertEquals(AnswerType.no, source.isMatch(target));
    }

    @Test
    void StreetAddress_getValue() {
        StreetAddress property = new StreetAddress("123 Main St");

        assertEquals("123 Main St", property.getValue());
    }

    @Test
    void StreetAddress_toString() {
        StreetAddress property = new StreetAddress("123 Main St");

        assertEquals("123 Main St", property.toString());
    }
}
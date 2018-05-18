package com.cft.contactmerge.contact.tests;

import com.cft.contactmerge.AnswerType;
import com.cft.contactmerge.contact.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void Constructor()
    {
        Address property = new Address("Tucson");

        assertNotNull(property);
    }

    @Test
    void Constructor_NullValue()
    {
        assertThrows(IllegalArgumentException.class, () -> new Address(null));
    }

    @Test
    void Constructor_EmptyValue()
    {
        assertThrows(IllegalArgumentException.class, () -> new Address(""));
    }

    @Test
    void GeneralProperty_isMatch_Yes_Equal() {
        Address source = new Address("Tucson");
        Address target = new Address("tucson");

        assertEquals(AnswerType.yes, source.isMatch(target));
    }

    @Test
    void GeneralProperty_isMatch_No() {
        Address source = new Address("Phoenix");
        Address target = new Address("Tucson");

        assertEquals(AnswerType.no, source.isMatch(target));
    }

    @Test
    void GeneralProperty_getValue() {
        Address property = new Address("Tucson");

        assertEquals("Tucson", property.getValue());
    }

    @Test
    void GeneralProperty_toString() {
        Address property = new Address("Tucson");

        assertEquals("Tucson", property.toString());
    }
}
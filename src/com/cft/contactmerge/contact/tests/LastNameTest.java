package com.cft.contactmerge.contact.tests;

import com.cft.contactmerge.AnswerType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.cft.contactmerge.contact.*;


class LastNameTest {

    @Test
    void Constructor()
    {
        LastName property = new LastName("Smith");

        assertNotNull(property);
    }

    @Test
    void Constructor_NullValue()
    {
        assertThrows(IllegalArgumentException.class, () -> new LastName(null));
    }

    @Test
    void Constructor_EmptyValue()
    {
        assertThrows(IllegalArgumentException.class, () -> new LastName(""));
    }

    @Test
    void LastName_isMatch_Yes_Equal() {
        LastName source = new LastName("ADAMS");
        LastName target = new LastName("Adams");

        assertEquals(AnswerType.yes, source.isMatch(target));
    }

    @Test
    void LastName_isMatch_Maybe_ShortPartMatch() {
        LastName source = new LastName("Adams Doe");
        LastName target = new LastName("Doe");

        assertEquals(AnswerType.maybe, source.isMatch(target));
    }

    @Test
    void LastName_isMatch_No() {
        LastName source = new LastName("Adamson");
        LastName target = new LastName("Adam");

        assertEquals(AnswerType.no, source.isMatch(target));
    }

    @Test
    void LastName_getValue() {
        LastName property = new LastName("Smith");

        assertEquals("Smith", property.getValue());
    }

    @Test
    void LastName_toString() {
        LastName property = new LastName("Smith");

        assertEquals("Smith", property.toString());
    }
}
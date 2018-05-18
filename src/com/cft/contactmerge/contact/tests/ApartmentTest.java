package com.cft.contactmerge.contact.tests;

import com.cft.contactmerge.AnswerType;
import com.cft.contactmerge.contact.Apartment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApartmentTest {

    @Test
    void Constructor()
    {
        Apartment property = new Apartment("1B");

        assertNotNull(property);
    }

    @Test
    void Constructor_NullValue()
    {
        assertThrows(IllegalArgumentException.class, () -> new Apartment(null));
    }

    @Test
    void Constructor_EmptyValue()
    {
        assertThrows(IllegalArgumentException.class, () -> new Apartment(""));
    }

    @Test
    void Apartment_isMatch_Yes_Equal() {
        Apartment source = new Apartment("1b");
        Apartment target = new Apartment("1B");

        assertEquals(AnswerType.yes, source.isMatch(target));
    }

    @Test
    void Apartment_isMatch_No() {
        Apartment source = new Apartment("1010");
        Apartment target = new Apartment("1020");

        assertEquals(AnswerType.no, source.isMatch(target));
    }

    @Test
    void Apartment_getValue() {
        Apartment property = new Apartment("4C");

        assertEquals("4C", property.getValue());
    }

    @Test
    void Apartment_toString() {
        Apartment property = new Apartment("1010");

        assertEquals("1010", property.toString());
    }
}
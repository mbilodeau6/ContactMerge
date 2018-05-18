package com.cft.contactmerge.contact.tests;

import com.cft.contactmerge.AnswerType;
import com.cft.contactmerge.contact.PhoneNumber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberTest {

    @Test
    void Constructor()
    {
        PhoneNumber property = new PhoneNumber("(520) 555-1234");

        assertNotNull(property);
    }

    @Test
    void Constructor_NullValue()
    {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(null));
    }

    @Test
    void Constructor_EmptyValue()
    {
        assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(""));
    }

    @Test
    void PhoneNumber_isMatch_Yes_Equal() {
        PhoneNumber source = new PhoneNumber("(520) 555-1234");
        PhoneNumber target = new PhoneNumber("(520) 555-1234");

        assertEquals(AnswerType.yes, source.isMatch(target));
    }

    @Test
    void PhoneNumber_isMatch_No() {
        PhoneNumber source = new PhoneNumber("(520) 555-1234");
        PhoneNumber target = new PhoneNumber("(617) 555-1234");

        assertEquals(AnswerType.no, source.isMatch(target));
    }

    @Test
    void PhoneNumber_getValue() {
        PhoneNumber property = new PhoneNumber("(520) 555-1234");

        assertEquals("(520) 555-1234", property.getValue());
    }

    @Test
    void PhoneNumber_toString() {
        PhoneNumber property = new PhoneNumber("(520) 555-1234");

        assertEquals("(520) 555-1234", property.toString());
    }
}
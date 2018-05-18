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

        assertEquals("(520) 555-1234", property.getValue());
    }
}
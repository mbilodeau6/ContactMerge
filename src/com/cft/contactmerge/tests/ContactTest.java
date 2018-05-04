package com.cft.contactmerge.tests;

import com.cft.contactmerge.ContactMatchType;
import org.junit.jupiter.api.Test;
import com.cft.contactmerge.AnswerType;
import com.cft.contactmerge.Contact;
import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    @Test
    void setFirstName() {
        Contact newContact = new Contact();
        newContact.setFirstName("abc");
        assertEquals("abc", newContact.getFirstName());
    }

    @Test
    void setLastName() {
        Contact newContact = new Contact();
        newContact.setLastName("def");
        assertEquals("def", newContact.getLastName());
    }

    @Test
    void setAddress() {
        Contact newContact = new Contact();
        newContact.setAddress("ghi");
        assertEquals("ghi", newContact.getAddress());
    }

    @Test
    void setCity() {
        Contact newContact = new Contact();
        newContact.setCity("jkl");
        assertEquals("jkl", newContact.getCity());
    }

    @Test
    void setZip() {
        Contact newContact = new Contact();
        newContact.setZip("mno");
        assertEquals("mno", newContact.getZip());
    }

    @Test
    void setPhone() {
        Contact newContact = new Contact();
        newContact.setPhone("pqr");
        assertEquals("pqr", newContact.getPhone());
    }

    @Test
    void setEmail() {
        Contact newContact = new Contact();
        newContact.setEmail("stu");
        assertEquals("stu", newContact.getEmail());
    }

    /* ========================================================================
     * There are a large number of CompareTo tests which are broken into steps
     * reflecting the rules used to determine if a ContactToMerge matches an
     * ExistingContact.
     * ========================================================================
     */

    // Helper methods for tests
    private Contact createBaseContact()
    {
        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contact.setAddress("123 Main St");
        contact.setCity("Tucson");
        contact.setState("AZ");
        contact.setZip("85750");
        contact.setPhone("(520) 123-4567");
        contact.setEmail("jdoe@gmail.com");

        return contact;
    }

    private Contact createNonMatchingBaseContact()
    {
        Contact contact = new Contact();
        contact.setFirstName("Adam");
        contact.setLastName("Smith");
        contact.setAddress("1400 Broadway");
        contact.setCity("Boston");
        contact.setState("MA");
        contact.setZip("02144");
        contact.setPhone("(617) 123-4567");
        contact.setEmail("asmith@comcast.net");

        return contact;
    }

    // Step 1 - Return ContactMatchType.NoMatch if nothing is specified in the
    // ContactToMerge or the ExistingContact
    @Test
    void compareTo_NoMatch_NothingSpecifiedInContactToMerge()
    {
        Contact c1 = new Contact();
        Contact c2 = createBaseContact();

        assertEquals(ContactMatchType.NoMatch, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_NoMatch_NothingSpecifiedInExistingContact()
    {
        Contact c1 = createBaseContact();
        Contact c2 = new Contact();

        assertEquals(ContactMatchType.NoMatch, c1.compareTo(c2).getMatchType());
    }

    // Step 2 - Return ContactMatchType.Identical if all the parts that are specified in
    // either Contact match

    // Step 3 - Return ContactMatchType.NoMatch is if all of the parts that are specified
    // in ContactToMerge do not match the ExistingContact

    @Test
    void compareTo_NoMatchOnAny() {
        Contact c1 = createBaseContact();
        Contact c2 = createNonMatchingBaseContact();

        assertEquals(ContactMatchType.NoMatch, c1.compareTo(c2).getMatchType());
    }

    // Step 4 - Return ContactMatchType.Match if the Name and at least one of the other
    // parts (Address, Phones, or Email) match if all the parts in the ContactToMerge
    // that don't match are empty or null

    // Step 5 - Return ContactMatchType.MatchButModifying if the Name and at least one
    // of the other parts (Address, Phones, or Email) match but there is at least one
    // part that doesn't match

    // Step 6 - Return ContactMatchType.PotentialMatch if the Names might match

    // Step 7 - Return ContactMatchType.Related if at least 1 of Address, Phone, or
    // Email match

    // Step 8 - Return ContactMatchType.PotentiallyRelated if at least one of
    // Address, Phone, or Email is "might match"

    // Step 9 - Misc cases of ContactMatchType.NoMatch
}
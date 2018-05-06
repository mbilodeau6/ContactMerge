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

    private void clearAddress(Contact contact)
    {
        contact.setAddress(null);
        contact.setCity(null);
        contact.setState(null);
        contact.setZip(null);
    }

    private void copyAddress(Contact targetContact, Contact sourceContact)
    {
        targetContact.setAddress(sourceContact.getAddress());
        targetContact.setCity(sourceContact.getCity());
        targetContact.setState(sourceContact.getState());
        targetContact.setZip(sourceContact.getZip());
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
    @Test
    void compareTo_Identical_AllParts()
    {
        Contact c1 = createBaseContact();
        Contact c2 = createBaseContact();

        assertEquals(ContactMatchType.Identical, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_Identical_AddressMissing()
    {
        Contact c1 = createBaseContact();
        clearAddress(c1);

        Contact c2 = createBaseContact();
        clearAddress(c2);

        assertEquals(ContactMatchType.Identical, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_Identical_PhoneMissing()
    {
        Contact c1 = createBaseContact();
        c1.setPhone(null);
        Contact c2 = createBaseContact();
        c2.setPhone("");

        assertEquals(ContactMatchType.Identical, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_Identical_EmailMissing()
    {
        Contact c1 = createBaseContact();
        c1.setEmail("");
        Contact c2 = createBaseContact();
        c2.setEmail(null);

        assertEquals(ContactMatchType.Identical, c1.compareTo(c2).getMatchType());
    }

    // Step 3 - Return ContactMatchType.NoMatch if all of the parts that are specified
    // in ContactToMerge do not match the ExistingContact

    @Test
    void compareTo_NoMatch_AllParts() {
        Contact c1 = createBaseContact();
        Contact c2 = createNonMatchingBaseContact();

        assertEquals(ContactMatchType.NoMatch, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_NoMatch_AddressMissing()
    {
        Contact c1 = createBaseContact();
        clearAddress(c1);

        Contact c2 = createNonMatchingBaseContact();

        assertEquals(ContactMatchType.NoMatch, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_NoMatch_PhoneMissing()
    {
        Contact c1 = createBaseContact();
        c1.setPhone(null);
        Contact c2 = createNonMatchingBaseContact();

        assertEquals(ContactMatchType.NoMatch, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_NoMatch_EmailMissing()
    {
        Contact c1 = createBaseContact();
        c1.setEmail(null);
        Contact c2 = createNonMatchingBaseContact();

        assertEquals(ContactMatchType.NoMatch, c1.compareTo(c2).getMatchType());
    }

    // Step 4 - Return ContactMatchType.Match if the Name and at least one of the other
    // parts (Address, Phones, or Email) match and if all the parts in the ContactToMerge
    // that don't match are empty or null
    @Test
    void compareTo_Match_NameAndAddress()
    {
        Contact notMatching = createNonMatchingBaseContact();

        Contact c1 = createBaseContact();
        c1.setEmail(null);
        c1.setPhone(null);
        Contact c2 = createBaseContact();
        c2.setEmail(notMatching.getEmail());
        c2.setPhone(notMatching.getPhone());

        assertEquals(ContactMatchType.Match, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_Match_NameAndEmail()
    {
        Contact notMatching = createNonMatchingBaseContact();

        Contact c1 = createBaseContact();
        c1.setPhone(null);
        clearAddress(c1);

        Contact c2 = createBaseContact();
        c2.setPhone(notMatching.getPhone());
        copyAddress(c1, notMatching);

        assertEquals(ContactMatchType.Match, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_Match_NameAndPhone()
    {
        Contact notMatching = createNonMatchingBaseContact();

        Contact c1 = createBaseContact();
        c1.setEmail(null);
        clearAddress(c1);

        Contact c2 = createBaseContact();
        c2.setEmail(notMatching.getEmail());
        copyAddress(c1, notMatching);

        assertEquals(ContactMatchType.Match, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_PotentialMatch_OnlyNameSpecified()
    {
        Contact c1 = new Contact();
        c1.setLastName("Smith");
        c1.setFirstName("Adam");

        Contact c2 = new Contact();
        c2.setLastName(c1.getLastName());
        c2.setFirstName(c1.getFirstName());

        assertEquals(ContactMatchType.Match, c1.compareTo(c2).getMatchType());
    }

    // Step 5 - Return ContactMatchType.MatchButModifying if the Name and at least one
    // of the other parts (Address, Phones, or Email) match but there is at least one
    // part that doesn't match
    @Test
    void compareTo_MatchButModifying_NameAndAddress()
    {
        Contact notMatching = createNonMatchingBaseContact();

        Contact c1 = createBaseContact();
        Contact c2 = createBaseContact();
        c2.setEmail(notMatching.getEmail());
        c2.setPhone(notMatching.getPhone());

        assertEquals(ContactMatchType.MatchButModifying, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_MatchButModifying_NameAndEmail()
    {
        Contact notMatching = createNonMatchingBaseContact();

        Contact c1 = createBaseContact();

        Contact c2 = createBaseContact();
        c2.setPhone(notMatching.getPhone());
        copyAddress(c1, notMatching);

        assertEquals(ContactMatchType.MatchButModifying, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_MatchButModifying_NameAndPhone()
    {
        Contact notMatching = createNonMatchingBaseContact();

        Contact c1 = createBaseContact();

        Contact c2 = createBaseContact();
        c2.setEmail(notMatching.getEmail());
        copyAddress(c1, notMatching);

        assertEquals(ContactMatchType.MatchButModifying, c1.compareTo(c2).getMatchType());
    }

    // Step 6 - Return ContactMatchType.PotentialMatch if the Names might match
    // or the Name does match but nothing else matches
    @Test
    void compareTo_PotentialMatch_OnlyNameMatches()
    {
        Contact c1 = createBaseContact();

        Contact c2 = createNonMatchingBaseContact();
        c2.setLastName(c1.getLastName());
        c2.setFirstName(c1.getFirstName());

        assertEquals(ContactMatchType.PotentialMatch, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_PotentialMatch_NameMightMatch()
    {
        Contact c1 = createBaseContact();

        Contact c2 = createNonMatchingBaseContact();
        c2.setLastName(c1.getLastName());

        // Set first name to first initial to cause a maybe result on name matching
        c2.setFirstName(c1.getFirstName().substring(0, 1));

        assertEquals(ContactMatchType.PotentialMatch, c1.compareTo(c2).getMatchType());
    }

    // Step 7 - Return ContactMatchType.Related if at least 1 of Address, Phone, or
    // Email match
    @Test
    void compareTo_Related_AddressMatch()
    {
        Contact c1 = createBaseContact();

        Contact c2 = createNonMatchingBaseContact();
        copyAddress(c2, c1);

        assertEquals(ContactMatchType.Related, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_Related_PhoneMatch()
    {
        Contact c1 = createBaseContact();

        Contact c2 = createNonMatchingBaseContact();
        c2.setPhone(c1.getPhone());

        assertEquals(ContactMatchType.Related, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_Related_EmailMatch()
    {
        Contact c1 = createBaseContact();

        Contact c2 = createNonMatchingBaseContact();
        c2.setEmail(c1.getEmail());

        assertEquals(ContactMatchType.Related, c1.compareTo(c2).getMatchType());
    }

    // Step 8 - Return ContactMatchType.PotentiallyRelated if at least one of
    // Address, Phone, or Email is "might match"
    @Test
    void compareTo_PotentiallyRelated_AddressMightMatch()
    {
        Contact c1 = createBaseContact();

        Contact c2 = createNonMatchingBaseContact();

        // Make address match a maybe because road type (i.e. "St") is missing
        c2.setAddress("123 Main");

        assertEquals(ContactMatchType.Related, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_PotentiallyRelated_PhoneMightMatch()
    {
        // TODO: Need a way to get a "maybe" result on phone number. May
        // need to wait until we create an interface so that we can use a mock.
    }

    @Test
    void compareTo_PotentiallyRelated_EmailMightMatch()
    {
        // TODO: Need a way to get a "maybe" result on phone number. May
        // need to wait until we create an interface so that we can use a mock.
    }
}
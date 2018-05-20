package com.cft.contactmerge.contact.tests;

import com.cft.contactmerge.AnswerType;
import com.cft.contactmerge.contact.ContactMatchType;
import com.cft.contactmerge.contact.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ContactTest {

    @Test
    void Constructor() {
        Contact newContact = new Contact();

        assertNotNull(newContact);
    }

    @Test
    void setName() {
        Contact newContact = new Contact();
        IContactProperty<Name> mockName = mock(Name.class);

        newContact.setName(mockName);

        assertEquals(mockName, newContact.getName());
    }

    @Test
    void setAddress() {
        Contact newContact = new Contact();
        IContactProperty<Address> mockAddress = mock(Address.class);

        newContact.setAddress(mockAddress);

        assertEquals(mockAddress, newContact.getAddress());
    }

    @Test
    void setPhone() {
        Contact newContact = new Contact();
        IContactProperty<String> mockPhoneNumber = mock(IContactProperty.class);

        newContact.setPhone(mockPhoneNumber);

        assertEquals(mockPhoneNumber, newContact.getPhone());
    }

    @Test
    void setEmail() {
        Contact newContact = new Contact();
        IContactProperty<String> mockEmail = mock(IContactProperty.class);

        newContact.setEmail(mockEmail);
        assertEquals(mockEmail, newContact.getEmail());
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
        contact.setName(new Name(new FirstName("John"), new LastName("Doe")));
        contact.setAddress(new Address(new StreetAddress("123 Main St"),
                new GeneralProperty("Tucson"),
                new GeneralProperty("AZ"),
                null,
                new GeneralProperty("85750")));
        contact.setPhone(new PhoneNumber("(520) 123-4567"));
        contact.setEmail(new GeneralProperty("jdoe@gmail.com"));

        return contact;
    }

    // Creates a stub so that compareTo() can execute. The Contact object will never actually compare
    // its parts with this object.
    private IContact createContactStub(boolean addressSet, boolean phoneSet, boolean emailSet)
    {
        IContactProperty<Name> nameMock = mock(Name.class);

        IContactProperty<Address> addressMock = null;

        if (addressSet) {
            addressMock = mock(Address.class);
//        when(streetAddressMock.getValue()).thenReturn("This is where you would find the StreetAddress value");
        }

        IContactProperty<String> phoneMock = null;

        if (phoneSet) {
            phoneMock = mock(IContactProperty.class);
        }

        IContactProperty<String> emailMock = null;

        if (emailSet) {
            emailMock = mock(IContactProperty.class);
        }

        IContact contactToCompareWith = mock(Contact.class);
        when(contactToCompareWith.getName()).thenReturn(nameMock);
        when(contactToCompareWith.getAddress()).thenReturn(addressMock);
        when(contactToCompareWith.getPhone()).thenReturn(phoneMock);
        when(contactToCompareWith.getEmail()).thenReturn(emailMock);

        return contactToCompareWith;
    }

    // Executes a specific test, verifies the result, and verifies that the internal name parts
    // are called as expected.
    private void runIsMatchTest(AnswerType answerTypeForNameIsMatch,
                                AnswerType answerTypeForAddressIsMatch,
                                AnswerType answerTypeForPhoneIsMatch,
                                AnswerType answerTypeForEmailIsMatch,
                                ContactMatchType expectedMatchResult,
                                boolean targetAddressSet,
                                boolean targetPhoneSet,
                                boolean targetEmailSet)
    {
        // Set up Address with mock internals
        IContactProperty<Name> nameMock = mock(Name.class);
        when(nameMock.isMatch(any())).thenReturn(answerTypeForNameIsMatch);

        IContactProperty<Address> addressMock = null;
        boolean sourceAddressSet = answerTypeForAddressIsMatch != null;

        if (sourceAddressSet) {
            addressMock = mock(Address.class);
            when(addressMock.isMatch(any())).thenReturn(answerTypeForAddressIsMatch);
        }

        IContactProperty phoneMock = null;
        boolean sourcePhoneSet = answerTypeForPhoneIsMatch != null;

        if (sourcePhoneSet) {
            phoneMock = mock(IContactProperty.class);
            when(phoneMock.isMatch(any())).thenReturn(answerTypeForPhoneIsMatch);
        }

        IContactProperty emailMock = null;
        boolean sourceEmailSet = answerTypeForEmailIsMatch != null;

        if (sourceEmailSet) {
            emailMock = mock(IContactProperty.class);
            when(emailMock.isMatch(any())).thenReturn(answerTypeForEmailIsMatch);
        }

        Contact contactWithMockInternals = new Contact();
        // TODO: Switch to use constructor for initialization once that is set up
        contactWithMockInternals.setName(nameMock);
        contactWithMockInternals.setAddress(addressMock);
        contactWithMockInternals.setPhone(phoneMock);
        contactWithMockInternals.setEmail(emailMock);

        IContact contactToComparewith = createContactStub(targetAddressSet, targetPhoneSet, targetEmailSet);

        // Run test
        ContactMatchResult result = contactWithMockInternals.compareTo(contactToComparewith);
        assertEquals(expectedMatchResult, result.getMatchType());

        // Verify expected internals were called
        verify(nameMock).isMatch(any());

        if (sourceAddressSet && targetAddressSet) {
            verify(addressMock).isMatch(any());
        }

        if (sourcePhoneSet && targetPhoneSet) {
            verify(phoneMock).isMatch(any());
        }

        if (sourceEmailSet && targetEmailSet) {
            verify(emailMock).isMatch(any());
        }
    }

    // This version does not set the target property if the corresponding source property is unset.
    private void runIsMatchTest(AnswerType answerTypeForNameIsMatch,
                                AnswerType answerTypeForAddressIsMatch,
                                AnswerType answerTypeForPhoneIsMatch,
                                AnswerType answerTypeForEmailIsMatch,
                                ContactMatchType expectedMatchResult) {
        runIsMatchTest(answerTypeForNameIsMatch,
                answerTypeForAddressIsMatch,
                answerTypeForPhoneIsMatch,
                answerTypeForEmailIsMatch,
                expectedMatchResult,
                answerTypeForAddressIsMatch != null,
                answerTypeForPhoneIsMatch != null,
                answerTypeForEmailIsMatch != null);
    }


//    private Contact createNonMatchingBaseContact()
//    {
//        Contact contact = new Contact();
//        contact.setFirstName("Adam");
//        contact.setLastName("Smith");
//        contact.setAddress("1400 Broadway");
//        contact.setCity("Boston");
//        contact.setState("MA");
//        contact.setZip("02144");
//        contact.setPhone("(617) 123-4567");
//        contact.setEmail("asmith@comcast.net");
//
//        return contact;
//    }
//
//    private void clearAddress(Contact contact)
//    {
//        contact.setAddress(null);
//        contact.setCity(null);
//        contact.setState(null);
//        contact.setZip(null);
//    }
//
//    private void copyAddress(Contact targetContact, Contact sourceContact)
//    {
//        targetContact.setAddress(sourceContact.getAddress());
//        targetContact.setCity(sourceContact.getCity());
//        targetContact.setState(sourceContact.getState());
//        targetContact.setZip(sourceContact.getZip());
//    }

    // Step 1 - Return ContactMatchType.NoMatch if nothing is specified in the
    // ContactToMerge or the ExistingContact
    // TODO: The following tests won't be valid when Name is required
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
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes, ContactMatchType.Identical);
    }

    // TODO: Need to change these tests to use mock objects. Add back in once we are ready to start make them pass.
    @Test
    void compareTo_Identical_AddressMissing()
    {
        runIsMatchTest(AnswerType.yes, null, AnswerType.yes, AnswerType.yes, ContactMatchType.Identical);
    }

    @Test
    void compareTo_Identical_PhoneMissing()
    {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, null, AnswerType.yes, ContactMatchType.Identical);
    }

    @Test
    void compareTo_Identical_EmailMissing()
    {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes, null, ContactMatchType.Identical);
    }

//    // Step 3 - Return ContactMatchType.NoMatch if all of the parts that are specified
//    // in ContactToMerge do not match the ExistingContact
//    @Test
//    void compareTo_NoMatch_AllParts() {
//        Contact c1 = createBaseContact();
//        Contact c2 = createNonMatchingBaseContact();
//
//        assertEquals(ContactMatchType.NoMatch, c1.compareTo(c2).getMatchType());
//    }
//
//    @Test
//    void compareTo_NoMatch_AddressMissing()
//    {
//        Contact c1 = createBaseContact();
//        clearAddress(c1);
//
//        Contact c2 = createNonMatchingBaseContact();
//
//        assertEquals(ContactMatchType.NoMatch, c1.compareTo(c2).getMatchType());
//    }
//
//    @Test
//    void compareTo_NoMatch_PhoneMissing()
//    {
//        Contact c1 = createBaseContact();
//        c1.setPhone(null);
//        Contact c2 = createNonMatchingBaseContact();
//
//        assertEquals(ContactMatchType.NoMatch, c1.compareTo(c2).getMatchType());
//    }
//
//    @Test
//    void compareTo_NoMatch_EmailMissing()
//    {
//        Contact c1 = createBaseContact();
//        c1.setEmail(null);
//        Contact c2 = createNonMatchingBaseContact();
//
//        assertEquals(ContactMatchType.NoMatch, c1.compareTo(c2).getMatchType());
//    }
//
//    // Step 4 - Return ContactMatchType.Match if the Name and at least one of the other
//    // parts (Address, Phones, or Email) match and if all the parts in the ContactToMerge
//    // that don't match are empty or null
//
//    @Test
//    void compareTo_Match_NameAndAddress()
//    {
//        Contact notMatching = createNonMatchingBaseContact();
//
//        Contact c1 = createBaseContact();
//        c1.setEmail(null);
//        c1.setPhone(null);
//
//        Contact c2 = createBaseContact();
//        c2.setEmail(notMatching.getEmail());
//        c2.setPhone(notMatching.getPhone());
//
//        assertEquals(ContactMatchType.Match, c1.compareTo(c2).getMatchType());
//    }
//
//    @Test
//    void compareTo_Match_NameAndEmail()
//    {
//        Contact notMatching = createNonMatchingBaseContact();
//
//        Contact c1 = createBaseContact();
//        c1.setPhone(null);
//        clearAddress(c1);
//
//        Contact c2 = createBaseContact();
//        c2.setPhone(notMatching.getPhone());
//        copyAddress(c1, notMatching);
//
//        assertEquals(ContactMatchType.Match, c1.compareTo(c2).getMatchType());
//    }
//
//    @Test
//    void compareTo_Match_NameAndPhone()
//    {
//        Contact notMatching = createNonMatchingBaseContact();
//
//        Contact c1 = createBaseContact();
//        c1.setEmail(null);
//        clearAddress(c1);
//
//        Contact c2 = createBaseContact();
//        c2.setEmail(notMatching.getEmail());
//        copyAddress(c1, notMatching);
//
//        assertEquals(ContactMatchType.Match, c1.compareTo(c2).getMatchType());
//    }
//
//    @Test
//    void compareTo_PotentialMatch_OnlyNameSpecified()
//    {
//        Contact c1 = new Contact();
//        c1.setLastName("Smith");
//        c1.setFirstName("Adam");
//
//        Contact c2 = new Contact();
//        c2.setLastName(c1.getLastName());
//        c2.setFirstName(c1.getFirstName());
//
//        assertEquals(ContactMatchType.Match, c1.compareTo(c2).getMatchType());
//    }
//
//    // Step 5 - Return ContactMatchType.MatchButModifying if the Name and at least one
//    // of the other parts (Address, Phones, or Email) match but there is at least one
//    // part that doesn't match
//
//    @Test
//    void compareTo_MatchButModifying_NameAndAddress()
//    {
//        Contact notMatching = createNonMatchingBaseContact();
//
//        Contact c1 = createBaseContact();
//        Contact c2 = createBaseContact();
//        c2.setEmail(notMatching.getEmail());
//        c2.setPhone(notMatching.getPhone());
//
//        assertEquals(ContactMatchType.MatchButModifying, c1.compareTo(c2).getMatchType());
//    }
//
//    @Test
//    void compareTo_MatchButModifying_NameAndEmail()
//    {
//        Contact notMatching = createNonMatchingBaseContact();
//
//        Contact c1 = createBaseContact();
//
//        Contact c2 = createBaseContact();
//        c2.setPhone(notMatching.getPhone());
//        copyAddress(c1, notMatching);
//
//        assertEquals(ContactMatchType.MatchButModifying, c1.compareTo(c2).getMatchType());
//    }
//
//    @Test
//    void compareTo_MatchButModifying_NameAndPhone()
//    {
//        Contact notMatching = createNonMatchingBaseContact();
//
//        Contact c1 = createBaseContact();
//
//        Contact c2 = createBaseContact();
//        c2.setEmail(notMatching.getEmail());
//        copyAddress(c1, notMatching);
//
//        assertEquals(ContactMatchType.MatchButModifying, c1.compareTo(c2).getMatchType());
//    }
//
//    // Step 6 - Return ContactMatchType.PotentialMatch if the Names might match
//    // or the Name does match but nothing else matches
//    @Test
//    void compareTo_PotentialMatch_OnlyNameMatches()
//    {
//        Contact c1 = createBaseContact();
//
//        Contact c2 = createNonMatchingBaseContact();
//        c2.setLastName(c1.getLastName());
//        c2.setFirstName(c1.getFirstName());
//
//        assertEquals(ContactMatchType.PotentialMatch, c1.compareTo(c2).getMatchType());
//    }
//
//    @Test
//    void compareTo_PotentialMatch_NameMightMatch()
//    {
//        Contact c1 = createBaseContact();
//
//        Contact c2 = createNonMatchingBaseContact();
//        c2.setLastName(c1.getLastName());
//
//        // Set first name to first initial to cause a maybe result on name matching
//        c2.setFirstName(c1.getFirstName().substring(0, 1));
//
//        assertEquals(ContactMatchType.PotentialMatch, c1.compareTo(c2).getMatchType());
//    }
//
//    // Step 7 - Return ContactMatchType.Related if at least 1 of Address, Phone, or
//    // Email match
//    @Test
//    void compareTo_Related_AddressMatch()
//    {
//        Contact c1 = createBaseContact();
//
//        Contact c2 = createNonMatchingBaseContact();
//        copyAddress(c2, c1);
//
//        assertEquals(ContactMatchType.Related, c1.compareTo(c2).getMatchType());
//    }
//
//    @Test
//    void compareTo_Related_PhoneMatch()
//    {
//        Contact c1 = createBaseContact();
//
//        Contact c2 = createNonMatchingBaseContact();
//        c2.setPhone(c1.getPhone());
//
//        assertEquals(ContactMatchType.Related, c1.compareTo(c2).getMatchType());
//    }
//
//    @Test
//    void compareTo_Related_EmailMatch()
//    {
//        Contact c1 = createBaseContact();
//
//        Contact c2 = createNonMatchingBaseContact();
//        c2.setEmail(c1.getEmail());
//
//        assertEquals(ContactMatchType.Related, c1.compareTo(c2).getMatchType());
//    }
//
//    // Step 8 - Return ContactMatchType.PotentiallyRelated if at least one of
//    // Address, Phone, or Email is "might match"
//    @Test
//    void compareTo_PotentiallyRelated_AddressMightMatch()
//    {
//        Contact c1 = createBaseContact();
//
//        Contact c2 = createNonMatchingBaseContact();
//
//        // Make address match a maybe because road type (i.e. "St") is missing
//        c2.setAddress("123 Main");
//
//        assertEquals(ContactMatchType.Related, c1.compareTo(c2).getMatchType());
//    }

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
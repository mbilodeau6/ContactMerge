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

    /* -------------------------------------------------------------------------
     * There are a large number of CompareTo tests which are broken into steps
     * reflecting the rules used to determine if a ContactToMerge matches an
     * ExistingContact.
     * -------------------------------------------------------------------------
     */

    // Helper methods for tests
    private Contact createBaseContact()
    {
        Contact contact = new Contact();
        contact.setName(new Name(new LastName("Doe"), new FirstName("John")));
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

    // Step 3 - Return ContactMatchType.NoMatch if all of the parts that are specified
    // in ContactToMerge do not match the ExistingContact
    @Test
    void compareTo_NoMatch_AllParts() {
        runIsMatchTest(AnswerType.no, AnswerType.no, AnswerType.no, AnswerType.no, ContactMatchType.NoMatch);
    }

    @Test
    void compareTo_NoMatch_AddressMissing()
    {
        runIsMatchTest(AnswerType.no, null, AnswerType.no, AnswerType.no, ContactMatchType.NoMatch);
    }

    @Test
    void compareTo_NoMatch_PhoneMissing()
    {
        runIsMatchTest(AnswerType.no, AnswerType.no, null, AnswerType.no, ContactMatchType.NoMatch);
    }

    @Test
    void compareTo_NoMatch_EmailMissing()
    {
        runIsMatchTest(AnswerType.no, AnswerType.no, AnswerType.no, null, ContactMatchType.NoMatch);
    }

    // Step 4 - Return ContactMatchType.Match if the Name and at least one of the other
    // parts (Address, Phones, or Email) match and if all the parts in the ContactToMerge
    // that don't match are empty or null

    @Test
    void compareTo_Match_NameAndAddress()
    {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, null,
                null, ContactMatchType.Match,
                true, true, true);
    }

    @Test
    void compareTo_Match_NameAndEmail()
    {
        runIsMatchTest(AnswerType.yes, null, null,
                AnswerType.yes, ContactMatchType.Match, true, true, true);
    }

    @Test
    void compareTo_Match_NameAndPhone()
    {
        runIsMatchTest(AnswerType.yes, null, AnswerType.yes,
                null, ContactMatchType.Match,
                true, true, true);
    }

    @Test
    void compareTo_PotentialMatch_OnlyNameSpecified()
    {
        runIsMatchTest(AnswerType.yes, null, null,
                null, ContactMatchType.PotentialMatch,
                true, true, true);
    }

    // Step 5 - Return ContactMatchType.MatchButModifying if the Name and at least one
    // of the other parts (Address, Phones, or Email) match but there is at least one
    // part that doesn't match

    @Test
    void compareTo_MatchButModifying_NameAndAddress()
    {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.no, AnswerType.no, ContactMatchType.MatchButModifying);
    }

    @Test
    void compareTo_MatchButModifying_NameAndEmail()
    {
        runIsMatchTest(AnswerType.yes, AnswerType.no, AnswerType.no, AnswerType.yes, ContactMatchType.MatchButModifying);
    }

    @Test
    void compareTo_MatchButModifying_NameAndPhone()
    {
        runIsMatchTest(AnswerType.yes, AnswerType.no, AnswerType.yes, AnswerType.no, ContactMatchType.MatchButModifying);
    }

    // Step 6 - Return ContactMatchType.PotentialMatch if the Names might match
    // or the Name does match but nothing else matches
    @Test
    void compareTo_PotentialMatch_OnlyNameMatches()
    {
        runIsMatchTest(AnswerType.yes, AnswerType.no, AnswerType.no, AnswerType.no, ContactMatchType.PotentialMatch);
    }

    @Test
    void compareTo_PotentialMatch_NameMightMatch()
    {
        runIsMatchTest(AnswerType.maybe, AnswerType.no, AnswerType.no, AnswerType.no, ContactMatchType.PotentialMatch);
    }

    // Step 7 - Return ContactMatchType.Related if at least 1 of Address, Phone, or
    // Email match
    @Test
    void compareTo_Related_AddressMatch()
    {
        runIsMatchTest(AnswerType.no, AnswerType.yes, AnswerType.no, AnswerType.no, ContactMatchType.Related);
    }

    @Test
    void compareTo_Related_PhoneMatch()
    {
        runIsMatchTest(AnswerType.no, AnswerType.no, AnswerType.yes, AnswerType.no, ContactMatchType.Related);
    }

    @Test
    void compareTo_Related_EmailMatch()
    {
        runIsMatchTest(AnswerType.no, AnswerType.no, AnswerType.yes, AnswerType.no, ContactMatchType.Related);
    }

    // Step 8 - Return ContactMatchType.PotentiallyRelated if at least one of
    // Address, Phone, or Email is "might match"
    @Test
    void compareTo_PotentiallyRelated_AddressMightMatch()
    {
        runIsMatchTest(AnswerType.no, AnswerType.maybe, AnswerType.no, AnswerType.no,
                ContactMatchType.PotentiallyRelated);
    }

    @Test
    void compareTo_PotentiallyRelated_PhoneMightMatch()
    {
        runIsMatchTest(AnswerType.no, AnswerType.no, AnswerType.maybe, AnswerType.no,
                ContactMatchType.PotentiallyRelated);
    }

    @Test
    void compareTo_PotentiallyRelated_EmailMightMatch()
    {
        runIsMatchTest(AnswerType.no, AnswerType.no, AnswerType.no, AnswerType.maybe,
                ContactMatchType.PotentiallyRelated);
    }

    /* -------------------------------------------------------------------------
     * Special cases
     * -------------------------------------------------------------------------
     */

    @Test
    void compareTo_PotentiallyRelated_SameLastName() {
        Contact c1 = createBaseContact();
        Contact c2 = new Contact();
        c2.setName(new Name(new LastName("Doe"), new FirstName("Adam")));
        c2.setAddress(new Address(new StreetAddress("92 Broadway"),
                new GeneralProperty("Tucson"),
                new GeneralProperty("AZ"),
                null,
                new GeneralProperty("85750")));
        c2.setPhone(new PhoneNumber("(520) 987-6543"));
        c2.setEmail(new GeneralProperty("asmith@homail.com"));

        assertEquals(ContactMatchType.PotentiallyRelated, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_MatchButModifying_RealContacts() {
        Contact c1 = createBaseContact();

        // Same name and phone number
        Contact c2 = new Contact();
        c2.setName(c1.getName());
        c2.setAddress(new Address(new StreetAddress("92 Broadway"),
                new GeneralProperty("Tucson"),
                new GeneralProperty("AZ"),
                null,
                new GeneralProperty("85750")));
        c2.setPhone(c1.getPhone());
        c2.setEmail(new GeneralProperty("asmith@homail.com"));

        assertEquals(ContactMatchType.MatchButModifying, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_Related_RealContacts() {
        Contact c1 = createBaseContact();
        Contact c2 = new Contact();
        c2.setName(new Name(new FirstName("Adam"), new LastName("Smith")));
        c2.setAddress(new Address(new StreetAddress("92 Broadway"),
                new GeneralProperty("Tucson"),
                new GeneralProperty("AZ"),
                null,
                new GeneralProperty("85750")));
        c2.setPhone(c1.getPhone());
        c2.setEmail(new GeneralProperty("asmith@homail.com"));

        assertEquals(ContactMatchType.Related, c1.compareTo(c2).getMatchType());
    }

    @Test
    void compareTo_Potentially_RealContacts() {
        Contact c1 = createBaseContact();

        // Address is same but without the street type
        Contact c2 = new Contact();
        c2.setName(new Name(new FirstName("Adam"), new LastName("Smith")));
        c2.setAddress(new Address(new StreetAddress("123 Main"),
                new GeneralProperty("Tucson"),
                new GeneralProperty("AZ"),
                null,
                new GeneralProperty("85750")));
        c2.setPhone(new PhoneNumber("(520) 987-6543"));
        c2.setEmail(new GeneralProperty("asmith@homail.com"));

        assertEquals(ContactMatchType.PotentiallyRelated, c1.compareTo(c2).getMatchType());
    }
}
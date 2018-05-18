package com.cft.contactmerge.contact.tests;

import com.cft.contactmerge.AnswerType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.cft.contactmerge.contact.*;


class NameTest {

    private String createVerificationMessage(String itemName)
    {
        return "Verify " + itemName;
    }

    @Test
    void NameProperty_Constructor()
    {
        Name property = new Name(new LastNameProperty("Doe"), new FirstNameProperty("Jane"));

        assertNotNull(property);
    }

    @Test
    void NameProperty_getValue() {
        Name property = new Name(new LastNameProperty("Doe"), new FirstNameProperty("Jane"));

        assertEquals("Jane", property.getValue().getFirstName().getValue(), createVerificationMessage("FirstName"));
        assertEquals("Doe", property.getValue().getLastName().getValue(), createVerificationMessage("LastName"));
    }

    @Test
    void NameProperty_toString() {
        Name property = new Name(new LastNameProperty("Doe"), new FirstNameProperty("Jane"));

        assertEquals("Doe, Jane", property.toString());
    }

    @Test
    void NameProperty_getFirstName() {
        Name property = new Name(new LastNameProperty("Doe"), new FirstNameProperty("Jane"));

        assertEquals("Jane", property.getFirstName().getValue());
    }

    @Test
    void NameProperty_getLastName() {
        Name property = new Name(new LastNameProperty("Doe"), new FirstNameProperty("Jane"));

        assertEquals("Doe", property.getLastName().getValue());
    }

    // The following code creates a Name object that is built on mock parts so that we
    // can fully test the isMatch() logic. You use it by creating a Name with 
    // createNameWithMockedInternals() and then make sure all of the mocks were called
    // by calling verifyExpectedNamePartsCalled().
    private class NameWithMockedParts {
        public Name name;
        public IContactProperty firstNameMock;
        public IContactProperty lastNameMock;
    }

    private NameWithMockedParts createNameWithMockedInternals(AnswerType lastNameIsMatch, AnswerType firstNameIsMatch)
    {
        NameWithMockedParts nameMock = new NameWithMockedParts();

        nameMock.firstNameMock = mock(IContactProperty.class);
        when(nameMock.firstNameMock.isMatch(any())).thenReturn(firstNameIsMatch);

        nameMock.lastNameMock = mock(IContactProperty.class);
        when(nameMock.lastNameMock.isMatch(any())).thenReturn(lastNameIsMatch);

        nameMock.name = new Name(nameMock.lastNameMock, nameMock.firstNameMock);

        return nameMock;
    }

    private void verifyExpectedNamePartsCalled(NameWithMockedParts nameMock, boolean verifyIsMatchCalledOnFirstName)
    {
        verify(nameMock.lastNameMock).isMatch(any());

        if (verifyIsMatchCalledOnFirstName) {
            verify(nameMock.firstNameMock).isMatch(any());
        }
    }

    // Creates a stub so that isMatch() can execute. The Name object will never actually compare
    // its parts with this object.
    private IContactProperty<Name> createNameStub()
    {
        IContactProperty<Name> nameToCompareWith = mock(IContactProperty.class);
        when(nameToCompareWith.getValue()).thenReturn(new Name(null, null));

        return nameToCompareWith;
    }

    // Executes a specific test, verifies the result, and verifies that the internal name parts
    // are called as expected.
    private void runIsMatchTest(AnswerType answerTypeForLastNameIsMatch,
                                AnswerType answerTypeForFirstNameIsMatch,
                                AnswerType expectedAnswerType)
    {
        NameWithMockedParts nameToCompareMock = createNameWithMockedInternals(answerTypeForLastNameIsMatch, answerTypeForFirstNameIsMatch);

        assertEquals(expectedAnswerType, nameToCompareMock.name.isMatch(createNameStub()));

        boolean isCheckRequiredOnFirstName = (answerTypeForLastNameIsMatch != AnswerType.no);
        verifyExpectedNamePartsCalled(nameToCompareMock, isCheckRequiredOnFirstName);
    }

    // The actual isMatch tests
    @Test
    void NameProperty_isMatchWhenLastNameYesAndFirstNameYes() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes);
    }

    @Test
    void NameProperty_isMatchWhenLastNameYesAndFirstNameMaybe() {
        runIsMatchTest(AnswerType.yes, AnswerType.maybe, AnswerType.maybe);
    }

    @Test
    void NameProperty_isMatchWhenLastNameYesAndFirstNameNo() {
        runIsMatchTest(AnswerType.yes, AnswerType.no, AnswerType.no);
    }

    @Test
    void NameProperty_isMatchWhenLastNameMaybeAndFirstNameYes() {
        runIsMatchTest(AnswerType.maybe, AnswerType.yes, AnswerType.maybe);
    }

    @Test
    void NameProperty_isMatchWhenLastNameMaybeAndFirstNameMaybe() {
        runIsMatchTest(AnswerType.maybe, AnswerType.maybe, AnswerType.maybe);
    }

    @Test
    void NameProperty_isMatchWhenLastNameMaybeAndFirstNameNo() {
        runIsMatchTest(AnswerType.maybe, AnswerType.no, AnswerType.no);
    }

    @Test
    void NameProperty_isMatchWhenLastNameNoAndFirstNameYes() {
        runIsMatchTest(AnswerType.no, AnswerType.yes, AnswerType.no);
    }

    @Test
    void NameProperty_isMatchWhenLastNameNoAndFirstNameMaybe() {
        runIsMatchTest(AnswerType.no, AnswerType.yes, AnswerType.no);
    }

    @Test
    void NameProperty_isMatchWhenLastNameNoAndFirstNameNo() {
        runIsMatchTest(AnswerType.no, AnswerType.no, AnswerType.no);
    }

}

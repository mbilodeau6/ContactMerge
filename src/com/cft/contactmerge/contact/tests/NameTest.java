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

    private LastName createMockLastName(String lastName) {
        LastName lastNameMock = mock(LastName.class);
        when(lastNameMock.getValue()).thenReturn(lastName);

        return lastNameMock;
    }

    private FirstName createMockFirstName(String firstName) {
        FirstName firstNameMock = mock(FirstName.class);
        when(firstNameMock.getValue()).thenReturn(firstName);

        return firstNameMock;
    }

    /* -------------------------------------------------------------------------------------
     * Constructor Tests
     * -------------------------------------------------------------------------------------
     */
    @Test
    void Constructor()
    {
        Name property = new Name(createMockLastName("Doe"), createMockFirstName("Jane"));

        assertNotNull(property);
    }

    @Test
    void Constructor_NullLastName()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new Name(null, createMockFirstName("Jane")));
    }

    @Test
    void Constructor_NullFirstName()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new Name(createMockLastName("Doe"), null));
    }

    /* -------------------------------------------------------------------------------------
     * Tests for retrieving contents of the Name property
     * -------------------------------------------------------------------------------------
     */
    @Test
    void getValue() {
        LastName mockLastName = createMockLastName("Doe");
        FirstName mockFirstName = createMockFirstName("Jane");

        Name property = new Name(mockLastName, mockFirstName);

        assertEquals(mockFirstName, property.getValue().getFirstName(), "Verify FirstName");
        assertEquals(mockLastName, property.getValue().getLastName(), "Verify LastName");
    }

    @Test
    void Name_toString() {
        LastName mockLastName = createMockLastName("Doe");
        FirstName mockFirstName = createMockFirstName("Jane");

        Name property = new Name(mockLastName, mockFirstName);

        assertEquals("Doe, Jane", property.toString());
    }

    @Test
    void getFirstName() {
        LastName mockLastName = createMockLastName("Doe");
        FirstName mockFirstName = createMockFirstName("Jane");

        Name property = new Name(mockLastName, mockFirstName);

        assertEquals(mockFirstName, property.getFirstName());
    }

    @Test
    void getLastName() {
        LastName mockLastName = createMockLastName("Doe");
        FirstName mockFirstName = createMockFirstName("Jane");

        Name property = new Name(mockLastName, mockFirstName);

        assertEquals(mockLastName, property.getLastName());
    }

    /* ----------------------------------------------------------------------------------
     * Remainging code/tests are for isMatch() testing
     * ----------------------------------------------------------------------------------
     */

    // Creates a stub so that isMatch() can execute. The Name object will never actually compare
    // its parts with this object.
    private IContactProperty<Name> createNameStub()
    {

        LastName lastNameMock = mock(LastName.class);
        when(lastNameMock.getValue()).thenReturn("This is where you would find LastName value");

        FirstName firstNameMock = mock(FirstName.class);
        when(firstNameMock.getValue()).thenReturn("This is where you would find FirstName value");

        IContactProperty<Name> nameToCompareWith = mock(IContactProperty.class);
        when(nameToCompareWith.getValue()).thenReturn(new Name( lastNameMock, firstNameMock));

        return nameToCompareWith;
    }

    // Executes a specific test, verifies the result, and verifies that the internal name parts
    // are called as expected.
    private void runIsMatchTest(AnswerType answerTypeForLastNameIsMatch,
                                AnswerType answerTypeForFirstNameIsMatch,
                                AnswerType expectedAnswerType)
    {
        // Set up Name with mock internals
        LastName lastNameMock = mock(LastName.class);
        when(lastNameMock.isMatch(any())).thenReturn(answerTypeForLastNameIsMatch);

        FirstName firstNameMock = mock(FirstName.class);
        when(firstNameMock.isMatch(any())).thenReturn(answerTypeForFirstNameIsMatch);

        Name nameWithMockInternals = new Name(lastNameMock, firstNameMock);

        // Run test
        assertEquals(expectedAnswerType, nameWithMockInternals.isMatch(createNameStub()));

        // Verify expected internals were called
        verify(lastNameMock).isMatch(any());

        boolean isCheckRequiredOnFirstName = (answerTypeForLastNameIsMatch != AnswerType.no);

        if (isCheckRequiredOnFirstName) {
            verify(firstNameMock).isMatch(any());
        }
    }

    // The actual isMatch tests
    @Test
    void isMatchWhenLastNameYesAndFirstNameYes() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes);
    }

    @Test
    void isMatchWhenLastNameYesAndFirstNameMaybe() {
        runIsMatchTest(AnswerType.yes, AnswerType.maybe, AnswerType.maybe);
    }

    @Test
    void isMatchWhenLastNameYesAndFirstNameNo() {
        runIsMatchTest(AnswerType.yes, AnswerType.no, AnswerType.no);
    }

    @Test
    void isMatchWhenLastNameMaybeAndFirstNameYes() {
        runIsMatchTest(AnswerType.maybe, AnswerType.yes, AnswerType.maybe);
    }

    @Test
    void isMatchWhenLastNameMaybeAndFirstNameMaybe() {
        runIsMatchTest(AnswerType.maybe, AnswerType.maybe, AnswerType.maybe);
    }

    @Test
    void isMatchWhenLastNameMaybeAndFirstNameNo() {
        runIsMatchTest(AnswerType.maybe, AnswerType.no, AnswerType.no);
    }

    @Test
    void isMatchWhenLastNameNoAndFirstNameYes() {
        runIsMatchTest(AnswerType.no, AnswerType.yes, AnswerType.no);
    }

    @Test
    void isMatchWhenLastNameNoAndFirstNameMaybe() {
        runIsMatchTest(AnswerType.no, AnswerType.yes, AnswerType.no);
    }

    @Test
    void isMatchWhenLastNameNoAndFirstNameNo() {
        runIsMatchTest(AnswerType.no, AnswerType.no, AnswerType.no);
    }

    // Adding one test that uses the real internal properties to make sure everything is
    // wired up correctly. All other isMatch() tests are using mocks.
    @Test
    void isMatch_Maybe_RealInternalProperties() {
        Name sourceName = new Name(new LastName("Adams"), new FirstName("Bobby Joe"));

        Name targetName = new Name(new LastName("Adams"), new FirstName("Joe"));

        assertEquals(AnswerType.maybe, sourceName.isMatch(targetName));
    }

}

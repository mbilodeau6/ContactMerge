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

    /* -------------------------------------------------------------------------------------
     * Constructor Tests
     * -------------------------------------------------------------------------------------
     */
    @Test
    void Constructor()
    {
        Name property = new Name(SharedTestHelpers.createMockContactProperty("Doe"),
                SharedTestHelpers.createMockContactProperty("Jane"));

        assertNotNull(property);
    }

    @Test
    void Constructor_NullLastName()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new Name(null, SharedTestHelpers.createMockContactProperty("Jane")));
    }

    @Test
    void Constructor_NullFirstName()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new Name(SharedTestHelpers.createMockContactProperty("Doe"), null));
    }

    /* -------------------------------------------------------------------------------------
     * Tests for retrieving contents of the Name property
     * -------------------------------------------------------------------------------------
     */
    @Test
    void getValue() {
        IContactProperty<String> mockLastName = SharedTestHelpers.createMockContactProperty("Doe");
        IContactProperty<String> mockFirstName = SharedTestHelpers.createMockContactProperty("Jane");

        Name property = new Name(mockLastName, mockFirstName);

        assertEquals(mockFirstName, property.getValue().getFirstName(), "Verify FirstName");
        assertEquals(mockLastName, property.getValue().getLastName(), "Verify LastName");
    }

    @Test
    void Name_toString() {
        IContactProperty<String> mockLastName = SharedTestHelpers.createMockContactProperty("Doe");
        IContactProperty<String> mockFirstName = SharedTestHelpers.createMockContactProperty("Jane");

        Name property = new Name(mockLastName, mockFirstName);

        assertEquals("Doe, Jane", property.toString());
    }

    @Test
    void getFirstName() {
        IContactProperty<String> mockLastName = SharedTestHelpers.createMockContactProperty("Doe");
        IContactProperty<String> mockFirstName = SharedTestHelpers.createMockContactProperty("Jane");

        Name property = new Name(mockLastName, mockFirstName);

        assertEquals(mockFirstName, property.getFirstName());
    }

    @Test
    void getLastName() {
        IContactProperty<String> mockLastName = SharedTestHelpers.createMockContactProperty("Doe");
        IContactProperty<String> mockFirstName = SharedTestHelpers.createMockContactProperty("Jane");

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

        IContactProperty<String> lastNameMock = mock(IContactProperty.class);
        when(lastNameMock.getValue()).thenReturn("This is where you would find LastName value");

        IContactProperty<String> firstNameMock = mock(IContactProperty.class);
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
        IContactProperty lastNameMock = mock(IContactProperty.class);
        when(lastNameMock.isMatch(any())).thenReturn(answerTypeForLastNameIsMatch);

        IContactProperty firstNameMock = mock(IContactProperty.class);
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
}

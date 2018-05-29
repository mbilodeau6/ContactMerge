package com.cft.contactmerge.contact.tests;

import com.cft.contactmerge.AnswerType;
import com.cft.contactmerge.contact.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddressTest {

    private Address createTestAddress() {
        return new Address(SharedTestHelpers.createMockContactProperty("123 Main St"),
                SharedTestHelpers.createMockContactProperty("10"),
                SharedTestHelpers.createMockContactProperty("Tucson"),
                SharedTestHelpers.createMockContactProperty("AZ"),
                SharedTestHelpers.createMockContactProperty("85750"));
    }

    /* -------------------------------------------------------------------------------------
     * Constructor Tests
     * -------------------------------------------------------------------------------------
     */
    @Test
    void Constructor()
    {
        Address address = createTestAddress();
        assertNotNull(address);
    }

    @Test
    void Constructor_ApartmentAndZipOptional()
    {
        Address address = new Address(SharedTestHelpers.createMockContactProperty("123 Main St"),
                null,
                SharedTestHelpers.createMockContactProperty("Tucson"),
                SharedTestHelpers.createMockContactProperty("AZ"),
                null);

        assertNotNull(address);
    }

    @Test
    void Constructor_NullStreetAddress()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new Address(null,
                        null,
                        SharedTestHelpers.createMockContactProperty("Tucson"),
                        SharedTestHelpers.createMockContactProperty("AZ"),
                        null));
    }

    @Test
    void Constructor_NullCity()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new Address(SharedTestHelpers.createMockContactProperty("123 Main St"),
                        null,
                        null,
                        SharedTestHelpers.createMockContactProperty("AZ"),
                        null));
    }

    @Test
    void Constructor_NullState()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new Address(SharedTestHelpers.createMockContactProperty("123 Main St"),
                        null,
                        SharedTestHelpers.createMockContactProperty("Tucson"),
                        null, null));
    }

    /* ----------------------------------------------------------------------------------
     * Helper methods for isMatch() testing
     * ----------------------------------------------------------------------------------
     */

    // Creates a stub so that isMatch() can execute. The Address object will never actually compare
    // its parts with this object.
    private IContactProperty<Address> createAddressStub(boolean apartmentSet, boolean zipSet)
    {
        IContactProperty<String> streetAddressMock = mock(IContactProperty.class);
        when(streetAddressMock.getValue()).thenReturn("This is where you would find the StreetAddress value");

        IContactProperty<String> cityMock = mock(IContactProperty.class);
        when(cityMock.getValue()).thenReturn("This is where you would find City value");

        IContactProperty<String> stateMock = mock(IContactProperty.class);
        when(stateMock.getValue()).thenReturn("This is where you would find the State value");

        IContactProperty<String> apartmentMock = null;

        if (apartmentSet) {
            apartmentMock = mock(IContactProperty.class);
            when(apartmentMock.getValue()).thenReturn("This is where you would find Apartment value");
        }

        IContactProperty<String> zipMock = null;
        if (zipSet) {
            zipMock = mock(IContactProperty.class);
            when(zipMock.getValue()).thenReturn("This is where you would find the Zip value");
        }

        IContactProperty<Address> addressToCompareWith = mock(IContactProperty.class);
        when(addressToCompareWith.getValue()).thenReturn(new Address( streetAddressMock,
                apartmentMock, cityMock, stateMock, zipMock));

        return addressToCompareWith;
    }

    // Executes a specific test, verifies the result, and verifies that the internal name parts
    // are called as expected.
    private void runIsMatchTest(AnswerType answerTypeForStreetAddressIsMatch,
                                AnswerType answerTypeForCityIsMatch,
                                AnswerType answerTypeForStateIsMatch,
                                AnswerType answerTypeForApartmentIsMatch,
                                AnswerType answerTypeForZipIsMatch,
                                AnswerType expectedAnswerType,
                                boolean targetApartmentSet,
                                boolean targetZipSet)
    {
        // Set up Address with mock internals
        IContactProperty streetAddressMock = mock(IContactProperty.class);
        when(streetAddressMock.isMatch(any())).thenReturn(answerTypeForStreetAddressIsMatch);

        IContactProperty cityMock = mock(IContactProperty.class);
        when(cityMock.isMatch(any())).thenReturn(answerTypeForCityIsMatch);

        IContactProperty stateMock = mock(IContactProperty.class);
        when(stateMock.isMatch(any())).thenReturn(answerTypeForStateIsMatch);

        IContactProperty apartmentMock = null;
        boolean sourceApartmentSet = answerTypeForApartmentIsMatch != null;

        if (sourceApartmentSet) {
            apartmentMock = mock(IContactProperty.class);
            when(apartmentMock.isMatch(any())).thenReturn(answerTypeForApartmentIsMatch);
        }

        IContactProperty zipMock = null;
        boolean sourceZipSet = answerTypeForZipIsMatch != null;

        if (sourceZipSet) {
            zipMock = mock(IContactProperty.class);
            when(zipMock.isMatch(any())).thenReturn(answerTypeForZipIsMatch);
        }

        Address addressWithMockInternals = new Address(streetAddressMock,
                apartmentMock, cityMock, stateMock, zipMock);

        // Run test
        assertEquals(expectedAnswerType, addressWithMockInternals.isMatch(createAddressStub(targetApartmentSet, targetZipSet)));

        // Verify expected internals were called
        verify(streetAddressMock).isMatch(any());
        verify(cityMock).isMatch(any());
        verify(stateMock).isMatch(any());

        if (sourceApartmentSet && targetApartmentSet) {
            verify(apartmentMock).isMatch(any());
        }

        if (sourceZipSet && targetZipSet) {
            verify(zipMock).isMatch(any());
        }
    }

    // This version makes the source and target apartment/zip null if the corresponding AnswerType
    // is null
    private void runIsMatchTest(AnswerType answerTypeForStreetAddressIsMatch,
                                AnswerType answerTypeForCityIsMatch,
                                AnswerType answerTypeForStateIsMatch,
                                AnswerType answerTypeForApartmentIsMatch,
                                AnswerType answerTypeForZipIsMatch,
                                AnswerType expectedAnswerType)
    {
        runIsMatchTest(answerTypeForStreetAddressIsMatch,
                answerTypeForCityIsMatch,
                answerTypeForStateIsMatch,
                answerTypeForApartmentIsMatch,
                answerTypeForZipIsMatch,
                expectedAnswerType,
                answerTypeForApartmentIsMatch != null,
                answerTypeForZipIsMatch != null);
    }

    /* -------------------------------------------------------------------------------------
     * isMatch tests
     * -------------------------------------------------------------------------------------
     */

    @Test
    void isMatch_Yes_AllPartsMatch() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes);
    }

    @Test
    void isMatch_Yes_AllPartsMatchButZipIsMaybe() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.maybe, AnswerType.yes);
    }

    @Test
    void isMatch_Yes_AllPartsMatchButZipsMissing() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes, null, AnswerType.yes);
    }

    @Test
    void isMatch_Yes_AllPartsMatchButApartmentsMissing() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes, null, AnswerType.yes, AnswerType.yes);
    }

    @Test
    void isMatch_Maybe_StreetAddressMaybe() {
        runIsMatchTest(AnswerType.maybe, AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.maybe);
    }

    @Test
    void isMatch_Maybe_CityMaybe() {
        runIsMatchTest(AnswerType.yes, AnswerType.maybe, AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.maybe);
    }

    @Test
    void isMatch_Maybe_StateMaybe() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.maybe, AnswerType.yes, AnswerType.yes, AnswerType.maybe);
    }

    @Test
    void isMatch_Maybe_ZipMissingInFirst() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes, null,
                AnswerType.maybe, true, true);
    }

    @Test
    void isMatch_Maybe_ZipMissingInSecond() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes,
                AnswerType.maybe, true, false);
    }

    @Test
    void isMatch_Maybe_AptMissingInFirst() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes, null, AnswerType.yes,
                AnswerType.maybe, true, true);
    }

    @Test
    void isMatch_Maybe_AptMissingInSecond() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes,
                AnswerType.maybe, false, true);
    }

    @Test
    void isMatch_Maybe_AptMaybe() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.maybe, AnswerType.yes, AnswerType.maybe);
    }

    @Test
    void isMatch_Maybe_ZipNo() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.no, AnswerType.maybe);
    }

    @Test
    void isMatch_No_StreetAddressNo() {
        runIsMatchTest(AnswerType.no, AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.no);
    }

    @Test
    void isMatch_No_StateNo() {
        runIsMatchTest(AnswerType.yes, AnswerType.no, AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.no);
    }

    @Test
    void isMatch_No_CityNo() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.no, AnswerType.yes, AnswerType.yes, AnswerType.no);
    }

    @Test
    void isMatch_No_AptNo() {
        runIsMatchTest(AnswerType.yes, AnswerType.yes, AnswerType.yes, AnswerType.no, AnswerType.yes, AnswerType.no);
    }

    @Test
    void isMatch_No_NoOverridesMaybe() {
        runIsMatchTest(AnswerType.maybe, AnswerType.maybe, AnswerType.no, AnswerType.yes, AnswerType.yes, AnswerType.no);
    }

    // Adding one test that uses the real internal properties to make sure everything is
    // wired up correctly. All other isMatch() tests are using mocks.
    @Test
    void isMatch_Maybe_RealInternalProperties() {
        Address sourceAddress = new Address(new StreetAddress("123 Main St"),
                null,
                new GeneralProperty("Tucson"),
                new GeneralProperty("AZ"),
                null);

        Address targetAddress = new Address(new StreetAddress("123 Main St"),
                new Apartment("10B"),
                new GeneralProperty("Tucson"),
                new GeneralProperty("AZ"),
                null);

        assertEquals(AnswerType.maybe, sourceAddress.isMatch(targetAddress));
    }

    /* -------------------------------------------------------------------------------------
     * Tests for value retrieval
     * -------------------------------------------------------------------------------------
     */

    @Test
    void getValue() {
        Address address = createTestAddress();
        assertEquals(address, address.getValue());
    }

    @Test
    void Address_toString_FullAddress() {
        Address address = createTestAddress();
        assertEquals("123 Main St, 10, Tucson, AZ", address.toString());
    }

    @Test
    void Address_toString_MissingApartmentAndZip() {
        Address address = new Address(SharedTestHelpers.createMockContactProperty("123 Main St"),
                null,
                SharedTestHelpers.createMockContactProperty("Tucson"),
                SharedTestHelpers.createMockContactProperty("AZ"),
                null);

        assertEquals("123 Main St, Tucson, AZ", address.toString());
    }

    @Test
    void getStreetAddress() {
        Address address = createTestAddress();
        assertEquals("123 Main St", address.getStreetAddress().getValue());
    }

    @Test
    void getApartment() {
        Address address = createTestAddress();
        assertEquals("Tucson", address.getCity().getValue());
    }

    @Test
    void getCity() {
        Address address = createTestAddress();
        assertEquals("AZ", address.getState().getValue());
    }

    @Test
    void getState() {
        Address address = createTestAddress();
        assertEquals("10", address.getApartment().getValue());
    }

    @Test
    void getZip() {
        Address address = createTestAddress();
        assertEquals("85750", address.getZip().getValue());
    }
}
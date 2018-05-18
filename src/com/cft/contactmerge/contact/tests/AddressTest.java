package com.cft.contactmerge.contact.tests;

import com.cft.contactmerge.contact.IContactProperty;
import com.cft.contactmerge.contact.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AddressTest {

    /* -------------------------------------------------------------------------------------
     * Constructor Tests
     * -------------------------------------------------------------------------------------
     */
    @Test
    void Constructor()
    {
        Address property = new Address(SharedTestHelpers.createMockContactProperty("123 Main St"),
                SharedTestHelpers.createMockContactProperty("Tucson"),
                SharedTestHelpers.createMockContactProperty("AZ"),
                SharedTestHelpers.createMockContactProperty("10"),
                SharedTestHelpers.createMockContactProperty("85750"));

        assertNotNull(property);
    }

    @Test
    void Constructor_ApartmentAndZipOptional()
    {
        Address property = new Address(SharedTestHelpers.createMockContactProperty("123 Main St"),
                SharedTestHelpers.createMockContactProperty("Tucson"),
                SharedTestHelpers.createMockContactProperty("AZ"),
                null, null);

        assertNotNull(property);
    }

    @Test
    void Constructor_NullStreetAddress()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new Address(null,
                        SharedTestHelpers.createMockContactProperty("Tucson"),
                        SharedTestHelpers.createMockContactProperty("AZ"),
                        null, null));
    }

    @Test
    void Constructor_NullCity()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new Address(SharedTestHelpers.createMockContactProperty("123 Main St"),
                        null,
                        SharedTestHelpers.createMockContactProperty("AZ"),
                        null, null));
    }

    @Test
    void Constructor_NullState()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new Address(SharedTestHelpers.createMockContactProperty("123 Main St"),
                        SharedTestHelpers.createMockContactProperty("Tucson"),
                        null, null, null));
    }

    /* -------------------------------------------------------------------------------------
     * isMatch tests
     * -------------------------------------------------------------------------------------
     */

    @Test
    void isMatch() {
        // TODO: Test needed
    }


    /* -------------------------------------------------------------------------------------
     * Tests for value retrieval
     * -------------------------------------------------------------------------------------
     */

    @Test
    void getValue() {
        // TODO: Test needed
    }

    @Test
    void Address_toString() {
        // TODO: Test needed
    }

    @Test
    void getStreetAddress() {
        // TODO: Test needed
    }

    @Test
    void getApartment() {
        // TODO: Test needed
    }

    @Test
    void getCity() {
        // TODO: Test needed
    }

    @Test
    void getState() {
        // TODO: Test needed
    }

    @Test
    void getZip() {
        // TODO: Test needed
    }
}
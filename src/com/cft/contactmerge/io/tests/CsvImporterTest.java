package com.cft.contactmerge.io.tests;

import com.cft.contactmerge.contact.Contact;
import com.cft.contactmerge.io.CsvImporter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UnknownFormatConversionException;

import static org.junit.jupiter.api.Assertions.*;

class CsvImporterTest {

    /* --------------------------------------------------------------------------------
     * Testing Load(filename)
     * -------------------------------------------------------------------------------- */
    @Test
    void Load_NullFileName() {
        CsvImporter importer = new CsvImporter();

        assertThrows(IllegalArgumentException.class, () -> importer.Load( (String) null));
    }

    @Test
    void Load_EmptyFileName() {
        CsvImporter importer = new CsvImporter();

        assertThrows(IllegalArgumentException.class, () -> importer.Load(""));
    }

    @Test
    void Load_NonExistentFileName() {
        // TODO: Would be great if we could test this without access the file system but articles
        // I've read indicate you shouldn't try to mock File. How else to do it?
        CsvImporter importer = new CsvImporter();

        assertThrows(FileNotFoundException.class, () -> importer.Load("c:\\DoesNotExist\\DoesNotExist.xml"));
    }

    /* --------------------------------------------------------------------------------
     * Testing Load(inputStream)
     * -------------------------------------------------------------------------------- */
    @Test
    void Load_NullStream() {
        CsvImporter importer = new CsvImporter();

        assertThrows(IllegalArgumentException.class, () -> importer.Load((InputStream) null));
    }

    @Test
    void Load_EmptyStream() throws IOException {
        CsvImporter importer = new CsvImporter();

        String testString = "";

        try (InputStream inputStream = new ByteArrayInputStream(testString.getBytes())) {
            assertThrows(UnknownFormatConversionException.class, () -> importer.Load(inputStream));
        }
    }

    /* --------------------------------------------------------------------------------
     * Testing Iterator
     * -------------------------------------------------------------------------------- */

    private static String testCsv = "Bidder #,CRM ID,Type,Salutation,First,Last,Company,Phone #,E-mail,Address,City,State,Zip,Country,User Defined 1,User Defined 2,Notes,VIP,Table #,Table Name,Ticket #,Ticket Package,Express Checkout Credit Card,RSVP Status,Checked In\n" +
            "5,,Person,,John,Doe,,(520) 123-4567,jdoe@gmail.com,123 Main St,Tucson,Arizona,85716,,,Beider,,No,18,Group 1,3,Comp Ticket-Honoree,,,Yes\n" +
            "5,,Person,,Adam,Smith,,(520) 111-2222,adam.smith@yahoo.com,1010 Speedway Blvd,Tucson,AZ,85716,,,,,No,18,Group 1,1,Comp Ticket-Honoree,,,Yes\n";

    @Test
    void IteratorCalledBeforeLoad() {
        CsvImporter importer = new CsvImporter();

        assertThrows(IllegalStateException.class, () -> importer.iterator().hasNext());
    }

    @Test
    void Load_EmptyDataSet() throws IOException {
        CsvImporter importer = new CsvImporter();

        String testString = " ";

        try (InputStream inputStream = new ByteArrayInputStream(testString.getBytes())) {
            importer.Load(inputStream);
        }

        assertFalse(importer.iterator().hasNext());
    }

    @Test
    void IteratorHasNext() throws IOException {
        CsvImporter importer = new CsvImporter();

        try (InputStream inputStream = new ByteArrayInputStream(testCsv.getBytes())) {
            importer.Load(inputStream);
        }

        assertTrue(importer.iterator().hasNext());
    }

    @Test
    void IteratorReturnsExpectedData() throws IOException {
        CsvImporter importer = new CsvImporter();

        try (InputStream inputStream = new ByteArrayInputStream(testCsv.getBytes())) {
            importer.Load(inputStream);
        }

        ArrayList<String> lastNames = new ArrayList<String>(Arrays.asList("Doe", "Smith"));
        ArrayList<String> firstNames = new ArrayList<String>(Arrays.asList("John", "Adam"));
        ArrayList<String> addresses = new ArrayList<String>(Arrays.asList("123 Main St", "1010 Speedway Blvd"));
        ArrayList<String> phones = new ArrayList<String>(Arrays.asList("(520) 123-4567", "(520) 111-2222"));
        ArrayList<String> emails = new ArrayList<String>(Arrays.asList("jdoe@gmail.com", "adam.smith@yahoo.com"));

        int i = 0;
        for (Contact contact : importer)
        {
            assertEquals(lastNames.get(i), contact.getName().getValue().getLastName().getValue(), String.format("Verify lastNames[%d]", i));
            assertEquals(firstNames.get(i), contact.getName().getValue().getFirstName().getValue(), String.format("Verify firstNames[%d]", i));
            assertEquals(addresses.get(i), contact.getAddress().getValue().getStreetAddress().getValue(), String.format("Verify addresses[%d]", i));
            assertEquals(phones.get(i), contact.getPhone().getValue(), String.format("Verify phones[%d]", i));
            assertEquals(emails.get(i), contact.getEmail().getValue(), String.format("Verify emails[%d]", i));

            i++;
        }

        assertEquals(2, i, "Verify contact count");
    }
}
package com.cft.contactmerge;

import com.cft.contactmerge.contact.Contact;
import com.cft.contactmerge.io.CsvImporter;
import com.cft.contactmerge.io.XmlImporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("Loading Merge Target...");

        List<Contact> existingContacts = new ArrayList<Contact>();
        XmlImporter existingContactImporter = new XmlImporter();

        try {
            existingContactImporter.Load("C:\\src\\ContactMerge\\Documentation\\SampleData\\GiftWorksDonors.xml");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        for (Contact contact: existingContactImporter) {
            existingContacts.add(contact);
        }

        System.out.println(String.format("Loaded %d existing contacts", existingContacts.size()));
        System.out.println();

        System.out.println("Loading Merge Source...");
        List<Contact> contactsToMerge = new ArrayList<Contact>();
        CsvImporter toMergeImporter = new CsvImporter();

        try {
            toMergeImporter.Load("C:\\src\\ContactMerge\\Documentation\\SampleData\\BidPalAttendees.csv");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        for (Contact contact: toMergeImporter) {
            contactsToMerge.add(contact);
        }

        System.out.println(String.format("Loaded %d contacts to merge", contactsToMerge.size()));
        System.out.println();

        System.out.println("Comparing Contacts...");
        System.out.println("Done");
    }
}

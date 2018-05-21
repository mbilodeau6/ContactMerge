package com.cft.contactmerge;

import com.cft.contactmerge.contact.Contact;
import com.cft.contactmerge.io.XmlImporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("Loading Merge Target...");

        List<Contact> targets = new ArrayList<Contact>();
        XmlImporter importer = new XmlImporter();

        try {
            importer.Load("C:\\src\\ContactMerge\\Documentation\\SampleData\\GiftWorksDonors.xml");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        for (Contact contact: importer) {
            targets.add(contact);
        }

        System.out.println(String.format("Loaded %d Targets", targets.size()));
        System.out.println();

        System.out.println("Loading Merge Source...");
        System.out.println("Comparing Contacts...");
        System.out.println("Done");
    }
}

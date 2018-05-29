package com.cft.contactmerge;

import com.cft.contactmerge.contact.IContact;
import com.cft.contactmerge.io.CsvImporter;
import com.cft.contactmerge.io.XmlImporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("Loading Merge Target...");

        List<IContact> existingContacts = new ArrayList<IContact>();
        XmlImporter existingContactImporter = new XmlImporter();

        try {
            existingContactImporter.Load("C:\\src\\ContactMerge\\Documentation\\SampleData\\GiftWorksDonors.xml");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        for (IContact contact: existingContactImporter) {
            existingContacts.add(contact);
        }

        System.out.println(String.format("Loaded %d existing contacts", existingContacts.size()));
        System.out.println();

        System.out.println("Loading Merge Source...");
        List<IContact> contactsToMerge = new ArrayList<IContact>();
        CsvImporter toMergeImporter = new CsvImporter();

        try {
            toMergeImporter.Load("C:\\src\\ContactMerge\\Documentation\\SampleData\\BidPalAttendees.csv");
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        for (IContact contact: toMergeImporter) {
            contactsToMerge.add(contact);
        }

        System.out.println(String.format("Loaded %d contacts to merge", contactsToMerge.size()));
        System.out.println();

        System.out.println("Comparing Contacts...");
        MatchMaker matchMaker = new MatchMaker(existingContacts, contactsToMerge);
        List<ProposedMatch> matches = matchMaker.getProposedMatches();

        for (ProposedMatch match: matches) {
            System.out.println(String.format("%s has %d proposed matches", match.getContactToMerge().getName(), match.getPossibleTargetContacts().size()));
        }

        System.out.println();


        System.out.println("Done");
    }
}

/*
 * MergeFileExporter will be responsible for taking the final matching results
 * creating a file with the merged records.
 */
package com.cft.contactmerge.io;

import java.io.*;
import java.util.Collection;
import com.cft.contactmerge.contact.IContact;
import com.cft.contactmerge.ProposedMatch;
import com.cft.contactmerge.ColumnMap;

public class MergeFileExporter{

    private Collection<ProposedMatch> proposedMatches;

    public MergeFileExporter(Collection<ProposedMatch> proposedMatches)
    {
        if (proposedMatches == null) {
            throw new IllegalArgumentException("proposedMatches must not be null");
        }

        this.proposedMatches = proposedMatches;
    }

    private void writeContact(BufferedWriter writer, IContact contact, boolean contactToMerge)
            throws IOException {
        writer.write( String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                contact.getName().getValue().getLastName(),
                contact.getName().getValue().getFirstName(),
                contact.getAddress() == null ? "" : contact.getAddress().getValue().getStreetAddress(),
                contact.getAddress() == null ? "" : contact.getAddress().getValue().getCity(),
                contact.getAddress() == null ? "" : contact.getAddress().getValue().getState(),
                contact.getAddress() == null ? "" : contact.getAddress().getValue().getZip(),
                contact.getPhone() == null ? "" : contact.getPhone(),
                contact.getEmail() == null ? "" : contact.getEmail(),
                contactToMerge == true ? "Yes" : "No"));
        writer.newLine();
    }

    public void createMergeFile(SupportedFileType fileType, String filename, Collection<ColumnMap> columnMaps)
            throws IOException {
        if (filename == null || filename.trim() == "")
        {
            throw new IllegalArgumentException("filename is required");
        }

        if (fileType == null) {
            throw new IllegalArgumentException("fileType required");
        }

        if (fileType != SupportedFileType.TSV) {
            throw new UnsupportedOperationException("Only support TSV at the moment");
        }

        FileWriter fw = new FileWriter(filename);

        try (BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write("LastName\tFirstName\tStreetName\tCity\tState\tZip\tPhone\tEmail\tContactToMerge");
            writer.newLine();
            for(ProposedMatch match: proposedMatches) {
                IContact contactToMerge = match.getContactToMerge();
                writeContact(writer, contactToMerge, true);

                for(IContact possibleMatch: match.getPossibleTargetContacts()) {
                    writeContact(writer, possibleMatch, false);
                }
            }
        }
    }
}

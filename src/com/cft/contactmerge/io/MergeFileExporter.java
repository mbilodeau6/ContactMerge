/*
 * MergeFileExporter will be responsible for taking the final matching results
 * creating a file with the merged records.
 */
package com.cft.contactmerge.io;

import java.io.OutputStream;
import java.util.Collection;
import com.cft.contactmerge.ProposedMatch;
import com.cft.contactmerge.ColumnMap;

public class MergeFileExporter {

    private Collection<ProposedMatch> proposedMatches;

    public MergeFileExporter(Collection<ProposedMatch> proposedMatches)
    {
        if (proposedMatches == null) {
            throw new IllegalArgumentException("proposedMatches must not be null");
        }

        this.proposedMatches = proposedMatches;
    }

    public void createMergeFile(SupportedFileType fileType, String fileName, Collection<ColumnMap> columnMaps)
    {
        // TODO: Add code here
    }

    public void createMergeFile(SupportedFileType fileType, OutputStream outStream, Collection<ColumnMap> columnMaps)
    {
        // TODO: Add code here
    }

}

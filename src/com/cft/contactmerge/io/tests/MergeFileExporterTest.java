package com.cft.contactmerge.io.tests;

import com.cft.contactmerge.ColumnMap;
import com.cft.contactmerge.ProposedMatch;
import com.cft.contactmerge.io.MergeFileExporter;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

import com.cft.contactmerge.io.SupportedFileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MergeFileExporterTest {

    @Test
    void Constructor()
    {
        MergeFileExporter exporter = new MergeFileExporter(new ArrayList<ProposedMatch>());

        assertNotNull(exporter);
    }

    @Test
    void Constructor_nullProposedMatches()
    {
        assertThrows(IllegalArgumentException.class, () -> new MergeFileExporter(null));
    }

    @Test
    void createMergeFile_nullFileType() {
    }

    @Test
    void createMergeFile_nullFileName() {
    }

    @Test
    void createMergeFile_invalidFileName() {
    }

    @Test
    void createMergeFile_tsvNoColumnMap() throws IOException {
        try (OutputStream outStream = new ByteArrayOutputStream())
        {
            // TODO: Add code to test createFile()
        }
    }

    // TODO: Add remaining tests here
    // 1. csv
    // 2. xml
    // 3. Using ColumnMap for each type

}
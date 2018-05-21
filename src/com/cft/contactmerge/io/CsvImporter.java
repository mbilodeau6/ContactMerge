package com.cft.contactmerge.io;

import com.cft.contactmerge.contact.*;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class CsvImporter implements IImporter, Iterable<Contact> {

    private List<String> data = null;
    private Map<String, Integer> columnMap;

    public void Load(String filename) throws FileNotFoundException, IOException {
        // TODO: This method may be the same in all Importer classes. Create parent class?
        if (filename == null || filename.trim() == "")
        {
            throw new IllegalArgumentException("filename is required");
        }

        File inputFile = new File(filename);

        if (!inputFile.exists() || inputFile.isDirectory())
        {
            throw new FileNotFoundException(String.format("Unable to find %s", filename));
        }

        try (InputStream inputStream = new FileInputStream(inputFile)) {
            Load(inputStream);
        }
    }

    private Map<String, Integer> getColumnMap(String headerLine) {
        Map<String, Integer> columnMap = new Hashtable<String, Integer>();

        List<String> columns = SplitStringUtilities.splitCsvString(headerLine);

        int i = 0;
        for (String column: columns) {
            columnMap.put(column, i++);
        }

        return columnMap;
    }

    public void Load(InputStream inputStream) {
        if (inputStream == null)
        {
            throw new IllegalArgumentException("inputStream is required");
        }

        try {
            if (!(inputStream.available() > 0))
                throw new UnknownFormatConversionException("inputStream not available");
        }
        catch (IOException e)
        {
            throw new UnknownFormatConversionException("inputStream not available");
        }

        // Load header line and data
        try (Scanner scanner = new Scanner(inputStream)) {
            if (!scanner.hasNextLine()) {
                throw new UnknownFormatConversionException("inputStream not available");
            }

            this.columnMap = getColumnMap(scanner.nextLine());

            data = new ArrayList<String>();

            while(scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        }
    }

    public Iterator<Contact> iterator() {
        if (data == null)
        {
            throw new IllegalStateException("Must load file before using the iterator");
        }


        Iterator<Contact> it = new Iterator<Contact>() {
            int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < data.size();
            }

            @Override
            public Contact next() {
                Contact contact = new Contact();

                List<String> parts = SplitStringUtilities.splitCsvString(data.get(currentIndex++));

                contact.setName(new Name(new LastName(parts.get(columnMap.get("Last"))),
                        new FirstName(parts.get(columnMap.get("First")))));

                contact.setAddress(new Address(new StreetAddress(parts.get(columnMap.get("Address"))),
                        new GeneralProperty(parts.get(columnMap.get("City"))),
                        new GeneralProperty(parts.get(columnMap.get("State"))),
                        null,
                        new GeneralProperty(parts.get(columnMap.get("Zip")))));

                contact.setPhone(new PhoneNumber(parts.get(columnMap.get("Phone #"))));

                contact.setEmail(new GeneralProperty(parts.get(columnMap.get("E-mail"))));

                return contact;
            }
        };

        return it;
    }
}

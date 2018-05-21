package com.cft.contactmerge.io;

import com.cft.contactmerge.contact.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

public class XmlImporter implements IImporter, Iterable<Contact> {

    private Map<String, String> columnMap;
    private NodeList dataNodes;

    public void Load(String filename) throws FileNotFoundException, IOException
    {
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

    public void Load(InputStream inputStream)
    {
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

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);

            columnMap = getXmlAttribuateMap(doc.getElementsByTagName("Field"));
            dataNodes = doc.getElementsByTagName("row");
        }
        catch(SAXException | IOException | ParserConfigurationException e)
        {
            throw new UnknownFormatConversionException(e.getMessage());
        }
    }

    // TODO: Need to figure out a flexible way to map XML values with
    // properties. This is a temporary solution so that we can start playing
    // with data from GiftWorks.
    private Map<String, String> getXmlAttribuateMap(NodeList nodes)
    {
        Map<String, String> map = new HashMap<String, String>();

        for (int i=0; i < nodes.getLength(); i++)
        {
            NamedNodeMap mapItem = nodes.item(i).getAttributes();
            map.put(mapItem.item(1).getNodeValue(), mapItem.item(0).getNodeValue());
        }

        return map;
    }

    // TODO: Need to figure out a flexible way to map XML values with
    // properties. This is a temporary solution so that we can start playing
    // with data from GiftWorks.
    private Map<String, String> getContactValues(Node node)
    {
        Map<String, String> values = new HashMap<String, String>();

        for (int j=0; j < node.getAttributes().getLength(); j++) {
            values.put(node.getAttributes().item(j).getNodeName(), node.getAttributes().item(j).getNodeValue());
        }

        return values;
    }

    public Iterator<Contact> iterator()
    {
        if (dataNodes == null)
        {
            throw new IllegalStateException("Must load file before using the iterator");
        }

        Iterator<Contact> it = new Iterator<Contact>() {
            private int nodeIndex = 0;

            @Override
            public boolean hasNext() {
                return nodeIndex < dataNodes.getLength();
            }

            @Override
            public Contact next() {
                Contact contact = new Contact();

                Map<String, String> data = getContactValues(dataNodes.item(nodeIndex++));

                // TODO: Need to figure out what to do if name is missing. Can't create a valid contact
                // but how do we communicate the issue back to the user?
                contact.setName(new Name(new LastName(data.get(columnMap.get("indiv_lastname"))),
                        new FirstName(data.get(columnMap.get("indiv_firstname")))));

                // TODO: Can this be refactored to reduce duplicate code/patterns?
                String streetAddress = data.get(columnMap.get("donor_address1"));
                String city = data.get(columnMap.get("donor_city"));
                String state = data.get(columnMap.get("donor_state"));
                String zip = data.get(columnMap.get("donor_zip"));

                if (streetAddress != null && !streetAddress.isEmpty() &&
                        city != null && !city.isEmpty() &&
                        state != null && !state.isEmpty() &&
                        zip != null && !zip.isEmpty()) {
                    contact.setAddress(new Address(new StreetAddress(streetAddress),
                            new GeneralProperty(city),
                            new GeneralProperty(state),
                            null,
                            new GeneralProperty(zip)));
                }

                String phone = data.get(columnMap.get("donor_phone"));

                if (phone != null && !phone.isEmpty()) {
                    contact.setPhone(new PhoneNumber(phone));
                }

                String email = data.get(columnMap.get("donor_email"));

                if (email != null && !email.isEmpty()) {
                    contact.setEmail(new GeneralProperty(email));
                }

                return contact;
            }
        };

        return it;
    }
}

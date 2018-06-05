package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;

import java.util.Dictionary;
import java.util.HashMap;

public class Contact implements IContact {
    private IContactProperty<Name> name;
    private HashMap<String, String> properties = new HashMap<String, String>();

    // TODO: Should support multiple addresses, phones, and emails for each contact
    private IContactProperty<Address> address;
    private PhoneNumber phone;
    private IContactProperty<String> email;

    // TODO: Name should be required for a Contract (i.e. add constructor to override default constructor)

    public void setName(IContactProperty<Name> name)
    {
        this.name = name;
    }
    public IContactProperty<Name> getName() { return this.name; }

    public void setAddress(IContactProperty<Address> address) { this.address = address; }
    public IContactProperty<Address> getAddress() { return this.address; };

    public void setPhone(PhoneNumber phone) { this.phone = phone; };
    public PhoneNumber getPhone() { return this.phone; };

    public void setEmail(IContactProperty<String> email) { this.email = email; };
    public IContactProperty<String> getEmail() { return this.email; };

    private ContactMatchResult calculateMatchResult(AnswerType nameMatch,
                                                    ContactMatchTally tally,
                                                    AnswerType lastNameMatch) {
        // If everything matches
        if (nameMatch == AnswerType.yes && tally.getYesCount() > 0 &&
                (tally.getYesCount() + tally.getBothNullCount()) == tally.getTotalSubmitted()) {
            return new ContactMatchResult(ContactMatchType.Identical);
        }

        // If name and at least one other key contact property matches but a key contact property doesn't match
        if (nameMatch == AnswerType.yes && tally.getYesCount() > 0 && tally.getNoCount() > 0) {
            return new ContactMatchResult(ContactMatchType.MatchButModifying);
        }
        // If name and at least one other key contact property matches
        if (nameMatch == AnswerType.yes && tally.getYesCount() > 0) {
            return new ContactMatchResult(ContactMatchType.Match);
        }

        // If only name matches or name is a maybe match
        if (nameMatch != AnswerType.no) {
            return new ContactMatchResult(ContactMatchType.PotentialMatch);
        }

        // If name doesn't match but one of the other key fields does match
        if (tally.getYesCount() > 0) {
            return new ContactMatchResult(ContactMatchType.Related);
        }

        // If name doesn't match but one of the other key fields is a maybe match
        if (tally.getMaybeCount() > 0  || lastNameMatch != AnswerType.no) {
            return new ContactMatchResult(ContactMatchType.PotentiallyRelated);
        }

        // Default
        return new ContactMatchResult(ContactMatchType.NoMatch);
    }

    public ContactMatchResult compareTo(IContact compareContact)
    {
        AnswerType nameMatch = AnswerType.no;
        AnswerType lastNameMatch = AnswerType.no;

        // TODO: Won't need to test if name is null once name is required in Contact
        if (this.name != null && compareContact.getName() != null)
        {
            nameMatch = this.name.isMatch(compareContact.getName());

            if (this.name.getValue() != null) {
                lastNameMatch = this.name.getValue().getLastName().isMatch(compareContact.getName().getValue().getLastName());
            }
        }

        // We are going to determine if any no's or maybe's are found in any part
        // TODO: Performance note. In theory, we may be able to make a determination before running all comparisons.
        ContactMatchTally tally = new ContactMatchTally();

        tally.addComparison(this.address, compareContact.getAddress());
        tally.addComparison(this.phone, compareContact.getPhone());
        tally.addComparison(this.email, compareContact.getEmail());

        return calculateMatchResult(nameMatch, tally, lastNameMatch);
    }

    public void setPropertyValue(String property, String value) {
        if (property == null || property == "") {
            throw new IllegalArgumentException("Property required");
        }

        if (value == null) {
            throw new IllegalArgumentException("Value required");
        }

        if (properties.containsKey(property)) {
            throw new UnsupportedOperationException("Does not support updating an existing property");
        }
        properties.put(property, value);
    }

    public String getPropertyValue(String property) {
        if (property == null || property == "") {
            throw new IllegalArgumentException("Property required");
        }

        return properties.get(property);
    }

    public boolean containsProperty(String property) {
        if (property == null || property == "") {
            throw new IllegalArgumentException("Property required");
        }

        return properties.containsKey(property);
    }
}
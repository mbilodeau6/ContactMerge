package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;

public class Contact implements IContact {
    private IContactProperty<Name> name;

    // TODO: Should support multiple addresses, phones, and emails for each contact
    private IContactProperty<Address> address;
    private IContactProperty<String> phone;
    private IContactProperty<String> email;

    // TODO: Name should be required for a Contract (i.e. add constructor to override default constructor)

    public void setName(IContactProperty<Name> name)
    {
        this.name = name;
    }
    public IContactProperty<Name> getName() { return this.name; }

    public void setAddress(IContactProperty<Address> address) { this.address = address; }
    public IContactProperty<Address> getAddress() { return this.address; };

    public void setPhone(IContactProperty<String> phone) { this.phone = phone; };
    public IContactProperty<String> getPhone() { return this.phone; };

    public void setEmail(IContactProperty<String> email) { this.email = email; };
    public IContactProperty<String> getEmail() { return this.email; };

    public ContactMatchResult compareTo(IContact compareContact)
    {
        ContactMatchTally tally = new ContactMatchTally();

        // We are going to determine if any no's or maybe's are found in any part
        // TODO: Performance note. In theory, we may be able to make a determination before running all comparisons.
        tally.addComparison(this.name, compareContact.getName());
        tally.addComparison(this.address, compareContact.getAddress());
        tally.addComparison(this.phone, compareContact.getPhone());
        tally.addComparison(this.email, compareContact.getEmail());

        // This is where we make the decision on what to return
        ContactMatchType matchType = ContactMatchType.NoMatch;

        if (tally.getNoCount() > 0)
            matchType = ContactMatchType.NoMatch;
        else {
            if (tally.getMaybeCount() > 0)
                matchType = ContactMatchType.PotentialMatch;
            else {
                if (tally.getYesCount() > 0) {
                    matchType = ContactMatchType.Identical;
                }
            }
        }

        return new ContactMatchResult(matchType);
    }
}

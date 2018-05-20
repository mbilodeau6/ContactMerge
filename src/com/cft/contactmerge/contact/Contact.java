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
}

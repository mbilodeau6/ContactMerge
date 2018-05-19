package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;

import java.util.HashSet;
import java.util.Set;

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
        Set<AnswerType> answerTypesFound = new HashSet<AnswerType>();

        // We are going to determine if any no's or maybe's are found in any part
        // TODO: Performance note. In theory, as soon as we hit the first no, we could return no. Need to monitor.

        // TODO: Name should never be empty. Should be able to remove the check once Contact is changed to enforce.
        if (this.name != null && compareContact.getName() != null) {
            answerTypesFound.add(this.name.isMatch(compareContact.getName()));
        }
        else {
            answerTypesFound.add(AnswerType.no);
        }

        if (this.address != null && compareContact.getAddress() != null) {
            answerTypesFound.add(this.address.isMatch(compareContact.getAddress()));
        }
        else {
            answerTypesFound.add(AnswerType.maybe);
        }

        if (this.phone != null && compareContact.getPhone() != null) {
            answerTypesFound.add(this.phone.isMatch(compareContact.getPhone()));
        }
        else {
            answerTypesFound.add(AnswerType.maybe);
        }

        if (this.email != null && compareContact.getEmail() != null) {
            answerTypesFound.add(this.email.isMatch(compareContact.getEmail()));
        }
        else {
            answerTypesFound.add(AnswerType.maybe);
        }

        ContactMatchType matchType = ContactMatchType.NoMatch;

        // If any parts do not match, we return no. If everything has at least a maybe match
        // we return maybe. We also return maybe if Zip if a no. Everything must match for
        // us to return a yes.
        if (answerTypesFound.contains(AnswerType.no))
            matchType = ContactMatchType.NoMatch;
        else {
            if (answerTypesFound.contains(AnswerType.maybe))
                matchType = ContactMatchType.PotentialMatch;
            else {
                if (answerTypesFound.contains(AnswerType.yes)) {
                    matchType = ContactMatchType.Identical;
                }
            }
        }

        return new ContactMatchResult(matchType);
    }
}

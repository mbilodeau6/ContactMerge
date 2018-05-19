package com.cft.contactmerge.contact;

import com.cft.contactmerge.ContactMatchResult;
import com.cft.contactmerge.ContactMatchType;

public class Contact implements IContact {
    private IContactProperty<Name> name;

    // TODO: Should support multiple addresses, phones, and emails for each contact
    private IContactProperty<Address> address;
    private IContactProperty<String> phone;
    private IContactProperty<String> email;

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
        // TODO: Need to add logic
        return new ContactMatchResult(ContactMatchType.NoMatch);
    }
}

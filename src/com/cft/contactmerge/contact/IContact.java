package com.cft.contactmerge.contact;

public interface IContact {
    public void setName(IContactProperty<Name> name);
    public IContactProperty<Name> getName();

    public void setAddress(IContactProperty<Address> address);
    public IContactProperty<Address> getAddress();

    public void setPhone(IContactProperty<String> phone);
    public IContactProperty<String> getPhone();

    public void setEmail(IContactProperty<String> email);
    public IContactProperty<String> getEmail();

    public ContactMatchResult compareTo(IContact compareContact);
}

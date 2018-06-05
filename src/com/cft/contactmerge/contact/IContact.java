package com.cft.contactmerge.contact;

public interface IContact {
    public void setName(IContactProperty<Name> name);
    public IContactProperty<Name> getName();

    public void setAddress(IContactProperty<Address> address);
    public IContactProperty<Address> getAddress();

    public void setPhone(PhoneNumber phone);
    public PhoneNumber getPhone();

    public void setEmail(IContactProperty<String> email);
    public IContactProperty<String> getEmail();

    public void setPropertyValue(String property, String value);
    public String getPropertyValue(String property);
    public boolean containsProperty(String property);

    public ContactMatchResult compareTo(IContact compareContact);
}

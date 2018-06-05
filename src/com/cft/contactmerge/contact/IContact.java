package com.cft.contactmerge.contact;

public interface IContact {
    public void setName(Name name);
    public Name getName();

    public void setAddress(Address address);
    public Address getAddress();

    public void setPhone(PhoneNumber phone);
    public PhoneNumber getPhone();

    public void setEmail(GeneralProperty email);
    public GeneralProperty getEmail();

    public void setPropertyValue(String property, String value);
    public String getPropertyValue(String property);
    public boolean containsProperty(String property);

    public ContactMatchResult compareTo(IContact compareContact);
}

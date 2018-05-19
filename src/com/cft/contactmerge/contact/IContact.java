package com.cft.contactmerge.contact;

import com.cft.contactmerge.ContactMatchResult;

public interface IContact {
    public void setFirstName(String firstName);
    public String getFirstName();

    public void setLastName(String lastName);
    public String getLastName();

    public void setAddress(String address);
    public String getAddress();

    public void setCity(String city);
    public String getCity();

    public void setState(String state);
    public String getState();

    public void setZip(String zip);
    public String getZip();

    public void setPhone(String phone);
    public String getPhone();

    public void setEmail(String email);
    public String getEmail();

    public ContactMatchResult compareTo(IContact compareContact);
}

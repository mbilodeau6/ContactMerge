package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;

public class Address implements IContactProperty<Address> {
    private IContactProperty<String> streetAddress;
    private IContactProperty<String> apartment;
    private IContactProperty<String> city;
    private IContactProperty<String> state;
    private IContactProperty<String> zip;

    public Address(IContactProperty<String> streetAddress,
                   IContactProperty<String> city,
                   IContactProperty<String> state,
                   IContactProperty<String> apartment,
                   IContactProperty<String> zip) {

        if (streetAddress == null) {
            throw new IllegalArgumentException("streetAddress is required");
        }

        if (city == null) {
            throw new IllegalArgumentException("streetAddress is required");
        }

        if (state == null) {
            throw new IllegalArgumentException("streetAddress is required");
        }
    }

    public AnswerType isMatch(IContactProperty<Address> otherAddress) {
        return null;
    }

    public Address getValue()
    {
        return this;
    }

    public String toString() {
        // TODO: Should retrun apartment, city and state but need to format
        return this.streetAddress.toString();
    }

    public IContactProperty<String> getStreetAddress()
    {
        return this.streetAddress;
    }

    public IContactProperty<String> getApartment()
    {
        return this.apartment;
    }

    public IContactProperty<String> getCity()
    {
        return this.city;
    }

    public IContactProperty<String> getState()
    {
        return this.state;
    }

    public IContactProperty<String> getZip()
    {
        return this.zip;
    }
}

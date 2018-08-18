package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;
import java.util.HashSet;
import java.util.Set;

public class Address implements IContactProperty<Address> {
    private IContactProperty<String> streetAddress;
    private IContactProperty<String> apartment;
    private IContactProperty<String> city;
    private State state;
    private Zip zip;

    public Address(StreetAddress streetAddress,
                   Apartment apartment,
                   GeneralProperty city,
                   State state,
                   Zip zip) {

        if (streetAddress == null) {
            throw new IllegalArgumentException("streetAddress is required");
        }

        if (city == null) {
            throw new IllegalArgumentException("city is required");
        }

        if (state == null) {
            throw new IllegalArgumentException("state is required");
        }

        this.streetAddress = streetAddress;
        this.apartment = apartment;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public AnswerType isMatch(IContactProperty<Address> otherAddress) {
        Set<AnswerType> answerTypesFound = new HashSet<AnswerType>();

        // We are going to determine if any no's or maybe's are found in any part
        // TODO: Performance note. In theory, as soon as we hit the first no, we could return no. Need to monitor.
        answerTypesFound.add(this.streetAddress.isMatch(otherAddress.getValue().streetAddress));
        answerTypesFound.add(this.city.isMatch(otherAddress.getValue().city));
        answerTypesFound.add(this.state.isMatch(otherAddress.getValue().state));

        // Apartment is optional. Only try to compare if it is specified in both addresses.
        // If it is missing in both, it is a match.
        AnswerType apartmentResult = AnswerType.no;

        if (this.apartment != null && otherAddress.getValue().apartment != null) {
            apartmentResult = this.apartment.isMatch(otherAddress.getValue().apartment);
            answerTypesFound.add(apartmentResult);
        }
        else {
            if (this.apartment == null && otherAddress.getValue().apartment == null) {
                apartmentResult = AnswerType.yes;
            }
        }

        // Zip is optional. Only try to compare if it is specified in both addresses.
        // If it is missing in both, it is a match.
        AnswerType zipResult = AnswerType.no;

        if (this.zip != null && otherAddress.getValue().zip != null) {
            zipResult = this.zip.isMatch(otherAddress.getValue().zip);
        }
        else {
            if (this.zip == null && otherAddress.getValue().zip == null) {
                zipResult = AnswerType.yes;
            }
        }

        // If any parts do not match, we return no. If everything has at least a maybe match
        // we return maybe. We also return maybe if Zip if a no. Everything must match for
        // us to return a yes.
        if (answerTypesFound.contains(AnswerType.no))
            return AnswerType.no;

        if (answerTypesFound.contains(AnswerType.maybe))
            return AnswerType.maybe;

        if (answerTypesFound.contains(AnswerType.yes)) {
            if (zipResult != AnswerType.yes || apartmentResult == AnswerType.no) {
                return AnswerType.maybe;
            }

            return AnswerType.yes;
        }

        // Shouldn't be possible but just in case
        return AnswerType.no;
    }

    public Address getValue()
    {
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.streetAddress.getValue()).append(", ");

        if (this.apartment != null) {
            sb.append(this.apartment.getValue()).append(", ");
        }

        sb.append(this.city.getValue()).append(", ").append(this.state.getValue());

        return sb.toString();
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

    public State getState()
    {
        return this.state;
    }

    public IContactProperty<String> getZip()
    {
        return this.zip;
    }
}

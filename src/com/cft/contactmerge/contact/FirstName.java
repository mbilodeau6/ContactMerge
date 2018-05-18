package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;
import java.util.Collection;

public class FirstName implements IContactProperty<String> {

    private String firstName;

    // TODO: This code assumes that names will be normalized before being compared. Normalization
    // should probably be part of construction. Things that should happen as part of normalization:
    // 1. Strip out punctuation and unnecessary spaces
    // 2. Normalize capitalization

    public FirstName(String firstName)
    {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("firstName is required");
        }

        this.firstName = firstName;
    }

    public AnswerType isMatch(IContactProperty<String> otherProperty) {
        return PropertyMatchingHelpers.doNamePartsMatch(this.firstName, otherProperty.getValue());
    }

    public String getValue() { return firstName; }

    public String toString() {
        return firstName;
    }
}

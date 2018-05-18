package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;

import java.util.Collection;

public class LastName implements IContactProperty<String> {
    private String lastName;

    public LastName(String lastName)
    {
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("lastName is required");
        }

        this.lastName = lastName;
    }

    public AnswerType isMatch(IContactProperty<String> otherProperty) {
        return PropertyMatchingHelpers.doNamePartsMatch(this.lastName, otherProperty.getValue());
    }

    public String getValue()
    {
        return lastName;
    }

    public String toString() {
        return lastName;
    }

}

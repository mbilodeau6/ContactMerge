package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;

import java.rmi.UnexpectedException;

public class Name implements IContactProperty<Name>
{
    private FirstName firstName;
    private LastName lastName;

    public Name(LastName lastName, FirstName firstName)
    {
        if (lastName == null) {
            throw new IllegalArgumentException("LastName is required");
        }

        if (firstName == null) {
            throw new IllegalArgumentException("FirstName is required");
        }

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AnswerType isMatch(IContactProperty<Name> otherName) {

        FirstName firstNameToCompareWith = otherName.getValue().getFirstName();
        LastName lastNameToCompareWith = otherName.getValue().getLastName();

        switch (lastName.isMatch(lastNameToCompareWith))
        {
            case yes:
                return firstName.isMatch(firstNameToCompareWith);
            case maybe:
                if (firstName.isMatch(firstNameToCompareWith) != AnswerType.no) {
                    return AnswerType.maybe;
                }
        }

        return AnswerType.no;
    }

    public Name getValue()
    {
        return this;
    }

    public String toString() {
        return lastName.getValue() + ", " + firstName.getValue();
    }

    public FirstName getFirstName()
    {
        return firstName;
    }

    public LastName getLastName()
    {
        return lastName;
    }
}

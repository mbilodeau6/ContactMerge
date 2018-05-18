package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;

import java.rmi.UnexpectedException;

public class Name implements IContactProperty<Name>
{
    private IContactProperty<String> firstName;
    private IContactProperty<String> lastName;

    public Name(IContactProperty<String> lastName, IContactProperty<String> firstName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AnswerType isMatch(IContactProperty<Name> otherProperty) {

        IContactProperty<String> firstNameToCompareWith = otherProperty.getValue().getFirstName();
        IContactProperty<String> lastNameToCompareWith = otherProperty.getValue().getLastName();

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
        return lastName + ", " + firstName;
    }

    public IContactProperty<String> getFirstName()
    {
        return firstName;
    }

    public IContactProperty<String> getLastName()
    {
        return lastName;
    }
}

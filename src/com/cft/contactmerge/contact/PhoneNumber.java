package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;

public class PhoneNumber implements IContactProperty<String> {
    private String phone;

    // TODO: This code assumes that phone numbers will be normalized before being compared. Normalization
    // should probably be part of construction. Things that should happen as part of normalization:
    // 1. Use standard format (###) ###-####
    // 2. Figure out how to handle extensions

    public PhoneNumber(String phone)
    {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("phone is required");
        }

        this.phone = phone;
    }

    public AnswerType isMatch(IContactProperty<String> otherProperty) {
        return PropertyMatchingHelpers.doNamePartsMatch(this.phone, otherProperty.getValue());
    }

    public String getValue() { return phone; }

    public String toString() {
        return phone;
    }
}

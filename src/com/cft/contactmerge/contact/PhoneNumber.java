package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;

public class PhoneNumber extends GeneralProperty {

    // TODO: This code assumes that phone numbers will be normalized before being compared. Normalization
    // should probably be part of construction. Things that should happen as part of normalization:
    // 1. Use standard format (###) ###-####
    // 2. Figure out how to handle extensions

    public PhoneNumber(String phoneNumber)
    {
        super(phoneNumber);
    }
}

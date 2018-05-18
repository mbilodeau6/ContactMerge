package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;

import java.util.Arrays;
import java.util.Collection;

public class StreetAddress implements IContactProperty<String> {
    private String streetAddress;

    private static Collection<String> addressDirections = Arrays.asList("n", "w", "e", "s", "ne", "nw", "se", "sw");
    private static Collection<String> streetTypes = Arrays.asList("st", "ave", "dr", "ln", "trl", "cir", "blvd", "rd");

    // TODO: This code assumes that addresses will be normalized before being compared. Normalization
    // should probably be part of construction. Things that should happen as part of normalization:
    // 1. Strip out punctuation and unnecessary spaces
    // 2. Replace longer street type names and directions with the short forms
    // 3. Use standard format for PO Box (P. O. Box, Box, etc)
    // 4. Normalize capitalization
    // 5. Pull out apartment/unit number if that has been provided

    public StreetAddress(String streetAddress)
    {
        if (streetAddress == null || streetAddress.isEmpty()) {
            throw new IllegalArgumentException("streetAddress is required");
        }

        this.streetAddress = streetAddress;
    }

    public AnswerType isMatch(IContactProperty<String> otherProperty) {
        // Test for exact match
        if (PropertyMatchingHelpers.doPropertyPartsMatch(streetAddress, otherProperty.getValue()) == AnswerType.yes)
        {
            return AnswerType.yes;
        }

        // Test for parts matching after removing commonly missed items
        Collection<String> sourceParts = PropertyMatchingHelpers.splitPropertyStringOnNonAlphaNumeric(streetAddress);
        sourceParts.removeAll(addressDirections);
        sourceParts.removeAll(streetTypes);

        Collection<String> targetParts = PropertyMatchingHelpers.splitPropertyStringOnNonAlphaNumeric(otherProperty.getValue());
        targetParts.removeAll(addressDirections);
        targetParts.removeAll(streetTypes);

        if (PropertyMatchingHelpers.doPropertyPartsMatchOrderDoesMatter(sourceParts, targetParts) != AnswerType.no) {
            return AnswerType.maybe;
        }

        return AnswerType.no;
    }

    public String getValue()
    {
        return streetAddress;
    }

    public String toString() {
        return streetAddress;
    }

}

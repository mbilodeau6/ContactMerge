package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;

public class Zip extends GeneralProperty {
    public Zip(String zip) {
        super(zip);
    }

    @Override
    public AnswerType isMatch(IContactProperty<String> otherProperty) {
        if (PropertyMatchingHelpers.doPropertyPartsMatch(this.value, otherProperty.getValue()) == AnswerType.yes) {
            return AnswerType.yes;
        }

        if (this.value.length() > 4 && otherProperty.getValue().length() > 4 &&
                !PropertyMatchingHelpers.containsAlpha(this.value) && !PropertyMatchingHelpers.containsAlpha(otherProperty.getValue())) {
            String mainPartSource = this.value.substring(0, 5);
            String mainPartTarget = otherProperty.getValue().substring(0, 5);

            return (PropertyMatchingHelpers.doPropertyPartsMatch(mainPartSource, mainPartTarget));
        }

        return AnswerType.no;
    }
}
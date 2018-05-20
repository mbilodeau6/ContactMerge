package com.cft.contactmerge.contact;

import com.cft.contactmerge.AnswerType;

import java.util.ArrayList;
import java.util.Collection;

public class ContactMatchTally {
    private Collection<AnswerType> answerTypesFound = new ArrayList<AnswerType>();

    public void addComparison(IContactProperty source, IContactProperty target)
    {
        if (source != null && target != null) {
            answerTypesFound.add(source.isMatch(target));
        }
        else {
            if (source == null && target == null) {
                answerTypesFound.add(AnswerType.yes);
            }
            else {
                answerTypesFound.add(AnswerType.no);
            }
        }
    }

    public int getYesCount() {
        return (int) answerTypesFound.stream().filter(x -> x == AnswerType.yes).count();
    }

    public int getNoCount() {
        return (int) answerTypesFound.stream().filter(x -> x == AnswerType.no).count();
    }

    public int getMaybeCount() {
        return (int) answerTypesFound.stream().filter(x -> x == AnswerType.maybe).count();
    }
}

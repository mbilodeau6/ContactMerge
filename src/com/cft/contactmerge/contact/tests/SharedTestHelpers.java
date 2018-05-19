package com.cft.contactmerge.contact.tests;

import com.cft.contactmerge.contact.IContactProperty;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SharedTestHelpers {
    public static IContactProperty<String> createMockContactProperty(String propertyValue)
    {
        IContactProperty<String> contactPartMock = mock(IContactProperty.class);
        when(contactPartMock.getValue()).thenReturn(propertyValue);

        return contactPartMock;
    }
}

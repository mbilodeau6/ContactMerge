package com.cft.contactmerge.contact.tests;

import com.cft.contactmerge.contact.IContactProperty;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SharedTestHelpers {
    public static IContactProperty<String> createMockContactProperty(String name)
    {
        IContactProperty<String> namePartMock = mock(IContactProperty.class);
        when(namePartMock.getValue()).thenReturn(name);

        return namePartMock;
    }
}

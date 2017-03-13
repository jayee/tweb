/*
 */
package org.tweb.testutil;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author jonas
 */
public class MockTest {
    @Before 
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

}

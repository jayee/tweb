/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.storage;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jonas
 */
public class TranslationFileTest {
    
    /**
     * Test of pathExcludingRoot method, of class TranslationFile.
     */
    @Test
    public void testPathExcludingRoot() {
        TranslationFile translationFile = new TranslationFile("/path/folder1/folder2/last/props.properties", "en");
        Assert.assertEquals("/path/folder1/folder2/last/props.properties", translationFile.fullPath);


        String result = translationFile.pathExcludingRoot("/path/folder1");
        assertEquals("folder2/last/props.properties", result);

        result = translationFile.pathExcludingRoot("/path/folder1/");
        assertEquals("folder2/last/props.properties", result);
    }
    
}

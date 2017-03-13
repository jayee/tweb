/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.storage;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 *
 * @author jonas
 */
public interface DirectoryReader {

    Collection<TranslationUnit> readFromDirectory(File basedir) throws IOException;
    
}

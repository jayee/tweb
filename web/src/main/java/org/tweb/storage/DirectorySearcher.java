/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.storage;

import org.tweb.storage.properties.DirectoryReaderProperties;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;

/**
 *
 * @author jonas
 */
public class DirectorySearcher {

    @Inject
    DirectoryReader reader;

    Collection<TranslationUnit> translationUnits;

    public void searchDirectories(File basedir) throws IOException {
        translationUnits = new ArrayList<>();
        searchRecursive(basedir);
    }

    private void searchRecursive(File basedir) throws IOException {
        for (File file : basedir.listFiles()) {
            if (file.isDirectory()) {
                searchRecursive(file);
                continue;
            }
        }

        Collection<TranslationUnit> units = reader.readFromDirectory(basedir);
        if (!units.isEmpty()) {
            translationUnits.addAll(units);
        }
    }

    public Collection<TranslationUnit> getTranslationUnits() {
        return translationUnits;
    }

}

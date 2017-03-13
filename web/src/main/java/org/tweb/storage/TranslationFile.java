/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.storage;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author jonas
 */
public class TranslationFile {
    
    // fullPath is full path with root_path including
    public String fullPath;
    public String lang;

    public TranslationFile(String path, String lang) {
        this.fullPath = path;
        this.lang = lang;
    }

    public String pathExcludingRoot(String root) {
        Path p = Paths.get(root);
        return p.relativize(Paths.get(fullPath)).toString();
    }
    
    
}

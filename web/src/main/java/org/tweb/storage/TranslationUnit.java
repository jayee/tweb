/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.storage;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author jonas
 */
public class TranslationUnit {
    TranslationFile defaultTranslation;
    Collection<TranslationFile> translations = new ArrayList<>();

    public void setDefaultTranslation(TranslationFile translationFile) {
        defaultTranslation = translationFile;
    }

    public void addTranslation(TranslationFile translationFile) {
        translations.add(translationFile);
    }

    public TranslationFile getDefaultTranslation() {
        return defaultTranslation;
    }

    public Collection<TranslationFile> getTranslations() {
        return translations;
    }
    
    
}

/*
 */
package org.tweb.translation.entity;

/**
 *
 * @author jonas
 */
public interface TranslationPersister {

    // Loads the full translation object
    Translation load();

    // Persist the given translation keys
    void write(Translation translation);
    
}

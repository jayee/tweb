package org.tweb.translation.boundary;

import com.typesafe.config.Config;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import org.tweb.application.util.ConfigKeys;
import org.tweb.search.control.SearchRepoFactory;
import org.tweb.storage.DirectoryReader;
import org.tweb.storage.TranslationUnit;
import org.tweb.storage.properties.PropertyFile;
import org.tweb.summary.control.StatisticsRepoFactory;
import org.tweb.summary.control.StatisticsUpdater;
import org.tweb.translation.entity.Translation;

/**
 *
 */
@Stateless
public class Translations {

    @Inject
    Logger tracer;

    @Inject
    Config config;

    @Inject
    StatisticsUpdater statisticsUpdater;

    /**
     * Reads a translation from the path and language components
     * @param path full path including extension but without language part
     * @param language the language to load
     * @return The found translation. Exception if any errors.
     */
    public Translation find(String path, String language) {
        Translation trans = null;

        if (config.getInt(ConfigKeys.STORAGE_PROPERTIES) == config.getInt(ConfigKeys.TRANSLATION_STORAGE)) {
            trans = loadPropertyFile(path, language);
        } else {
            throw new ApplicationConfigurationException("Unknown storage type specified in the configuration");
        }

        return trans;
    }

    /**
     * Writes one or more translation keys to the translation object
     * @param path full path including extension but without language part
     * @param translation the translation object. Must include language and some translations
     */
    public void update(String path, Translation translation) {
        if (config.getInt(ConfigKeys.STORAGE_PROPERTIES) == config.getInt(ConfigKeys.TRANSLATION_STORAGE)) {
            updatePropertyFile(path, translation);
        } else {
            throw new ApplicationConfigurationException("Unknown storage type specified in the configuration");
        }
    }

    private Translation loadPropertyFile(String filePath, String language) {
        PropertyFile propertyFile = CDI.current().select(PropertyFile.class).get();
        propertyFile.init(filePath, language);
        return propertyFile.load();
    }

    private void updatePropertyFile(String path, Translation translation) {
        PropertyFile propertyFile = CDI.current().select(PropertyFile.class).get();
        propertyFile.init(path, translation.lang);
        propertyFile.write(translation);

        statisticsUpdater.updateFromPath(path);
    }


}

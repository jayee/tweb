/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.storage.properties;

import com.typesafe.config.Config;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.tweb.application.util.ConfigKeys;
import org.tweb.storage.DirectoryReader;
import org.tweb.storage.TranslationFile;
import org.tweb.storage.TranslationUnit;

/**
 *
 * @author jonas
 */
public class DirectoryReaderProperties implements DirectoryReader {

    @Inject
    Logger logger;

    @Inject
    Config config;

    @Inject
    PathUtil pathUtil;

    Collection<String> languages;
    String defaultCode;

    @PostConstruct
    public void init() {
        languages = config.getStringList(ConfigKeys.LANGUAGES);
        defaultCode = config.getString(ConfigKeys.DEFAULT_LANGUAGE_NAME);
    }

    @Override
    public Collection<TranslationUnit> readFromDirectory(File basedir) throws IOException {
        Collection<TranslationUnit> units = new HashSet<>();
        Collection<String> processedBasenames = new HashSet<>();

        for (File file : basedir.listFiles(name -> name.isFile() && name.getName().endsWith(".properties"))) {
            String filename = file.getName();
            BasenameLang basenameLang = pathUtil.parseTranslationFilename(filename);
            if (basenameLang != null && !processedBasenames.contains(basenameLang.basename) && languages.contains(basenameLang.lang)) {
                processedBasenames.add(basenameLang.basename);
                TranslationUnit unit = createTranslationUnit(basedir.getPath(), basenameLang.basename);
                if (unit != null) {
                    units.add(unit);
                }
            }
        }

        return units;
    }

    private TranslationUnit createTranslationUnit(String parentDir, String basename) throws IOException {
        TranslationUnit unit = new TranslationUnit();

        // Set default translation
        String defaultName = createTranslationFilename(basename, defaultCode);
        Path path = Paths.get(parentDir, defaultName);
        // Return null if no default translation found
        if (!Files.exists(path)) {
            logger.warning("Missing default translation, or extra/unused translations: " + basename);
            return null;
        }

        unit.setDefaultTranslation(new TranslationFile(path.toString(), defaultCode));
        
        // Add the rest of translations found in the file system
        for (String lang : languages) {
            path = Paths.get(parentDir, createTranslationFilename(basename, lang));
            if (Files.exists(path)) {
                unit.addTranslation(new TranslationFile(path.toString(), lang));
            } else if (config.getBoolean(ConfigKeys.CREATE_MISSING_FILES)) {
                try {
                    Files.createFile(path);
                } catch (IOException e) {
                    logger.severe("Unable to create new language file: " + path.toString());
                }
            }
        }
        return unit;
    }

    private String createTranslationFilename(String basename, String langCode) {
        String name;
        if ("".equals(langCode)) {
            name = basename + ".properties";
        } else {
            name = basename + "_" + langCode + ".properties";
        }

        return name;
    }

}

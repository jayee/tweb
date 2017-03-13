/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.summary.control;

import com.typesafe.config.Config;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.TimerConfig;
import javax.inject.Inject;
import org.tweb.application.util.ConfigKeys;
import org.tweb.search.control.SearchRepoFactory;
import org.tweb.storage.DirectoryReader;
import org.tweb.storage.properties.PathUtil;
import org.tweb.search.control.SearchRepo;
import org.tweb.storage.DirectorySearcher;
import org.tweb.storage.TranslationFile;
import org.tweb.storage.TranslationUnit;
import org.tweb.storage.properties.stats.SearchableProperties;
import org.tweb.storage.properties.stats.StatisticsProperties;
import org.tweb.summary.boundary.LanguageSummary;

/**
 *
 * @author jonas
 */
@Singleton
public class StatisticsUpdater {

    @Inject
    Logger logger;

    @Inject
    Config config;

    @Inject
    DirectorySearcher dirSearcher;

    @Inject
    DirectoryReader dirReader;

    @Inject
    PathUtil pathUtil;

    @Inject
    SearchRepoFactory searchRepoFactory;

    @Inject
    StatisticsRepoFactory statisticsRepoFactory;

    int numberOfTranslationUnits;

    ZonedDateTime lastStart;

    Duration lastDuration;

    private String rootPath;

    @PostConstruct
    void init() {
        rootPath = config.getString(ConfigKeys.ROOT_PATH);
    }

    public int getNumberOfTranslationUnits() {
        return numberOfTranslationUnits;
    }

    public ZonedDateTime getLastStart() {
        return lastStart;
    }

    public String getLastDurationString() {
        long minutes = lastDuration.toMinutes();
        long milliseconds = lastDuration.minusMinutes(minutes).toMillis();
        return String.format("%d minutes and %d,%03d seconds", minutes, milliseconds/1000, milliseconds%1000);
    }

    public void initRepos() {
        StatisticsRepo statRepo = statisticsRepoFactory.getRepo();
        SearchRepo searchRepo = searchRepoFactory.newRepo();

        lastStart = ZonedDateTime.now();
        logger.info("Initializing repos from " + rootPath + " at " + lastStart.toString());

        try {
            dirSearcher.searchDirectories(new File(rootPath));
            Collection<TranslationUnit> translationFiles = dirSearcher.getTranslationUnits();

            for (TranslationUnit unit : translationFiles) {
                updateFromTranslationUnit(statRepo, searchRepo, unit);
            }

            numberOfTranslationUnits = translationFiles.size();
            lastDuration = Duration.between(lastStart, ZonedDateTime.now());
            logger.info(String.format("Indexed %d translation units in %s", numberOfTranslationUnits, getLastDurationString()));

            searchRepoFactory.setRepo(searchRepo);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to init the repos", e);
        }
        
        
    }

    private void updateFromTranslationUnit(StatisticsRepo statRepo, SearchRepo searchRepo, TranslationUnit unit) throws IOException {
        TranslationFile defaultTranslation = unit.getDefaultTranslation();
        String filePath = defaultTranslation.pathExcludingRoot(rootPath);
        SearchableProperties defaultProps = new SearchableProperties(filePath, defaultTranslation.lang);
        readIntoProperties(defaultTranslation.fullPath, defaultProps);

        searchRepo.clearDocuments(defaultProps.searchIndex());
        searchRepo.writeDocument(defaultProps.searchIndex());
        statRepo.saveLanguageSummary(new LanguageSummary(filePath, ""));

        for (TranslationFile translationFile : unit.getTranslations()) {
            filePath = translationFile.pathExcludingRoot(rootPath);
            StatisticsProperties props = new StatisticsProperties(defaultProps, pathUtil.createDefaultFilePath(filePath), translationFile.lang);
            readIntoProperties(translationFile.fullPath, props);

            statRepo.saveLanguageSummary(props.summarize());
            searchRepo.writeDocument(props.searchIndex());
        }
    }

    private void readIntoProperties(String file, Properties props) {
        try (FileReader fileReader = new FileReader(file);
                BufferedReader reader = new BufferedReader(fileReader)) {
            props.load(reader);

        }
        catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void updateFromPath(String filepath) {
        StatisticsRepo statRepo = statisticsRepoFactory.getRepo();
        SearchRepo searchRepo = searchRepoFactory.getRepo();

        Path p = Paths.get(rootPath, filepath);
        try {
            Collection<TranslationUnit> units = dirReader.readFromDirectory(p.getParent().toFile());

            for (TranslationUnit unit : units) {
                updateFromTranslationUnit(statRepo, searchRepo, unit);
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception during update of statistics from path: " + filepath, e);
        }

    }
}

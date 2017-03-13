/*
 */
package org.tweb.storage.properties;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.tweb.application.util.ConfigExposer;
import org.tweb.application.util.LoggerExposer;
import org.tweb.storage.properties.PathUtil;

/**
 *
 * @author jonas
 */
@RunWith(Arquillian.class)
public class PathUtilIT {

    @Inject
    PathUtil cut;

    @Inject
    Config config;

    @After
    public void down() {
        System.setProperty("default_language_name", "");
        ConfigFactory.invalidateCaches();
    }

    @Deployment
    public static WebArchive create() {
        return ShrinkWrap.create(WebArchive.class).
                addClasses(PathUtil.class, ConfigExposer.class, LoggerExposer.class).
                addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testCreateFilePathDefaultLang() {
        String createdPath = cut.createFilePath("/example/path/to/files/any.properties", "");
        assertEquals("/example/path/to/files/any.properties", createdPath);
    }

    @Test
    public void testCreateFilePathDefaultLang2() {
        String createdPath = cut.createFilePath("/example/path/to/files/any.properties", null);
        assertEquals("/example/path/to/files/any.properties", createdPath);
    }

    @Test
    public void testCreateFilePathSomeLang() {
        String createdPath = cut.createFilePath("/example/path/to/files/any.properties", "xy");
        assertEquals("/example/path/to/files/any_xy.properties", createdPath);
    }

    @Test
    public void testCreateFilePathWithDefaultLangSet() {
        System.setProperty("default_language_name", "en");
        ConfigFactory.invalidateCaches();
        cut.config = ConfigFactory.defaultReference();
        String createdPath = cut.createFilePath("/example/path/to/files/any.properties", null);
        assertEquals("/example/path/to/files/any_en.properties", createdPath);
    }

    @Test
    public void testCreateDefaultFilePathFromSv() {
        String defaultFilePath = cut.createDefaultFilePath("/example/path/to/files/any_sv.properties");
        assertEquals("/example/path/to/files/any.properties", defaultFilePath);
    }

    @Test
    public void testCreateDefaultFilePathFromSvSE() {
        String defaultFilePath = cut.createDefaultFilePath("/example/path/to/files/any_sv_SE.properties");
        assertEquals("/example/path/to/files/any.properties", defaultFilePath);
    }

    @Test
    public void testCreateDefaultFilePathFromSvSEVar() {
        String defaultFilePath = cut.createDefaultFilePath("/example/path/to/files/any_sv_SE_var.properties");
        assertEquals("/example/path/to/files/any.properties", defaultFilePath);
    }

    @Test
    public void testParseTranslationFilename1() {
        BasenameLang basenameLang = cut.parseTranslationFilename("path/any_sv.properties");
        assertEquals("path/any", basenameLang.basename);
        assertEquals("sv", basenameLang.lang);
    }

    @Test
    public void testParseTranslationFilename2() {
        BasenameLang basenameLang = cut.parseTranslationFilename("path/any_sv_SE.properties");
        assertEquals("path/any", basenameLang.basename);
        assertEquals("sv_SE", basenameLang.lang);
    }
}

/*
 */
package org.tweb.storage.properties;

import com.typesafe.config.Config;
import org.tweb.application.util.ConfigKeys;

import javax.inject.Inject;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;

/**
 *
 * @author jonas
 */
public class PathUtil {

    @Inject
    Config config;

    public String createFilePath(String path, String language) {
        int lastPos = path.lastIndexOf('.');
        String filePath = config.getString(ConfigKeys.PATH_FORMAT).replace("~PATH", path.substring(0, lastPos));

        String sep = config.getString(ConfigKeys.FILE_LANGUAGE_SEPARATOR);

        if (language == null) {
            language = config.getString(ConfigKeys.DEFAULT_LANGUAGE_NAME);
        }
        
        if ("".equals(language)) {
            sep = "";
        }

        filePath = filePath.replace("~SEPARATOR", sep);
        filePath = filePath.replace("~LANG", language);

        return filePath;
    }

    public String createDefaultFilePath(String path) {
        // path_format = ~PATH~SEPARATOR~LANG.properties

        String sep = config.getString(ConfigKeys.FILE_LANGUAGE_SEPARATOR);
        String def = config.getString(ConfigKeys.DEFAULT_LANGUAGE_NAME);

        Path p = Paths.get(path);

        BasenameLang basenameLang = parseTranslationFilename(p.getFileName().toString());
        String filePath = config.getString(ConfigKeys.PATH_FORMAT).replace("~PATH", p.getParent().resolve(basenameLang.basename).toString());

        if (def.isEmpty()) {
            sep = "";
        }

        filePath = filePath.replace("~SEPARATOR", sep);
        filePath = filePath.replace("~LANG", def);

        return filePath;
    }


    public BasenameLang parseTranslationFilename(String filename) {
        Matcher matcher = PropertiesCommon.filepattern.matcher(filename);
        if (!matcher.matches()) {
            return null;
        }

        BasenameLang basenameLang = new BasenameLang(matcher.group(1), matcher.group(2));
        if (matcher.groupCount() > 2 && matcher.group(3) != null) {
            basenameLang.lang = basenameLang.lang + matcher.group(3);
        }
        return basenameLang;
    }
}

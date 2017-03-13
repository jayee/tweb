/*
 */
package org.tweb.info.boundary;

import com.typesafe.config.Config;
import org.tweb.application.util.ConfigKeys;
import org.tweb.summary.control.StatisticsUpdater;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author jonas
 */
class ApplicationInfo {

    @Inject
    Config config;

    @Inject
    StatisticsUpdater statUpdater;


    JsonObject getApplicationInfoJson() {
        JsonObjectBuilder json = Json.createObjectBuilder();
        json.add("version", config.getString(ConfigKeys.PROJECT_VERSION));
        json.add("build", config.getString(ConfigKeys.BUILD_DATE));

        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        config.getStringList(ConfigKeys.LANGUAGES).forEach(e -> jsonArray.add(e));
        json.add("languages", jsonArray);

        json.add("defaultOptions",
                Json.createObjectBuilder().
                        add("lang", config.getString(ConfigKeys.DEFAULT_OPTIONS_LANG)).
                        build()
        );

        String google_key = config.getString(ConfigKeys.GOOGLE_TRANSLATE_KEY);
        if (!google_key.startsWith(ConfigKeys.REPLACE_VALUE_START)) {
            json.add("googleTranslationKey", google_key);
        }

        json.add("lastscandate", statUpdater.getLastStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")));
        json.add("lastscanduration", statUpdater.getLastDurationString());
        json.add("nroftranslations", statUpdater.getNumberOfTranslationUnits());

        return json.build();
    }

}

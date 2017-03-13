/*
 */
package org.tweb.summary.boundary;

import org.tweb.summary.control.StatisticsRepoFactory;

import javax.inject.Inject;
import java.util.Collection;


/**
 * @author jonas
 */
class Summary {

    @Inject
    StatisticsRepoFactory statRepo;

    Collection<LanguageSummary> getTranslationSummary(String lang) {
        return statRepo.getRepo().summarizeLanguage(lang);
        //return "{ lastChanged:2015-10-01 }";
    }

}

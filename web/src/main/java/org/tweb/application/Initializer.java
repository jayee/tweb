/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.application;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;

import com.typesafe.config.Config;
import org.tweb.application.util.ConfigKeys;
import org.tweb.search.control.SearchRepoFactory;
import org.tweb.summary.control.StatisticsRepoFactory;
import org.tweb.summary.control.StatisticsUpdater;


/**
 *
 * @author jonas
 */
@Singleton
@Startup
public class Initializer {

    @Inject
    Config config;

    @Inject
    StatisticsUpdater updater;
   

    @Resource
    TimerService timerService;

    long lastExecute = 0;

    @PostConstruct
    void init() {
        // Periodic update, as long as we don't have implemented partial updates
        timerService.createIntervalTimer(0, 5*1000, new TimerConfig(null, false));
    }

    @Timeout
    public void timeout(Timer timer) {
        if (System.currentTimeMillis() > lastExecute + (config.getLong(ConfigKeys.SECONDS_BETWEEN_RESCAN) * 1000)) {
            lastExecute = System.currentTimeMillis() + Integer.MAX_VALUE;
            try {
                updater.initRepos();
            } finally {
                lastExecute = System.currentTimeMillis();
            }
        }

    }
}

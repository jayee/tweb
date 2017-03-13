/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.summary.control;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author jonas
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class StatisticsRepoFactory {
    
    @Inject
    StatisticsRepo statRepo;

    public StatisticsRepo getRepo() {
        return statRepo;
    }
    
}

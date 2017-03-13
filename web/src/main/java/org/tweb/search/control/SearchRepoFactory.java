/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tweb.search.control;

import org.tweb.storage.properties.PropertyFile;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

/**
 *
 * @author jonas
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class SearchRepoFactory {

    @Inject
    SearchRepo repo;

    public SearchRepo getRepo() {
        return repo;
    }

    public SearchRepo newRepo() {
        return CDI.current().select(SearchRepo.class).get();
    }

    public void setRepo(SearchRepo repo) {
        // Clear previous repo
        this.repo.clear();
        // Set new repo
        this.repo = repo;
    }

}

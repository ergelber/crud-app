package com.aquent.crudapp.service;

import java.util.List;

import com.aquent.crudapp.domain.Client;

/**
 * Client operations.
 * @param <Client>
 */
public interface ClientService {

    /**
     * Retrieves all of the person/client records.
     *
     * @return list of person/client records
     */
    List<Client> listEntities();

    /**
     * Creates a new person/client record.
     * @param <Client>
     *
     * @param person/client the values to save
     * @return the new person/client ID
     */
    Integer createEntity(Client entity);

    /**
     * Retrieves a person record by ID.
     *
     * @param id the person ID
     * @return the person/client record
     */
     Object readEntity(Integer id);

    /**
     * Updates an existing person record.
     * @param <Client>
     *
     * @param person/client the new values to save
     */
    void updateEntity(Client entity);

    /**
     * Deletes a person/client record by ID.
     *
     * @param id the person/client ID
     */
    void deleteEntity(Integer id);

    /**
     * Validates populated person/client data.
     * @param <Client>
     *
     * @param person/client the values to validate
     * @return list of error messages
     */
    List<String> validateEntity(Client entity);
}

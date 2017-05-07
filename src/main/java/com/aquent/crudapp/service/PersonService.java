package com.aquent.crudapp.service;

import java.util.List;

import com.aquent.crudapp.domain.Person;

/**
 * Person operations.
 * @param <Person>
 */
public interface PersonService {

    /**
     * Retrieves all of the person/client records.
     *
     * @return list of person/client records
     */
    List<Person> listEntities();

    /**
     * Creates a new person/client record.
     * @param <Person>
     *
     * @param person/client the values to save
     * @return the new person/client ID
     */
    Integer createEntity(Person entity);

    /**
     * Retrieves a person record by ID.
     *
     * @param id the person ID
     * @return the person/client record
     */
     Object readEntity(Integer id);

    /**
     * Updates an existing person record.
     * @param <Person>
     *
     * @param person/client the new values to save
     */
    void updateEntity(Person entity);

    /**
     * Deletes a person/client record by ID.
     *
     * @param id the person/client ID
     */
    void deleteEntity(Integer id);

    /**
     * Validates populated person/client data.
     * @param <Person>
     *
     * @param person/client the values to validate
     * @return list of error messages
     */
    List<String> validateEntity(Person entity);
}

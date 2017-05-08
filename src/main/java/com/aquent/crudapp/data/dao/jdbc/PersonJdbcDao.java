package com.aquent.crudapp.data.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aquent.crudapp.data.dao.PersonDao;
import com.aquent.crudapp.domain.Person;

/**
 * Spring JDBC implementation of {@link PersonDao}.
 */
public class PersonJdbcDao implements PersonDao {

    private static final String SQL_LIST_PEOPLE = "SELECT p.*, c.client_id as client_id, c.company as company FROM person p "
									    		+ "LEFT JOIN client_persons cp "
									    		+ "on p.person_id = cp.person_id "
									    		+ "LEFT JOIN client c "
									    		+ "on cp.client_id = c.client_id "
									    		+ "ORDER BY p.last_name";
    private static final String SQL_READ_PERSON = "SELECT p.*, c.client_id as client_id, c.company as company FROM person p "
									    		+ "LEFT JOIN client_persons cp "
									    		+ "on p.person_id = cp.person_id "
									    		+ "LEFT JOIN client c "
									    		+ "on cp.client_id = c.client_id "
									    		+ "WHERE p.person_id = :personId LIMIT 1";
    private static final String SQL_DELETE_CLIENT_PERSON = "DELETE FROM client_persons WHERE person_id = :personId";
    private static final String SQL_DELETE_PERSON = "DELETE FROM person WHERE person_id = :personId";
    private static final String SQL_UPDATE_PERSON = "UPDATE person SET (first_name, last_name, email_address, street_address, city, state, zip_code)"
												+ " = (:firstName, :lastName, :emailAddress, :streetAddress, :city, :state, :zipCode)"
												+ " WHERE person_id = :personId";
    private static final String SQL_CREATE_PERSON = "INSERT INTO person (first_name, last_name, email_address, street_address, city, state, zip_code)"
                                                + " VALUES (:firstName, :lastName, :emailAddress, :streetAddress, :city, :state, :zipCode)";
    private static final String SQL_CREATE_CLIENT_PERSON = "INSERT INTO client_persons (person_id, client_id)"
    											+ " VALUES (:person_id, :client_id)";
    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> listPeople() {
    	PersonRowMapper mapper = new PersonRowMapper();
        namedParameterJdbcTemplate.getJdbcOperations().query(SQL_LIST_PEOPLE, mapper);
        List<Person> people = new ArrayList<Person>();
        for(Person p : mapper.people.values()) {
        	people.add(p);
        }
        return people;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person readPerson(Integer personId) {
        return namedParameterJdbcTemplate.queryForObject(SQL_READ_PERSON, Collections.singletonMap("personId", personId), new PersonRowMapper());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void deletePerson(Integer personId) {
    	// have to delete from the client_persons table first
        namedParameterJdbcTemplate.update(SQL_DELETE_CLIENT_PERSON, Collections.singletonMap("personId", personId));
        namedParameterJdbcTemplate.update(SQL_DELETE_PERSON, Collections.singletonMap("personId", personId));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void updatePerson(Person person) {
    	if(person.getClientIds() != null && person.getClientIds().size() > 0) {
    		// add the person_id and client_id combinations 
    		// one by one to the client_persons table
        	for(int i : person.getClientIds()) {
        		MapSqlParameterSource params = new MapSqlParameterSource();
        		params.addValue("client_id", i, Types.INTEGER);
        		params.addValue("person_id", person.getPersonId(), Types.INTEGER);
        		try {
        			namedParameterJdbcTemplate.update(SQL_CREATE_CLIENT_PERSON, params);
        		} catch (DuplicateKeyException e) {
        			System.out.println("*** Person " + person.getPersonId() + " already has Client " + i + " associated ****");
        		}
        	}
        }
        namedParameterJdbcTemplate.update(SQL_UPDATE_PERSON, new BeanPropertySqlParameterSource(person));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Integer createPerson(Person person) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_CREATE_PERSON, new BeanPropertySqlParameterSource(person), keyHolder);
        if(person.getClientIds() != null && person.getClientIds().size() > 0) {
        	// add the person_id and client_id combinations 
    		// one by one to the client_persons table
        	for(int i : person.getClientIds()) {
        		MapSqlParameterSource params = new MapSqlParameterSource();
        		params.addValue("client_id", i, Types.INTEGER);
        		params.addValue("person_id", keyHolder.getKey().intValue(), Types.INTEGER);
        		namedParameterJdbcTemplate.update(SQL_CREATE_CLIENT_PERSON, params);
        	}
        }
        return keyHolder.getKey().intValue();
    }

    /**
     * Row mapper for person records.
     */
    private class PersonRowMapper implements RowMapper<Person> {
    	
    	public HashMap<Integer, Person> people = new HashMap<Integer, Person>();
    	
        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        	Person person = people.get(rs.getInt("person_id"));
        	
        	if (person == null) {
        		person = new Person();
        		person.setPersonId(rs.getInt("person_id"));
                person.setFirstName(rs.getString("first_name"));
                person.setLastName(rs.getString("last_name"));
                person.setEmailAddress(rs.getString("email_address"));
                person.setStreetAddress(rs.getString("street_address"));
                person.setCity(rs.getString("city"));
                person.setState(rs.getString("state"));
                person.setZipCode(rs.getString("zip_code"));
                person.setClientIds(new ArrayList<Integer>());
                person.setCompanies(new ArrayList<String>());
                people.put(person.getPersonId(), person);
        	}

            person.getClientIds().add(rs.getInt("client_id"));
            person.getCompanies().add(rs.getString("company"));
            return person;
        }
        
        
    }
    
    
}

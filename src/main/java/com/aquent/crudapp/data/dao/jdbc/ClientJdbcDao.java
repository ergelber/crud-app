package com.aquent.crudapp.data.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aquent.crudapp.data.dao.ClientDao;
import com.aquent.crudapp.domain.Client;
import com.aquent.crudapp.domain.Person;

/**
 * Spring JDBC implementation of {@link ClientDao}.
 */
public class ClientJdbcDao implements ClientDao {

    private static final String SQL_LIST_CLIENTS = "SELECT DISTINCT c.*, p.person_id as person_id, p.first_name, p.last_name as name FROM client c "
									    		+ "LEFT JOIN client_persons cp "
									    		+ "on c.client_id = cp.client_id "
									    		+ "LEFT JOIN person p "
									    		+ "on cp.person_id = p.person_id "
									    		+ "ORDER BY c.company";
    private static final String SQL_READ_CLIENT = "SELECT c.*, p.first_name, p.person_id as person_id, p.last_name as name FROM client c "
									    		+ "LEFT JOIN client_persons cp "
									    		+ "on c.client_id = cp.client_id "
									    		+ "LEFT JOIN person p "
									    		+ "on cp.person_id = p.person_id " 
									    		+ " WHERE client_id = :clientId";
    private static final String SQL_DELETE_CLIENT_PERSON = "DELETE FROM client_persons WHERE client_id = :clientId";
    private static final String SQL_DELETE_CLIENT = "DELETE FROM client WHERE client_id = :clientId";
    private static final String SQL_UPDATE_CLIENT = "UPDATE client SET (company, website, phone, mailing)"
                                                  + " = (:company, :website, :phone, :mailing)"
                                                  + " WHERE client_id = :clientId";
    private static final String SQL_CREATE_CLIENT = "INSERT INTO client (company, website, phone, mailing)"
                                                  + " VALUES (:company, :website, :phone, :mailing)";
    private static final String SQL_CREATE_CLIENT_PERSON = "INSERT INTO client_persons (client_id, person_id)"
            									  + " VALUES (:client_id, :person_id)";
    private static final String SQL_UPDATE_CLIENT_PERSON = "UPDATE client_persons SET (client_id, person_id)"
			  									  + " = (:client_id, :person_id) WHERE client_id = :client_id AND person_id = :person_id";

    
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Client> listClients() {
    	return namedParameterJdbcTemplate.getJdbcOperations().query(SQL_LIST_CLIENTS, new ClientRowMapper());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Client readClient(Integer clientId) {
        return namedParameterJdbcTemplate.queryForObject(SQL_READ_CLIENT, Collections.singletonMap("clientId", clientId), new ClientRowMapper());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void deleteClient(Integer clientId) {
    	// have to delete from the client_persons table first
        namedParameterJdbcTemplate.update(SQL_DELETE_CLIENT_PERSON, Collections.singletonMap("clientId", clientId));
        namedParameterJdbcTemplate.update(SQL_DELETE_CLIENT, Collections.singletonMap("clientId", clientId));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void updateClient(Client client) {
    	if(client.getPersonIds() != null && client.getPersonIds().size() > 0) {
    		// add the person_id and client_id combinations 
    		// one by one to the client_persons table
        	for(int i : client.getPersonIds()) {
        		MapSqlParameterSource params = new MapSqlParameterSource();
        		params.addValue("client_id", client.getClientId(), Types.INTEGER);
        		params.addValue("person_id", i, Types.INTEGER);
        		try {
        			if (namedParameterJdbcTemplate.update(SQL_UPDATE_CLIENT_PERSON, params) == 0) {
        				namedParameterJdbcTemplate.update(SQL_CREATE_CLIENT_PERSON, params);
        			}
        		} catch (DuplicateKeyException e) {
        			System.out.println("*** Client " + client.getClientId() + " already has Person " + i + " associated ****");
        		}
        		
        	}
        }
        namedParameterJdbcTemplate.update(SQL_UPDATE_CLIENT, new BeanPropertySqlParameterSource(client));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Integer createClient(Client client) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_CREATE_CLIENT, new BeanPropertySqlParameterSource(client), keyHolder);
        if(client.getPersonIds() != null && client.getPersonIds().size() > 0) {
        	// add the person_id and client_id combinations 
    		// one by one to the client_persons table
        	for(int i : client.getPersonIds()) {
        		MapSqlParameterSource params = new MapSqlParameterSource();
        		params.addValue("person_id", i, Types.INTEGER);
        		params.addValue("client_id", keyHolder.getKey().intValue(), Types.INTEGER);
        		namedParameterJdbcTemplate.update(SQL_CREATE_CLIENT_PERSON, params);
        	}
        }
        return keyHolder.getKey().intValue();
    }

    /**
     * Row mapper for client records.
     */
    private static final class ClientRowMapper implements RowMapper<Client> {
    	public HashMap<Integer, Client> clients = new HashMap<Integer, Client>();
    	
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
        	Client client = clients.get(rs.getInt("client_id"));
        	
        	if (client == null) {
	            client = new Client();
	            client.setClientId(rs.getInt("client_id"));
	            client.setCompany(rs.getString("company"));
	            client.setWebsite(rs.getString("website"));
	            client.setPhone(rs.getString("phone"));
	            client.setMailing(rs.getString("mailing"));
	            client.setPersonIds(new ArrayList<Integer>());
                client.setPersonNames(new ArrayList<String>());
                clients.put(client.getClientId(), client);
        	}
        	
        	client.getPersonIds().add(rs.getInt("person_id"));
        	client.getPersonNames().add(rs.getString("name"));
            return client;
        }
    }

}

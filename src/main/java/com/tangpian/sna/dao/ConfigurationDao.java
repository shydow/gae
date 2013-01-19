package com.tangpian.sna.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.stereotype.Repository;

import com.tangpian.sna.model.Configuration;
import com.tangpian.sna.model.Post;

@Repository
public class ConfigurationDao {
	public PersistenceManager getPersistenceManager() {
		return DataAccessHelper.getPersistenceManager();
	}

	public List<String> list(String type) {
		Query q = getPersistenceManager().newQuery(
				"select id from " + Configuration.class.getName()
						+ " where type == :type");
		return (List<String>) q.execute(type);
	}

	public void add(String type, String id) {
		Configuration configuration = new Configuration(type, id, null);
		getPersistenceManager().makePersistent(configuration);
	}
}

package com.tangpian.sna.dao;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.stereotype.Repository;

import com.tangpian.sna.model.User;

@Repository
public class UserDao {
	public PersistenceManager getPersistenceManager() {
		return DataAccessHelper.getPersistenceManager();
	}

	protected Class<User> getType() {
		return User.class;
	}

	public User get(String id) {
		try {
			User user = getPersistenceManager().getObjectById(getType(), id);
			return user;
		} catch (Exception e) {
			return null;
		}
	}

	public void add(User entity) {
		getPersistenceManager().makePersistent(entity);
	}
	
	
	public List<String> listIds() {
		Query q = getPersistenceManager().newQuery("select id from " + User.class.getName());
		return (List<String>) q.execute();
	}
	
	public List<User> list() {
		Query q = getPersistenceManager().newQuery(User.class);
		return (List<User>) q.execute();
	}
	
}

package com.tangpian.sna.dao;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.stereotype.Repository;

import com.tangpian.sna.model.UserProcessStatus;

@Repository
public class UserProcessStatusDao {

	public PersistenceManager getPersistenceManager() {
		return DataAccessHelper.getPersistenceManager();
	}

	public void add(UserProcessStatus processStatus) {
		getPersistenceManager().makePersistent(processStatus);
	}

	public List<UserProcessStatus> list() {
		Query q = getPersistenceManager().newQuery(UserProcessStatus.class);
		return (List<UserProcessStatus>) q.execute();
	}

	public UserProcessStatus get(String id) {
		return (UserProcessStatus) getPersistenceManager().getObjectById(id);
	}

	public void touch(UserProcessStatus userProcessStatus) {
		UserProcessStatus processStatus = getPersistenceManager()
				.getObjectById(UserProcessStatus.class,
						userProcessStatus.getId());
		if (processStatus != null) {
			processStatus.setLastProcessTime(new Date());
			getPersistenceManager().makePersistent(processStatus);
		} else {
			userProcessStatus.setLastProcessTime(new Date());
			getPersistenceManager().makePersistent(userProcessStatus);
		}
	}
}

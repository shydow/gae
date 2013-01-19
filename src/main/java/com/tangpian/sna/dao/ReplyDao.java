package com.tangpian.sna.dao;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.springframework.stereotype.Repository;

import com.tangpian.sna.model.Reply;

@Repository
public class ReplyDao {
	public PersistenceManager getPersistenceManager() {
		return DataAccessHelper.getPersistenceManager();
	}

	public void add(Reply reply) {
		getPersistenceManager().makePersistent(reply);
	}

	public Reply get(String id) {
		return getPersistenceManager().getObjectById(Reply.class, id);
	}
	
	public void addList(List<Reply> replies) {
//		getPersistenceManager().deletePersistentAll(replies);
		getPersistenceManager().makePersistentAll(replies);
	}
}

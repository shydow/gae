package com.tangpian.sna.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.stereotype.Repository;

import com.tangpian.sna.model.Relation;

@Repository
public class RelationDao {
	public PersistenceManager getPersistenceManager() {
		return DataAccessHelper.getPersistenceManager();
	}

	public List<Relation> list() {
		Query q = getPersistenceManager().newQuery(Relation.class);
		return (List<Relation>) q.execute();
	}

	private static final String QUERY_BY_SOURCE = "source == :sourceid";

	public List<Relation> ListBySource(String source) {
		Query q = getPersistenceManager().newQuery(Relation.class,
				QUERY_BY_SOURCE);
		return (List<Relation>) q.execute(source);
	}

	public void add(Relation relation) {
		getPersistenceManager().makePersistent(relation);
	}

	public void deleteBySource(String source) {
		Query q = getPersistenceManager().newQuery(Relation.class,
				QUERY_BY_SOURCE);
		q.deletePersistentAll(source);
	}
	
	public void addList(List<Relation> relations) {
//		getPersistenceManager().deletePersistentAll(relations);
		getPersistenceManager().makePersistent(relations);
	}

}

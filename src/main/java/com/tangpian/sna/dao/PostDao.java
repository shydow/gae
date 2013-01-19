package com.tangpian.sna.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.stereotype.Repository;

import com.tangpian.sna.model.Post;
import com.tangpian.sna.model.User;

@Repository
public class PostDao {
	public PersistenceManager getPersistenceManager() {
		return DataAccessHelper.getPersistenceManager();
	}

	public Post get(String id) {
		return getPersistenceManager().getObjectById(Post.class, id);
	}

	public void add(Post entity) {
		getPersistenceManager().makePersistent(entity);
	}

	private static final String QUERY_BY_PROFILEIDS = ":profileids.contains(userId)";

	public void deleteByProfileids(String... profileids) {
		Query query = getPersistenceManager().newQuery(Post.class,
				QUERY_BY_PROFILEIDS);
		query.deletePersistentAll(profileids);
	}

	public void addPosts(List<Post> posts) {
		getPersistenceManager().makePersistentAll(posts);
	}

	private static final String QUERY_BY_KEYS = ":ids.contains(id)";

	public List<Post> getPostsByKeys(List<String> ids) {
		Query query = getPersistenceManager().newQuery(Post.class,
				QUERY_BY_PROFILEIDS);
		List<Post> result = (List<Post>) query.executeWithArray(ids);
		return result;
	}
	
	public void addList(List<Post> posts) {
//		getPersistenceManager().deletePersistentAll(posts);
		getPersistenceManager().makePersistentAll(posts);
	}
	
	public List<String> listIds() {
		Query q = getPersistenceManager().newQuery("select id from " + Post.class.getName());
		return (List<String>) q.execute();
	}
	
	private static final String QUERY_BY_PROFILEID = ":profileids=userId";
	public List<Post> getPostsByProfileId(String profileid) {
		Query query = getPersistenceManager().newQuery(Post.class,
				QUERY_BY_PROFILEIDS);
		return (List<Post>) query.execute(profileid);
	}
}

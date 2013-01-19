package com.tangpian.sna.model;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.Comment;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Reply {
	@PrimaryKey
	String id;
	@Persistent
	Date publishedTime;
	@Persistent
	Text content;
	@Persistent
	String userId;
	@Persistent
	String postId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content.getValue();
	}

	public void setContent(String content) {
		this.content = new Text(content);
	}

	public Reply() {

	}

	public Reply(Comment comment,String postId) {

		this.id = comment.getId();
		this.content = new Text(comment.getObject().getContent());
		this.userId = comment.getActor().getId();
		this.publishedTime = new Date(comment.getPublished().getValue());
		this.postId = postId;
	}
	
	public Date getPublishedTime() {
		return publishedTime;
	}

	public void setPublishedTime(Date publishedTime) {
		this.publishedTime = publishedTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

}

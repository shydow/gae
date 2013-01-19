package com.tangpian.sna.model;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.api.services.plus.model.Activity;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Post {
	@PrimaryKey
	private String id;
	@Persistent
	private String itemUrl;
	@Persistent
	private Date publishedTime;
	@Persistent
	private Date updatedTime;
	@Persistent
	private Text content;
	@Persistent
	private String userId;
	@Persistent
	private String provider;

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

	public Post() {

	}

	public Post(Activity activity) {

		this.id = activity.getId();
		this.itemUrl = activity.getUrl();
		this.provider = activity.getProvider().getTitle();
		this.userId = activity.getActor().getId();
		this.content = new Text(activity.getObject().getContent());
		this.publishedTime = new Date(activity.getPublished().getValue());
		this.updatedTime = new Date(activity.getUpdated().getValue());
	}

	public String getItemUrl() {
		return itemUrl;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public Date getPublishedTime() {
		return publishedTime;
	}

	public void setPublishedTime(Date publishedTime) {
		this.publishedTime = publishedTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof Post)) {
			return false;
		}

		Post that = (Post) obj;
		return new EqualsBuilder().append(this.getId(), that.getId())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).hashCode();
	}
}

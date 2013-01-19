package com.tangpian.sna.model;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.api.services.plus.model.Person;

@PersistenceCapable
public class User {

	@PrimaryKey
	private String id;

	/*---------------------gplus User info start---------------- */
	@Persistent
	private String gplusAboutMe;
	@Persistent
	private String gplusBirthday;
	@Persistent
	private String gplusDisplayName;
	@Persistent
	private String gplusGender;
	@Persistent
	private String gplusName;
	@Persistent
	private String gplusUrl;
	@Persistent
	private String gplusRelationshipStatus;
	@Persistent
	private String gplusImageUrl;
	
	/*---------------------gplus User info end---------------- */

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGplusAboutMe() {
		return gplusAboutMe;
	}

	public void setGplusAboutMe(String gplusAboutMe) {
		this.gplusAboutMe = gplusAboutMe;
	}

	public String getGplusBirthday() {
		return gplusBirthday;
	}

	public void setGplusBirthday(String gplusBirthday) {
		this.gplusBirthday = gplusBirthday;
	}

	public String getGplusDisplayName() {
		return gplusDisplayName;
	}

	public void setGplusDisplayName(String gplusDisplayName) {
		this.gplusDisplayName = gplusDisplayName;
	}

	public String getGplusGender() {
		return gplusGender;
	}

	public void setGplusGender(String gplusGender) {
		this.gplusGender = gplusGender;
	}

	public String getGplusName() {
		return gplusName;
	}

	public void setGplusName(String gplusName) {
		this.gplusName = gplusName;
	}

	public String getGplusUrl() {
		return gplusUrl;
	}

	public void setGplusUrl(String gplusUrl) {
		this.gplusUrl = gplusUrl;
	}

	public String getGplusRelationshipStatus() {
		return gplusRelationshipStatus;
	}

	public void setGplusRelationshipStatus(String gplusRelationshipStatus) {
		this.gplusRelationshipStatus = gplusRelationshipStatus;
	}

	public String getGplusImageUrl() {
		return gplusImageUrl;
	}

	public void setGplusImageUrl(String gplusImageUrl) {
		this.gplusImageUrl = gplusImageUrl;
	}

	// public void setGaeUser(com.google.appengine.api.users.User user) {
	// this.id = user.getUserId();
	// this.nickname = user.getNickname();
	// this.email = user.getEmail();
	// }

	public void setGplusUser(Person user) {
		this.id = user.getId();
		this.gplusAboutMe = user.getAboutMe();
		this.gplusBirthday = user.getBirthday();
		this.gplusDisplayName = user.getDisplayName();
		this.gplusGender = user.getGender();
		this.gplusName = user.getName().getFormatted();
		this.gplusUrl = user.getUrl();
		this.gplusRelationshipStatus = user.getRelationshipStatus();
		this.gplusImageUrl = user.getImage().getUrl();
	}
}

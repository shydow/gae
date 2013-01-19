package com.tangpian.sna.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Relation {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;

	@Persistent
	private String source;

	@Persistent
	private String target;

	@Persistent
	private int value;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Relation() {

	}

	public Relation(String source, String target, int value) {
		this.source = source;
		this.target = target;
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof Relation)) {
			return false;
		}
		if (this == obj) {
			return true;
		} else {
			Relation relation = (Relation) obj;
			return new EqualsBuilder()
					.append(this.source, relation.getSource())
					.append(this.target, relation.getTarget()).build();
		}
	}
}

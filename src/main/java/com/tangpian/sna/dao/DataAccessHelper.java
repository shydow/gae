package com.tangpian.sna.dao;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class DataAccessHelper {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");
	
	public static PersistenceManager getPersistenceManager() {
		return pmfInstance.getPersistenceManager();
	}
}

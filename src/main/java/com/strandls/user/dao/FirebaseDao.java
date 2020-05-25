package com.strandls.user.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import com.strandls.user.pojo.FirebaseTokens;
import com.strandls.user.util.AbstractDAO;

public class FirebaseDao extends AbstractDAO<FirebaseTokens, Long> {

	private final Logger logger = LoggerFactory.getLogger(FirebaseDao.class);
	
	@Inject
	protected FirebaseDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public FirebaseTokens findById(Long id) {
		Session session = sessionFactory.openSession();
		FirebaseTokens entity = null;
		try {
			entity = session.get(FirebaseTokens.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}

}

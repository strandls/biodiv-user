/**
 * 
 */
package com.strandls.user.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.user.pojo.User;
import com.strandls.user.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserDao extends AbstractDAO<User, Long> {

	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public User findById(Long id) {
		Session session = sessionFactory.openSession();
		User entity = null;
		try {
			entity = session.get(User.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}

		return entity;
	}

}

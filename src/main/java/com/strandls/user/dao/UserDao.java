/**
 * 
 */
package com.strandls.user.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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

	@SuppressWarnings("unchecked")
	public User findByUserEmail(String email) {
		Session session = sessionFactory.openSession();
		String hql = "from User u where u.email = :email";
		User entity = null;
		try {
			Query<User> query = session.createQuery(hql); 
			query.setParameter("email", email);
			
			entity = query.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}
	
	public User findById(Long id, boolean activity) {
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
	
	@SuppressWarnings("unchecked")
	public User findByUserMobile(String mobileNumber) {
		Session session = sessionFactory.openSession();
		String hql = "from User u where u.mobileNumber = :mobileNumber";
		User entity = null;
		try {
			Query<User> query = session.createQuery(hql); 
			query.setParameter("mobileNumber", mobileNumber);
			
			entity = query.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public User findByUserEmailOrMobile(String data) {
		Session session = sessionFactory.openSession();
		String hql = "from User u where u.email = :data or u.mobileNumber = :data";
		User entity = null;
		try {
			Query<User> query = session.createQuery(hql); 
			query.setParameter("data", data);
			
			entity = query.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}

}

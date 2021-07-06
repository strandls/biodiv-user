package com.strandls.user.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.user.pojo.Language;
import com.strandls.user.pojo.Role;
import com.strandls.user.util.AbstractDAO;

public class RoleDao extends AbstractDAO<Role, Long> {

	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected RoleDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public Role findById(Long id) {
		Session session = sessionFactory.openSession();
		Role result = null;
		try {
			result = session.get(Role.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Role findRoleByProperty(String value, String property) {
		String qry = "from Role where :property = :value";
		Session session = sessionFactory.openSession();
		List<Role> resultList = new ArrayList<Role>();
		try {
			Query<Role> query = session.createQuery(qry);
			query.setParameter("property", property);
			query.setParameter("value", value);
			query.setMaxResults(1);
			resultList = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return resultList.get(0);
	}

}

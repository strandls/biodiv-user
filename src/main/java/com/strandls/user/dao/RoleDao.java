package com.strandls.user.dao;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
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
		// TODO Auto-generated method stub
		return null;
	}
	
}

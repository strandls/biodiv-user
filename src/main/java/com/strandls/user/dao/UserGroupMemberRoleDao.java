/**
 * 
 */
package com.strandls.user.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.user.pojo.UserGroupMemberRole;
import com.strandls.user.pojo.UserGroupMembersCount;
import com.strandls.user.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupMemberRoleDao extends AbstractDAO<UserGroupMemberRole, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupMemberRole.class);

	/**
	 * @param sessionFactory
	 */

	@Inject
	protected UserGroupMemberRoleDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupMemberRole findById(Long id) {
		Session session = sessionFactory.openSession();
		UserGroupMemberRole entity = null;
		try {
			entity = session.get(UserGroupMemberRole.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public UserGroupMemberRole findByUserGroupIdUserId(Long userGroupId, Long userId) {
		Session session = sessionFactory.openSession();
		UserGroupMemberRole result = null;

		String qry = "from UserGroupMemberRole where userGroupId = :ugId and sUserId = userId";
		try {

			Query<UserGroupMemberRole> query = session.createQuery(qry);
			query.setParameter("ugId", userGroupId);
			query.setParameter("userId", userId);
			result = query.getSingleResult();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	public List<UserGroupMemberRole> getUserGroup(Long sUserId) {

		String qry = "from UserGroupMemberRole where sUserId = :sUserId";
		Session session = sessionFactory.openSession();
		List<UserGroupMemberRole> result = new ArrayList<UserGroupMemberRole>();
		try {
			Query<UserGroupMemberRole> query = session.createQuery(qry);
			query.setParameter("sUserId", sUserId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupMemberRole> findUserGroupbyUserIdRole(Long userId) {

		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		String expert = properties.getProperty("userGroupExpert");
		String founder = properties.getProperty("userGroupFounder");

		String qry = "from UserGroupMemberRole where sUserId = :userId and roleId in ( " + founder + " , " + expert
				+ ")";
		Session session = sessionFactory.openSession();
		List<UserGroupMemberRole> result = null;
		try {
			Query<UserGroupMemberRole> query = session.createQuery(qry);
			query.setParameter("userId", userId);

			result = query.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
			try {
				in.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupMembersCount> fetchMemberCountUserGroup() {

		List<UserGroupMembersCount> result = new ArrayList<UserGroupMembersCount>();
		String qry = "select user_group_id, count(s_user_id) from user_group_member_role group by user_group_id order by count(s_user_id) desc";
		Session session = sessionFactory.openSession();
		try {
			Query<Object[]> query = session.createNativeQuery(qry);
			List<Object[]> resultObject = query.getResultList();
			for (Object[] o : resultObject) {
				result.add(new UserGroupMembersCount(Long.parseLong(o[0].toString()), Long.parseLong(o[1].toString())));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}

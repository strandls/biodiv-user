package com.strandls.user.util;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractService<T> {
	private static final Logger logger = LoggerFactory.getLogger(AbstractService.class);
	
	public final Class<T> entityClass;
	protected AbstractDAO<T, Long> dao;

	@SuppressWarnings("unchecked")
	public AbstractService(AbstractDAO<T, Long> dao) {
		logger.debug("\nAbstractService constructor");
		this.dao = dao;
		entityClass = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	public T save(T entity) {
		try {
			this.dao.save(entity);
			return entity;
		} catch (NoResultException re) {
			throw new NoResultException(re.getMessage());
		}
	}

	public T update(T entity) {
		try {
			this.dao.update(entity);
			return entity;
		} catch (NoResultException re) {
			throw new NoResultException(re.getMessage());
		}

	}

	public T delete(Long id) {
		try {
			T entity = this.dao.findById(id);
			this.dao.delete(entity);
			return entity;
		} catch (NoResultException re) {
			throw new NoResultException(re.getMessage());
		}
	}

	public T findById(Long id) {
		try {
			return this.dao.findById(id);
		} catch (RuntimeException re) {
			throw new NoResultException(re.getMessage());
		}
	}

	public List<T> findAll(int limit, int offset) {
		try {
			return this.dao.findAll(limit, offset);
		} catch (NoResultException re) {
			throw new NoResultException(re.getMessage());
		}
	}

	public List<T> findAll() {

		try {
			return this.dao.findAll();
		} catch (NoResultException re) {
			throw new NoResultException(re.getMessage());
		}
	}

}

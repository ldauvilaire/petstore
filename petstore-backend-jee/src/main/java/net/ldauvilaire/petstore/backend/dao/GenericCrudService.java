package net.ldauvilaire.petstore.backend.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;

import net.ldauvilaire.petstore.backend.util.Loggable;

/**
 * @author Laurent Dauvilaire
 *
 * @param <T>
 */
@Loggable
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class GenericCrudService<T> implements CrudService<T> {

	@Inject
	private transient Logger logger;

	@PersistenceContext(unitName="petstore")
	EntityManager em;

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#create(java.lang.Object)
	 */
	@Override
	public T create(T t) {
		this.em.persist(t);
		return t;
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#createAndRefresh(java.lang.Object)
	 */
	@Override
	public T createAndRefresh(T t) {
		this.em.persist(t);
		this.em.flush();
		this.em.refresh(t);
		return t;
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#find(java.lang.Class, java.lang.Object)
	 */
	@Override
	public T find(Class<T> type, Object id) {
		T t = (T) this.em.find(type, id);
		return t;
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#delete(java.lang.Class, java.lang.Object)
	 */
	@SuppressWarnings("all")
	@Override
	public boolean delete(Class type, Object id) {
		Object ref = this.em.getReference(type, id);
		if (ref == null) {
			return false;
		}
		this.em.remove(ref);
		return true;
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#update(java.lang.Object)
	 */
	@Override
	public T update(T t) {
		return (T) this.em.merge(t);
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#updateAndRefresh(java.lang.Object)
	 */
	@Override
	public T updateAndRefresh(T t) {
		this.em.merge(t);
		this.em.flush();
		this.em.refresh(t);
		return t;
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#refresh(java.lang.Object)
	 */
	@Override
	public void refresh (T t) {
		this.em.refresh(t);}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#findWithNamedQuery(java.lang.String)
	 */
	@SuppressWarnings("all")
	@Override
	public List findWithNamedQuery(String namedQueryName){
		return this.em.createNamedQuery(namedQueryName).getResultList();
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#findWithNamedQuery(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("all")
	@Override
	public List findWithNamedQuery(
			String namedQueryName,
			Map<String, Object> parameters) {

		return findWithNamedQuery(namedQueryName, parameters, 0);
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#findWithNamedQuery(java.lang.String, int)
	 */
	@SuppressWarnings("all")
	@Override
	public List findWithNamedQuery(
			String queryName,
			int resultLimit) {

		Query query = this.em.createNamedQuery(queryName);
		if (resultLimit > 0) {
			query.setMaxResults(resultLimit);
		}
		return query.getResultList();
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#findWithNamedQuery(java.lang.String, int, int)
	 */
	@SuppressWarnings("all")
	@Override
	public List<T> findWithNamedQuery(
			String queryName,
			int start,
			int resultLimit) {

		Query query = this.em.createNamedQuery(queryName);
		if (start > 0) {
			query.setFirstResult(start);
		}
		if (resultLimit > 0) {
			query.setMaxResults(resultLimit);
		}
		return query.getResultList();
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#findWithNamedQuery(java.lang.String, java.util.Map, int)
	 */
	@SuppressWarnings("all")
	@Override
	public List findWithNamedQuery(
			String namedQueryName,
			Map<String, Object> parameters,
			int resultLimit) {

		Set<Entry<String, Object>> rawParameters = parameters.entrySet();
		Query query = this.em.createNamedQuery(namedQueryName);
		if (resultLimit > 0) {
			query.setMaxResults(resultLimit);
		}
		for (Entry<String, Object> entry : rawParameters) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.getResultList();
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#findWithNamedQuery(java.lang.String, java.util.Map, int, int)
	 */
	@SuppressWarnings("all")
	@Override
	public List<T> findWithNamedQuery(
			String namedQueryName,
			Map<String, Object> parameters,
			int start,
			int resultLimit) {

		Set<Entry<String, Object>> rawParameters = parameters.entrySet();
		Query query = this.em.createNamedQuery(namedQueryName);
		if (start > 0) {
			query.setFirstResult(start);
		}
		if (resultLimit > 0) {
			query.setMaxResults(resultLimit);
		}
		for (Entry<String, Object> entry : rawParameters) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query.getResultList();
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#findByNativeQuery(java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("all")
	@Override
	public List findByNativeQuery(
			String sql,
			Class<? extends T> type) {

		return this.em.createNativeQuery(sql, type).getResultList();
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.dao.CrudService#findUniqueResultWithNamedQuery(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("all")
	@Override
	public T findUniqueResultWithNamedQuery(
			String namedQueryName,
			Map<String, Object> parameters){

		logger.debug("Request: {} {}", namedQueryName, parameters);
		Set<Entry<String, Object>> rawParameters = parameters.entrySet();
		Query query = this.em.createNamedQuery(namedQueryName);
		for (Entry<String, Object> entry : rawParameters) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		T result = null;
		try {
			result = (T) query.getSingleResult();
		} catch( NoResultException nre) { 
			logger.warn("No result found for the request: {} {}", namedQueryName, parameters);
			result = null;
		}

		return result;
	}

	protected void executeUnmappedQuery(String queryAsString) {
		logger.debug("Request: {}", queryAsString);
		Query query = this.em.createQuery(queryAsString);
		query.getResultList();
	}
}

package net.ldauvilaire.petstore.backend.dao;

import java.util.List;
import java.util.Map;

/**
 * @author Laurent Dauvilaire
 *
 * @param <T>
 */
public interface CrudService<T> {

	public T create(T t);
	public T createAndRefresh(T t);
	public T find(Class<T> type, Object id);
	public T update(T t);
	public T updateAndRefresh(T t);
	public boolean delete(Class<T> type, Object id);

	public void refresh (T t);

	public List<T> findWithNamedQuery(String namedQueryName);
	public List<T> findWithNamedQuery(String namedQueryName, Map<String, Object> parameters);
	public List<T> findWithNamedQuery(String queryName, int resultLimit);
	public List<T> findWithNamedQuery(String queryName, int start, int resultLimit);
	public List<T> findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int resultLimit);
	public List<T> findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int start, int resultLimit);

	public List<T> findByNativeQuery(String sql, Class<? extends T> type);
	public T findUniqueResultWithNamedQuery(String namedQueryName, Map<String, Object> parameters);
}

package de.markusseidl.massbusiness.business.common;


import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Iterator;
import java.util.List;

/**
 * QueryIterator - Proof of concept implementation(!)
 * <p>
 * Created on 17.07.2016
 */
public class QueryIterator<E> implements Iterator<E>, Iterable<E> {

    private static final Logger LOG = Logger.getLogger(QueryIterator.class);
    private static final int BLOCK_SIZE = 15000;

    private final long _count;

    private EntityManager _em;

    private CriteriaQuery<?> _query;

    private int _blockIndex = -1;

    private int _index = 0;

    private int _listSize = -1;

    /**
     * Stores the currently loaded <i>chunk</i> (which is of "index" _blockIndex * BLOCK_SIZE).
     */
    private List<E> _list = null;

    /**
     * Default constructor.
     *
     * @param em
     *            EntityManager
     * @param query
     *            CriteriaQuery for fetching all data
     * @param countQuery
     *            CriteriaQuery for fetching total request count
     */
    public QueryIterator(EntityManager em, CriteriaQuery<?> query, CriteriaQuery<?> countQuery) {

        _query = query;
        _em = em;

        // Calculate the count of all elements, for an easier hasNext and next calculation.
        Query q = _em.createQuery(countQuery);
        Object result = q.getSingleResult();
        if(result instanceof Tuple) {
            _count = (long) ((Tuple)result).get(0);
        } else {
            _count = (long) result;
        }
    }

    @Override
    public boolean hasNext() {
        return Math.max(0, _blockIndex * BLOCK_SIZE + _index) < _count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E next() {
        if (!hasNext()) {
            return null;
        }

        if (_index >= _listSize) {
            loadNextChunk();
        }

        return _list.get(_index++);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove operation not supported.");
    }

    /**
     * Loads next chunk
     */
    private void loadNextChunk() {
        _index = 0;
        _blockIndex++;

        Query q = _em.createQuery(_query);
        int firstResult = _blockIndex * BLOCK_SIZE;
        int maxResults = BLOCK_SIZE;

        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);

        LOG.trace("About to fetch from <" + firstResult + "> to <" + (firstResult + maxResults) + " of " + _count);
        _list = q.getResultList();
        _listSize = _list != null ? _list.size() : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<E> iterator() {
        return this;
    }
}

package de.markusseidl.massbusiness.business.common;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

/**
 * QueryIterator - Proof of concept implementation. Allows to iterate over a large set of data in little chunks transparently.
 */
public class QueryIterator<E> implements Iterator<E>, Iterable<E> {

  private static final Logger LOG = Logger.getLogger(QueryIterator.class);
  private static final int BLOCK_SIZE = 15000;

  private EntityManager _em;

  private CriteriaQuery<E> _query;

  /** Indicates that the iterator has reached it's end and that no further rows can be loaded. */
  private boolean _finished = false;

  /** The current block that has been fetched. */
  private int _blockIndex = -1;

  /** The index inside the last fetched block */
  private int _index = 0;

  /** Shortcut to _list.size(), refreshed if a new block is fetched. */
  private int _listSize = -1;

  /**
   * Stores the currently loaded <i>chunk</i> (which is of "index" _blockIndex * BLOCK_SIZE).
   */
  private List<E> _list = null;

  /**
   * Default constructor.
   *
   * @param em
   *     EntityManager
   * @param query
   *     CriteriaQuery for fetching all data
   */
  public QueryIterator(EntityManager em, CriteriaQuery<E> query) {
    _query = query;
    _em = em;
  }

  /**
   * Returns the next element that would be returned by {@link #next()} without advancing to the next element. Can be called multiple
   * times and will always return the same result until {@link #next()} is called. Returns null if {@link #hasNext()} returns false.
   */
  public E peek() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    return _list.get(_index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasNext() {
    if (_listSize <= _index) {
      loadNextChunk();
    }

    return _index < _listSize || !_finished;

  }

  /**
   * Loads next chunk, if possible.
   */
  private void loadNextChunk() {
    if (_finished) {
      return;
    }

    _index = 0;
    _blockIndex++;

    TypedQuery<E> q = _em.createQuery(_query);
    int firstResult = _blockIndex * BLOCK_SIZE;
    int maxResults = BLOCK_SIZE;

    q.setFirstResult(firstResult);
    q.setMaxResults(maxResults);

    LOG.trace("About to fetch from <" + firstResult + "> to <" + (firstResult + maxResults) + ">.");
    _list = q.getResultList();
    _listSize = _list != null ? _list.size() : 0;

    // if we load less than BLOCK_SIZE and we request BLOCK_SIZE results, there aren't any more rows in the database (=end reached).
    if (_listSize < BLOCK_SIZE - 1) {
      _finished = true;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public E next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    return _list.get(_index++);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<E> iterator() {
    return new QueryIterator<>(_em, _query);
  }

}

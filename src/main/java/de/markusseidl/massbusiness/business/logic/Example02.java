package de.markusseidl.massbusiness.business.logic;

import de.markusseidl.massbusiness.business.common.QueryIterator;
import de.markusseidl.massbusiness.business.domain.Disponent;
import de.markusseidl.massbusiness.business.domain.StoragePart;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Example02 - Join
 *
 * Created on 20.09.2016
 */
public class Example02 {

  private final static Logger LOG = Logger.getLogger(Example01.class);

  private final EntityManager _em;

  public Example02(EntityManager em) {
    _em = em;
  }

  /** Demeonstrates the join example (second part) of the article. */
  public Map<Disponent, Integer> run() {
    Map<Disponent, Integer> result = new HashMap<>();

    QueryIterator<Disponent> it_d = createDisponentIterator();
    QueryIterator<StoragePart> it_sp = createStorageIterator();
    while (it_d.hasNext()) {
      Disponent d = it_d.peek();

      boolean first = true;
      result.put(d, 0);

      while (it_sp.hasNext()) {
        StoragePart sp = it_sp.peek();

        // LOGIK

        String start = d.getStartPartNumber();
        String end = d.getEndPartNumber();

        boolean smaller = start.compareTo(sp.getPartId()) > 0;
        boolean larger = end.compareTo(sp.getPartId()) < 0;

        if (larger) {
          break; // storage part is for the next disponent
        } else if (smaller) {
          it_sp.next(); // discard part - no disponent
        } else {
          // connect d + sp
          if (first) {
            first = false;
            it_d.next(); // accept
          }
          it_sp.next(); // accept

          result.put(d, result.get(d) + 1);
        }
      }

      if (first) {
        it_d.next(); // no storage parts for this disponent.
      }
    }

    System.out.println(result);

    return result;
  }

  private QueryIterator<StoragePart> createStorageIterator() {
    CriteriaBuilder cb = _em.getCriteriaBuilder();

    CriteriaQuery<StoragePart> dataQuery = cb.createQuery(StoragePart.class);
    Root<StoragePart> r = dataQuery.from(StoragePart.class);
    dataQuery.select(r);
    dataQuery.orderBy(cb.asc(r.get("_partId")));

    return new QueryIterator<>(_em, dataQuery);
  }

  private QueryIterator<Disponent> createDisponentIterator() {
    CriteriaBuilder cb = _em.getCriteriaBuilder();

    CriteriaQuery<Disponent> dataQuery = cb.createQuery(Disponent.class);
    Root<Disponent> r = dataQuery.from(Disponent.class);
    dataQuery.select(r);
    dataQuery.orderBy(cb.asc(r.get("_startPartNumber")));

    return new QueryIterator<>(_em, dataQuery);
  }

}

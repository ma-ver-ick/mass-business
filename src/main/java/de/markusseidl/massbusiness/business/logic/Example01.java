package de.markusseidl.massbusiness.business.logic;

import static de.markusseidl.massbusiness.business.logic.GroupFunctions.groupByKeyForV1;
import static de.markusseidl.massbusiness.business.logic.GroupFunctions.groupByKeyForV2;
import static de.markusseidl.massbusiness.business.logic.GroupFunctions.whereCriteriaForGroupByV1;

import de.markusseidl.massbusiness.business.common.QueryIterator;
import de.markusseidl.massbusiness.business.domain.StoragePart;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Example01 - Group by
 */
public class Example01 {

  private final static Logger LOG = Logger.getLogger(Example01.class);

  private final EntityManager _em;

  public Example01(EntityManager em) {
    _em = em;
  }

  /**
   * JPQL-version of this method: "SELECT sp, COUNT(sp) FROM StoragePart sp GROUP BY sp._partType"
   */
  public Map<Object, Long> run01V1_forGroup() {
    CriteriaBuilder cb = _em.getCriteriaBuilder();

    CriteriaQuery<StoragePart> dataQuery = cb.createQuery(StoragePart.class);
    Root<StoragePart> r = dataQuery.from(StoragePart.class);
    dataQuery.select(r);
    dataQuery.orderBy(cb.asc(r.get("_partId")));

    QueryIterator<StoragePart> it = new QueryIterator<>(_em, dataQuery);

    Map<Object, Long> result = new TreeMap<>();

    for (StoragePart sp : it) {
      Object key = groupByKeyForV1(sp);
      Long temp = result.get(key);
      if (temp == null) {
        temp = 1L;
      } else {
        temp++;
      }

      result.put(key, temp);
    }

//    LOG.info("Group by results: ");
//    LOG.info("------------------");
//    for (Map.Entry<Object, Long> e : result.entrySet()) {
//      LOG.info(" " + e.getKey() + "\t" + e.getValue());
//    }

    return result;
  }

  /**
   * Second example for the first part of the article, {@link GroupFunctions#groupByKeyForV2(StoragePart)}.
   * Basically it uses a formula to calculate a value for each storage part and bins the result in 10 categories.
   */
  public Map<Object, Long> run01V2() {
    CriteriaBuilder cb = _em.getCriteriaBuilder();

    CriteriaQuery<StoragePart> dataQuery = cb.createQuery(StoragePart.class);
    Root<StoragePart> r = dataQuery.from(StoragePart.class);
    dataQuery.select(r);
//    dataQuery.orderBy(cb.asc(r.get("_partId")));

    QueryIterator<StoragePart> it = new QueryIterator<>(_em, dataQuery);

    Map<Object, Long> result = new TreeMap<>();

    for (StoragePart sp : it) {
      Object key = groupByKeyForV2(sp);
      Long temp = result.get(key);
      if (temp == null) {
        temp = 1L;
      } else {
        temp++;
      }

      result.put(key, temp);
    }

    LOG.info("Group by results: ");
    LOG.info("------------------");
    for (Map.Entry<Object, Long> e : result.entrySet()) {
      LOG.info(" " + e.getKey() + "\t" + e.getValue());
    }

    return result;
  }

  public long run01V1_inverse(StoragePart changedPart) {

    CriteriaBuilder cb = _em.getCriteriaBuilder();

    CriteriaQuery<StoragePart> dataQuery = cb.createQuery(StoragePart.class);
    Root<StoragePart> r = dataQuery.from(StoragePart.class);
    dataQuery.select(r);
    dataQuery.where(whereCriteriaForGroupByV1(changedPart, r, cb));
    dataQuery.orderBy(cb.asc(r.get("_partId")));

    QueryIterator<StoragePart> it = new QueryIterator<>(_em, dataQuery);

    long result = 0;
    Object groupIdentifier = groupByKeyForV1(changedPart);

    for (StoragePart sp : it) {
      Object key = groupByKeyForV1(sp);
      if (!groupIdentifier.equals(key)) {
        continue;
      }

      result++;
    }

    return result;
  }

  public int run01V2_inverse(StoragePart groupIdentifier) {
    CriteriaBuilder cb = _em.getCriteriaBuilder();

    CriteriaQuery<StoragePart> dataQuery = cb.createQuery(StoragePart.class);
    Root<StoragePart> r = dataQuery.from(StoragePart.class);
    dataQuery.select(r);
    dataQuery.where(GroupFunctions.whereCriteriaForGroupByV2(groupIdentifier, r, cb));
    dataQuery.orderBy(cb.asc(r.get("_partId")));

    QueryIterator<StoragePart> it = new QueryIterator<>(_em, dataQuery);

    Object groupSourceKey = GroupFunctions.groupByKeyForV2(groupIdentifier);

    int ret = 0;
    for (StoragePart sp : it) {

      Object temp = GroupFunctions.groupByKeyForV2(sp);
      if (!Objects.equals(temp, groupSourceKey)) {
        System.out.println(temp);
        continue;
      }

      ret++;
    }

    return ret;
  }

}

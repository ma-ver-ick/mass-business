package de.markusseidl.massbusiness.business.logic;

import de.markusseidl.massbusiness.business.common.QueryIterator;
import de.markusseidl.massbusiness.business.domain.StoragePart;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Map;
import java.util.TreeMap;

import static de.markusseidl.massbusiness.business.logic.Example01Descriminator.groupByKeyFor;

/**
 * Example01
 * <p>
 * Created on 11.08.2016
 */
public class Example01 {

    private final static Logger LOG = Logger.getLogger(Example01.class);

    private final EntityManager _em;


    public Example01(EntityManager em) {
        _em = em;
    }

    public void run() {
        CriteriaBuilder cb = _em.getCriteriaBuilder();

        CriteriaQuery<StoragePart> dataQuery = cb.createQuery(StoragePart.class);
        Root<StoragePart> r = dataQuery.from(StoragePart.class);
        dataQuery.select(r);
        dataQuery.orderBy(cb.asc(r.get("_partType")));

        CriteriaQuery<Tuple> countQuery = cb.createTupleQuery();
        Root<StoragePart> r2 = countQuery.from(StoragePart.class);
        countQuery.multiselect(cb.count(r2));

        // _em.createQuery("SELECT sp, COUNT(sp) FROM StoragePart sp GROUP BY sp._partType");

        QueryIterator<StoragePart> it = new QueryIterator<>(_em, dataQuery, countQuery);

        Map<Object, Long> result = new TreeMap<>();

        for (StoragePart sp : it) {
            Long temp = result.get(groupByKeyFor(sp));
            if (temp == null) {
                temp = 1L;
            } else {
                temp++;
            }

            result.put(sp.getPartType(), temp);
        }

        LOG.info("Group by results: ");
        LOG.info("------------------");
        for (Map.Entry<Object, Long> e : result.entrySet()) {
            LOG.info(" " + e.getKey() + "\t" + e.getValue());
        }
    }

}

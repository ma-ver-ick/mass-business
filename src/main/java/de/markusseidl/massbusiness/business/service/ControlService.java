package de.markusseidl.massbusiness.business.service;

import de.markusseidl.massbusiness.business.domain.StoragePart;
import de.markusseidl.massbusiness.business.logic.Example01;

import org.apache.log4j.Logger;

import java.util.Random;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * ControlService
 * <p>
 * Created on 24.07.2016
 */
@Stateless
public class ControlService {

  private final static Logger LOG = Logger.getLogger(ControlService.class);

  @PersistenceContext(unitName = "default_batch")
  private EntityManager _em;

  @Asynchronous
  public void createStorageParts() {
    LOG.info("Started StoragePart generation...");
    try {

      final String[] types = new String[]{"Zukaufteil", "Normteil", "Verbrauchsmaterial"};
      Random rnd = new Random();

      for (int i = 0; i < 10_000; i++) {
        StoragePart
            sp =
            new StoragePart(String.format("SP_%08d", i), types[i % types.length], rnd.nextInt(100_000), rnd.nextFloat() * 100.0f);
        _em.persist(sp);

        if (i % 1_000 == 0) {
          LOG.info(" * Write next chunk <" + i + ">...");
          _em.flush();
          _em.clear();
        }
      }

    } finally {
      LOG.info("... finished StoragePart generation.");
    }
  }

  @Asynchronous
  public void runExample01() {
    LOG.info("Simulating group by...");
    try {
      new Example01(_em).run();
    } finally {
      LOG.info("... finished simulating group by.");
    }
  }

}

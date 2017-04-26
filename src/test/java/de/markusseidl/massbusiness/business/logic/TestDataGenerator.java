package de.markusseidl.massbusiness.business.logic;

import de.markusseidl.massbusiness.business.domain.Disponent;
import de.markusseidl.massbusiness.business.domain.StoragePart;

import org.apache.log4j.Logger;

import java.util.Random;

import javax.persistence.EntityManager;

/**
 * TestDataGenerator
 * <p>
 * Created on 24.07.2016
 */
public class TestDataGenerator {

  private final static Logger LOG = Logger.getLogger(TestDataGenerator.class);

  public static void createData(EntityManager em) {
    LOG.info("Started data generation...");
    try {

      em.createNativeQuery("TRUNCATE TABLE T_DISPONENT").executeUpdate();
      em.createNativeQuery("TRUNCATE TABLE T_STORAGE_PART").executeUpdate();

      final String[] types = new String[]{StoragePart.TYPE_ZUKAUFTEIL, StoragePart.TYPE_NORMTEIL, StoragePart.TYPE_VERBRAUCHSMATERIAL};
      Random rnd = new Random(42);

      final int countStorageParts = 10_000;
      final String storageNumberFormat = "SP_%08d";

      LOG.info("StoragePart:");
      for (int i = 0; i < countStorageParts; i++) {
        StoragePart
            sp =
            new StoragePart(String.format(storageNumberFormat, i), types[i % types.length], rnd.nextInt(100_000), rnd.nextFloat() * 100.0f);
        em.persist(sp);

        if (i % 1_000 == 0) {
          LOG.info(" * Write next chunk <" + i + ">...");
          em.flush();
          em.clear();
        }
      }

      em.flush();
      em.clear();

      LOG.info("Disponent:");
      int storageNumber = 0;
      int disponentNumber = 0;
      while (storageNumber < countStorageParts) {
        Disponent d = new Disponent();

        d.setId(String.format("D_%08d", disponentNumber));
        d.setStartPartNumber(String.format(storageNumberFormat, storageNumber));

        if (countStorageParts - storageNumber < 100) {
          storageNumber = countStorageParts;
        } else {
          int maxRange = countStorageParts - storageNumber;
          if (maxRange > 900) {
            maxRange = 900;
          }
          storageNumber += 100 + rnd.nextFloat() * maxRange;
        }
        d.setEndPartNumber(String.format(storageNumberFormat, storageNumber));

        em.persist(d);
        if (disponentNumber % 1_000 == 0) {
          LOG.info(" * Write next chunk <" + disponentNumber + ">...");
          em.flush();
          em.clear();
        }

        disponentNumber++;
      }

      // this doesn't have any storage parts
      Disponent d = new Disponent();
      d.setId(String.format("D_%08d", disponentNumber));
      d.setStartPartNumber(String.format(storageNumberFormat, storageNumber));
      storageNumber += 100;
      d.setEndPartNumber(String.format(storageNumberFormat, storageNumber));
      em.persist(d);

      em.flush();
      em.clear();

    } finally {
      LOG.info("... finished data generation.");
    }
  }


}

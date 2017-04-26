package de.markusseidl.massbusiness.business.logic;

import de.markusseidl.massbusiness.business.domain.Disponent;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * Example02Test
 *
 * @since 1.0
 */
public class Example02Test {

  private static EJBContainer CONTAINER;
  private static Context CONTEXT;
  @PersistenceContext(name = "default_batch")
  private EntityManager _em;
  @Resource
  private UserTransaction _transaction;

  @BeforeClass
  public static void start() {
    CONTAINER = Util.createContainer();
    CONTEXT = CONTAINER.getContext();
  }

  @AfterClass
  public static void shutdown() throws NamingException {
    if (CONTEXT != null) {
      CONTEXT.close();
    }

    if (CONTAINER != null) {
      CONTAINER.close();
    }
  }

  @Before
  public void inject() throws NamingException, SystemException, NotSupportedException {
    CONTAINER.getContext().bind("inject", this);

    _transaction.begin();
    _em.joinTransaction();
  }

  @After
  public void reset() throws NamingException, HeuristicRollbackException, HeuristicMixedException, RollbackException, SystemException {
    _transaction.rollback(); // we don't care about transaction for in-memory databases

    CONTAINER.getContext().unbind("inject");
  }

  @Test
  public void test_run() throws Exception {
    TestDataGenerator.createData(_em);

    Example02 e = new Example02(_em);
    Map<Disponent, Integer> result = e.run();

    // D_00000004 = 901
    // D_00000006 = 165
    // D_00000007 = 664

    for (Entry<Disponent, Integer> entry : result.entrySet()) {
      if("D_00000004".equals(entry.getKey().getId())) {
        Assert.assertEquals(901, (int)entry.getValue());
      } else if("D_00000006".equals(entry.getKey().getId())) {
        Assert.assertEquals(165, (int)entry.getValue());
      } else if("D_00000007".equals(entry.getKey().getId())) {
        Assert.assertEquals(664, (int)entry.getValue());
      }
    }
  }
}
package de.markusseidl.massbusiness.business.logic;

import de.markusseidl.massbusiness.business.domain.StoragePart;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

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
 * Example01Test
 * <p>
 * Created on 19.08.2016
 */
public class Example01Test {

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
    _transaction.rollback(); // we don't care about transaction for in-memory databases. Also this deletes the test data after each test.

    CONTAINER.getContext().unbind("inject");
  }

  @Test
  public void test01V1() throws Exception {
    TestDataGenerator.createData(_em);

    Example01 e = new Example01(_em);
    Map<Object, Long> result = e.run01V1();

    Assert.assertEquals(Long.valueOf(3333), result.get(StoragePart.TYPE_NORMTEIL));
    Assert.assertEquals(Long.valueOf(3333), result.get(StoragePart.TYPE_VERBRAUCHSMATERIAL));
    Assert.assertEquals(Long.valueOf(3334), result.get(StoragePart.TYPE_ZUKAUFTEIL));
  }

  @Test
  public void test01V2() throws Exception {
    TestDataGenerator.createData(_em);

    Example01 e = new Example01(_em);
    Map<Object, Long> result = e.run01V2();

    Assert.assertEquals(Long.valueOf(5640), result.get(0));
    Assert.assertEquals(Long.valueOf(1923), result.get(1));
    Assert.assertEquals(Long.valueOf(953), result.get(2));
    Assert.assertEquals(Long.valueOf(590), result.get(3));
    Assert.assertEquals(Long.valueOf(372), result.get(4));
    Assert.assertEquals(Long.valueOf(216), result.get(5));
    Assert.assertEquals(Long.valueOf(153), result.get(6));
    Assert.assertEquals(Long.valueOf(95), result.get(7));
    Assert.assertEquals(Long.valueOf(40), result.get(8));
    Assert.assertEquals(Long.valueOf(18), result.get(9));
  }

  @Test
  public void test01V2_reverse() throws Exception {
    TestDataGenerator.createData(_em);

    // this part is from group "1"
    StoragePart sp = new StoragePart("SP_00004799", StoragePart.TYPE_VERBRAUCHSMATERIAL, 68616, 73.59329f);

    Example01 e = new Example01(_em);

    Assert.assertEquals(Long.valueOf(1923), Long.valueOf(e.run01V2_inverse(sp)));
  }

  @Test
  public void test01V1_reverse() throws Exception {
    TestDataGenerator.createData(_em);

    // this is a pseudo part from group StoragePart.TYPE_NORMTEIL
    StoragePart sp = new StoragePart("SP_00004799", StoragePart.TYPE_NORMTEIL, -1, -1);

    Example01 e = new Example01(_em);

    Assert.assertEquals(Long.valueOf(3333), Long.valueOf(e.run01V1_inverse(sp)));
  }

}

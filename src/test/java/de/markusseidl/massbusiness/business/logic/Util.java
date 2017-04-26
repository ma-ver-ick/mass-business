package de.markusseidl.massbusiness.business.logic;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;

/**
 * Util
 *
 * @author msei
 * @since 1.0
 */
public class Util {
  private Util() {}

  public static EJBContainer createContainer() {
    Properties p = new Properties();
    p.put("jdbc/myDatasource", "new://Resource?type=DataSource");
    p.put("jdbc/myDatasource.JdbcDriver", "org.hsqldb.jdbcDriver");
    p.put("jdbc/myDatasource.JdbcUrl", "jdbc:hsqldb:mem:testdb");

    return EJBContainer.createEJBContainer(p);
  }
}

FROM jboss/wildfly

COPY module.xml /opt/jboss/wildfly/modules/system/layers/base/org/eclipse/persistence/main/module.xml
COPY eclipselink.jar /opt/jboss/wildfly/modules/system/layers/base/org/eclipse/persistence/main/eclipselink.jar

RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#70365
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

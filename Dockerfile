FROM jboss/wildfly:17.0.1.Final

COPY jello-cards/target/cards.war /opt/jboss/wildfly/standalone/deployments/cards.war
COPY jello-audit/target/audit.war /opt/jboss/wildfly/standalone/deployments/audit.war

CMD [ "/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-full.xml", "-b 0.0.0.0", "-bmanagement 0.0.0.0" ]
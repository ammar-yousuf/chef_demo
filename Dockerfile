#
#EZRxLookup is a web app and all it needs is a tomcat server
#
FROM tomcat:7-jre7

#
#Since we are embedding an elasticsearch and we need to tell where to create the data directory
#This directory is used to ingest 5000k SPL records(openFDA API Limitation) and index the data to power our suggestions
#Since this is a prototype the ingestion happens  on startup of the app
#The data folder specified in elasticsearch.yml is ${CATALINA_BASE}/webapps/EZRxLookup/data
#
ENV CATALINA_BASE /usr/local/tomcat

#
#Set the memory parameters for the tomcat
#
ENV JAVA_OPTS "$JAVA_OPTS -Xms2048M -Xmx4096M"

#
#Copy the WAR file after a clean build to cert env
#
COPY build/libs/EZRxLookup.war /usr/local/tomcat/webapps/
# IEnergyDa

IEnergyDa is a Java EE web application and it can be installed in any servlet container (tested on tomcat 7+).

The configuration setup is very easy and it concerns the file "jeerda.properties"

    db.url=jdbc:postgresql://host:5432/ienergy
    db.username=XXXX
    db.password=XXXX

Practically, it is necessary to use account credential to access the postgres database.

If you have to configure administration functions that are protected by spring security, it is necessary to configure the file "spring-security.xml"

The database structure is available in https://github.com/iEnergy/IEnergyDa/blob/master/ddl/ienergy_da.sql

## Requirements 

* Server Linux debian-like
* Java 1.7+
* Dog 2.5+
* Tomcat 7+
* Postgres 9+

## Development

The application is realized with Eclipse EE and Maven

## Libraries 

The project uses following libraries

* spring 3.2.3.RELEASE http://spring.io/
* jasper report 5.5.2 http://www.jaspersoft.com/it
* jackson 1.9.7 http://jackson.codehaus.org/
* hibernate 4.2.6.Final http://hibernate.org/
FROM openjdk

VOLUME /tmp

ADD maven/todoapp-0.0.1-SNAPSHOT.jar todoapp.jar

ENTRYPOINT ["java","-jar","todoapp.jar"]
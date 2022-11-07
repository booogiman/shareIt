FROM amazoncorretto:11
#ENV TZ=Europe/Moscow
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY target/*.jar server.jar
ENTRYPOINT ["java","-jar","/server.jar"]
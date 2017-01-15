FROM openjdk:8

RUN wget https://services.gradle.org/distributions/gradle-3.3-bin.zip
RUN unzip gradle-3.3-bin.zip
RUN mv gradle-3.3 /opt/

ENV GRADLE_HOME /opt/gradle-3.3

COPY ./src/ /srv/mqip/src/
COPY build.gradle /srv/mqip/build.gradle

RUN ls /srv/mqip/src/

EXPOSE 8080

CMD cd /srv/mqip/ && /opt/gradle-3.3/bin/gradle appRun
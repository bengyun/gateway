FROM maven:3.6.1-ibmjava-8-alpine as builder

WORKDIR /usr/src/app
COPY . /usr/src/app

RUN mvn install:install-file \
   -Dfile=vendor/starwsn-mqtt-protocol-1.1.0.jar \
   -DgroupId=com.starwsn \
   -DartifactId=starwsn-mqtt-protocol \
   -Dversion=1.1.0 \
   -Dpackaging=jar \
   -DgeneratePom=true

RUN mvn package

FROM openjdk:8-jre-alpine
COPY --from=builder /usr/src/app/target/revosion-mqtt-test-0.0.1-jar-with-dependencies.jar /usr/share/bengyun/
#COPY --from=builder /app/main.js /usr/share/nginx/html
#COPY --from=builder /app/css/mainflux.css /usr/share/nginx/html/css/
#COPY docker/nginx.conf /etc/nginx/conf.d/default.conf
#COPY src/Websocket.js /usr/share/nginx/html/src/
#COPY docker/entrypoint.sh /entrypoint.sh

ENTRYPOINT ["java", "-jar", "/usr/share/bengyun/revosion-mqtt-test-0.0.1-jar-with-dependencies.jar"]
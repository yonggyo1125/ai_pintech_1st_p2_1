FROM yonggyo00/ubuntu
ARG JAR_PATH=build/libs/pokemon-0.0.1-SNAPSHOT.jar
COPY ${JAR_PATH} app.jar
RUN mkdir uploads

ENV SPRING_PROFILES_ACTIVE=default,prod,email,dl
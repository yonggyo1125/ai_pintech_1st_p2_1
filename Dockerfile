FROM yonggyo00/ubuntu:bert
ARG JAR_PATH=build/libs/pokemon-0.0.1-SNAPSHOT.jar
ARG PORT=3000
COPY ${JAR_PATH} app.jar
RUN mkdir uploads

ENV SPRING_PROFILES_ACTIVE=default,prod,email,dl
ENV FILE_PATH=/uploads/
ENV FILE_URL=/uploads/
ENV PYTHON_RUN=...
ENV PYTHON_SCRIPT=...
ENV DL_DATA_URL=...
ENV PYTHON_SCRIPT2=...
ENV PYTHON_BERT=...
ENV REDIS_PORT=6379
ENV DDL_AUTO=none

ENTRYPOINT ["java", "-jar", "-Ddb.host=${DB_HOST}", "-Ddb.password=${DB_PASSWORD}", "-Ddb.username=${DB_USERNAME}", "-Dddl.auto=${DDL_AUTO}", "-Ddl.data.url=${DL_DATA_URL}", "-Dpython.run=${PYTHON_RUN}", "-Dpython.script=${PYTHON_SCRIPT}", "-Dredis.host=${REDIS_HOST}", "-Dredis.port=${REDIS_PORT}", "-Dmail.username=${MAIL_USERNAME}", "-Dmail.password=${MAIL_PASSWORD}", "-Dpython.script2=${PYTHON_SCRIPT2}", "-Dpython.bert=${PYTHON_BERT}", "app.jar"]

EXPOSE ${PORT}
FROM gradle:jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -Pprod --no-daemon
RUN mkdir -p /data/logs && chmod -R 777 /data/logs

FROM eclipse-temurin:17-jammy
# 타임존 관련 설정 제거
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENV TZ=Asia/Seoul
ENTRYPOINT ["java","-Duser.timezone=Asia/Seoul","-jar","/app/app.jar","--spring.profiles.active=prod"]

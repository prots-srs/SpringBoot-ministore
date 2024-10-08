# https://docs.docker.com/go/dockerfile-reference/
# start clear stage
FROM eclipse-temurin:17-jdk-jammy AS base
WORKDIR /build
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/

# run maven tests *Test*
FROM base AS test
WORKDIR /build
COPY ./src src/
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw test

# preposition dependency
FROM base AS deps
WORKDIR /build
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -DskipTests

# create jar
FROM deps AS package
WORKDIR /build
COPY ./src src/
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

# extract jar
FROM package AS extract    
WORKDIR /build
RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

# FROM extract AS development
# WORKDIR /build
# we will do thomething here
# COPY --chmod=0755 ./uploads uploads/
# COPY --chmod=0755 ./config config/
# RUN cp -r /build/target/extracted/dependencies/. ./
# RUN cp -r /build/target/extracted/spring-boot-loader/. ./
# RUN cp -r /build/target/extracted/snapshot-dependencies/. ./
# RUN cp -r /build/target/extracted/application/. ./
# ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000
# CMD [ "java", "-Dspring.profiles.active=postgres", "org.springframework.boot.loader.launch.JarLauncher" ]

FROM eclipse-temurin:17-jre-jammy AS final
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser
# we will do thomething here
COPY --chmod=0755 --chown=appuser ./uploads uploads/
COPY --chmod=0755 --chown=appuser ./config config/
COPY --from=extract build/target/extracted/dependencies/ ./
COPY --from=extract build/target/extracted/spring-boot-loader/ ./
COPY --from=extract build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract build/target/extracted/application/ ./
EXPOSE 8080
ENTRYPOINT [ "java", "-Dspring.profiles.active=postgres", "org.springframework.boot.loader.launch.JarLauncher" ]
# Docker CMD vs ENTRYPOINT Demo

This project demonstrates the difference between `ENTRYPOINT` and `CMD` in a Dockerfile using a minimal java web application built with Maven. The application displays a simple web page. Its appearance and text change based on whether a `--special` flag is passed as an argument when running the Docker container.

## Files

* *`pom.xml`*: Maven project configuration.
* `src/`: Java source code for the Spring Boot application.
* *`Dockerfile`*: Instructions to build the Docker image (builds the app inside).

## Building the Docker Image

1. Navigate to the project's root directory (where the *`Dockerfile`* is located) in your terminal.
2. Run the Docker build command:
```bash
docker build -t java-app .
```
*(You can replace `java-app` with any image name you prefer)*

This command reads the `Dockerfile`, downloads the base Maven image, copies the project files, builds the Java application using `mvn package` inside the container, and creates the final image.

## Running the Container

The `Dockerfile` uses `ENTRYPOINT` to set the base command (`java -jar /app/target/demo-0.0.1-SNAPSHOT.jar`) and `CMD` to provide default arguments (none in this case). Arguments provided after the image name in `docker run` override the `CMD` and are appended to the `ENTRYPOINT`.

### 1. Run in Default Mode

To run the container without any extra arguments, 

```bash
docker run -d -p 8080:8080 --name demo-default java-app
```
* `-d`: Run in detached (background) mode.
* `-p 8080:8080`: Map port 8080 on your host to port 8080 in the container.
* `--name demo-default`: Assign a name to the container for easy management.

**Expected Output:** Open your web browser to `http://<ip-address>:8080`. You should see the web page styled for the **DEFAULT** mode (e.g., orange theme).

### 2. Run in Special Mode

To run the container and activate the special behavior, pass the `--special` flag after the image name. This flag overrides the default `CMD` and is appended to the `ENTRYPOINT` command.

```bash
docker run -d -p 8080:8080 --name demo-special java-app --special
```

The command executed inside the container becomes effectively: `java -jar /app/target/demo-0.0.1-SNAPSHOT.jar --special`

**Expected Output:** Open your web browser to `http://<ip-address>:8080`. You should see the web page styled for the **SPECIAL** mode (e.g., cyan theme).

This demonstrates how `ENTRYPOINT` provides the fixed executable part of the command, while arguments passed to `docker run` (which override `CMD`) allow you to modify the behavior of that executable.
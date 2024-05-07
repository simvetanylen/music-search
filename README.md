
## Setup

Build jar :
```
./gradlew build
```

Create backend docker image :
```
docker build --tag music-backend .
```

Docker compose :
```
docker compose -f ./docker-compose.yaml up
```

Backend is started only when all other services are available. So it can take some times before backend begin to start.

## Usage

* The backend should be up and running at localhost:8080
* Swagger UI should be available at http://localhost:8080/swagger-ui/index.html
* Crud operations are available alongside required search queries
* Events are published on kafka on each change, consumer only log received messages
* Webservices are monitored using open-telemetry, you can check metrics at http://localhost:16686/
* Trace id and span id are saved inside kafka messages

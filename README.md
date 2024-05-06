
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

## Usage

* The backend should be up and running at localhost:8080
* Swagger UI should be available at http://localhost:8080/swagger-ui/index.html
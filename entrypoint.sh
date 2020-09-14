#!/bin/sh

mvn clean install -D skipTests

./wait-for-it.sh -t 0 "${SPRING_CLOUD_CONFIG_URI}"
./wait-for-it.sh -t 0 "${SERVICE_DISCOVERY_HOST}":"${SERVICE_DISOVERY_PORT}"
./wait-for-it.sh -t 0 "${API_GATEWAY_HOST}":"${API_GATEWAY_PORT}"
./wait-for-it.sh -t 0 "${DB_HOST}":"${DB_PORT}"
./wait-for-it.sh -t 0 "${ELASTICSEARCH_HOST}":"${ELASTICSEARCH_PORT}"

# Change working directory to run mvn spring-boot:run
cd api
# now run the requested CMD without forking a subprocess
exec "$@"

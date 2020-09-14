#!/bin/sh

mvn clean install -D skipTests

./wait-for-it.sh -t 0 "${SERVICE_DISCOVERY_HOST}":"${SERVICE_DISOVERY_PORT}"
./wait-for-it.sh -t 0 "${API_GATEWAY_HOST}":"${API_GATEWAY_PORT}"
./wait-for-it.sh -t 0 "${DB_HOST}":"${DB_PORT}"
./wait-for-it.sh -t 0 "${ELASTICSEARCH_HOST}":"${ELASTICSEARCH_PORT}"

# now run the requested CMD without forking a subprocess
exec "$@"

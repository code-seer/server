#!/bin/sh

mvn clean install -D skipTests

./wait-for-it.sh -t 0 "${API_GATEWAY_HOST}":"${API_GATEWAY_PORT}"

# Change working directory to run mvn spring-boot:run
cd api
# now run the requested CMD without forking a subprocess
exec "$@"

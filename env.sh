#!/bin/sh

export SPRING_APPLICATION_NAME=starter
export SPRING_PROFILES_ACTIVE=default
export SPRING_CLOUD_CONFIG_URI=http://localhost:8888
export SERVICE_DISCOVERY_HOST=localhost
export SERVICE_DISCOVERY_PORT=8761
export PUSH_TYPESCRIPT_FILES=true
export GIT_REMOTE=git@bitbucket.org:learnet-app/codegen.git
export GIT_COMMIT_MESSAGE="Add Starter app codegen files"
export CODEGEN_ARTIFACT_DIR=starter
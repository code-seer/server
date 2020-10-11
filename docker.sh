#!/bin/sh
# Ensure gcloud project is active before proceeding

# Login to gcloud
# gcloud auth login

# Get list of projects
#gcloud projects list

# Select a project
gcloud config set project learnet

# Create executable jar
mvn clean install -D skipTests

## Pull base image
docker pull gcr.io/learnet/learnet-base:latest
#
## Build docker image
docker build -t learnet-starter .
#
### Tag image
docker tag learnet-starter gcr.io/learnet/learnet-starter:latest
#
## Push image
docker push gcr.io/learnet/learnet-starter:latest
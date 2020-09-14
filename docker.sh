#!/bin/sh
# Ensure gcloud project is active before proceeding

# Login to gcloud
# gcloud auth login

# Get list of projects
#gcloud projects list

# Select a project
gcloud config set project modern-lms

# Remove any build files
mvn clean

## Pull base image
docker pull gcr.io/modern-lms/lms-baseimage:latest
#
## Build docker image
docker build -t lms-starter .
#
### Tag image
docker tag lms-starter gcr.io/modern-lms/lms-starter:latest
#
## Push image
#docker push gcr.io/modern-lms/lms-starter:latest
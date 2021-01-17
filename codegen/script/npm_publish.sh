#!/bin/sh
#export $(xargs < .env)

echo "Publishing to NPM registry..."

cd ../target/generated-resources/typescript

# Set auth token
npm config set '//gitlab.learnet.io/api/v4/projects/$CI_PROJECT_ID/packages/npm/:_authToken' "$CI_JOB_TOKEN"

# Set registry for @learnet group scope
npm config set @learnet:registry https://gitlab.learnet.io/api/v4/projects/$CI_PROJECT_ID/packages/npm/

# Install dependencies
npm install

# Install typescript if not available
npm install typescript

# Publish to the registry
npm publish
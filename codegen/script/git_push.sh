#!/bin/sh

if [ "$PUSH_TYPESCRIPT_FILES" ]
then
  # Push TypeScript files to the codegen repository

  archive=codegen-1.0-SNAPSHOT-typescript.zip

  cd ../target
  git clone "${GIT_REMOTE}"


  # Move the files to be pushed
  cd codegen && rm -rf "$CODEGEN_ARTIFACT_DIR"
  mkdir -p "$CODEGEN_ARTIFACT_DIR"
  cp ../../target/$archive "$CODEGEN_ARTIFACT_DIR"

  # Adds the files in the local repository and stages them for commit.
  git add .

  # Commits the tracked changes and prepares them to be pushed to a remote repository.
  git commit -m "${GIT_COMMIT_MESSAGE}"

  # Pushes (Forces) the changes in the local repository up to the remote repository
  echo "Git pushing to ${GIT_REMOTE}"
  git push origin master
fi



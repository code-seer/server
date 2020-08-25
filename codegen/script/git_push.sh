#!/bin/sh
# Push TypeScript files to the codegen repository

archive=codegen-1.0-SNAPSHOT-typescript.zip
#ar=${DEPLOY_ENV:-default_value}
#git_remote=git@bitbucket.org:modern-lms/codegen.git
#commit_message="Add Course Service codegen files"

cd ../target
#git clone git@bitbucket.org:modern-lms/codegen.git
git clone "${GIT_REMOTE}"


# Move the files to be pushed
cd codegen && rm -rf "$ARTIFACT_DIR"
mkdir -p "$ARTIFACT_DIR"
cp ../../target/$archive "$ARTIFACT_DIR"

# Adds the files in the local repository and stages them for commit.
git add .

# Commits the tracked changes and prepares them to be pushed to a remote repository.
git commit -m "${COMMIT_MESSAGE}"

# Pushes (Forces) the changes in the local repository up to the remote repository
echo "Git pushing to ${GIT_REMOTE}"
git push origin master



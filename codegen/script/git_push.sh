#!/bin/sh
# Push TypeScript files to the codegen repository

archive=codegen-1.0-SNAPSHOT-typescript.zip
git_remote=git@bitbucket.org:modern-lms/codegen.git
commit_message="Add Course Service codegen files"

cd ../target
git clone git@bitbucket.org:modern-lms/codegen.git

# Move the files to be pushed

cd codegen && rm -rf artifacts/course
mkdir -p artifacts/course
cp ../../target/$archive artifacts/course

# Adds the files in the local repository and stages them for commit.
git add .

# Commits the tracked changes and prepares them to be pushed to a remote repository.
git commit -m "$commit_message"

# Pushes (Forces) the changes in the local repository up to the remote repository
echo "Git pushing to ${git_remote}"
git push origin master



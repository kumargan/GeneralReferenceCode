#!/bin/bash
set -e

#Go to root directory
BASEDIR=$(dirname "$0")

#Account variables
AWS_REGION="ap-south-1"

cd $BASEDIR/

git_commit=$(git rev-parse --short HEAD)
git_branch=${BRANCH_NAME}
epoch=$(date +%s)

#For building docker images locally
local=0
if [ "$git_branch" = "" ];then
   echo "Branch name not found in env variables. Build running on local."
   git_branch="$(git rev-parse --abbrev-ref HEAD)"
   local=1
fi

if [ "$git_branch" = "dev" ];then
   env_name="dev"
elif [ "$git_branch" = "stage" ];then
   env_name="stg"
elif [ "$git_branch" = "preprod" ];then
   env_name="preprod"
elif [ "$git_branch" = "master" ];then
   env_name="prod"
else
   echo "Incorrect env set for branch, check serverless-build.sh for details."
   exit 1
fi

./serverless-deploy.sh ${env_name}

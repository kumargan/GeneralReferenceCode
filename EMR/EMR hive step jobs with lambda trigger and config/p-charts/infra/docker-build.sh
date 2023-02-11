#!/bin/bash
set -e

#Go to root directory
BASEDIR=$(dirname "$0")

#Account variables
AWS_REGION="ap-south-1"
REGISTRY="656952484900.dkr.ecr.ap-south-1.amazonaws.com"

#App specific variables
ecr_repo_name="portfolio-charts"

cd $BASEDIR/../
git_commit=$(git rev-parse --short HEAD)
git_branch=${BRANCH_NAME}
epoch=$(date +%s)

#For building docker images locally
local=0
if [ "$git_branch" = "" ];then
   echo "Branch name not found in env variables"
   git_branch="$(git rev-parse --abbrev-ref HEAD)"
   local=1
fi

tag="${git_branch}-${git_commit}-${epoch}"
echo "$tag" > docker-tag.txt


if [ "$git_branch" = "dev" ];then
	env_name="dev"
elif [ "$git_branch" = "stage" ];then
   env_name="stage"
elif [ "$git_branch" = "preprod" ];then
   env_name="preprod"
elif [ "$git_branch" = "prod" ];then
   env_name="production"
else
	env_name="local"
fi


echo "*** Building Docker image: ${REGISTRY}/${ecr_repo_name}:${tag} ***"
docker build -f Dockerfile --build-arg git_commit_id=${git_commit} -t ${REGISTRY}/${ecr_repo_name}:${tag} .
echo "Image built successfully".

if [ $local = 0 ];then
   $(aws ecr get-login --no-include-email --region ${AWS_REGION})
   echo "Pushing image to Docker Registry."
   docker push ${REGISTRY}/${ecr_repo_name}:${tag}
   echo "Image pushed successfully."

   docker rmi -f ${REGISTRY}/${ecr_repo_name}:${tag}
   echo "Deleted image from build server."
fi

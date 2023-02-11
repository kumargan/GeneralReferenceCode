#!/bin/bash
set -e

if [ "$#" -lt 1 ];then
    echo "usage: $0 stage"
    echo "No stage provided as parameter"
    exit 1
fi


a=$(echo $RANDOM)
function="holdings-EMR-step-job"
stage=$1

echo "*** Building serverless for env: $stage ***"

cd ../
temp_dir="/tmp/$function-$stage-$(echo $RANDOM)"
echo "*** Directory for current deploy: $temp_dir ***"
mkdir $temp_dir
cp -r . $temp_dir
cd $temp_dir


echo "##########################"
echo "1. Packaging artifacts"
echo "##########################"
serverless package --stage=$stage -p .serverless
printf "\n\n\n"

echo "##########################"
echo "2. Deploying package to AWS($stage)"
echo "##########################"
serverless deploy --stage=$stage -p .serverless

rm -rf $temp_dir


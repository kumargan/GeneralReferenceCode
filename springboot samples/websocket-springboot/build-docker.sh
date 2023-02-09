#!bin bash
# Required in old versions of mac.
unset DOCKER_HOST

javapath=`which java`

if [ -z != $javapath ] 
	then
		echo "java already installed"
	else
		echo " install java and continue, exiting "
		exit
fi

mavenpath=`which mvn`	

if [ -z != $mavenpath ] 
	then
		echo "maven already installed"
	else
		echo " install maven and continue, exiting "
		exit
	fi

dockerpath=`which docker`
 
 if [ -z != $dockerpath ] 
 then
 		echo "docker already installed"
 else
 		echo "please install docker and continue, exiting "
 		exit
 fi


rm -rf target

mvn package 
if [ $? -eq 0 ]; then
	echo "packaging successful "
	echo "building docker image"
	docker build -t feed-mux:latest .
	echo "docker image build successful "
	#push to ECR
	#aws ecr get-login-password --region us-east-1 --profile=personal| docker login --username AWS --password-stdin 491516382502.dkr.ecr.us-east-1.amazonaws.com
	#docker tag feed-mux 491516382502.dkr.ecr.us-east-1.amazonaws.com/feed-mux
	#docker push 491516382502.dkr.ecr.us-east-1.amazonaws.com/feed-mux
	
else
	echo "packaging failed, check docker-build.logs"
fi


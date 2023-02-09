	echo "**********************************************************************************"
	echo "stopping old containers"
	file="docker.imgid"
		if [ -f "$file" ]
		then
       	docker stop `cat docker.imgid`
		fi
	cp /dev/null docker.imgid
	echo "**********************************************************************************"
	echo "deleting dangling images from docker engine"
	docker rmi -f `docker images --filter 'dangling=true' -q --no-trunc`
	docker rm -v $(docker ps --filter status=exited -q 2>/dev/null) 2>/dev/null	
	echo "**********************************************************************************"
	echo "starting fresh build"
	
	sh build-docker.sh >> docker-build.logs
	
	echo "**********************************************************************************"
	echo "starting docker "
	docker run -d -p 8080:8080 feed-mux:latest > docker.imgid
	echo "started docker successfully, image id : " `cat docker.imgid` >> docker-build.logs
	docker logs --follow `cat docker.imgid` >> docker-build.logs &
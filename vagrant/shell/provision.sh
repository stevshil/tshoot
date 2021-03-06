#!/bin/bash

# Script to build the demo system

# Install Docker dependencies and Docker
yum -y install yum-utils device-mapper-persistent-data lvm2
if grep 6.9 /etc/redhat-release >/dev/null 2>&1
then
	rpm --import https://www.elrepo.org/RPM-GPG-KEY-elrepo.org
	yum -y install http://www.elrepo.org/elrepo-release-6-8.el6.elrepo.noarch.rpm
	yum -y --enablerepo=elrepo-kernel install kernel-lt
	yum -y install http://dl.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
	yum -y install docker-io
	chkconfig docker on
	service docker start
else
	yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
	yum -y install docker-ce mysql nc
	systemctl enable docker
	systemctl start docker
fi

yum -y install mysql nc

# Make the test results directory
if ! [[ -d /var/testresults ]]
then
  mkdir /var/testresults
fi

if [[ -d /var/testresults ]]
then
  chmod 755 /var/testresults
fi

# Add the vagrant user to Docker group
if ! grep docker /etc/group | grep vagrant >/dev/null 2>&1
then
  sed -i 's/^\(docker:x:.*\)/\1vagrant/' /etc/group
else
  echo "Failed to add vagrant to docker group"
	echo "Check /etc/group and ensure vagrant is in the docker group"
  #exit 1
fi

# Add the Docker registry server for where the Docker images will live
# [[ ! -d /var/lib/registry ]] && mkdir /var/lib/registry
# Link the /var/lib/registry to a local drive to save download times
if [[ ! -d /vagrant/registry ]]
then
  mkdir /vagrant/registry
fi

if [[ ! -L /var/lib/registry ]]
then
  ln -s /vagrant/registry /var/lib/registry
fi

# Check if docker reg is already running
if ! docker ps | grep dockreg >/dev/null 2>&1
then
	if ! docker run -itd -p 5000:5000 --restart always --name dockreg -v /var/lib/registry:/var/lib/registry steve353/registry:2
	then
	  echo "Failed to start docker registry server"
	  exit 2
	fi
fi

# Download mysql and activemq containers and add to local repo
# Check if we have our own local one first
if ! docker images | grep localhost:5000/mysql >/dev/null 2>&1
then
  if ! docker pull steve353/mysql:5.6
  then
    echo "Failed to get MySQL 5.6 image from Docker Hub"
    exit 3
  fi
  docker tag steve353/mysql:5.6 localhost:5000/mysql
  if ! docker push localhost:5000/mysql
  then
    echo "Failed to push our own copy of MySQL to local registry"
    exit 5
  fi
  docker rmi steve353/mysql:5.6
fi

if ! docker images | grep localhost:5000/activemq >/dev/null 2>&1
then
  if ! docker pull steve353/activemq:5.14.3
  then
    echo "Failed to get ActiveMQ 5.14.3 from Docker Hub"
    exit 4
  fi
  docker tag steve353/activemq:5.14.3 localhost:5000/activemq
  if ! docker push localhost:5000/activemq
  then
    echo "Failed to push our own copy of ActiveMQ to local registry"
    exit 6
  fi
  docker rmi steve353/activemq:5.14.3
fi

# Build the App Docker image and publish to local repo
cd /vagrant/docker
if ! ./buildapp.sh web
then
  echo "Failed to build test image"
  exit 7
fi
docker tag citi/test localhost:5000/citi/test
if ! docker push localhost:5000/citi/test
then
  echo "Failed to push test image to our local repo"
  exit 8
fi
docker rmi citi/test

# Remove the tagged images
for image in localhost:5000/citi/test localhost:5000/mysql localhost:5000/activemq
do
  docker rmi $image
done

# CentOS image need ID to remove it
docker rmi `docker images | grep centos | awk '{print $3}'`

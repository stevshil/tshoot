#!/bin/bash

case $1 in
	'web')
		if ! docker build -f webapp.Dockerfile -t citi/test1 .
		then
			echo "Failed to build test image"
			exit 1
		fi
		;;
	'sql')
		docker build -f mysql.Dockerfile -t citi/mysql .
		;;
	*)
		echo "Unknown option"
		;;
esac

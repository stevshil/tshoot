#!/bin/bash

/usr/sbin/sshd -D &

echo $APPVERSION >/opt/trade-app/version

/opt/trade-app/bin/runwebapp.sh

while :
do
	sleep 3600
done

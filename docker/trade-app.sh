#!/bin/bash

cd /opt/trade-app/target
find /opt/trade-app -name trade-record.log -exec rm -f {} \;
# rm /opt/trade-app/trade-record.log
#ln -s /var/log/trade-app/trade-record.log /opt/trade-app/trade-record.log
export VERSION=$(cat /opt/trade-app/version)

[[ ! -d /var/log/trade-app ]] && mkdir -p /var/log/trade-app

java -Dspring.config.location=application.properties -jar trade-app-${VERSION}.jar >/var/log/trade-app/server.stdout 2>/var/log/trade-app/server.stderr &

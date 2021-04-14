#!/bin/bash

cd /opt/trade-app
# find /opt/trade-app -name trade-record.log -exec rm -f {} \;
# rm /opt/trade-app/trade-record.log
# ln -s /var/log/trade-app/trade-record.log /opt/trade-app/trade-record.log
export VERSION=$APPVERSION

# Remove the install files
rm -rf /tmp/tradeapp.tgz /tmp/mysql-connector-java-5.1.42.tar.gz >/dev/null 2>&1

[[ ! -d /var/log/trade-app ]] && mkdir -p /var/log/trade-app

service trade-app start

#!/bin/bash

cd /opt/trade-app/target
#ln -s /var/log/trade-app/trade-record.log /opt/trade-app/trade-record.log
export VERSION=$(cat /opt/trade-app/version)

[[ ! -d /var/log/trade-app ]] && mkdir -p /var/log/trade-app

# Injector must run in foreground
/usr/bin/java -cp injector-${VERSION}.jar -Dspring.config.location=application-injector.properties -Dloader.main=com.neueda.trade.injector.Injector org.springframework.boot.loader.PropertiesLauncher >/var/log/trade-app/injector.stdout 2>/var/log/trade-app/injector.stderr

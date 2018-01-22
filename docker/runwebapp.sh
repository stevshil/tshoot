#!/bin/bash

cd /opt/trade-app
find /opt/trade-app -name trade-record.log -exec rm -f {} \;
# rm /opt/trade-app/trade-record.log
ln -s /var/log/trade-app/trade-record.log /opt/trade-app/trade-record.log
export VERSION=$APPVERSION

# Remove the install files
rm -rf /tmp/tradeapp.tgz /tmp/mysql-connector-java-5.1.42.tar.gz >/dev/null 2>&1

[[ ! -d /var/log/trade-app ]] && mkdir -p /var/log/trade-app

java -Dspring.config.location=target/application.properties,application.properties -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9990 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -jar target/trade-app-${VERSION}.jar >/var/log/trade-app/server.stdout 2>/var/log/trade-app/server.stderr &

/usr/bin/java -cp target/trade-app-${VERSION}.jar -Dspring.config.location=target/application-injector.properties,application-injector.properties -Dloader.main=com.neueda.trade.injector.Injector org.springframework.boot.loader.PropertiesLauncher >/var/log/trade-app/injector.stdout 2>/var/log/trade-app/injector.stderr

while :
do
	sleep 30
	# Keep running even if injector dies
done

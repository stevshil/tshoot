#!/bin/bash

# This script should be in the same location as the trade-app JAR file

nohup java -Dspring.profiles.active=prod -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9990 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -jar target/trade-app-0.1.0.jar >trade-app.stdout 2>trade-app.stderr &

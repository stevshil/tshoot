#!/bin/bash

# This script should be in the same location as the trade-app JAR file

nohup java -cp target/trade-app-0.1.0.jar -Dspring.profiles.active=prod -Dloader.main=com.neueda.trade.injector.Injector org.springframework.boot.loader.PropertiesLauncher >injector.stdout 2>injector.stderr &
